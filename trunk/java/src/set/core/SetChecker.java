package set.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableSet;

import set.util.MathUtil;

public class SetChecker
{
    private HashMap<Integer, Set> validSets;
    private HashMap<Integer, Integer> setAliases;

    public SetChecker()
    {
        validSets = new HashMap<Integer, Set>(1080);
        setAliases = new HashMap<Integer, Integer>(6 * 1080);
    }

    
    public void build()
    {
        System.out.println("Building set checking tables...");
        
        buildSetTable();
        buildAliasTable();
        
        System.out.println("Set table entries: " + validSets.size());
        System.out.println("Set alias table entries: " + setAliases.size());
    }
    
    
    /**
     * Builds the internal hash table used for checking sets.
     */
    private void buildSetTable()
    {
        for (int cardC = 0; cardC < 81; cardC++)
        {
            for (int cardB = 0; cardB < cardC; cardB++)
            {
                int c[] = MathUtil.decToBase3Array(cardC);
                int b[] = MathUtil.decToBase3Array(cardB);
                int a[] = new int[4];
                int attributes[] = new int[4];

                // determine the attributes of cardA such that the 3 cards
                // form a valid set
                for (int i = 0; i < a.length; i++)
                {
                    if (b[i] == c[i])
                    {
                        a[i] = b[i];
                        attributes[i] = a[i];
                    }
                    else
                    {
                        a[i] = 3 - b[i] - c[i];
                        attributes[i] = Set.DIFFERENT_ATTRIB;
                    }
                }

                int cardA = a[0] * 27 + a[1] * 9 + a[2] * 3 + a[3];

                if (cardA < cardB)
                {
                    int cards[] = { cardA, cardB, cardC };
                    Set s = new Set(cards, attributes);
                    validSets.put(s.id(), s);
                }
            }
        }
    }
    
    private void buildAliasTable()
    {
        for (Iterator<Set> itr = validSets.values().iterator(); itr.hasNext(); )
        {
            Set s = itr.next();
            int setID = s.id();
            int cardID[] = { s.card(0), s.card(1), s.card(2) };
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++)
                    {
                        if (i != j && i != k && j != k)
                        {
                            int aliasID = Set.computeID(cardID[i], cardID[j], cardID[k]);
                            setAliases.put(aliasID, setID);
                            
                        }
                    }
                }
            }
            
        }
    }
    
    
    public LinkedList<Set> allSets()
    {
        LinkedList<Set> allSets = new LinkedList<Set>();
        
        Collection<Set> internalCollection = validSets.values();
        
        for (Set s : internalCollection)
        {
            allSets.add(s);  //FIXME: s may still be modifiable
        }
        
        return allSets;
    }

    public boolean isThisASet(int id)
    {
        return setAliases.containsKey(id);
    }
    
    public boolean isThisASet(int card1, int card2, int card3)
    {
        int id = Set.computeID(card1, card2, card3);
        return isThisASet(id);
    }
    
    public boolean isThisASet(Card card1, Card card2, Card card3)
    {
        return isThisASet(card1.id(), card2.id(), card3.id());
    }
    
    public Set getSetByID(int aliasID)
    {
        int setID = setAliases.get(aliasID);
        return validSets.get(setID);
    }
    
    public Set getSet(Card card1, Card card2, Card card3)
    {
        int aliasID = Set.computeID(card1.id(), card2.id(), card3.id());
        return getSetByID(aliasID);
    }
    
    /**
     * Determines if a collection of cards contains a set.
     * @param sortedField A sorted collection (such as a TreeSet) of card IDs.
     * @return true if the collection contains a set, false otherwise.
     */
    public boolean isThereASet(NavigableSet<Integer> sortedField)
    {
        for (Iterator<Integer> itr_i = sortedField.iterator(); itr_i.hasNext(); )
        {
            int card1 = itr_i.next();
            NavigableSet<Integer> subset_j = sortedField.tailSet(card1, false);
            
            for(Iterator<Integer> itr_j = subset_j.iterator(); itr_j.hasNext(); )
            {
                int card2 = itr_j.next();
                NavigableSet<Integer> subset_k = sortedField.tailSet(card2, false);
                
                for (Iterator<Integer> itr_k = subset_k.iterator(); itr_k.hasNext(); )
                {
                    int card3 = itr_k.next();
                    if (isThisASet(card1, card2, card3))
                        return true;
                }
            }
        }
            
        return false;
    }
    
    /**
     * Determines if a game field contains a set.
     * 
     * @param field The list of cards to check for sets.
     * @return true, if there is at least one set on the field.
     */
    public boolean isThereASet(List<Card> field)
    {
        for (ListIterator<Card> itr_i = field.listIterator(); itr_i.hasNext(); )
        {
            Card card1 = itr_i.next();
            
            for (ListIterator<Card> itr_j = field.listIterator(itr_i.nextIndex()); itr_j.hasNext(); )
            {
                Card card2 = itr_j.next();
                
                for (ListIterator<Card> itr_k = field.listIterator(itr_j.nextIndex()); itr_k.hasNext(); )
                {
                    Card card3 = itr_k.next();
                    
                    if (isThisASet(card1, card2, card3))
                        return true;
                }
            }
        }
        
        return false;
    }
}
