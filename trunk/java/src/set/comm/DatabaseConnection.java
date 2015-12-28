package set.comm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import set.core.Card;
import set.core.Player;
import set.core.Set;
import set.util.MathUtil;


public class DatabaseConnection
{
    public static final int RECOVERY_ATTEMPTS = 1;
    
    //TODO: need to set optimal parameters
    public static final long DEADREC_DELAY_BASE = 100;
    public static final int DEADREC_NUM_MAX = 6;
    public static final int DEADREC_ATTEMPTS = 6;
    
    private static final int PLAYS_OUTCOME_DISCONNECTED = -1;
    private static final int PLAYS_OUTCOME_LOSE = 0;
    private static final int PLAYS_OUTCOME_WIN = 1;
    private static final int PLAYS_OUTCOME_TIE = 2;
    
    private static final int GAMES_STATE_NOTSTARTED = 0;
    private static final int GAMES_STATE_STARTED = 1;
    private static final int GAMES_STATE_ENDED = 2;


    private Connection con;
    private Statement statement;

    private PreparedStatement ps_InsertCards;
    private PreparedStatement ps_InsertSets;

    private PreparedStatement ps_InsertPlayer;
    private PreparedStatement ps_InsertPlays;
    private PreparedStatement ps_UpdatePlayers_rating;
    private PreparedStatement ps_UpdatePlays_outcome;
    private PreparedStatement ps_UpdateGames;
    private PreparedStatement ps_UpdateGames_numplayers;
    private PreparedStatement ps_UpdateGames_start;
    private PreparedStatement ps_UpdateGames_end;
    private PreparedStatement ps_InsertSetFound;

    private long deadRecDelay;
    private int deadRecNum;
    Random rng;


    /**
     * Connects to the database.
     */
    public DatabaseConnection()
    {
        connect("jdbc:mysql://127.0.0.1/ece464", "root", "");
        prepare();
        
        rng = new Random();
    }

