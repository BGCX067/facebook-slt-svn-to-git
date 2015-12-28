package set.comm;

import java.io.BufferedReader;

import set.core.Game;
import set.core.GameManager;
import set.core.Player;
import set.gui.ClientApplet;
import set.util.VersionMismatchException;

/**
 * Listens for messages from the client, connects the client to the game,
 * and notifies the game when messages are received.
 */
public class ServerInput implements Runnable
{
    private ServerConnection connection;
    private BufferedReader in;
    private ServerOutput outputClass;
    private GameManager gameMgr;
    private Game game;
    private Player player;
    
    
    /**
     * Sets up the input class so that it can receive data from the client.
     * 
     * @param connection The object managing the socket for this connection.
     * @param in The input stream to listen on.
     * @param outputClass The output class used for replying to the client.
     * @param gameMgr The object that manages the games on the server.
     */
    public ServerInput(ServerConnection connection, BufferedReader in, ServerOutput outputClass, GameManager gameMgr)
    {
        this.connection = connection;
        this.in = in;
        this.outputClass = outputClass;
        this.gameMgr = gameMgr;
        player = null;
    }
    
    
    @Override
    public void run()
    {
        String data;
        
        try
        {
            while((data = in.readLine()) != null)
            {
                Protocol decodedData = new Protocol(data);
                char msgType = decodedData.msgType();

                if (msgType == Protocol.CLIENT_AUTH)
                {
                    String fid = decodedData.args(0);
                    String authKey = decodedData.args(1);
                    int gid = Integer.parseInt(decodedData.args(2));
                    String version = decodedData.args(3);
                    
                    if (!version.equals(ClientApplet.CLIENT_VERSION))
                    {
                        outputClass.sendInvalidVersionMsg(version, ClientApplet.CLIENT_VERSION);
                        
                        System.err.println("Version Mismatch: client " + version + ", expected " + ClientApplet.CLIENT_VERSION);
                        System.err.println("Rejected CLIENT_AUTH (" + fid + ", " + authKey + ", " + gid + ")");
                        throw new VersionMismatchException(version, ClientApplet.CLIENT_VERSION);
                    }
                    
                    //(DEBUG) System.out.println("CLIENT_AUTH (" + fid + ", " + authKey + ", " + gid + ")");
                    
                    WebConnection webConn = new WebConnection();
                    String pname = webConn.authenticate(fid, authKey, gid);
                    if (pname != null)
                    {
                        //add player with <fid> to game with <gid>
                        player = new Player(fid, pname);
                        game = gameMgr.getGame(gid);
                        
                        if (game.addPlayer(player))
                        {
                            outputClass.confirmAuth();
                            game.addListener(outputClass);
                        }
                        else
                        {
                            outputClass.sendGameFullMsg();
                            player = null;
                            break;
                        }
                    }
                    else
                    {
                        outputClass.sendInvalidLoginMsg();
                        break;
                    }
                }
                else if (msgType == Protocol.CLIENT_READY)
                {
                    //(DEBUG) System.out.println("Player ready");
                    game.playerIsReady(player);                    
                }
                else if (msgType == Protocol.CLIENT_DECLARE_SET)
                {
                    int card1 = Integer.parseInt(decodedData.args(0));
                    int card2 = Integer.parseInt(decodedData.args(1));
                    int card3 = Integer.parseInt(decodedData.args(2));
                    long time = Long.parseLong(decodedData.args(3));
                    
                    //(DEBUG) System.out.println("CLIENT_DECLARE_SET (" + card1 + ", " + card2 + ", " + card3 + ", " + time + ")");
                    
                    game.setDeclared(player, card1, card2, card3, time);
                }
                else if (msgType == Protocol.TEST_CONNECTION)
                {
                    //(DEBUG) System.out.println("Received data from client.");
                }
            }
        }
        catch (Exception e)
        {
            // nothing needs to be done here - connection will terminate
        }
        finally
        {
            //(DEBUG) System.out.println("Client disconnect initiated by ServerInput");
            
            try
            {
                if (player != null)
                {
                    game.removePlayer(player);
                    game.removeListener(outputClass);
                }
            }
            catch (IllegalStateException e1)
            {
                // nothing needs to be done here - game is over
            }
            finally
            {
                connection.disconnect();
            }
        }
    }
}
