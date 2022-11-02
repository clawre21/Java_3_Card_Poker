import java.util.ArrayList;
import java.util.Random;

public class Dealer {
    Deck theDeck;
    ArrayList <Card> dealersHand;
    ArrayList <Card> newHand;
    int round = 1;

    Dealer(){
        // initializing the deck to a new deck and the hand to a list of cards
        theDeck = new Deck();
        dealersHand = new ArrayList <Card>();
    }


    public ArrayList<Card> dealHand(){
        if(theDeck.size() <= 34){
            theDeck.newDeck();
        }
        return theDeck.getCard();
    };
}

