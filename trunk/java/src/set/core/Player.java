package set.core;

import java.util.LinkedList;

public class Player
{
    private String fid;
    private int pid;
    private String name;
    private boolean isReady;
    private int score;
    private LinkedList<Set> setsFound;
    private LinkedList<Long> timeTaken;
    
    public static double computeRatingIncrease(int setsFound, int numPlayers)
    {
        return (setsFound * ((double)numPlayers - 1) / 2);
    }
    
    public static double computeDCRatingIncrease(int setsFound, int numPlayers)
    {
        return (setsFound * ((double)numPlayers - 1) / 4);
    }
    
    public Player(String fid, String name)
    {
        this.fid = fid;
        this.name = name;
        isReady = false;
        score = 0;
        setsFound = new LinkedList<Set>();
        timeTaken = new LinkedList<Long>();
    }
    
    public Player(String fid, int pid, String name)
    {
        this.fid = fid;
        this.pid = pid;
        this.name = name;
        isReady = false;
        score = 0;
        setsFound = new LinkedList<Set>();
        timeTaken = new LinkedList<Long>();
    }
    
    public String getFID()
    {
        return fid;
    }
    
    public int getPID()
    {
        return pid;
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean isReady()
    {
        return isReady;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public LinkedList<Set> getSetsFound()
    {
        return setsFound;
    }
    
    public LinkedList<Long> getTimeTaken()
    {
        return timeTaken;
    }
    
    public void setPID(int pid)
    {
        this.pid = pid;
    }
    
    public void setReady()
    {
        isReady = true;
    }
    
    public void setFound(Set s, long time)
    {
        setsFound.add(s);
        timeTaken.add(time);
        score++;
    }

    @Override
    public int hashCode()
    {
        return fid.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Player)
            return this.fid.equals(((Player) obj).getFID());
        else
            return false;
    }
    
    
}
