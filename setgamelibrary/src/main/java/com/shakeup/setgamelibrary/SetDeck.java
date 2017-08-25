package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck to hold all 81 cards
 * 0 - Normal Mode
 * 1 - Easy Mode. One card characteristic will stay the game in all cards.
 * In easy mode the deck is only 27 cards.
 */
public class SetDeck {

    ArrayList<SetCard> mDeck;

    /**
     * Empty constructor to make SetDeck parcelable
     */
    public SetDeck() {
    }

    /**
     * Constructor creates the 81 card deck using all card possibilities
     * 0 - Normal Mode
     * 1 - Easy Mode
     */
    public SetDeck(int difficulty) {
        if (difficulty == SetGame.DIFFICULTY_NORMAL) { // Normal Mode
            mDeck = createNormalDeck();
        } else if (difficulty == SetGame.DIFFICULTY_EASY) { // Easy Mode
            mDeck = createEasyDeck();
        }
    }


    /**
     * Create a SetDeck of all (81) possible cards and shuffle them.
     * @return A list of all possible cards shuffled
     */
    public ArrayList<SetCard> createNormalDeck() {
        ArrayList<SetCard> deck = new ArrayList<>();

        // Generate all possible SetCards and add them to the deck
        for (CardShape shape : CardShape.values()) {
            for (CardColor color : CardColor.values()) {
                for (CardCount count : CardCount.values()) {
                    for (CardFill fill : CardFill.values()) {

                        deck.add(new SetCard(shape, color, count, fill));

                    }
                }
            }
        }

        // Shuffle the deck
        Collections.shuffle(deck);

        return deck;
    }

    /**
     * Create an Easy mode deck. This deck consists of all (81) possible combinations of cards
     * separated into three sections.
     * Example: If staticChar is COUNT and staticValue is TWO
     *  Section 1 - All cards where count is TWO, shuffled
     *  Section 2 - All cards where count is THREE, shuffled
     *  Section 3 - All cards where count is ONE, shuffled
     * @return An easy mode deck of SET Cards
     */
    public ArrayList<SetCard> createEasyDeck() {
        int staticChar = (int) Math.floor(Math.random() * 4);
        int staticValue = (int) Math.floor(Math.random() * 3);
        // Full Deck
        ArrayList<SetCard> deck = new ArrayList<>();
        // Array of 3 easy decks
        ArrayList<ArrayList<SetCard>> threeDecks = new ArrayList<>();
        threeDecks.add(new ArrayList<SetCard>());
        threeDecks.add(new ArrayList<SetCard>());
        threeDecks.add(new ArrayList<SetCard>());

        switch (staticChar) {
            case 0: // static Color
                for (int i = 0; i < 3; i++) {
                    for (CardShape shape : CardShape.values()) {
                        for (CardCount count : CardCount.values()) {
                            for (CardFill fill : CardFill.values()) {
                                int colorValue = (i + staticValue) % 3;
                                threeDecks.get(i).add(new SetCard(
                                        shape,
                                        CardColor.values()[colorValue],
                                        count,
                                        fill));
                            }
                        }
                    }
                    // Shuffle the section
                    Collections.shuffle(threeDecks.get(i));
                }
                break;
            case 1: // static Shape
                for (int i = 0; i < 3; i++) {
                    for (CardColor color : CardColor.values()) {
                        for (CardCount count : CardCount.values()) {
                            for (CardFill fill : CardFill.values()) {
                                int shapeValue = (i + staticValue) % 3;
                                threeDecks.get(i).add(new SetCard(
                                        CardShape.values()[shapeValue],
                                        color,
                                        count,
                                        fill));
                            }
                        }
                    }
                    // Shuffle the section
                    Collections.shuffle(threeDecks.get(i));
                }
                break;
            case 2: // static Count
                for (int i = 0; i < 3; i++) {
                    for (CardShape shape : CardShape.values()) {
                        for (CardColor color : CardColor.values()) {
                            for (CardFill fill : CardFill.values()) {
                                int staticCount = (i + staticValue) % 3;
                                threeDecks.get(i).add(new SetCard(
                                        shape,
                                        color,
                                        CardCount.values()[staticCount],
                                        fill));
                            }
                        }
                    }
                    // Shuffle the section
                    Collections.shuffle(threeDecks.get(i));
                }
                break;
            case 3: // static Fill
                for (int i = 0; i < 3; i++) {
                    for (CardShape shape : CardShape.values()) {
                        for (CardColor color : CardColor.values()) {
                            for (CardCount count : CardCount.values()) {
                                int staticFill = (i + staticValue) % 3;
                                threeDecks.get(i).add(new SetCard(
                                        shape,
                                        color,
                                        count,
                                        CardFill.values()[staticFill]));
                            }
                        }
                    }
                    // Shuffle the section
                    Collections.shuffle(threeDecks.get(i));
                }
                break;
        }

        // Combine threeDecks into a single deck
        for (ArrayList someDeck : threeDecks) {
            deck.addAll(someDeck);
        }

        return deck;
    }

    /**
     * Draw a card from the deck
     *
     * @return A SetCard object of the drawn card
     */
    public SetCard drawCard() {
        // Return the removed card
        return mDeck.remove(0);
    }

    /**
     * Re-shuffle the discard pile into the deck
     */
    public void refillDeck(ArrayList<SetCard> discardPile) {
        // Shuffle the discard deck
        Collections.shuffle(discardPile);
        // Add cards from the discard pile to the live deck
        for (SetCard card : discardPile) {
            mDeck.add(card);
        }
        while(!discardPile.isEmpty()) {
            discardPile.remove(0);
        }
    }

    public int getCount() {
        return mDeck.size();
    }

    public boolean isEmpty() {
        return mDeck.isEmpty();
    }

    public ArrayList<SetCard> getDeckArray() {
        return mDeck;
    }

}
