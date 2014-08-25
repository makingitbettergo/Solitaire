package solitaire;

import solitaire.controller.SolitaireController;
import solitaire.model.Solitaire;
import solitaire.sound_play.SolitaireSoundPlayer;
import solitaire.view.SolitaireGameFrame;

/*
 * This is the entry of the assignment 2.
 *  
 * Student Name: Yue Li Student ID: 1251124
 * Student Name: Vahe Grigorian Student ID: 1121969
 * Equal contribution
 */
public class SolitaireGame {

	public static void main(String[] args) {
		startGameMVC();
		// startGameCUI();
	}

	public static void startGameCUI() {
		Solitaire solitaire = new Solitaire();
		solitaire.startGameConsole();
	}

	public static void startGameMVC() {
		SolitaireGameFrame view = new SolitaireGameFrame();
		Solitaire model = new Solitaire();
		SolitaireController controller = new SolitaireController();
		view.addController(controller);
		// SolitaireSoundPlayer ssp = new SolitaireSoundPlayer();
		controller.addModel(model);
		controller.addView(view);
		// controller.addSoundPlayer(ssp);
		// ssp.soundPlay();
	}
}
