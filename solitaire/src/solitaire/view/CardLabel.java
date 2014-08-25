package solitaire.view;

import javax.swing.Icon;
import javax.swing.JLabel;

import solitaire.model.Card;

public class CardLabel extends JLabel {

	public Card value;

	public CardLabel(Icon icon, Card value) {
		super(icon);
		this.value = value;
	}
}
