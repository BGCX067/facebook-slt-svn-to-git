package set.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import set.core.Card;
import set.util.VersionMismatchException;

/**
 * Listens for messages from the server and notifies the GUI when messages are
 * received. This class is meant to be run as a thread.
 */
public class ClientInput implements Runnable
{
    private Vector<GameEventListener> listeners;
    private BufferedReader in;
    
    /**
     * Sets up the input class so that it can receive data.
     * 
     * @param in The input stream to receive data from.
     */
    public ClientInput(BufferedReader in)
    {
        this.in = in;
        listeners = new Vector<GameEventListener>();
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
                
                GameEvent event = new GameEvent(this);
                
                if (msgType == Protocol.CONFIRM_AUTH)
                {
                    event.setEventType(GameEvent.START_EVENT);
                }
                else if (msgType == Protocol.VERSION_MISMATCH)
                {
                    throw new VersionMismatchException(decodedData.args(0), decodedData.args(1));
                }
                else if (msgType == Protocol.DENY_INVALID_LOGIN)
                {
                    event.setEventType(GameEvent.INVALID_LOGIN_EVENT);
                }
                else if (msgType == Protocol.DENY_GAME_FULL)
                {
                    event.setEventType(GameEvent.GAME_FULL_EVENT);
                }
                else if (msgType == Protocol.CONFIRM_ALL_READY)
                {
                    event.setEventType(GameEvent.READY_EVENT);
                    int numCards = Integer.parseInt(decodedData.args(0));
                    int numArgs = decodedData.numArgs();
                    int numPlayers = numArgs - numCards - 1;
                    
                    ArrayList<Card> cardList = new ArrayList<Card>(numCards);
                    String playerNames[] = new String[numPlayers];
                    
                    // fill out cards
                    // (i = 0 refers to the value of numCards)
                    for (int i = 1; i <= numCards; i++)
                    {
                        String s = decodedData.args(i);
                        int cardValue = s.charAt(0) - '0';
                        int cardShape = s.charAt(1) - '0';
                        int cardShade = s.charAt(2) - '0';
                        int cardColor = s.charAt(3) - '0';

                        Card c = new Card(cardValue, cardShape, cardShade, cardColor);
                        cardList.add(c);
                    }
                    
                    // fill out player names
                    int j = 0;
                    for (int i = numCards + 1; i < numArgs; i++)
                    {
                        playerNames[j++] = decodedData.args(i);
                    }
                    
                    event.setNewCards(cardList);
                    event.setPlayers(playerNames);
                }
                else if (msgType == Protocol.SERVER_UPDATE_BOARD)
                {
                    event.setEventType(GameEvent.BOARD_UPDATE_EVENT);
                    
                    int setFinder = Integer.parseInt(decodedData.args(0));
                    event.setSetFinder(setFinder);
                    
                    int card1 = Integer.parseInt(decodedData.args(1));
                    int card2 = Integer.parseInt(decodedData.args(2));
                    int card3 = Integer.parseInt(decodedData.args(3));
                    event.setPositions(card1, card2, card3);
                    
                    ArrayList<Card> cardList = new ArrayList<Card>(decodedData.numArgs() - 3);
                    
                    
                    for (int i = 4; i < decodedData.numArgs(); ++i)
                    {
                        String s = decodedData.args(i);
                        int cardValue = s.charAt(0) - '0';
                        int cardShape = s.charAt(1) - '0';
                        int cardShade = s.charAt(2) - '0';
                        int cardColor = s.charAt(3) - '0';
                        
                        Card c = new Card(cardValue, cardShape, cardShade, cardColor);
                        cardList.add(c);                        
                    }
                    event.setNewCards(cardList);
                }
                else if (msgType == Protocol.SERVER_UPDATE_SCORES)
                {
                    //TODO: not currently used
                    
                    event.setEventType(GameEvent.SCORE_UPDATE_EVENT);
                    
                    int n = decodedData.numArgs();
                    int numPlayers = n / 2;
                    String[] playerNames = new String[numPlayers];
                    int[] scores = new int[numPlayers];
                    
                    int argPos = 0;
                    for (int i = 0; i < numPlayers; ++i)
                    {
                        playerNames[i] = decodedData.args(argPos);
                        ++argPos;
                    }
                    for (int i = 0; i < numPlayers; ++i)
                    {
                        scores[i] = Integer.parseInt(decodedData.args(argPos));
                        ++argPos;
                    }
                    
                    event.setScores(playerNames, scores);
                }
                else if (msgType == Protocol.SERVER_GAME_OVER)
                {
                    event.setEventType(GameEvent.GAME_OVER_EVENT);
                }
                else if (msgType == Protocol.TEST_CONNECTION)
                {
                    System.out.println("Client received data from server.");
                    event.setEventType(GameEvent.TEST_CONNECTION);
                }
                
                
                fireEvent(event);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Add an event listener. The listener will have its
     *  <code>eventReceived</code> method called when messages come in from the
     *  server.
     * @param l The event listener.
     */
    public synchronized void addListener(GameEventListener l)
    {
        listeners.add(l);
    }

    private synchronized void fireEvent(GameEvent event)
    {
        for (GameEventListener l : listeners)
        {
            l.eventReceived(event);
        }
    }
}
