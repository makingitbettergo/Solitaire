package solitaire.model;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A card is an implementation of Abstract card
 * 
 * @author Yue Li
 * @version 25-04-14
 * 
 */
public class Card extends AbstractCard<Card> {

	public boolean isBack;
	public boolean isSelected;

	public Card(int index) {
		super(index);
		this.isSelected = false;
	}

	@Override
	public int getSuit() {
		return super.cardIndex / 13;
	}

	@Override
	public int getFace() {
		int result = (super.cardIndex % 13);
		return result + 1;
	}

	public Color colour() {
		if (getSuit() == 0 || getSuit() == 3) {
			return Color.RED;
		} else {
			return Color.BLACK;
		}
	}

	@Override
	public String getValue() {
		String result = "";
		switch (getFace()) {
		case 1:
			result += "A";
			break;
		case 11:
			result += "J";
			break;
		case 12:
			result += "Q";
			break;
		case 13:
			result += "K";
			break;
		default:
			result += getFace();
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		if (!isBack) {
			switch (getSuit()) {
			case 0:
				result += "Heart";
				break;
			case 1:
				result += "Spade";
				break;
			case 2:
				result += "Club";
				break;
			case 3:
				result += "Diamond";
				break;
			default:
				break;
			}
			result += getValue();
		} else {
			result += "Back";
		}
		return result;
	}

	public Icon getDefaultIcon() {
		Icon icon = null;
		if (!this.isBack) {
			icon = new ImageIcon(getClass().getResource(
					"/solitaire/res/cards/" + this.toString() + ".png"));
		} else {
			icon = new ImageIcon(getClass().getResource("/solitaire/res/cards/Back.png"));
		}
		return icon;
	}

	public Icon getSelectedIcon() {
		Icon icon = null;
		if (!this.isBack) {
			icon = new ImageIcon(getClass().getResource(
					"/solitaire/res/cards/" + this.toString() + "t.png"));
		} else {
			icon = new ImageIcon(getClass().getResource("/solitaire/res/cards/Back.png"));
		}
		return icon;
	}

	@Deprecated
	public void painThis(Graphics g) {
		g.drawString(toString(), 5, 5);
		g.fillRect(10, 10, 50, 85);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 52; i++) {
			Card card = new Card(i);
			System.out.println(card.toString());
		}
	}

}
