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


    /**
     * Constructor to initialize a game of SET
     */
    public SetGame(){



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
