package set.comm;

import java.io.IOException;

/**
 * Tests the communication between the client and the server.
 */
public class ClientTester {

    /**
     * @param args unused
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        ClientOutput output = null;
        ClientInput input = null;
        
        try
        {
            ClientConnection connection = new ClientConnection();
            output = connection.getOutputClass();
            input = connection.getInputClass();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (output == null || input == null)
        {
            System.exit(1);
        }
        
        (new Thread(input)).start();
        
        output.testConnection();
    }

}
