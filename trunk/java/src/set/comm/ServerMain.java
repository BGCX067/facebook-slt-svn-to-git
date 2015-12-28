package set.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import set.core.GameManager;

/**
 * The main server. Listens for incoming connections from clients.
 */
public class ServerMain implements Runnable
{    
    public static final String SERVER_IP = "199.98.20.112";
    //public static final String SERVER_IP = "127.0.0.1";	
    //public static final String SERVER_IP = "24.239.155.156";
    //public static final String SERVER_IP = "sleepbot.kicks-ass.net";	
	
    public static final int SERVER_PORT = 5950;
    //public static final int SERVER_PORT = 8080;
    
    private GameManager gameMgr;
    
    /**
     * Starts a new game on the server.
     */
    public ServerMain()
    {
        gameMgr = new GameManager();
    }
    
    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                
                //(DEBUG) System.out.println("Accepted connection: " + clientSocket.getInetAddress());
                
                ServerConnection connection = new ServerConnection(clientSocket, gameMgr);                
                (new Thread(connection.getInputClass())).start();
                
                //TODO: tests the connection (remove in final release)
                //ServerOutput output = connection.getOutputClass();
                //output.testConnection();
            }
        }
        catch (IOException e)
        {
            System.err.println("Error occurred in main server.");
            e.printStackTrace();
        }        
    }
}
