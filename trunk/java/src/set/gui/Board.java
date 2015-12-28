package set.gui;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.*;

import java.lang.System;

import set.core.Card;
import set.core.SetChecker;
import set.comm.*;

public class Board extends JPanel implements ActionListener, GameEventListener {
	
	private ClientConnection connection;
	private ClientInput input;
	private ClientOutput output;
	
	private boolean isConnected;
	private boolean gameIsOver;
	private boolean gameInProgress;
	
	private static final int X = 1200;
	private static final int Y = 500;
	private static final int WAIT = 2500;
	
	private static final int LOGIN_XPOSN = 1;
	private static final int LOGIN_YPOSN = 1;
	private static final int INIT_XPOSN = 1;
	private static final int INIT_YPOSN = 1;
	
	//private static final int REFRESH_XPOSN = 1;
	//private static final int REFRESH_YPOSN = 4;
	
	private static final int NUM_ROWS = 3;
	private static final int STATS_START_XPOSN = 0;
	private static final int STATS_START_YPOSN = 6;
	private static final int STATS_LABEL_YPOSN = 5;
	
	private static final int END_SHOW_SCORES_XPOSN = 2;
	private static final int END_SHOW_NAMES_XPOSN = 1;
	private static final int END_SHOW_NAMES_YPOSN = 1;

	//protected JButton refresh;
    protected JButton init;
	private JLabel scorelabel;

	private ArrayList<Card> tempcards;
	private ArrayList<JButton> buttons;
	private HashMap<JButton,Integer> buttonhash;
	private ArrayList<JLabel> usrnames;
	
	// STATS: Displays the int scores as label
	private ArrayList<JLabel> stats;
	
	private int posn1,posn2,posn3;
	
    private long startTime, stopTime, duration;
	
	private GridBagConstraints c, l;
	
    private static final String startScreenStr = "Logging in...";
    private JLabel loginLabel;
    
    private String players[];
    private int scores[];
    private int setfinder, max, winner;
    private String outcome;
    
    private SetChecker setcheck; 
   
    private String fid;
    private String key;
    private int gid;

    private LinkedList<JButton> cardsSelected;
	
	public Board(String fid, String key, int gid) { 
		input = null;
		output = null;
		connection = null;
		isConnected = false;
		gameIsOver = false;
		
		usrnames = new ArrayList<JLabel>();
        stats = new ArrayList<JLabel>();
        outcome = "";
        
    	tempcards = new ArrayList<Card>();
    	buttons = new ArrayList<JButton>();
    	buttonhash = new HashMap<JButton,Integer>();
    	cardsSelected = new LinkedList<JButton>();
    	
    	setcheck = new SetChecker();
    	setcheck.build();
        
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(X,Y));
		
		c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		set_up_login();
		
