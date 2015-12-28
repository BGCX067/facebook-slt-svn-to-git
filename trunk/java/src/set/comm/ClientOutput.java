package set.comm;

import java.io.PrintWriter;

import set.gui.ClientApplet;

/**
 * Sends messages to the game server.
 */
public class ClientOutput
{
    private PrintWriter out;
    
    /**
     * Sets up the output class so that it can send data to the server.
     * 
     * @param out The output stream to print data to.
     */
    public ClientOutput(PrintWriter out)
    {
        this.out = out;
    }
    
    /**
     * Sends a message to the server indicating that a client is requesting
     * to join a specific game.
     * 
     * @param fid The unique ID of the player.
     * @param authKey An authentication key for this session.
     * @param gid The ID of the game to join.
     */
    public void sendAuthMsg(String fid, String authKey, int gid)
    {
        String[] args = new String[4];
        args[0] = fid;
        args[1] = authKey;
        args[2] = "" + gid;
        args[3] = ClientApplet.CLIENT_VERSION;
        
        out.println(new Protocol(Protocol.CLIENT_AUTH, 4, args));
    }
    
    /**
     * Sends a message to the server indicating that the client is ready to
     * start the game.
     */
    public void sendReadyMsg()
    {
        out.println(new Protocol(Protocol.CLIENT_READY, 0, null));
    }
    
    /**
     * Sends a message to the server indicating that the client is declaring
     * a set.
     * 
     * @param card_i The board position (index) of the first card in the set.
     * @param card_j The board position of the second card in the set.
     * @param card_k The board position of the third card in the set.
     * @param time The time it took the player to find the set, in 
     *  milliseconds.
     */
    public void sendSetDeclareMsg(int card_i, int card_j, int card_k, long time)
    {
        String[] args = new String[4];
        args[0] = "" + card_i;
        args[1] = "" + card_j;
        args[2] = "" + card_k;
        args[3] = "" + time;
        
        out.println(new Protocol(Protocol.CLIENT_DECLARE_SET, 4, args));
        System.out.println("CLIENT_DECLARE_SET, time = " + time);
    }
    
    /**
     * Sends a packet to the server to test that the connection is working.
     */
    public void testConnection()
    {
        System.out.println("Sending data to server:");
        Protocol data = new Protocol(Protocol.TEST_CONNECTION, 0, null);
        System.out.println(data);
        out.println(data);
    }
}
