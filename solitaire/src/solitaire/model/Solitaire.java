package solitaire.model;

import java.util.Observable;
import java.util.Scanner;

import solitaire.structure.Stack;

/*
 * Solitaire core. This class is implemented for MVC pattern. Meanwhile it also
 * remains the functions for the cli version
 * 
 * Student Name: Yue Li Student ID: 1251124
 * Student Name: Vahe Grigorian Student ID: 1121969
 * Equal contribution
 */
public class Solitaire extends Observable {

	public CardDeck deck;
	public CardStack[] stacks = new CardStack[4];
	public CardList[] lists = new CardList[7];
	public boolean isGameRunning;
	private Scanner input;
	private boolean sessionContinue;
	private final String[] commands = { "DrawCard", "DeckTo", "Link", "Send", "Restart", "Help",
			"Quit" };

	public Solitaire() {
		isGameRunning = true;
		initSolitaireConsole();
	}

	/**
	 * initialize game data.
	 */
	public void initSolitaireConsole() {
		input = new Scanner(System.in);
		sessionContinue = true;
		for (int stackIndex = 0; stackIndex < this.stacks.length; stackIndex++) {
			this.stacks[stackIndex] = new CardStack();
		}
		for (int lstNum = 0; lstNum < 7; lstNum++) {
			for (int cardCount = 0; cardCount < lstNum + 1; cardCount++) {
				this.lists[lstNum] = new CardList(lstNum / 2);
			}
		}
		this.deck = new CardDeck();
	}

	/**
	 * after each move, the game checks if the wing condition is filled.
	 * 
	 * @return true if game is completed, false otherwise
	 */
	public boolean isWon() {
		boolean allStacksFilled = false;
		if (stacks[0].cardStack.size == 13 && stacks[1].cardStack.size == 13
				&& stacks[2].cardStack.size == 13 && stacks[3].cardStack.size == 13) {
			allStacksFilled = true;
		}
		if (allStacksFilled) {
			System.out.println("Congratulations! You have won the game.");
		}
		return allStacksFilled;
	}

	/**
	 * helper method for receiving user input in order to control game flow in
	 * console
	 * 
	 * @return
	 */
	private boolean startOrEnd() {
		String userIn = null;
		boolean isValid = false;
		do {
			System.out.println("Please enter the number of the entry" + "\n#1 New\n#2 Stop");
			userIn = input.nextLine();
			isValid = userIn.equals("1") || userIn.equals("2");
		} while (userIn == null || !isValid);
		if (userIn.equals("1")) {
			return true;
		} else {
			endGame();
			return false;
		}
	}

	public void startGameConsole() {
		while (isGameRunning) {
			displayAsciiWelcomeMessage();
			distributeCards();
			if (startOrEnd()) {
				System.out.println("A new game started, have fun");
				while (solitaireGameUpdate()) {
					String message = "Type in command: ";
					for (String text : commands) {
						message += text + " | ";
					}
					System.out.println("--------------Your move-----------------\n" + message);
					String text = input.nextLine();
					executeCommand(text);
				}
			}
			initSolitaireConsole();
		}

	}

	/**
	 * This should be the trigger for MVC pattern
	 */
	public void startGameForGUI() {
		initSolitaireConsole();
		distributeCards();
	}

	public void drawCard() {
		this.deck.drawCard();
		setChanged();
		notifyObservers(this);
	}

	public void deckTo(int lstNum) {
		if (lists[lstNum].cards.size() > 0) {
			Card temp = this.deck.cards.getFirst();
			Card lstTailCard = this.lists[lstNum].getTailCard();
			if (appendCardRule(temp, lstTailCard)) {
				// this is the top card of the deck
				if (temp != null && !deck.isEmpty() && lstNum >= 0 && lstNum < this.lists.length) {
					// check the last card of the card list
					this.lists[lstNum].cards.addLast(deck.takeCard());
					this.lists[lstNum].getTailCard();
				} else {
					System.err.println("invalid argument");
				}
			}
		} else {
			this.lists[lstNum].addToTail(deck.takeCard());
		}
		setChanged();
		notifyObservers(this);
	}

	/**
	 * link all the cards from an index
	 * 
	 * @param card
	 * @param lstNum
	 * @param indexNum
	 * @param targetLstNum
	 */
	public void link(Card card, int lstNum, int indexNum, int targetLstNum) {
		Stack<Card> tempStack = new Stack<>();
		CardList targetLst = this.lists[targetLstNum];// was target
		CardList current = this.lists[lstNum];
		if (targetLst.cards.size() > 0) {
			Card endLstCard = targetLst.getTailCard();
			if (appendCardRule(card, endLstCard)) {
				for (int i = current.cards.size() - 1; i >= indexNum; i--) {
					Card anotherCard = current.moveTail();
					tempStack.push(anotherCard);
				}
				while (tempStack.size() > 0) {
					targetLst.addToTail(tempStack.pop());
				}
			}
		} else {
			for (int i = current.cards.size() - 1; i >= indexNum; i--) {
				Card anotherCard = current.moveTail();
				tempStack.push(anotherCard);
			}
			while (tempStack.size() > 0) {
				targetLst.addToTail(tempStack.pop());
			}
		}
		setChanged();
		notifyObservers(this);

	}

