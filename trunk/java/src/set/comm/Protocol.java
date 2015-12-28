package set.comm;

/**
 * Defines the communication protocol for sending data between the client and
 * the server.
 */
public class Protocol
{
    public static final char msgStart = 'S';
    public static final char msgEnd = 'E';
    public static final char fieldEnd = ',';
    
    public static final char VERSION_MISMATCH = '!';
    public static final char TEST_CONNECTION = 'T';
    
    public static final char CLIENT_AUTH = 'a';
    public static final char CONFIRM_AUTH = 'A';
    
    public static final char DENY_GAME_FULL = 'F';
    public static final char DENY_INVALID_LOGIN = 'I';
    public static final char CLIENT_READY = 'r';
    public static final char CONFIRM_ALL_READY = 'R';
    
    public static final char CLIENT_DECLARE_SET = 'd';
    public static final char SERVER_UPDATE_BOARD = 'U';
    public static final char SERVER_UPDATE_SCORES = 'C';
    public static final char SERVER_INIT_SCORES = 'B';
    public static final char SERVER_GAME_OVER = 'X';
    
    
    private char msgType;
    private int numArgs;
    private String[] args;
    
    /**
     * Decodes a raw string of data based on the protocol.
     * 
     * @param data The data to decode, which presumably was encoded before
     *  using the <code>toString</code> method of this class.
     */
    public Protocol(String data)
    {
        char cData[] = data.toCharArray();
        msgType = cData[1];
        numArgs = cData[2] - '0';
        
        if (numArgs > 0)
        {
            args = new String[numArgs];
            int pos = 3;
            for (int i = 0; i < numArgs; ++i)
            {
                args[i] = "";
                while (cData[pos] != fieldEnd)
                {
                    args[i] += cData[pos];
                    ++pos;
                }
                ++pos;
            }
        }
        else
        {
            args = null;
        }
    }
    
    /**
     * Encodes a raw string of data that can be sent across the network,
     *  based on the protocol.
     *  
     * @param msgType The type of the message.
     * @param numArgs The number of arguments that follow.
     * @param args The arguments associated with this message.
     */
    public Protocol(char msgType, int numArgs, String[] args)
    {
        this.msgType = msgType;
        this.numArgs = numArgs;
        this.args = args;
    }
    
    /**
     * @return The type of the message.
     */
    public char msgType()
    {
        return msgType;
    }
    
    /**
     * @return The number of arguments contained in this message.
     */
    public int numArgs()
    {
        return numArgs;
    }
    
    /**
     * Gets the arguments contained in this message.
     * 
     * @param n The index of the argument.
     * @return The nth argument.
     */
    public String args(int n)
    {
        if (n >= 0 && n < numArgs)
            return args[n];
        else
            return null;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        str += msgStart;
        str += msgType;
        str += (char)(numArgs + '0');
        
        for (int i = 0; i < numArgs; ++i)
        {
            str += args[i] + ",";
        }
        
        str += msgEnd;
        
        return str;
    }
}
