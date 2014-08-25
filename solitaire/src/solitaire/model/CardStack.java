package solitaire.model;

import solitaire.structure.Stack;

public class CardStack {
	public Stack<Card> cardStack;

	public CardStack() {
		cardStack = new Stack<>();
	}

	public void add(Card card) {
		cardStack.push(card);
	}

}
