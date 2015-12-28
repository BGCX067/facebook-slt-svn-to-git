package set.comm;

/**
 * Starts the server.
 */
public class ServerStarter
{
    public static void main(String[] args)
    {
        ServerMain server = new ServerMain();
        (new Thread(server)).start();
        System.out.println("Server has started.");
    }
}
