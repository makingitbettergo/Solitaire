package solitaire.model;

import java.util.Random;

import solitaire.structure.CircularLinkedList;
import solitaire.structure.Queue;

public class CardDeck {

	public CircularLinkedList<Card> cards;
	public Card currentCard;
	public Queue<Card> queue;

	public CardDeck() {
		this.cards = new CircularLinkedList<>();
		queue = new Queue<>();
		initCards();
		currentCard = cards.getFirst();
	}

	/**
	 * draw a card from the deck.
	 */
	public void drawCard() {
		if (cards.size() == 0 && queue.size() > 0) {
			cards = new CircularLinkedList<>();
			while (!queue.isEmpty()) {
				Card temp = queue.dequeue();
				cards.addLast(temp);
			}
			currentCard = cards.getFirst();
		} else if (cards.size() > 0) {
			Card removed = cards.removeFirst();
			queue.enqueue(removed);
			if (cards.size() > 0) {
				currentCard = cards.getFirst();
				currentCard.isBack = false;
			} else {
				currentCard = null;
			}
		}
	}

	public Card takeCard() {
		Card card = null;
		card = cards.removeFirst();
		if (cards.size() > 0) {
			currentCard = cards.getFirst();
		}
		return card;
	}

	public boolean isEmpty() {
		return cards.size == 0;
	}

	private void initCards() {
		int[] indices = shuffle();
		for (int i : indices) {
			Card temp = new Card(i);
			this.cards.addFirst(temp);
		}

	}

	private int[] shuffle() {
		int[] cardsRaw = new int[52];
		for (int i = 0; i < 52; i++) {
			cardsRaw[i] = i;
		}
		// Fisher-Yates_shuffle
		Random ran = new Random();
		for (int i = 51; i > 0; i--) {
			int temp = cardsRaw[i];
			int inRangeIndex = ran.nextInt(i);
			cardsRaw[i] = cardsRaw[inRangeIndex];
			cardsRaw[inRangeIndex] = temp;
		}
		return cardsRaw;
	}

	public static void main(String[] args) {
		CardDeck cd = new CardDeck();
		// while (!cd.isEmpty()) {
		// System.out.println(cd.cards.size());
		// cd.takeCard();
		// }
		// System.out.println(cd.cards);
		int[] rlt = cd.shuffle();
		for (int i : rlt) {
			System.out.println(i);
		}
	}

}
