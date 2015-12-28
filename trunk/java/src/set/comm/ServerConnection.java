package set.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import set.core.GameManager;

/**
 * Establishes a connection with a client. A <code>ServerConnection</code>
 * object must be constructed for each client that connects to the game.
 */
public class ServerConnection
{
    public static final long DISCONNECT_DELAY = 10000;
    
    private PrintWriter out;
    private BufferedReader in;
    private ServerOutput outputClass;
    private ServerInput inputClass;
    private Socket socket;
    
    public ServerConnection(Socket socket, GameManager gameMgr)
    {
        this.socket = socket;
        
        try
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputClass = new ServerOutput(this, out);
            //game.addListener(outputClass);
            inputClass = new ServerInput(this, in, outputClass, gameMgr);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @return The output class associated with this connection.
     */
    public ServerOutput getOutputClass()
    {
        return outputClass;
    }
    
    /**
     * @return The input class associated with this connection.
     */
    public ServerInput getInputClass()
    {
        return inputClass;
    }
    
    public void disconnect()
    {
        if (socket.isClosed())
            return;
        
        out.flush();
        
//        long time = System.currentTimeMillis();
//        
//        while (System.currentTimeMillis() - time < DISCONNECT_DELAY)
//        {
//            try
//            {
//                Thread.sleep(DISCONNECT_DELAY);
//            }
//            catch (InterruptedException e)
//            {
//                // do nothing, must enforce the specified delay
//            }
//        }
        
        try
        {
            socket.close();
            //(DEBUG) System.out.println("Socket successfully released.");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
