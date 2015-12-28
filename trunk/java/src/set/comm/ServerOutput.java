package set.comm;

import java.io.PrintWriter;
import java.util.ArrayList;

import set.core.Card;

/**
 * Sends messages to the client.
 */
public class ServerOutput implements GameEventListener
{
    private ServerConnection connection;
    private PrintWriter out;
    
    /**
     * Sets up the output class so that it can send data to the client.
     * 
     * @param connection The connection associated witht this output class.
     * @param out The output stream to print to.
     */
    public ServerOutput(ServerConnection connection, PrintWriter out)
    {
        this.connection = connection;
        this.out = out;
    }    
    
    /**
     * Send a message indicating that the player has been successfully
     * authenticated and has joined the game.
     */
    public void confirmAuth()
    {
        out.println(new Protocol(Protocol.CONFIRM_AUTH, 0, null));
    }
    
    /**
     * Send a message indicating that the game client is out of date.
     * 
     * @param invalidVersion The version of the game client.
     * @param expectedVersion The version of the server.
     */
    public void sendInvalidVersionMsg(String invalidVersion, String expectedVersion)
    {
        String args[] = new String[2];
        args[0] = invalidVersion;
        args[1] = expectedVersion;
        
        out.println(new Protocol(Protocol.VERSION_MISMATCH, 2, args));
    }
    
    /**
     * Sends a message indicating that the client's login credentials are
     * invalid.
     */
    public void sendInvalidLoginMsg()
    {
        out.println(new Protocol(Protocol.DENY_INVALID_LOGIN, 0, null));
    }
    
    /**
     * Sends a message indicating that the game is full, or that the user
     * is already logged in.
     */
    public void sendGameFullMsg()
    {
        out.println(new Protocol(Protocol.DENY_GAME_FULL, 0, null));
    }
    
    /**
     * Senda a message notifying the client to begin the game.
     * @param board The initial game board.
     * @param playerNames The names of the players in the game.
     */
    public void confirmAllReady(ArrayList<Card> board, String[] playerNames)
    {
        int numArgs = board.size() + playerNames.length + 1;
        String args[] = new String[numArgs];
        
        int i = 0;
        
        //Note: need to send size of board so that we can distinguish between
        // card IDs and player names when decoding the message at the client
        args[i++] = "" + board.size();
        
        for (Card card : board)
        {
            args[i++] = card.toString();
        }
        
        for (int j = 0; j < playerNames.length; j++)
        {
            args[i++] = playerNames[j];
        }
        
        out.println(new Protocol(Protocol.CONFIRM_ALL_READY, numArgs, args));
        //(DEBUG) System.out.println("CONFIRM_ALL_READY");
    }
    
    /**
     * This method is called after a set has been found to notify the client to
     * update its local copy of the game board and the scores.
     * 
     * @param card_i The position of the first card in the previous set.
     * @param card_j The position of the second card in the previous set.
     * @param card_k The position of the third card in the previous set.
     * @param newBoard The new game board.
     * @param setFinder The pid of the player that found the previous set.
     */
    public void sendBoardUpdate(int card_i, int card_j, int card_k, ArrayList<Card> newBoard, int setFinder)
    {
        int numArgs = 4 + newBoard.size();
        String args[] = new String[numArgs];
        args[0] = "" + setFinder;
        args[1] = "" + card_i;
        args[2] = "" + card_j;
        args[3] = "" + card_k;
        
        int i = 4;
        for (Card card : newBoard)
        {
            String s = "";
            s += card.getvalue();
            s += card.getshape();
            s += card.getshading();
            s += card.getcolor();
            args[i++] = s;
        }
        
        out.println(new Protocol(Protocol.SERVER_UPDATE_BOARD, numArgs, args));
        //(DEBUG) System.out.println("SERVER_UPDATE_BOARD (Board size = " + newBoard.size() + ", Set Finder = " + setFinder + ")");
    }
    
    
    //TODO: should be unused now, but may be needed later for future
    // implementation of refresh button
    /**
     * Sends a message to the client containing the players' scores.<br />
     * Important: Precondition: <code>players.length == scores.length</code>
     * 
     * @param players The names of the players in the game.
     * @param scores The players' scores.
     */
    public void sendScoreUpdate(String[] players, int[] scores)
    {
        int numArgs = players.length + scores.length;
        String[] args = new String[numArgs];
        
        int argPos = 0;
        for (int i = 0; i < players.length; ++i)
        {
            args[argPos++] = players[i];
        }
        for (int i = 0; i < scores.length; ++i)
        {
            args[argPos++] = "" + scores[i];
        }
        
        out.println(new Protocol(Protocol.SERVER_UPDATE_SCORES, numArgs, args));
    }
    
    public void sendGameOverMsg()
    {
        out.println(new Protocol(Protocol.SERVER_GAME_OVER, 0, null));
    }
    
    /**
     * Tests the connection by sending a packet to the client.
     */
    public void testConnection()
    {
        System.out.println("Sending data to client:");
        Protocol data = new Protocol(Protocol.TEST_CONNECTION, 0, null);
        System.out.println(data);
        out.println(data);
    }

    @Override
    public synchronized void eventReceived(GameEvent e)
    {        
        int eventType = e.getEventType();
        
        if (eventType == GameEvent.START_EVENT)  //unused? - called directly by ServerInput
        {
            confirmAuth();
        }
        else if (eventType == GameEvent.INVALID_LOGIN_EVENT)
        {
            sendInvalidLoginMsg();
        }
        else if (eventType == GameEvent.GAME_FULL_EVENT)
        {
            sendGameFullMsg();
        }
        else if (eventType == GameEvent.READY_EVENT)
        {
            confirmAllReady(e.getNewCards(), e.getPlayers()); // start game, send cards
        }
        else if (eventType == GameEvent.BOARD_UPDATE_EVENT)
        {
            sendBoardUpdate(e.getPosition1(), e.getPosition2(), e.getPosition3(), e.getNewCards(), e.getSetFinder());
        }
        else if (eventType == GameEvent.SCORE_UPDATE_EVENT) //TODO: should be unused
        {
            sendScoreUpdate(e.getPlayers(), e.getScores());
        }
        else if (eventType == GameEvent.GAME_OVER_EVENT)
        {
            sendGameOverMsg();
        }
        else if (eventType == GameEvent.TEST_CONNECTION)
        {
            testConnection();
        }
        else if (eventType == GameEvent.FORCED_DISCONNECT)
        {
            //(DEBUG) System.out.println("Client disconnect forced by GameEvent, ServerOutput");
            connection.disconnect();
        }
    }
}
