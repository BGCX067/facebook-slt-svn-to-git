package set.comm;

import java.util.Collection;
import java.util.LinkedList;

import set.core.Card;
import set.core.Deck;
import set.core.Set;
import set.core.SetChecker;

public class DatabaseInit
{
    public static void main(String[] args)
    {
        DatabaseConnection db = new DatabaseConnection();
        
        Deck deck = new Deck();
        SetChecker setChk = new SetChecker();
        setChk.build();
        
        LinkedList<Card> allCards = deck.allCards();
        Collection<Set> allSets = setChk.allSets();
        
        System.out.println("Loading Cards...");
        
        for (Card c : allCards)
        {
            if(!db.insertCard(c))
                return;
        }
        
        System.out.println("Loading Sets..."); 
        
        for (Set s : allSets)
        {
            if(!db.insertSet(s))
                return;
        }
        
        System.out.println("Done.");
    }

}
