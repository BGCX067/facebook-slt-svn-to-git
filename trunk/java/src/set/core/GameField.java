package set.core;

import java.util.ArrayList;

public class GameField
{
	private ArrayList<Card> field;
	private Deck deck;
	private SetChecker setChk;
	
	/**
	 * Places 12 cards on the game field (or more if there is no set).
	 * @param setChk The class that is used to check sets.
	 */
	public GameField(SetChecker setChk)
	{
	    deck = new Deck();
	    field = new ArrayList<Card>(21);
	    this.setChk = setChk;
	    
	    for (int i = 0; i < 12; i++)
	        field.add(deck.getCard());
	    
	    while (!setChk.isThereASet(field))
	    {
	        field.add(deck.getCard());
	        field.add(deck.getCard());
	        field.add(deck.getCard());
	    }
	}
	
	/**
	 * @return A list of the cards currently on the field.
	 */
	public ArrayList<Card> getField()
	{
	    return field;
	}
	
	/**
	 * Gets the card at the specified position on the field.
	 * 
	 * @param pos The position of the card.
	 * @return the card.
	 */
	public Card getCard(int pos)
	{
	    return field.get(pos);
	}
	
	
	/**
	 * Removes the specified cards from the field. If there are less than 12
	 * cards on the field, or there are no sets on the field, draws new cards
	 * if possible.
	 * 
	 * @param card_i The position of the first card.
	 * @param card_j The position of the second card.
	 * @param card_k The position of the third card.
	 * @return true, if after removing the cards and drawing new ones, there
	 *  are still sets remaining on the field.<br />
	 *  false, if the deck is empty and there are no sets left on the field.
	 */
	public boolean removeSet(int card_i, int card_j, int card_k)
	{
	    if (field.size() == 12 && deck.size() >= 3)
	    {
	        Card newCard1 = deck.getCard();
            Card newCard2 = deck.getCard();
            Card newCard3 = deck.getCard();
            
            field.set(card_i, newCard1);
            field.set(card_j, newCard2);
            field.set(card_k, newCard3);
	    }
	    else
	    {
			Card card_1 = field.get(card_i);
			Card card_2 = field.get(card_j);
			Card card_3 = field.get(card_k);
	        field.remove(card_1);
	        field.remove(card_2);
	        field.remove(card_3);
	    }
	    
	    while (!setChk.isThereASet(field))
	    {
	        if (deck.size() < 3)
	            return false;
	        
	        field.add(deck.getCard());
	        field.add(deck.getCard());
	        field.add(deck.getCard());
	    }
	    
	    return true;
	}
}
