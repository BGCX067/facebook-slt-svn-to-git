package set.core;

import java.util.List;
import java.lang.Math;

/**
 * @deprecated
 */
public class Player_old {
	
	private boolean isready;
	private double win_percentage; // Maybe expand into win_percentage with 2 players, 3 players, 4 players if there's enough time
    private double set_percentage;
    private int sets_declared;
    private double elo_rating;
    private String username;
    private int games_played;
    
    public double get_elo () {
    	return elo_rating;
    }
    
    public void setReady () {
    	isready = true;
    }
    
    public boolean isReady () {
    	return isready;
    }
    
    // K factor is just 10 right now but will probably change later
    public void update_elo (List<Player_old> players, List<Integer> position, int index){
    	double expected_score = 0;
    	int N = players.size();
    	double denominator = (double) (N*(N-1))/2;
    	for (int count = 0; count < N; count++){
    		if (count == index)
    			continue;
    		double numerator = Math.pow ( (1 + Math.pow(10, ( (players.get(count).get_elo() - elo_rating)/400)) ) , -1);
    		expected_score = expected_score + (numerator/denominator);    		
    	}
    	double S_value = (double) (N - position.get(index)) / denominator;
    	elo_rating = elo_rating + 10*(S_value - expected_score);
    }
    
    public void update_win () {
    	games_played = games_played + 1;
    	win_percentage = ( (win_percentage*games_played) + 1) / games_played;
    }
    
    public void update_loss () {
    	games_played = games_played + 1;
    	win_percentage = (win_percentage*games_played) / games_played;
    }    
    
    public Player_old (String name) {
    	isready = false;
    	username = name;
    }
    
    public String getName() {
    	return username;
    }
    
    public void update_set (int set){
    	sets_declared = sets_declared + 1;
    	set_percentage = (set_percentage*sets_declared + set) / sets_declared;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        
        if (other instanceof Player_old)
        {
            if ( ((Player_old)other).getName().equals(this.getName()) )
                    return true;
        }
        return false;
    }
}
