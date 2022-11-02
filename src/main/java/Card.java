public class Card {
    public char suit;
    public int value;

    public boolean available;
    Card(char suit, int value){
        this.suit = suit;
        this.value = value;
        available = true;
    }

    public String toString(){
        return String.valueOf(suit)+String.valueOf(value);
    }


}
