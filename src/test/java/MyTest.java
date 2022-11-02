import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.ArrayList;
class MyTest {

	@Test
	void test() {
		Deck d = new Deck();
		Card c = new Card('H', 1);
		ThreeCardLogic logic = new ThreeCardLogic();
		assertEquals(d.size(), 52);
	}

	@Test
	void testDeck() {
		Deck d = new Deck();
		assertEquals(d.size(), 52);
		int spades = 0;
		int hearts = 0;
		int clubs = 0;
		int diamonds = 0;
		for (int i = 0; i < 52; i++) {
			// set Club for the current i value
			if (d.get(i).suit == 'C') {
				clubs++;
			}
			if (d.get(i).suit == 'H') {
				hearts++;
			}
			if (d.get(i).suit == 'D') {
				diamonds++;
			}
			if (d.get(i).suit == 'S') {
				spades++;
			}
		}
		assertEquals(13, clubs);
		assertEquals(13, hearts);
		assertEquals(13, diamonds);
		assertEquals(13, spades);

	}

	@Test
	void testNewDeck() {
		Deck d = new Deck();
		assertEquals(d.size(), 52);
		int original_val = d.get(0).value;
		d.newDeck();
		assertNotEquals(d.get(0).value, original_val);
		assertEquals(d.size(), 52); // check size is still right

		original_val = d.get(51).value;
		d.newDeck();
		assertNotEquals(d.get(0).value, original_val);
		assertEquals(d.size(), 52);

		// make sure the newDeck still contains the right elements
		int spades = 0;
		int hearts = 0;
		int clubs = 0;
		int diamonds = 0;
		for (int i = 0; i < 52; i++) {
			// set Club for the current i value
			if (d.get(i).suit == 'C') {
				clubs++;
			}
			if (d.get(i).suit == 'H') {
				hearts++;
			}
			if (d.get(i).suit == 'D') {
				diamonds++;
			}
			if (d.get(i).suit == 'S') {
				spades++;
			}
		}
		assertEquals(13, clubs);
		assertEquals(13, hearts);
		assertEquals(13, diamonds);
		assertEquals(13, spades);

	}

	@Test
	void testLogicFlush() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.clear();
		cards.add(new Card('C', 3));
		cards.add(new Card('C', 3));
		cards.add(new Card('C', 1));
		assertEquals(4, logic.evalHand(cards));
		assertEquals(15, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('H', 7));
		cards.add(new Card('H', 3));
		cards.add(new Card('H', 1));
		assertEquals(4, logic.evalHand(cards));

		cards.clear();
		cards.add(new Card('D', 14));
		cards.add(new Card('D', 13));
		cards.add(new Card('D', 1));
		assertEquals(4, logic.evalHand(cards));

