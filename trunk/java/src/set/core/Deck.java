package set.core;

import java.util.LinkedList;
import java.util.Random;

public class Deck
{
    private LinkedList<Card> deck_list;
    private Random rng;

    /**
     * Constructs a new deck of cards.
     */
    public Deck()
    {
        deck_list = new LinkedList<Card>();
        rng = new Random();

        for (int count1 = 0; count1 < 3; count1++)
        {
            for (int count2 = 0; count2 < 3; count2++)
            {
                for (int count3 = 0; count3 < 3; count3++)
                {
                    for (int count4 = 0; count4 < 3; count4++)
                    {
                        Card newCard = new Card(count1, count2, count3, count4);
                        deck_list.add(newCard);
                    }
                }
            }
        }
    }

    /**
     * @return the number of cards left in the deck.
     */
    public int size()
    {
        return deck_list.size();
    }

    /**
     * @return true if the deck has no cards left.
     */
    public boolean isEmpty()
    {
        return (deck_list.size() == 0);
    }

    /**
     * Randomly selects a card and removes it from the deck.
     * 
     * @return the card that was drawn.
     */
    public Card getCard()
    {
        if (size() - 1 == 0) // if there is only one card left,
            return deck_list.remove(0); // remove that card

        int rand = rng.nextInt(size() - 1);
        return deck_list.remove(rand);
    }

    //FIXME: internal deck list should not be modifiable
    public LinkedList<Card> allCards()
    {
        return deck_list;
    }
}