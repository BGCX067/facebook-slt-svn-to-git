package set.comm;

import java.util.ArrayList;
import java.util.EventObject;

import set.core.Card;

public class GameEvent extends EventObject 
{
    public static final int FORCED_DISCONNECT = -1;
    public static final int BOARD_UPDATE_EVENT = 1;
    public static final int GAME_OVER_EVENT = 2;
    public static final int READY_EVENT = 3;
    public static final int START_EVENT = 4;
    public static final int INVALID_LOGIN_EVENT = 5;
    public static final int GAME_FULL_EVENT = 6;
    public static final int SCORE_UPDATE_EVENT = 7;
    public static final int SCORE_INIT_EVENT = 8;
    //TODO:
    //public static final int REFRESH_EVENT = 9; 
    
    
    
    public static final int TEST_CONNECTION = -5;
    
    //note: it may be better to use derived classes here
    private int eventType;
    private int position1;
    private int position2;
    private int position3;
    private ArrayList<Card> cards; 
    private String[] players;
    private int[] scores;
    private int setfinder;
    private long duration;
    
    public GameEvent(Object source) 
    {
        super(source);
        eventType = -1;
        position1 = -1;
        position2 = -1;
        position3 = -1;
        //cards = new LinkedList<Card>();
        cards = new ArrayList<Card>();
    }
    
    public void setDuration(int durationFound) 
    {
    	duration = durationFound;
    }
    
    public long getDuration()
    {
    	return duration;
    }

    public void setEventType(int type)
    {
        eventType = type;
    }
    
    public int getEventType()
    {
        return eventType;
    }
    
    public void setPositions(int pos1, int pos2, int pos3)
    {
        position1 = pos1;
        position2 = pos2;
        position3 = pos3;
    }
    
    public int getPosition1()
    {
        return position1;
    }
    
    public int getPosition2()
    {
    	return position2;
    }
    
    public int getPosition3()
    {
    	return position3;
    }

    public void setNewCards(ArrayList<Card> newlist)
    {
    	cards = newlist;
    }

    public ArrayList<Card> getNewCards() 
    {
    	return cards;
    }
    
    
    // Keep this for now. We might need it for a future implementation of
    //  REFRESH_EVENT.
    /**
     * @deprecated
     */
    public void setScores(String[] players, int[] scores)
    {
        this.players = players;
        this.scores = scores;
    }
    
    public void setPlayers(String[] players)
    {
        this.players = players;
    }
    
    public String[] getPlayers()
    {
        return players;
    }
    
    /**
     * @deprecated
     */
    public int[] getScores()
    {
        return scores;
    }
    
    public int getSetFinder()
    {
    	return setfinder;
    }
    
    public void setSetFinder(int finder)
    {
    	setfinder = finder;
    }
}