	public boolean sendFromList(Card card, CardList cl) {
		// check if the tail card available
		boolean isAdded = true;
		// check if card is the same from the list
		if (card.equals(cl.getTailCard())) {
			isAdded = send(card);
			if (isAdded) {
				cl.moveTail();
			}
		}
		setChanged();
		notifyObservers(this);
		return isAdded;
	}

	public boolean sendFromDeck(Card card) {
		boolean result = send(card);
		if (result) {
			this.deck.takeCard();
		}
		setChanged();
		notifyObservers(this);
		return result;
	}

	private boolean send(Card card) {
		boolean result = true;
		switch (card.getSuit()) {
		case 0:
			if (this.stacks[0].cardStack.size() > 0) {
				if (stacks[0].cardStack.getFirst().getFace() + 1 == card.getFace()) {
					this.stacks[0].cardStack.push(card);
				} else {
					result = false;
				}
			} else if (card.getFace() == 1) {
				this.stacks[0].cardStack.push(card);
			} else {
				result = false;
			}
			break;
		case 1:
			if (this.stacks[1].cardStack.size() > 0) {
				if (stacks[1].cardStack.getFirst().getFace() + 1 == card.getFace()) {
					this.stacks[1].cardStack.push(card);
				} else {
					result = false;
				}
			} else if (card.getFace() == 1) {
				this.stacks[1].cardStack.push(card);
			} else {
				result = false;
			}
			break;
		case 2:
			if (this.stacks[2].cardStack.size() > 0) {
				if (stacks[2].cardStack.getFirst().getFace() + 1 == card.getFace()) {
					this.stacks[2].cardStack.push(card);
				} else {
					result = false;
				}
			} else if (card.getFace() == 1) {
				this.stacks[2].cardStack.push(card);
			} else {
				result = false;
			}
			break;
		case 3:
			if (this.stacks[3].cardStack.size() > 0) {
				if (stacks[3].cardStack.getFirst().getFace() + 1 == card.getFace()) {
					this.stacks[3].cardStack.push(card);
				} else {
					result = false;
				}
			} else if (card.getFace() == 1) {
				this.stacks[3].cardStack.push(card);
			} else {
				result = false;
			}
			break;
		default:
			System.err.println("error in toString method");
			break;
		}
		return result;
	}