		this.fid = fid;
		this.key = key;
		this.gid = gid;
	}
	
	/**
	 * Converts the "Logging in..." screen into the Ready-button screen.
	 */
	private synchronized void startToReady(){
		remove(loginLabel);
		revalidate();
		repaint();
		
		GridBagConstraints r = new GridBagConstraints();
		r.gridx = INIT_XPOSN;
		r.gridy = INIT_YPOSN;
		r.insets = new Insets(10,10,10,10);
		
		init = new JButton("Ready");
		init.addActionListener(this);
		init.setEnabled(true);
		init.setText("Ready!");
		init.setActionCommand("ready");
		
		add(init, r);
		
		revalidate();
		repaint();
		
	}
	/**
	 * Attempts to connect to the game server.
	 */
	private synchronized void connect() {
		
		if (!isConnected) {
            try {
                connection = new ClientConnection();
            } catch (IOException ex) {
            	remove(loginLabel);
            	revalidate();
            	repaint();
            	
                JOptionPane.showMessageDialog(this,
                        "Could not establish a connection.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            output = connection.getOutputClass();
            input = connection.getInputClass();
            input.addListener(this);
            (new Thread(input)).start();
            
            output.testConnection();
            
            output.sendAuthMsg(fid, key, gid);
            
            isConnected = true;
        }
		
	}
	
	/**
	 * Called in constructor. Sets up the initial state and attempts to connect to server.
	 */
	private synchronized void set_up_login() {
	    System.out.println("Logging in...");
		loginLabel = new JLabel(startScreenStr);
		
		l = new GridBagConstraints();
		l.gridx = LOGIN_XPOSN;
		l.gridy = LOGIN_YPOSN;
		l.insets = new Insets(10,10,10,10);
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
	        @Override
	        public Void doInBackground() {
	            System.out.println("Connecting..."); 
	            connect();
	            System.out.println("Connection successful");
	            return null;
	        }  
	    }; 
		add(loginLabel, l);

		revalidate();
		repaint();
		
		worker.execute();
		//System.out.println("Worker executed");
	}
	
	
	/**
	 * Upon receiving the board for the first time, sets up player name labels for scoring
	 * with scores initialized to 0.
	 * @param users The list of player usernames
	 */
	private synchronized void draw_stats(String [] users) {
		
	    GridBagConstraints usr = new GridBagConstraints();
        usr.gridx = STATS_START_XPOSN;
        usr.gridy = STATS_START_YPOSN;
	    
		for (int i = 0; i < users.length; i++) {
			JLabel usrtemp = new JLabel(users[i]);
			usrnames.add(usrtemp);
			add(usrtemp, usr);
			JLabel stattemp = new JLabel("" + 0);
			stats.add(stattemp);
			usr.gridx++;
			add(stattemp, usr);
			if ((i % 2) == 1) {
				usr.gridy++;
				usr.gridx = STATS_START_XPOSN;
			}
			else usr.gridx++;
		}
		
		revalidate();
		repaint();
	}
	
	/**
	 * Converts the Ready-button screen into the regular playing board:
	 * calls the function that draws the cards on the board
	 * @param cards The list of cards
	 */
	private synchronized void readyToField(ArrayList<Card> cards) {
		remove(init);

		// "Score" label
		GridBagConstraints usr = new GridBagConstraints();
		scorelabel = new JLabel("Scores:");
		usr.gridx = STATS_START_XPOSN;
		usr.gridy = STATS_LABEL_YPOSN;
		add(scorelabel, usr);
		
		// rest of the buttons
		gameplay(cards);
	}

	/**
	 * Places the cards onto the field (uses 81 saved images).
	 * @param newCardList The list of cards
	 */
	private synchronized void gameplay(ArrayList<Card> newCardList) {
	
		int posn = 0;
		for (int i = 0; i < newCardList.size(); i++) {
			Card cur = newCardList.get(i);

			ImageIcon cardIcon = createImageIcon("images/card" + cur.toString() + ".gif");
			JButton temp = new JButton(cardIcon);
			temp.addActionListener(this);
			temp.setActionCommand("pick");
			c.gridy = posn % NUM_ROWS;
			c.gridx = posn/NUM_ROWS;
			posn++;
			buttons.add(temp);
			buttonhash.put(temp,cur.id());
			if (!buttonhash.containsKey(temp))
				System.out.println("Wasn't inserted: " + temp + ", " + cur.id());
			add(temp, c);
		}
		revalidate();
		repaint();
		
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * At game end, determines the outcome (winner/tie).
	 * @return The string declaring the outcome, to be printed onto the board
	 */
	private synchronized String determine_outcome() {
        max = scores[0];
        for (int i = 1; i < scores.length; i++) {
        	if (max < scores[i]) {
        		max = scores[i];
        		winner = i;
        	}	
        }
        for (int j = winner+1; j < players.length; j++) {
        	if (scores[j] == max)
        		outcome = "It's a tie!";
        }
        if (outcome.equals(""))
        	outcome = players[winner] + " has won!";
        return outcome;
	}
	

	/**
	 * At the end of the game, deletes previously existing buttons and labels, and
	 * displays the name of the winner and all of the players' final scores on the screen. 
	 * @param endgameOutcome The string that announces the outcome of the game
	 */
	private synchronized void endGame(String endgameOutcome) {
		for (int i = 0; i < buttons.size(); i++) { 
			remove(buttons.get(i));
		}
        for (int i = 0; i < usrnames.size(); i++) {
            remove(usrnames.get(i));
            remove(stats.get(i));
        }
        
        remove(scorelabel);
        //remove(refresh);
        
		JLabel end = new JLabel(endgameOutcome);
		GridBagConstraints ec = new GridBagConstraints();
		GridBagConstraints ec2 = new GridBagConstraints();
		ec.gridx = END_SHOW_NAMES_XPOSN;
		ec.gridy = END_SHOW_NAMES_YPOSN;
		ec.insets = new Insets(10,10,10,10);
		add(end, ec);
		
		ec2.gridx = END_SHOW_SCORES_XPOSN;
		
		for (int i = 0; i < usrnames.size(); i++) {
			ec.gridy++;
			ec2.gridy = ec.gridy;
			add(usrnames.get(i), ec);
			add(stats.get(i), ec2);
		}
        
		revalidate();
		repaint();
	}
	
	/**
	 * Waits for WAIT seconds while the set that was just selected flashes green.
	 * Deletes old buttons from the field; 
	 * clears arraylist of buttons, hashmap of button-cardid values, and linked list of selected cards.
	 */
	private synchronized void delOldAll() {
		long start = System.currentTimeMillis();
		
		System.out.println("--begin delay--");
		while (System.currentTimeMillis() - start < WAIT)
		{
		    try
            {
                Thread.sleep(WAIT);
            }
            catch (InterruptedException e)
            {
                // Do nothing; must enforce the WAIT time.
            }
		}
		System.out.println("--end delay--");
		
		for (int i = 0; i < buttons.size(); i++) {
			remove(buttons.get(i));  
		}
		buttons.clear();
		buttonhash.clear();
		cardsSelected.clear();
	}
	
	/**
	 * Updates a player's score after they've found a set.
	 * @param setfinder The position (in the list of players) of the person who found the set
	 */
	private synchronized void update_stat(int setfinder) {
		remove(stats.get(setfinder));
		
	    GridBagConstraints ups = new GridBagConstraints();
	    if ((setfinder % 2) == 0)
	    	ups.gridx = STATS_START_XPOSN + 1;
	    else ups.gridx = STATS_START_XPOSN + 3;
        ups.gridy = STATS_START_YPOSN + setfinder/2;
        
        JLabel stattemp = new JLabel("" + scores[setfinder]);
        stats.set(setfinder, stattemp);
		add(stattemp, ups);
		
		revalidate();
		repaint();
	}
	
	// react to events initiated by the server
	public synchronized void eventReceived(GameEvent e) {
	    if (gameIsOver)
	        return;
	    
		int event = e.getEventType();
		
		// received if the user has been allowed into the game
		if (event == GameEvent.START_EVENT) {
			startToReady();
		}
		
		// if user was not authenticated, make the screen blank & show error dialog
		if (event == GameEvent.INVALID_LOGIN_EVENT) {
			remove(loginLabel);
			revalidate();
			repaint();
			
			JOptionPane.showMessageDialog(this,
					"Authentication failed!", "Error!",
                    JOptionPane.ERROR_MESSAGE);
		}
		// received if someone with the same username is already logged in or if the game is full
		else if (event == GameEvent.GAME_FULL_EVENT) {
			remove(loginLabel);
			revalidate();
			repaint();
			JOptionPane optPane = new JOptionPane("Cannot join the game: either the game is full or you are already logged in.",
					JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
			JPanel buttonPanel = (JPanel)optPane.getComponent(1);
			JButton buttonOk = (JButton)buttonPanel.getComponent(0);
			buttonOk.setMargin(new Insets(1,1,1,1));
			JDialog dialog = optPane.createDialog(this,"Error!");
			dialog.setVisible(true);
		}
		// received after all users are ready - receiving original board layout
		else if (event == GameEvent.READY_EVENT) {
		    if (gameInProgress)
		        return;
		    
		    tempcards = e.getNewCards();
		    players = e.getPlayers();
		    
		    scores = new int[players.length];
		    for (int i = 0; i < scores.length; i++)
		    	scores[i] = 0;

			gameInProgress = true;
			
        	readyToField(tempcards);
            draw_stats(players);
            
		}
		// received when a player has won
		else if (event == GameEvent.GAME_OVER_EVENT) {
		    gameIsOver = true;
        	outcome = determine_outcome();
        	endGame(outcome);
        	
		}
		// received when the server updates the board after someone has identified a set
		else if (event == GameEvent.BOARD_UPDATE_EVENT) {
			tempcards = e.getNewCards();
			posn1 = e.getPosition1();
			posn2 = e.getPosition2();
			posn3 = e.getPosition3();
			
			setfinder = e.getSetFinder();
			scores[setfinder]++;

			// the previously found set appears green for WAIT seconds, so the users can see which set was picked
			(buttons.get(posn1)).setBackground(Color.green);
			(buttons.get(posn2)).setBackground(Color.green);
			(buttons.get(posn3)).setBackground(Color.green);

        	delOldAll();
        	gameplay(tempcards);
        	update_stat(setfinder);
			
		}
		/*
		else if (event == GameEvent.REFRESH_EVENT) {
		}    
		*/
		// TESTING
		else if(event == GameEvent.TEST_CONNECTION) {
			JOptionPane.showMessageDialog(this,
					"Connected!", "Testing connection",
                    JOptionPane.INFORMATION_MESSAGE);
		}

	}
	
	// react to user-generated events
	public synchronized void actionPerformed(ActionEvent e){
		// when the ready button is pressed, informs the server and disables the ready button
		if ("ready".equals(e.getActionCommand())) {
			output.sendReadyMsg();
			init.setEnabled(false);
		}
		// when a card-button is pressed, selects or de-selects the card depending on its current state
		else if ("pick".equals(e.getActionCommand())) {
			// checks to see if the card has been already selected
			// if not, selects it; if selected already, de-selects it
			JButton curbutton = (JButton)e.getSource();
			// makes sure that the button that's picked is in the hash 
			// (and not, for example, picked during the WAIT seconds that the cards were green)
			if (buttonhash.containsKey(curbutton)) { 
				if (!curbutton.isSelected()) {
					curbutton.setSelected(true);
					curbutton.setBackground(Color.yellow);
					cardsSelected.add(curbutton);
					if (cardsSelected.size() == 3) {
						/*
						System.out.println("Selected:");
						for (int i = 0; i < cardsSelected.size(); i++)
							System.out.println(cardsSelected.get(i));
						if (buttons.size() != buttonhash.size())
							System.out.println("Error! Size of buttons: " + buttons.size() + "; size of hash: " + buttonhash.size());
						System.out.println("In Arraylist:");
						for (int i = 0; i < buttons.size(); i++)
							System.out.println("Position " + i + ": " + buttons.get(i));
						System.out.println("In Hashmap:");
						Set<Map.Entry<JButton,Integer>> printhash = buttonhash.entrySet();
						Iterator<Map.Entry<JButton,Integer>> itr = printhash.iterator();
						while(itr.hasNext()) {
							Map.Entry<JButton,Integer> cur = itr.next();
							System.out.println(cur.getKey());
							System.out.println(cur.getValue());
							System.out.println("In position " + buttons.indexOf(cur.getKey()));
						}
						*/
						
						JButton card1 = cardsSelected.remove();
						JButton card2 = cardsSelected.remove();
						JButton card3 = cardsSelected.remove();
						
						/*
						System.out.println("Cards selected, processing: ");
						System.out.println(card1);
						System.out.println("Position of card 1: " + buttons.indexOf(card1));
						System.out.println(card2);
						System.out.println("Position of card 2: " + buttons.indexOf(card2));
						System.out.println(card3);
						System.out.println("Position of card 3: " + buttons.indexOf(card3));
						*/

						int cd1 = (buttonhash.get(card1)).intValue();
						int cd2 = (buttonhash.get(card2)).intValue();
						int cd3 = (buttonhash.get(card3)).intValue();
						// check set locally; send to server only if valid set
						boolean setchk = setcheck.isThisASet(cd1, cd2,cd3);
						if (setchk) {			
							stopTime = System.currentTimeMillis();
							duration = stopTime-startTime;
							output.sendSetDeclareMsg(buttons.indexOf(card1), buttons.indexOf(card2), buttons.indexOf(card3), duration);
						}
						else {
							// de-select if not a set
							card1.setSelected(false);
							card1.setBackground(UIManager.getColor("Button.background"));
							card2.setSelected(false);
							card2.setBackground(UIManager.getColor("Button.background"));
							card3.setSelected(false);
							card3.setBackground(UIManager.getColor("Button.background"));
						}
					}
				}
				else { 
					curbutton.setSelected(false);
					curbutton.setBackground(UIManager.getColor("Button.background"));
					cardsSelected.remove(curbutton);
				}
			}
			
		}
		
		/*
		else if ("refresh".equals(e.getActionCommand())) {
		}
		*/
	}
	

    /**
     * Used for mapping saved card images onto buttons.
     * @param path The location of the images
     * @return an ImageIcon, or null if the path was invalid
     */
    private synchronized static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Board.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}