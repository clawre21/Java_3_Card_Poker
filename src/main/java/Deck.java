import java.util.ArrayList;
import java.util.*; // for shuffling, can I have this?
public class Deck extends ArrayList<Card> {
    int available = 52;
    Deck(){
        for (int i = 2; i < 15; i++){
            // set Club for the current i value
            this.add(new Card('C', i));
            // set Diamond for the current i value
            this.add(new Card('D', i));
            // set Spade
            this.add(new Card('S', i));
            // Set Heart
            this.add(new Card('H', i));
        }
        // randomize deck
        Collections.shuffle(this);

    }

    void newDeck(){
        // make all cards available again
        this.clear();
        for (int i = 2; i < 15; i++){
            // set Club for the current i value
            this.add(new Card('C', i));
            // set Diamond for the current i value
            this.add(new Card('D', i));
            // set Spade
            this.add(new Card('S', i));
            // Set Heart
            this.add(new Card('H', i));
        }
        Collections.shuffle(this);
    }


    ArrayList <Card> getCard(){
        ArrayList <Card> threeCards = new ArrayList <Card>();
        // removes the first three card from the deck when called
        threeCards.add(this.remove(0));
        threeCards.add(this.remove(0));
        threeCards.add(this.remove(0));

        return threeCards;
    }

    void dump(){
        ArrayList <Card> dumpArr = new ArrayList <Card>();
        // removes the first three card from the deck when called
        for (int i=0; i<this.size(); i++){
           System.out.print(this.get(i).suit);
            System.out.print(" ");
            System.out.print(this.get(i).value);
        }
    }


}