		cards.clear();
		cards.add(new Card('S', 7));
		cards.add(new Card('S', 8));
		cards.add(new Card('S', 5));
		assertEquals(4, logic.evalHand(cards));
		assertEquals(15, logic.evalPPWinnings(cards, 5));
	}

	@Test
	void testLogicStraight() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.add(new Card('C', 2));
		cards.add(new Card('H', 3));
		cards.add(new Card('C', 4));

		// should be a straight
		assertEquals(3, logic.evalHand(cards));

		cards.clear();
		cards.add(new Card('C', 2));
		cards.add(new Card('H', 4));
		cards.add(new Card('C', 3));
		assertEquals(3, logic.evalHand(cards));
		assertEquals(30, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('C', 4));
		cards.add(new Card('H', 3));
		cards.add(new Card('C', 2));
		assertEquals(3, logic.evalHand(cards));
		assertEquals(30, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('C', 4));
		cards.add(new Card('H', 2));
		cards.add(new Card('C', 3));
		assertEquals(3, logic.evalHand(cards));

		cards.clear();
		cards.add(new Card('C', 3));
		cards.add(new Card('H', 4));
		cards.add(new Card('C', 2));
		assertEquals(3, logic.evalHand(cards));

	}

	@Test
	void testLogicStraightFlush() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.clear();
		cards.add(new Card('C', 3));
		cards.add(new Card('C', 2));
		cards.add(new Card('C', 1));
		assertEquals(1, logic.evalHand(cards));
		assertEquals(200, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('H', 11));
		cards.add(new Card('H', 13));
		cards.add(new Card('H', 12));
		assertEquals(1, logic.evalHand(cards));


		cards.clear();
		cards.add(new Card('D', 7));
		cards.add(new Card('D', 5));
		cards.add(new Card('D', 6));
		assertEquals(1, logic.evalHand(cards));
	}

	@Test
	void testLogicThreeKind() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.clear();
		cards.add(new Card('C', 3));
		cards.add(new Card('H', 2));
		cards.add(new Card('D', 3));
		assertNotEquals(2, logic.evalHand(cards));

		cards.clear();
		cards.add(new Card('S', 3));
		cards.add(new Card('H', 3));
		cards.add(new Card('C', 3));
		assertEquals(2, logic.evalHand(cards));
		assertEquals(150, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('S', 14));
		cards.add(new Card('D', 14));
		cards.add(new Card('C', 14));
		assertEquals(2, logic.evalHand(cards));
	}

	@Test
	void testLogicPair() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.clear();
		cards.add(new Card('S', 3));
		cards.add(new Card('S', 2));
		cards.add(new Card('D', 3));
		assertEquals(5, logic.evalHand(cards));
		assertEquals(25, logic.evalPPWinnings(cards, 5));


		cards.clear();
		cards.add(new Card('C', 7));
		cards.add(new Card('H', 7));
		cards.add(new Card('D', 3));
		assertEquals(5, logic.evalHand(cards));
		assertEquals(25, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('C', 3));
		cards.add(new Card('S', 2));
		cards.add(new Card('D', 2));
		assertEquals(5, logic.evalHand(cards));

	}

	@Test
	void testLogicHigh() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.clear();
		cards.add(new Card('S', 13));
		cards.add(new Card('S', 2));
		cards.add(new Card('D', 3));
		assertEquals(0, logic.evalHand(cards));
		assertEquals(0, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('S', 12));
		cards.add(new Card('S', 2));
		cards.add(new Card('D', 14));
		assertEquals(0, logic.evalHand(cards));
		assertEquals(0, logic.evalPPWinnings(cards, 5));

		cards.clear();
		cards.add(new Card('S', 11));
		cards.add(new Card('S', 2));
		cards.add(new Card('D', 3));
		assertNotEquals(0, logic.evalHand(cards));

	}

	@Test
	void testLogicCompare() {
		Deck d = new Deck();
		ThreeCardLogic logic = new ThreeCardLogic();
		ArrayList<Card> dealer = new ArrayList<Card>();
		ArrayList<Card> player = new ArrayList<Card>();

		dealer.clear();
		dealer.add(new Card('S', 13));
		dealer.add(new Card('S', 2));
		dealer.add(new Card('D', 3));

		player.clear();
		player.add(new Card('S', 12));
		player.add(new Card('S', 2));
		player.add(new Card('D', 14));

		assertEquals(2, logic.compareHands(dealer, player));

		dealer.clear();
		dealer.add(new Card('S', 2));
		dealer.add(new Card('S', 3));
		dealer.add(new Card('D', 4));

		player.clear();
		player.add(new Card('S', 2));
		player.add(new Card('S', 5));
		player.add(new Card('D', 7));

		assertEquals(1, logic.compareHands(dealer, player));


		dealer.clear();
		dealer.add(new Card('S', 2));
		dealer.add(new Card('S', 3));
		dealer.add(new Card('D', 4));

		player.clear();
		player.add(new Card('S', 2));
		player.add(new Card('S', 5));
		player.add(new Card('D', 7));

		assertEquals(1, logic.compareHands(dealer, player));
	}
}