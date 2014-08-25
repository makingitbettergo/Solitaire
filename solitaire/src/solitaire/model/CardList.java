package solitaire.model;

import solitaire.structure.SinglyLinkedList;
import solitaire.structure.SinglyLinkedList.Element;
import solitaire.structure.Stack;

/**
 * 
 * @author Yue
 * 
 */
public class CardList {
	public SinglyLinkedList<Card> cards;

	public Card tailCard;// it is actually the head of singlylinkedlst

	public CardList(int openedIndex) {
		cards = new SinglyLinkedList<Card>();
	}

	/**
	 * The tail card actually is the head of SinglyLinkedList
	 * 
	 * @return the head element's value
	 */
	public Card getTailCard() {
		if (tailCard != null) {
			this.tailCard = this.cards.tail();
		} else {
			this.tailCard = this.cards.getTailElement().value;
		}
		return this.tailCard;
	}

	public void addToTail(Card card) {
		this.cards.addLast(card);
		getTailCard();
	}

	/**
	 * removes the tail card, reassign new head tail card must be executed first
	 * 
	 * @return the removed card
	 */
	public Card moveTail() {
		this.tailCard = this.cards.tail();
		Card temp = this.cards.removeLast();
		if (temp.isBack) {
			temp.isBack = false;
		}
		if (this.cards.head == null) {
			tailCard = null;
		} else {
			tailCard = this.cards.tail();
			tailCard.isBack = false;
		}
		return temp;
	}

	/**
	 * return a sub list from current list
	 * 
	 * @param index
	 * @return the cut list
	 * @deprecated
	 */
	public SinglyLinkedList<Card> cut(int index) {
		return cards.subList(index);
	}

	/**
	 * append this list to another list
	 * 
	 * @param otherList
	 * @deprecated
	 */
	public void link(CardList otherList) {
		Stack<Card> stack = new Stack<>();
		Element<Card> finger = cards.head;
		while (finger.hasNext()) {
			stack.push(finger.value);
			finger = finger.next;
		}
		while (stack.size > 0) {
			Card temp = stack.pop();
			otherList.cards.addFirst(temp);
		}
	}

	public static void main(String[] args) {
		CardList cl = new CardList(3);
		for (int i = 0; i < 20; i++) {
			Card temp = new Card(i);
			cl.addToTail(temp);
		}
		System.out.println(cl.cards);
		System.out.println(cl.getTailCard());
		SinglyLinkedList<Card> sll = cl.cut(9);
		System.out.println(cl.cards);
		System.out.println(sll);
	}

}
