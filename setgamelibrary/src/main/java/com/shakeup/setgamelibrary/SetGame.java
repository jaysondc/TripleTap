package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SetGame {

    int mSetsAvailable; // Number of available sets in the current hand
    ArrayList<SetCard> mSetHand; // Array to hold all drawn cards
    SetDeck currentDeck;


    /**
     * Constructor to initialize a game of SET
     */
    public SetGame(){

        // Create a new deck
        currentDeck = new SetDeck();

        mSetHand = new ArrayList<>();

        // Draw 16 cards
        for (int i = 0; i < 16; i++){
            mSetHand.add(currentDeck.drawCard());
        }




    }

    /**
     * This method returns the number of valid sets in the current drawn hand.
     *
     * @return Number of sets shown
     */
    public int getNumAvailableSets(){
        return -1;
    }

    /**
     * Takes 3 cards and returns true if they are a valid set, false if they are not
     * @param first
     * @param second
     * @param third
     * @return True if valid set, false if not
     */
    public boolean isValidSet(SetCard first, SetCard second, SetCard third){
        // Get 3rd card that would complete the set
        SetCard validThirdCard = first.getThirdCard(second);

        // Return whether the 3rd card given correctly completes the set
        return third.isEqualTo(validThirdCard);
    }








    /**
     * Deck to hold all 81 cards
     */
    public class SetDeck {

        ArrayList<SetCard> mDeck;

        /**
         * Constructor creates the 81 card deck using all card possibilities
         */
        public SetDeck(){
            mDeck = new ArrayList<>();

            // Generate all possible SetCards and add them to the deck
            for (CardShape shape : CardShape.values()) {
                for (CardColor color : CardColor.values()) {
                    for (CardCount count : CardCount.values()) {
                        for (CardFill fill : CardFill.values()) {

                            mDeck.add(new SetCard(shape, color, count, fill));

                        }
                    }
                }
            }

            // Shuffle the deck
            Collections.shuffle(mDeck);

        }

        /**
         * Draw a card from the deck
         * @return A SetCard object
         */
        public SetCard drawCard(){
            return mDeck.remove(mDeck.size()-1);
        }

        public int getCount(){
            return mDeck.size();
        }

        public boolean isEmpty(){
            return mDeck.isEmpty();
        }

        public ArrayList<SetCard> getDeckArray(){
            return mDeck;
        }

    }

}
