package Blackjack;

import java.util.Collections;
import java.util.ArrayList;

// Defines a class for cards 
class Card {
  private String face;
  private String suit;

  public Card(String face, String suit) {
    this.face = face;
    this.suit = suit;
  }

  public String getFace() {
    return face;
  }

  public String getSuit() {
    return suit;
  }
  // Gets value of the card based on its face.
  public int getValue() {
    switch (face) {
      case "Jack":
        return 10;
      case "Queen":
        return 10;
      case "King":
        return 10;
      case "Ace":
        //Actual value of ace (1 or 11) is handled in countTotal method
        return 11;
      default:
        return Integer.parseInt(face);
    }
  }

  @Override
  public String toString() {
    return face + " of " + suit;
  }
}
// Defines and creates a full deck of cards.
class Deck {
  private ArrayList<Card> deckCards = new ArrayList<>();
  private final String[] faces = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
  private final String[] suits = { "Spades", "Diamonds", "Clubs", "Hearts" };

  Deck() {
    for (String suit : suits) {
      for (String face : faces) {
        deckCards.add(new Card(face, suit));
      }
    }

  }
  // Shuffles the deck. 
  public void shuffle() {
    Collections.shuffle(deckCards);
  }
  // Takes the top card and returns it and removes from the deck.
  public Card deal() {
    Card card_to_deal = deckCards.get(0);
    deckCards.remove(0);
    return card_to_deal;
  }
  // Resets the deck to all 52 cards, and then shuffles it.
  public void resetDeck() {
    deckCards.clear();
    for (String suit : suits) {
      for (String face : faces) {
        deckCards.add(new Card(face, suit));
      }
    }
    shuffle();
  }

  @Override
  public String toString() {
    String builder = "";
    for (Card card : deckCards) {
      builder += card.getFace() + " of " + card.getSuit() + "\n";
    }
    return builder;
  }

}

class Hand {
  private ArrayList<Card> handCards = new ArrayList<>();

  // Counts the total value of the cards in hand.
  public int countTotal() {
    int total = 0;
    //Add up all the non-aces first
    for (Card card : handCards) {
      if (!card.getFace().equals("Ace")) {
        total += card.getValue();
      }
    }

    //Add up the aces
    if (numAces() != 0) {
      total += numAces();
      if (total <= 11) {
        total += 10;
      }
    }
    return total;
  }
  // Adds the dealt card to a hand. 
  public void hit(Deck some_deck) {
    handCards.add(some_deck.deal());
  }

  public ArrayList<Card> getHand() {
    return handCards;
  }

  public void clearHand() {
    handCards.clear();
  }

  public int numAces () {
    int howManyAces = 0;
    for (Card card : handCards) {
      if (card.getFace().equals("Ace")) {
        howManyAces += 1;
      }
    }
    return howManyAces;
  }

  @Override
  public String toString() {
    String builder = "";
    for (Card card : handCards) {
      builder += card.getFace() + " of " + card.getSuit() + "\n";
    }
    return builder;
  }

}