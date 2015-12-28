package set.core;

import java.util.HashSet;
import java.util.Vector;

import set.comm.DatabaseConnection;
import set.comm.GameEvent;
import set.comm.GameEventListener;
import set.util.ScheduledEvents;
import set.util.TimerEvent;

public class Game implements ScheduledEvents {

    public static final int MIN_PLAYERS = 1;
    public static final int MAX_PLAYERS = 10;
    public static final long DEAD_GAME_TIMEOUT = 10 * 60000;

    private GameField gamefield;

    private HashSet<Player> players;
    private int numReady;
    
    private boolean isStarted;
    private boolean isOver;

    private int gameID;
    private GameManager gameMgr;
    private SetChecker setChk;
    
    private int currentSeq;
    private Set previousSet;

    private Vector<GameEventListener> listeners;

    private DatabaseConnection db;

    private long createTime;
    private long lastActionTime;

    /**
     * Creates a new game.
     * 
     * @param gameID The unique ID of this game, which corresponds to <code>gid
     * </code> in the game database.
     * @param gameMgr The GameManager that controls all games on the server.
     */
    public Game(int gameID, GameManager gameMgr)
    {
        setChk = gameMgr.getSetChecker();

        gamefield = new GameField(setChk);
        players = new HashSet<Player>(MAX_PLAYERS);
        listeners = new Vector<GameEventListener>();
        isStarted = false;
        isOver = false;

        this.gameID = gameID;
        this.gameMgr = gameMgr;

        numReady = 0;

        currentSeq = 0;

        db = new DatabaseConnection();

        createTime = System.currentTimeMillis();
        lastActionTime = createTime;
    }

