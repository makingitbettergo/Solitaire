package solitaire.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import solitaire.controller.SolitaireController;
import solitaire.model.Card;
import solitaire.model.Solitaire;
import solitaire.sound_play.SolitaireSoundPlayer;

public class SolitaireGameFrame extends JFrame implements Observer {

	public SolitaireController controller;
	// holds the top card of the stack
	public JLabel[] stackLbls;
	// holds the cardLst
	public CardsPanel[] cardLstPnls;
	public CardLabel deckOpenCardLbl;
	public JLabel lblBack;

	/**
	 * create a JFrame to hold all the game elements.
	 */
	public SolitaireGameFrame() {
		setMinimumSize(new Dimension(730, 700));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new JLabel(new ImageIcon(getClass().getResource(
				"/solitaire/res/cards/background.png"))));
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickStartNewGame(e);
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeApp();
			}
		});

		JMenu mnNewMenu = new JMenu("Option");
		menuBar.add(mnNewMenu);

		JMenuItem mntmOption = new JMenuItem("Help");
		mntmOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelpMessages();
			}
		});
		mnNewMenu.add(mntmOption);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnNewMenu.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showVersionMessages();
			}
		});
		getContentPane().setLayout(null);
		setUpPanels();
		setVisible(true);
	}

	/**
	 * add controller to the view
	 * 
	 * @param controller
	 */
	public void addController(SolitaireController controller) {
		this.controller = controller;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Solitaire) {
			updateGraphicalUI();
		}
	}

	private void updateGraphicalUI() {
		// test if the open card is empty, dont display anything
		if (!controller.model.deck.isEmpty() && controller.model.deck.currentCard != null) {
			deckOpenCardLbl.setIcon(controller.model.deck.currentCard.getDefaultIcon());
			deckOpenCardLbl.value = controller.model.deck.currentCard;
		} else if (controller.model.deck.isEmpty() && controller.model.deck.queue.isEmpty()) {
			lblBack.setIcon(new ImageIcon("/solitaire/res/cards/Empty.png"));
			deckOpenCardLbl.setIcon(new ImageIcon("/solitaire/res/cards/Empty.png"));
		} else {
			deckOpenCardLbl.setIcon(new ImageIcon("/solitaire/res/cards/Empty.png"));
		}
		updatePanels();
		updateStacks();
		this.revalidate();
		if (controller.model.isWon()) {
			JOptionPane.showMessageDialog(this, "you solved Solitaire!!");
		}
	}

	/**
	 * start new game.
	 * 
	 * @param e
	 */
	public void clickStartNewGame(ActionEvent e) {
		initializeGame();
	}

	private void initializeGame() {
		lblBack.removeMouseListener(controller);
		deckOpenCardLbl.removeMouseListener(controller);
		// edited starts
		deckOpenCardLbl.value = controller.model.deck.currentCard;
		// edited ends
		controller.model.deleteObservers();
		controller.processingCard = null;
		for (JPanel panel : cardLstPnls) {
			panel.removeAll();
		}
		SolitaireSoundPlayer ssp = controller.ssp;
		controller = new SolitaireController();
		controller.ssp = ssp;
		controller.addView(this);
		controller.model = new Solitaire();
		controller.model.startGameForGUI();
		deckOpenCardLbl.setIcon(controller.model.deck.currentCard.getDefaultIcon());
		lblBack.setIcon(new ImageIcon(getClass().getResource("/solitaire/res/cards/Back.png")));
		lblBack.addMouseListener(controller);
		deckOpenCardLbl.addMouseListener(controller);
		updatePanels();
		updateStacks();
		this.controller.model.addObserver(this);
		repaint();
		revalidate();
	}

	private void setUpPanels() {
		JPanel deckPanel = new JPanel();
		deckPanel.setBounds(17, 12, 228, 125);
		deckPanel.setOpaque(false);
		getContentPane().add(deckPanel);
		deckPanel.setLayout(null);
		lblBack = new JLabel();
		lblBack.setBounds(12, 6, 75, 108);
		deckPanel.add(lblBack);

		deckOpenCardLbl = new CardLabel(null, null);
		deckOpenCardLbl.setBounds(128, 7, 76, 110);
		deckPanel.add(deckOpenCardLbl);

		JPanel stackPanel = new JPanel();
		stackPanel.setOpaque(false);
		stackPanel.setBounds(298, 12, 394, 125);
		getContentPane().add(stackPanel);
		stackPanel.setLayout(new GridLayout(1, 4, 0, 0));

		JLabel stackHeartLbl = new JLabel("");
		stackPanel.add(stackHeartLbl);

		JLabel stackSpadeLbl = new JLabel("");
		stackPanel.add(stackSpadeLbl);

		JLabel stackClubsLbl = new JLabel("");
		stackPanel.add(stackClubsLbl);

		JLabel stackDiamondsLbl = new JLabel("");
		stackPanel.add(stackDiamondsLbl);
		this.stackLbls = new JLabel[4];
		this.stackLbls[0] = stackHeartLbl;
		this.stackLbls[1] = stackSpadeLbl;
		this.stackLbls[2] = stackClubsLbl;
		this.stackLbls[3] = stackDiamondsLbl;

		CardsPanel cardLstPnl_1 = new CardsPanel();
		cardLstPnl_1.setOpaque(false);
		cardLstPnl_1.setBounds(17, 156, 88, 484);
		getContentPane().add(cardLstPnl_1);
		cardLstPnl_1.addIndex(0);
		CardsPanel cardLstPnl_2 = new CardsPanel();
		cardLstPnl_2.setOpaque(false);
		cardLstPnl_2.setBounds(117, 156, 88, 484);
		getContentPane().add(cardLstPnl_2);
		cardLstPnl_2.addIndex(1);

		CardsPanel cardLstPnl_3 = new CardsPanel();
		cardLstPnl_3.setOpaque(false);
		cardLstPnl_3.setBounds(217, 156, 88, 484);
		getContentPane().add(cardLstPnl_3);
		cardLstPnl_3.addIndex(2);

		CardsPanel cardLstPnl_4 = new CardsPanel();
		cardLstPnl_4.setOpaque(false);
		cardLstPnl_4.setBounds(317, 156, 88, 484);
		getContentPane().add(cardLstPnl_4);
		cardLstPnl_4.addIndex(3);

		CardsPanel cardLstPnl_5 = new CardsPanel();
		cardLstPnl_5.setOpaque(false);
		cardLstPnl_5.setBounds(417, 156, 88, 484);
		getContentPane().add(cardLstPnl_5);
		cardLstPnl_5.addIndex(4);

		CardsPanel cardLstPnl_6 = new CardsPanel();
		cardLstPnl_6.setOpaque(false);
		cardLstPnl_6.setBounds(517, 156, 88, 484);
		getContentPane().add(cardLstPnl_6);
		cardLstPnl_6.addIndex(5);

		CardsPanel cardLstPnl_7 = new CardsPanel();
		cardLstPnl_7.setOpaque(false);
		cardLstPnl_7.setBounds(617, 156, 88, 484);
		getContentPane().add(cardLstPnl_7);
		cardLstPnl_7.addIndex(6);
		cardLstPnls = new CardsPanel[7];
		cardLstPnls[0] = cardLstPnl_1;
		cardLstPnl_1.setLayout(new BoxLayout(cardLstPnl_1, BoxLayout.Y_AXIS));
		cardLstPnls[1] = cardLstPnl_2;
		cardLstPnl_2.setLayout(new BoxLayout(cardLstPnl_2, BoxLayout.Y_AXIS));
		cardLstPnls[2] = cardLstPnl_3;
		cardLstPnl_3.setLayout(new BoxLayout(cardLstPnl_3, BoxLayout.Y_AXIS));
		cardLstPnls[3] = cardLstPnl_4;
		cardLstPnl_4.setLayout(new BoxLayout(cardLstPnl_4, BoxLayout.Y_AXIS));
		cardLstPnls[4] = cardLstPnl_5;
		cardLstPnl_5.setLayout(new BoxLayout(cardLstPnl_5, BoxLayout.Y_AXIS));
		cardLstPnls[5] = cardLstPnl_6;
		cardLstPnl_6.setLayout(new BoxLayout(cardLstPnl_6, BoxLayout.Y_AXIS));
		cardLstPnls[6] = cardLstPnl_7;
		cardLstPnl_7.setLayout(new BoxLayout(cardLstPnl_7, BoxLayout.Y_AXIS));
	}

	private void updatePanels() {
		for (int i = 0; i < 7; i++) {
			cardLstPnls[i].removeAll();
			int size = controller.model.lists[i].cards.size();
			int index = 0;
			while (index < size) {
				Card temp = controller.model.lists[i].cards.getIndex(index);
				CardLabel lbl = new CardLabel(temp.getDefaultIcon(), temp);
				lbl.addMouseListener(controller);
				// to show tiled cards
				if (index != size - 1) {
					lbl.setBorder(new EmptyBorder(0, 0, -80, 0));
				}
				cardLstPnls[i].add(lbl);
				index++;
			}
			// if list doesnt have any card
			if (cardLstPnls[i].getComponents().length == 0) {
				CardLabel lbl = new CardLabel(new ImageIcon(getClass().getResource(
						"/solitaire/res/cards/Empty.png")), null);
				lbl.addMouseListener(controller);
				cardLstPnls[i].add(lbl);
			}
			// if list has card, add an jlabel to trigger the deckto event
			if (!controller.model.deck.isEmpty()) {
				JLabel arrow = new JLabel(new ImageIcon(getClass().getResource(
						"/solitaire/res/cards/arrow.png")));
				arrow.addMouseListener(controller);
				cardLstPnls[i].add(arrow);
			}
			cardLstPnls[i].repaint();
		}
	}

	private void updateStacks() {
		for (int i = 0; i < 4; i++) {
			Card card = null;
			if (controller.model.stacks[i].cardStack.size() > 0) {
				card = controller.model.stacks[i].cardStack.getFirst();
			}
			if (card != null) {
				this.stackLbls[i].setIcon(new ImageIcon(getClass().getResource(
						"/solitaire/res/cards/" + card.toString() + ".png")));
			} else {
				this.stackLbls[i].setIcon(new ImageIcon(getClass().getResource(
						"/solitaire/res/cards/defaultStack.png")));
			}
		}
	}

	/**
	 * to show the help message
	 */
	private void showHelpMessages() {
		String text = "-Click on the deck to drawcard\n-Click on cards to choose card\n"
				+ "-Once cards are selected, click on target list to link\n cards into another card list\n"
				+ "-Double click on the same card to send card to the stack\n-Click on the open card to send the card to stacks";
		JOptionPane.showMessageDialog(this, text, "Help", JOptionPane.PLAIN_MESSAGE);
	}

	private void showVersionMessages() {
		String text = "This Solitaire game is developed for \"Data Structure and Algorithms\"@AUT By Yue";
		JOptionPane.showMessageDialog(this, text, "About", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * close this jframe
	 */
	private void closeApp() {
		controller.ssp.terminatePlay();
		this.dispose();
	}
}
