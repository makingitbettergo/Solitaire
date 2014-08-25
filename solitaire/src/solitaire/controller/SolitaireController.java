package solitaire.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import solitaire.model.Card;
import solitaire.model.Solitaire;
import solitaire.sound_play.SolitaireSoundPlayer;
import solitaire.view.CardLabel;
import solitaire.view.CardsPanel;
import solitaire.view.SolitaireGameFrame;

public class SolitaireController extends MouseAdapter {

	public SolitaireGameFrame view;
	public Solitaire model;
	public Card processingCard;
	public SolitaireSoundPlayer ssp;

	/**
	 * This is the GUI controller to manipulate all the data processes for the
	 * mvc extension
	 */

	public void addView(SolitaireGameFrame view) {
		this.view = view;
	}

	public void addModel(Solitaire model) {
		this.model = model;
	}

	public void addSoundPlayer(SolitaireSoundPlayer ssp) {
		this.ssp = ssp;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource() instanceof CardLabel) {
			CardLabel lbl = (CardLabel) arg0.getSource();
			if (processingCard != null && lbl.value != null) {
				// lst index nums
				int[] indices = model.findElementIndices(processingCard.toString());
				// send
				if (processingCard.equals(lbl.value)) {
					model.sendFromList(processingCard, model.lists[indices[0]]);
					processingCard = null;
				} else {
					// link
					int[] targetIndices = model.findElementIndices(lbl.value.toString());
					if (targetIndices[0] != 9) {
						model.link(processingCard, indices[0], indices[1], targetIndices[0]);
					}
					processingCard = null;
				}
			} else if (processingCard != null && arg0.getSource() instanceof CardLabel
					&& ((CardLabel) arg0.getSource()).value == null) {
				// link to an empty card space
				CardLabel temp = (CardLabel) arg0.getSource();
				CardsPanel pnl = (CardsPanel) temp.getParent();
				int indices[] = model.findElementIndices(processingCard.toString());
				model.link(processingCard, indices[0], indices[1], pnl.index);
				processingCard = null;
			} else if (lbl.equals(this.view.deckOpenCardLbl)) {
				// send from deck
				model.sendFromDeck(lbl.value);
			} else if (lbl.value != null && !lbl.value.isBack
					&& !lbl.equals(this.view.deckOpenCardLbl)) {
				// find a link
				processingCard = lbl.value;
				lbl.setIcon(processingCard.getSelectedIcon());
				int[] indices = model.findElementIndices(processingCard.toString());
				int count = model.lists[indices[0]].cards.size - 1;
				while (count >= indices[1]) {
					CardLabel test = (CardLabel) view.cardLstPnls[indices[0]].getComponent(count);
					test.setIcon(test.value.getSelectedIcon());
					count--;
				}
			}
			lbl.repaint();
		} else if (arg0.getSource() instanceof JLabel
				&& ((JLabel) arg0.getSource()).equals(view.lblBack)) {
			model.drawCard();
		} else if (arg0.getSource() instanceof JLabel
				&& ((JLabel) arg0.getSource()).equals(new JLabel(new ImageIcon(getClass()
						.getResource("/solitaire/res/cards/Empty.png"))))) {
		} else if (arg0.getSource() instanceof JLabel) {
			// deckto
			JLabel lbl = (JLabel) arg0.getSource();
			if (lbl.getParent() instanceof CardsPanel) {
				CardsPanel cp = (CardsPanel) lbl.getParent();
				model.deckTo(cp.index);
				processingCard = null;
			}
		}
	}

}