    /**
     * Adds the specified player to the game.
     *   (Precondition: <code>player.isReady() == false</code>)
     *   
     * @param player The player to add to the game.
     * @return true if the player was successfully added
     */
    public synchronized boolean addPlayer(Player player)
    {
        if (isStarted || isOver || players.size() >= MAX_PLAYERS)
        {
            return false;
        }
        else
        {
            if (players.add(player))
            {
                lastActionTime = System.currentTimeMillis();

                //(DEBUG) System.out.println("Insert Player into Game ID "+gameID);
                db.insertPlayerIntoGame(player.getFID(), gameID);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Removes the specified player from the game. If there are no more players
     * remaining, the game ends.
     * @param player the player to remove
     */
    public synchronized void removePlayer(Player player)
    {
        //TODO: better handling of the disconnect

        if(isOver)
            throw new IllegalStateException();

        if (players.contains(player) && player.isReady())
            numReady--;

        if (players.remove(player))
        {
            db.disconnectPlayerFromGame(player.getFID(), gameID);

            if (isStarted)
            {
                db.updatePlayerDCStats(player, gameID, players.size() + 1);
                    //TODO: temporary fix; rating increase should be based on number of players before the disconnect
            }
        }

        if (isStarted && players.isEmpty())
            deadGame();
    }

    /**
     * Marks the specified player as ready. If all of the players are ready,
     * the game starts.
     * 
     * @param player the player that is ready
     */
    public synchronized void playerIsReady(Player player)
    {
        if (isOver)
            throw new IllegalStateException();

        lastActionTime = System.currentTimeMillis();

        player.setReady();
        numReady++;

        //(DEBUG) System.out.println(numReady + " out of " + players.size() + "are ready.");

        if (numReady >= MIN_PLAYERS && numReady == players.size())
        {
            // Start the game.
            isStarted = true;
            db.updateGameStart(gameID);

            GameEvent event = new GameEvent(this);
            event.setEventType(GameEvent.READY_EVENT);
            event.setNewCards(gamefield.getField());

            int nextPID = 0;
            String playerNames[] = new String[players.size()];

            for (Player p : players)
            {
                p.setPID(nextPID);
                playerNames[nextPID] = p.getName();
                nextPID++;
            }

            event.setPlayers(playerNames);

            fireEvent(event);
            System.out.println("Game " + gameID + " has started.");
        }
    }


    /**
     * This method is called whenever a player has found a set. If the set is
     * valid, the game updates the game field and the scores appropriately,
     * and then notifies the clients of the change in the game state.
     * If there are no sets left on the field, the game automatically ends.
     * 
     * @param player The player that is declaring the set.
     * @param card_i The first card in the set.
     * @param card_j The second card in the set.
     * @param card_k The third card in the set.
     * @param time The time (in milliseconds) that the player took to find the
     *  set (which was computed client-side).
     */
    public synchronized void setDeclared(Player player, int card_i, int card_j, int card_k, long time)
    {
        if (isOver)
            throw new IllegalStateException();

        lastActionTime = System.currentTimeMillis();

        //int seq = 100000;  //TODO: temp value for testing

        // Drop requests that reference an outdated board.
        //if (seq < currentSeq)
        //    return;

        Card card1 = gamefield.getCard(card_i);
        Card card2 = gamefield.getCard(card_j);
        Card card3 = gamefield.getCard(card_k);

        // if (seq < currentSeq && (previousSet.contains(card1) || previousSet.contains(card2) || previousSet.contains(card3))
        //  return;

        if (setChk.isThisASet(card1, card2, card3))
        {
            //currentSeq++;

            Set s = setChk.getSet(card1, card2, card3);
            previousSet = s;

            player.setFound(s, time);

            boolean hasNewField = gamefield.removeSet(card_i, card_j, card_k);

            GameEvent event = new GameEvent(this);
            event.setEventType(GameEvent.BOARD_UPDATE_EVENT);
            event.setNewCards(gamefield.getField());
            event.setPositions(card_i, card_j, card_k);
            event.setSetFinder(player.getPID());
            fireEvent(event);

            if (!hasNewField)
                endGame();
        }
    }

    //TODO: currently unused, but may need later
    /**
     * Sends an event notifying active listeners of the current players and
     * their scores. 
     */
    public synchronized void refreshScores()
    {
        GameEvent scoreEvent = new GameEvent(this);
        scoreEvent.setEventType(GameEvent.SCORE_UPDATE_EVENT);

        String[] playerNames = new String[players.size()];
        int[] scores = new int[players.size()];

        int i = 0;
        for (Player p : players)
        {
            playerNames[i] = p.getName();
            scores[i] = p.getScore();
            i++;
        }

        scoreEvent.setScores(playerNames, scores);
        fireEvent(scoreEvent);
    }


    /**
     * Ends the game. Statistics for this game, including player scores and 
     * sets found, are saved in the database. All players are notified that
     * the game is over and their connections are released.
     */
    public synchronized void endGame()
    {
        if (isOver)
            throw new IllegalStateException();

        isOver = true;

        GameEvent event = new GameEvent(this);
        event.setEventType(GameEvent.GAME_OVER_EVENT);
        fireEvent(event);

        //(DEBUG)
//        System.out.println("Final scores:");
//        for (Player p : players)
//        {
//            System.out.println(p.getName() + " " + p.getScore());
//        }

        gameMgr.endGame(gameID);
        db.updateGameEnd(gameID);
        db.updatePlayerStats(players, gameID);

        GameEvent event2 = new GameEvent(this);
        event2.setEventType(GameEvent.FORCED_DISCONNECT);
        fireEvent(event2);
        
        System.out.println("Game " + gameID + " has ended.");
    }

    /**
     * Prematurely declare this game over, and disconnect all players.
     */
    public synchronized void deadGame()
    {
        if (isOver)
            throw new IllegalStateException();

        isOver = true;

        GameEvent event = new GameEvent(this);
        event.setEventType(GameEvent.FORCED_DISCONNECT);
        fireEvent(event);

        db.updateGameEnd(gameID);

        //FIXME: this may need to be done as a single transaction
        for (Player p : players)
        {
            db.disconnectPlayerFromGame(p.getFID(), gameID);
            db.updatePlayerDCStats(p, gameID, players.size());
        }

        gameMgr.endGame(gameID);
        
        System.out.println("Game " + gameID + " is dead.");
    }

    /**
     * @return true, if the game has ended.
     */
    public synchronized boolean isOver()
    {
        return isOver;
    }

    /**
     * Adds an event listener for a connected client, allowing the client
     * to receive information about the state of the game.
     * 
     * @param l The event listener to add.
     */
    public synchronized void addListener(GameEventListener l)
    {
        if (isOver)
            throw new IllegalStateException();

        listeners.add(l);
    }

    /**
     * Removes the client's event listener so that the client will no longer
     * receive updated information about this game.
     * 
     * @param l The event listener to remove.
     */
    public synchronized void removeListener(GameEventListener l)
    {
        if (isOver)
            throw new IllegalStateException();

        listeners.remove(l);
    }

    private synchronized void fireEvent(GameEvent event)
    {
        for (GameEventListener l : listeners)
        {
            l.eventReceived(event);
        }
    }

    @Override
    public synchronized void checkpoint(TimerEvent e)
    {
        if (isOver)
            return;

        if (!isStarted && players.isEmpty())
            return;

        long curTime = System.currentTimeMillis();

        if (curTime - lastActionTime > DEAD_GAME_TIMEOUT)
            deadGame();
    }
}