    public boolean connect(String url, String username, String password)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            //statement = con.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private boolean prepare()
    {
        try
        {
            ps_InsertCards = con.prepareStatement(
                    "INSERT INTO cards(cardid, color, fill, shape, number) " +
                    "VALUES (?, ?, ?, ?, ?);"
            );
            
            ps_InsertSets = con.prepareStatement(
                    "INSERT INTO sets(setid, card1, card2, card3, color, fill, shape, number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
            );
            
            ps_InsertPlays = con.prepareStatement(
                    "INSERT INTO plays(playerid, gameid) " +
                    "VALUES (?, ?);"
            );
            
            ps_UpdatePlayers_rating = con.prepareStatement(
                    "UPDATE players P " +
                    "SET P.rating = P.rating + ? " +
                    "WHERE P.fid = ?;"
            );

            ps_UpdatePlays_outcome = con.prepareStatement(
                    "UPDATE plays P " +
                    "SET P.outcome = ? " +
                    "WHERE P.playerid = ? AND P.gameid = ?;"
            );

            ps_UpdateGames_numplayers = con.prepareStatement(
                    "UPDATE games G " +
                    "SET G.numplayers = G.numplayers + ? " +
                    "WHERE G.gid = ?;"
            );

            ps_UpdateGames_start = con.prepareStatement(
                    "UPDATE games G " +
                    "SET G.state = " + GAMES_STATE_STARTED + ", G.starttime = NOW() " +
                    "WHERE G.gid = ?;"
            );

            ps_UpdateGames_end = con.prepareStatement(
                    "UPDATE games G " +
                    "SET G.state = " + GAMES_STATE_ENDED + " " +
                    "WHERE G.gid = ?;"
            );

            ps_InsertSetFound = con.prepareStatement(
                    "INSERT INTO found(finder, setfound, game, timetaken) " +
                    "VALUES (?, ?, ?, ?);"
            );
        }
        catch (Exception e) 
        {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        
        return true;
    }


    public boolean insertCard(Card c)
    {
        int cardid = c.id();
        int color = c.getcolor();
        int fill = c.getshading();
        int shape = c.getshape();
        int number = c.getvalue();
        
        try
        {
            ps_InsertCards.setInt(1, cardid);
            ps_InsertCards.setInt(2, color);
            ps_InsertCards.setInt(3, fill);
            ps_InsertCards.setInt(4, shape);
            ps_InsertCards.setInt(5, number);
            
            ps_InsertCards.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        return true;
    }
    
    
    public boolean insertSet(Set s)
    {
        int setid = s.id();
        int card1 = s.card(0);
        int card2 = s.card(1);
        int card3 = s.card(2);
        int color = s.attribute(Set.COLOR);
        int fill = s.attribute(Set.SHADE);
        int shape = s.attribute(Set.SHAPE);
        int number = s.attribute(Set.VALUE);
        
        try
        {
            ps_InsertSets.setInt(1, setid);
            ps_InsertSets.setInt(2, card1);
            ps_InsertSets.setInt(3, card2);
            ps_InsertSets.setInt(4, card3);
            ps_InsertSets.setInt(5, color);
            ps_InsertSets.setInt(6, fill);
            ps_InsertSets.setInt(7, shape);
            ps_InsertSets.setInt(8, number);
            
            ps_InsertSets.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        return true;
    }
    
    
    public boolean insertPlayerIntoGame(String fid, int gid)
    {
        try
        {
            ps_InsertPlays.setObject(1, fid, Types.BIGINT);
            ps_InsertPlays.setInt(2, gid);

            ps_UpdateGames_numplayers.setInt(1, 1);
            ps_UpdateGames_numplayers.setInt(2, gid);

            ps_InsertPlays.executeUpdate();
            ps_UpdateGames_numplayers.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }

        return true;
    }

    public boolean disconnectPlayerFromGame(String fid, int gid)
    {
        try
        {
            ps_UpdatePlays_outcome.setInt(1, PLAYS_OUTCOME_DISCONNECTED);
            ps_UpdatePlays_outcome.setObject(2, fid, Types.BIGINT);
            ps_UpdatePlays_outcome.setInt(3, gid);
            
            ps_UpdateGames_numplayers.setInt(1, -1);
            ps_UpdateGames_numplayers.setInt(2, gid);
            
            ps_UpdatePlays_outcome.executeUpdate();
            ps_UpdateGames_numplayers.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }

        return true;
    }

    public boolean updateGameStart(int gid)
    {
        try
        {
            ps_UpdateGames_start.setInt(1, gid);
            ps_UpdateGames_start.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }

        return true;
    }
    
    public boolean updateGameEnd(int gid)
    {
        try
        {
            ps_UpdateGames_end.setInt(1, gid);
            ps_UpdateGames_end.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        return true;
    }
    
    
    public boolean updatePlayerDCStats(Player player, int gid, int numPlayers)
    {
        try
        {
            String fid = player.getFID();
            
            ps_InsertSetFound.setObject(1, fid, Types.BIGINT);
            ps_InsertSetFound.setInt(3, gid);
            
            LinkedList<Set> setList = player.getSetsFound();
            LinkedList<Long> timeList = player.getTimeTaken();
            
            ListIterator<Set> itr1 = setList.listIterator();
            ListIterator<Long> itr2 = timeList.listIterator();
            
            int numSetsFound = setList.size();
            
            // Insert database entry for each set found by the player.
            for (int i = 0; i < numSetsFound; i++)
            {
                Set s = itr1.next();
                long time = itr2.next();
                
                int setID = s.id();
                
                ps_InsertSetFound.setInt(2, setID);
                ps_InsertSetFound.setInt(4, (int)time);
                
                ps_InsertSetFound.executeUpdate();
            }
            
            // Increase the player's rating.
            ps_UpdatePlayers_rating.setDouble(1, Player.computeDCRatingIncrease(numSetsFound, numPlayers));
            ps_UpdatePlayers_rating.setObject(2, fid, Types.BIGINT);
            ps_UpdatePlayers_rating.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        
        return true;
    }
    
    
    public boolean updatePlayerStats(Collection<Player> players, int gid)
    {
        try
        {
            ps_InsertSetFound.setInt(3, gid);
            ps_UpdatePlays_outcome.setInt(3, gid);
            
            ps_UpdatePlays_outcome.setInt(1, PLAYS_OUTCOME_LOSE);
            
            int maxScore = 0;
            LinkedList<Player> winners = new LinkedList<Player>();
            
            int numPlayers = players.size();
            
            for (Player p : players)
            {
                String fid = p.getFID();
                ps_InsertSetFound.setObject(1, fid, Types.BIGINT);
                ps_UpdatePlays_outcome.setObject(2, fid, Types.BIGINT);
                
                LinkedList<Set> setList = p.getSetsFound();
                LinkedList<Long> timeList = p.getTimeTaken();
                
                ListIterator<Set> itr1 = setList.listIterator();
                ListIterator<Long> itr2 = timeList.listIterator();
                
                int numSetsFound = setList.size();
                
                // Insert database entry for each set found by the player.
                for (int i = 0; i < numSetsFound; i++)
                {
                    Set s = itr1.next();
                    long time = itr2.next();
                    
                    int setID = s.id();
                    
                    ps_InsertSetFound.setInt(2, setID);
                    ps_InsertSetFound.setInt(4, (int)time);
                    
                    ps_InsertSetFound.executeUpdate();
                }
                
                // Increase the player's rating.
                ps_UpdatePlayers_rating.setDouble(1, Player.computeRatingIncrease(numSetsFound, numPlayers));
                ps_UpdatePlayers_rating.setObject(2, fid, Types.BIGINT);
                ps_UpdatePlayers_rating.execute();

                ps_UpdatePlays_outcome.executeUpdate();
                
                // Determine the game winner.
                int score = p.getScore();
                
                if (score > maxScore)
                {
                    maxScore = score;
                    winners.clear();
                    winners.add(p);
                }
                else if (score == maxScore)
                {
                    winners.add(p);
                }
            }
            
            // Update game winner:
            if (winners.size() == 1)
            {
                ps_UpdatePlays_outcome.setObject(2, winners.get(0).getFID(), Types.BIGINT);
                ps_UpdatePlays_outcome.setInt(1, PLAYS_OUTCOME_WIN);
                ps_UpdatePlays_outcome.executeUpdate();
            }
            else
            {
                ps_UpdatePlays_outcome.setInt(1, PLAYS_OUTCOME_TIE);
                
                for (Player p : winners)
                {
                    ps_UpdatePlays_outcome.setObject(2, p.getFID(), Types.BIGINT);
                    ps_UpdatePlays_outcome.executeUpdate();
                }
            }
            
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        return true;
    }
    
    
    public boolean insertSetFound(String fid, int setID, int gid, long time)
    {
        try
        {
            ps_InsertSetFound.setObject(1, fid, Types.BIGINT);
            ps_InsertSetFound.setInt(2, setID);
            ps_InsertSetFound.setInt(3, gid);
            ps_InsertSetFound.setInt(4, (int)time);
            
            ps_InsertSetFound.executeUpdate();
            con.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("A database error has occurred. Attempting to initiate recovery steps...");
            
            abortCurrentTransaction();
            
            return false;
        }
        
        return true;
    }
    
    
    
    
    
    /**
     * Attempts to abort the current transaction in a way that preserves the
     * state of the database.
     */
    private void abortCurrentTransaction()
    {
        boolean trace = true;
        
        for (int i = 1; i <= RECOVERY_ATTEMPTS; i++)
        {
            try
            {
                if (!con.getAutoCommit())
                {
                    con.rollback();
                    System.err.println("The transaction was rolled back.");
                }
                else
                {
                    System.err.println("AutoCommit = true");
                    System.err.println("This transaction does not need to be rolled back.");
                }
                
                return;
            }
            catch (SQLException e1)
            {
                if (trace)
                {
                    e1.printStackTrace();
                    trace = false;
                }
                
                System.err.println("(Recovery attempt " + i + " of " + RECOVERY_ATTEMPTS + ")");
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
                System.err.println("Unexpected internal error (not an SQLException).");
                break;
            }
        }
        
        System.err.println("Fatal error. The transaction may not have been rolled back.");
        System.err.println("The database may be in an inconsistent state.");
    }
    
    
    private void deadlockRecoveryDelay()
    {
        // Calculate a random delay from 0 to DEADREC_DELAY_BASE * (2^deadRecNum)
        //  (exponential backoff algorithm)
        
        if (deadRecNum < DEADREC_NUM_MAX)
            ++deadRecNum;
        
        deadRecDelay = DEADREC_DELAY_BASE * rng.nextInt(MathUtil.powersOf2[deadRecNum]);
        
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < deadRecDelay)
        {
            try
            {
                Thread.sleep(deadRecDelay);
            }
            catch (InterruptedException e)
            {
                // nothing needs to be done here
            }
        }
    }
}
