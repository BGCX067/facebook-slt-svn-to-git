package set.core;

import java.util.HashMap;

import set.util.BackgroundTimer;

public class GameManager
{
    public static final int MAX_GAMES = 20;
    
    public static final long TIMER_PERIOD = 60000;

    private HashMap<Integer, Game> games;
    private SetChecker setChecker;
    
    private BackgroundTimer timer;
    
    public GameManager()
    {
        games = new HashMap<Integer, Game>();
        setChecker = new SetChecker();
        setChecker.build();
        
        timer = new BackgroundTimer(TIMER_PERIOD);
        timer.start();
    }
    
    /**
     * Obtains a game that players can join. If the specified game does not
     * exist, it is created.
     * 
     * @param gameID The unique ID of the game to obtain or create.
     * @return the game.
     */
    public Game getGame(int gameID)
    {
        if (games.containsKey(gameID))
        {
            return games.get(gameID);
        }
        else
        {
            //check against DB?
            Game game = new Game(gameID, this);
            games.put(gameID, game);
            timer.addListener(game);
            return game;
        }
    }
    
    /**
     * Removes the specified game from the set of active games.

     * @param gameID The unique ID of the game to remove.
     */
    public void endGame(int gameID)
    {
        Game game = games.get(gameID);
        timer.removeListenerDelayed(game);  // delayed removal is required to prevent thread deadlock
        games.remove(gameID);
    }
    
    /**
     * @return the object currently being used for checking sets.
     */
    public SetChecker getSetChecker()
    {
        return setChecker;
    }
}
