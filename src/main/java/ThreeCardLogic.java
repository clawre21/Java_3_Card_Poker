import java.util.ArrayList;
public class ThreeCardLogic {
    private void sort(ArrayList <Card> hand){
        // sort array based on value
        Card temp = new Card('H', 2);
        if(hand.get(0).value > hand.get(1).value){
            temp = hand.get(1);
            hand.set(1, hand.get(0));
            hand.set(0, temp);
        }
        if(hand.get(1).value < hand.get(2).value){
            return;
        }
        if(hand.get(1).value > hand.get(2).value){
            temp =  hand.get(2);
            hand.set(2, hand.get(1));
            hand.set(1, temp);
        }
        if(hand.get(0).value > hand.get(1).value){
            temp = hand.get(1);
            hand.set(1, hand.get(0));
            hand.set(0, temp);
        }
    }

    int evalHand(ArrayList <Card> hand){
        sort(hand);
        boolean straight = false;
        boolean flush = false;

        // check for straight
        if(((hand.get(0).value+1) == hand.get(1).value) && ((hand.get(1).value+1) == hand.get(2).value)){
            straight = true;
        }

        // check for flush
        if((hand.get(0).suit == hand.get(1).suit) && (hand.get(1).suit == hand.get(2).suit)){
            flush = true;
        }

        if(straight && flush){
            straight = false;
            flush = false;
            return 1;
        }

        // check for 3 of a kind
        if ((hand.get(0).suit != hand.get(1).suit) && (hand.get(0).suit != hand.get(2).suit)
                && (hand.get(1).suit != hand.get(2).suit)){ // check different suit
            if ((hand.get(0).value == hand.get(2).value)) { // check that it's all the same values
                return 2;
            }
        }

        // check for straight
        if(straight){
            return 3;
        }

        // check for flush
        if(flush){
            return 4;
        }

        // check for pair
        // if any two values equal
        if ((hand.get(0).value == hand.get(1).value) || (hand.get(0).value == hand.get(2).value)
                || (hand.get(1).value == hand.get(2).value)){
            return 5;
        }

        // just high card
        // last element should be the highest card because it's been sorted
        if ((hand.get(2).value == 12) || (hand.get(2).value == 13)
                || hand.get(2).value == 14){
            return 0;
        }

        return -1; // person lost?
    }

    int evalPPWinnings(ArrayList <Card> hand, int bet){
        int handType = evalHand(hand);
        // check if
        if(handType == 1){ // straight flush
            bet = bet*40;
        }
        if(handType == 2){ // three of a kind
            bet = bet*30;
        }
        if(handType == 3){ // straight
            bet = bet*6;
        }
        if(handType == 4){ // flush
            bet = bet*3;
        }
        if(handType == 5){ // pair
            bet = bet*5;
        }
        if(handType == 0){ // only high card
            bet = 0; // ?? is this right
        }
        return bet;
    }

    int compareHands(ArrayList <Card> dealer, ArrayList <Card> player){
        int dealerType = evalHand(dealer);
        int playerType = evalHand(player);

        // check if dealer has queen high
        if(dealerType == -1){
            // need to replay/push hand to next round
            return -1;
        }
        if((playerType == -1)){ // player does not have high card, but dealer does
            return 1; // dealer wins
        }
        // same hand type, check highest card
        if(playerType == dealerType){
            if(playerType == 5){ // it's a pair
                // check pair, pair value has to be in the middle cause there's only 3 cards
                if(dealer.get(1).value > player.get(1).value){ // dealer wins
                    return 1;
                }
                else if(dealer.get(1).value < player.get(1).value){ // dealer wins
                    return 2;
                }
                else{
                    if(dealer.get(2).value > player.get(2).value){ // dealer wins
                        return 1;
                    }
                    else if(dealer.get(2).value < player.get(2).value){ // dealer wins
                        return 2;
                    }
                    else{
                        return 0; // tie
                    }
                }
            }
            else if(dealer.get(2).value > player.get(2).value){ // dealer wins
                return 1;
            }
            else if(dealer.get(2).value < player.get(2).value){ // dealer wins
                return 2;
            }
            else{
                return 0; // tie
            }

        }
        if(playerType == 0 && dealerType != 0){ // player has high card only, dealer wins
            return 1;
        }
        if(dealerType == 0 && playerType != 0){ // dealer has high card only, player wins
            return 2;
        }
        if((dealerType < playerType)){ // dealer wins
            return 1;
        }
        if(playerType < dealerType){ // player wins
            return 2;
        }
        return 0;
    }
}