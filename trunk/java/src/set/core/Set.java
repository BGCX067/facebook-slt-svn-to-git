package set.core;

public class Set
{
    public static final int VALUE = 0;
    public static final int SHAPE = 1;
    public static final int SHADE = 2;
    public static final int COLOR = 3;
    
    public static final int DIFFERENT_ATTRIB = -1;
    
    public static int computeID(int card1, int card2, int card3)
    {
        return card1 * 6561 + card2 * 81 + card3;
    }


    private int[] cards;
    private int[] attributes;
    private int setID;

    public Set(int[] cards, int[] attributes)
    {
        this.cards = cards;
        this.attributes = attributes;
        this.setID = computeID(cards[0], cards[1], cards[2]);
    }
    
    public int card(int i)
    {
        return cards[i];
    }
    
    public int attribute(int i)
    {
        return attributes[i];
    }

    public int id()
    {
        return setID;
    }
    
    public boolean contains(Card card)
    {
        for (int i = 0; i < cards.length; i++)
        {
            if (card.id() == cards[i])
                return true;
        }
        
        return false;
    }
    
}
