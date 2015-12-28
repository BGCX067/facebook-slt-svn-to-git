package set.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Establishes a connection with the game server.
 */
public class ClientConnection
{
    private PrintWriter out;
    private BufferedReader in;
    private ClientOutput outputClass;
    private ClientInput inputClass;
    
    /**
     * Connect with the default options.
     * @throws IOException if the connection fails.
     */
    public ClientConnection() throws IOException
    {
        connect(ServerMain.SERVER_IP, ServerMain.SERVER_PORT);
    }
    
    /**
     * Connect to a specified server.
     * @param host The IP address or hostname of the server.
     * @param port The TCP port to connect to.
     * @throws IOException if the connection fails.
     */
    public ClientConnection(String host, int port) throws IOException
    {
        connect(host, port);
    }
    
    private void connect(String host, int port) throws IOException
    {
        Socket clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputClass = new ClientOutput(out);
        inputClass = new ClientInput(in);
    }
    
    /**
     * @return the output class associated with this connection.
     */
    public ClientOutput getOutputClass()
    {
        return outputClass;
    }
    
    /**
     * @return the input class associated with this connection.
     */
    public ClientInput getInputClass()
    {
        return inputClass;
    }
}
