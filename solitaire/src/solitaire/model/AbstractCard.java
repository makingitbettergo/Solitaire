package solitaire.model;

import java.awt.Color;

public abstract class AbstractCard<T> {
	public static final int ACE = 1;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int JOKER = 14;
	public static final int HEART = 0;
	public static final int SPADES = 1;
	public static final int CLUB = 2;
	public static final int DIAMONDS = 3;

	public int cardIndex;
	public int value;
	public Color colour;

	public AbstractCard(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	abstract public int getSuit();

	abstract public int getFace();

	abstract public String getValue();

	abstract public String toString();

}