	public int[] findElementIndices(String represention) {
		int[] result = { 9, 9 };// lst num and index
		for (int i = 0; i < this.lists.length; i++) {
			for (int index = 0; index < lists[i].cards.size; index++) {
				Card temp = lists[i].cards.getIndex(index);
				if (temp.toString().equalsIgnoreCase(represention)) {
					result[0] = i;
					result[1] = index;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private boolean solitaireGameUpdate() {
		if (!isWon() && sessionContinue) {
			System.out.println(displaySessionMessage());
		}
		return !isWon() && sessionContinue;
	}

	private void executeCommand(String input) {
		String command = input.split(" ")[0];
		if (command.compareToIgnoreCase(this.commands[0]) == 0) {
			// drawcard
			drawCard();
		} else if (command.compareToIgnoreCase(this.commands[1]) == 0) {
			// deckto
			if (input.split(" ").length == 2) {
				// convert user input to array index
				int lstNum = Integer.parseInt(input.split(" ")[1]) - 1;
				deckTo(lstNum);
			}
		} else if (command.compareToIgnoreCase(this.commands[2]) == 0) {
			// link
			if (input.split(" ")[1].compareToIgnoreCase("back") == 0) {
				System.err.println("You cannot link from back cards\n");
			} else if (input.split(" ").length == 3
					&& findElementIndices(input.split(" ")[1])[0] >= 0
					&& findElementIndices(input.split(" ")[1])[0] < this.lists.length
					&& Integer.parseInt(input.split(" ")[2]) > 0
					&& Integer.parseInt(input.split(" ")[2]) <= this.lists.length) {
				// index 0 for lst index 1 for element index
				int[] tempLstIndex = findElementIndices(input.split(" ")[1]);
				if (tempLstIndex != new int[] { 9, 9 }) {
					String cardRep = input.split(" ")[1];
					int targetLstIndex = Integer.parseInt(input.split(" ")[2]) - 1;
					System.out.println(cardRep + " at list# " + tempLstIndex[0] + " index "
							+ tempLstIndex[1] + " to " + input.split(" ")[2]);
					Card card = this.lists[tempLstIndex[0]].cards.getIndex(tempLstIndex[1]);
					link(card, tempLstIndex[0], tempLstIndex[1], targetLstIndex);
				} else {
					System.err.println("index is out of bound");
				}
			} else {
				System.err.println("Please provide the correct input format as \""
						+ "Link CardName 2\" as your argument");
			}
		} else if (command.compareToIgnoreCase(this.commands[3]) == 0) {
			// send
			String cardRep = input.split(" ")[1];
			int[] lstElementIndices = findElementIndices(cardRep);
			if (lstElementIndices[0] == 9 && !deck.isEmpty()
					&& deck.currentCard.toString().equals(cardRep)) {
				sendFromDeck(deck.currentCard);
			} else if (lstElementIndices[0] != 9) {
				Card card = lists[lstElementIndices[0]].cards.getIndex(lstElementIndices[1]);
				if (card != null) {
					sendFromList(card, lists[lstElementIndices[0]]);
					System.out.println("card is sent");
				} else {
					System.err.println("the card is not found");
				}
			} else {
				System.out.println("card is not present");
			}
		} else if (command.compareToIgnoreCase(this.commands[4]) == 0) {
			// restart
			this.sessionContinue = false;
		} else if (command.compareToIgnoreCase(this.commands[5]) == 0) {
			// help
			String instructions = "all the commands are case insensitive, need to follow the commands"
					+ "\nDrawCard"
					+ "\nDeck to #lst       (to append the open card from deck to the number of card list)"
					+ "\nLink $CardName #lst      (to link all cards after the card to another list)"
					+ "\nSend $CardName        (to send a card to card stack)"
					+ "\nRestart          (restart a new game session)"
					+ "\nQuit          (exit the game)";
			System.out.println(instructions);
		} else if (command.compareToIgnoreCase(this.commands[6]) == 0) {
			// quit
			System.out.println("current game is discarded");
			this.sessionContinue = false;
			endGame();
		} else {
			System.out.println("invalid command");
		}
	}

	private boolean appendCardRule(Card addingCard, Card lstTailCard) {
		if (lstTailCard == null) {
			return true;
		} else {
			System.out.println("Append " + addingCard + " to " + lstTailCard);
			boolean isNumberOkay = addingCard.getFace() + 1 == lstTailCard.getFace();
			if (!isNumberOkay)
				System.out.println("number mismatch");
			boolean isColorDifferent = !addingCard.colour().equals(lstTailCard.colour());
			if (!isColorDifferent)
				System.out.println("color is the same");
			return isNumberOkay && isColorDifferent;
		}
	}

	/**
	 * distribute cards into 7 lists
	 */
	private void distributeCards() {
		for (int lstNum = 0; lstNum < 7; lstNum++) {
			for (int cardCount = 0; cardCount <= lstNum; cardCount++) {
				Card temp = deck.takeCard();
				if (cardCount < lstNum && !deck.isEmpty()) {
					temp.isBack = true;
				}
				this.lists[lstNum].cards.addLast(temp);
			}
		}
	}

	private void endGame() {
		this.isGameRunning = false;
		System.out.println("Thanks for playing solitaire game, bye:D");

	}

	/**
	 * display message after each player action
	 * 
	 * @return the output text display message
	 */
	private String displaySessionMessage() {
		String currentRlt = "--------------Current Step-----------------" + "\nCard Deck: ";
		if (!this.deck.isEmpty()) {
			currentRlt += "Not Empty 	Open Card: ";
		} else {
			currentRlt += "Empty 	";
		}
		if (this.deck.currentCard != null && this.deck.cards.size() > 0) {
			currentRlt += this.deck.currentCard + "\n";
		} else {
			currentRlt += "Empty\n";
		}
		currentRlt += "Card Stacks: ";
		if (stacks[0].cardStack.size() == 0) {
			currentRlt += "HEARTS ";
		} else {
			currentRlt += stacks[0].cardStack.getFirst() + " ";
		}
		if (stacks[1].cardStack.size() == 0) {
			currentRlt += "SPADES ";
		} else {
			currentRlt += stacks[1].cardStack.getFirst() + " ";
		}
		if (stacks[2].cardStack.size() == 0) {
			currentRlt += "CLUBS ";
		} else {
			currentRlt += stacks[2].cardStack.getFirst() + " ";
		}
		if (stacks[3].cardStack.size() == 0) {
			currentRlt += "DIAMONDS ";
		} else {
			currentRlt += stacks[3].cardStack.getFirst() + " ";
		}
		currentRlt += "\n";

		currentRlt += "Card Lists:";
		for (int i = 0; i < 7; i++) {
			currentRlt += "\n" + (i + 1) + ": " + this.lists[i].cards;
		}
		return currentRlt;
	}

	private void displayAsciiWelcomeMessage() {
		String welcome = "============================================================"
				+ "\n  SSS    OOOO  LL    II TTTTTT   A    II RRRRR  EEEEEEE"
				+ "\n SS  SS OO  OO LL         TT    AAA      RR  RR EE"
				+ "\n   S    OO  OO LL    II   TT   AA AA  II RR RR  EE"
				+ "\n S  S   OO  OO LL    II   TT  AA   AA II RRRRR  EEEEEEE"
				+ "\n SS  SS OO  OO LL    II   TT  AAAAAAA II RR  R  EE"
				+ "\n  SSSS   OOOO  LLLLL II   TT  AA   AA II RR  RR EEEEEEE"
				+ "\n============================================================";
		System.out.println(welcome);
	}
}
