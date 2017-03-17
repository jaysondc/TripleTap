package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetGame {
    
    // Number of available sets in the current hand
    private int mNumSetsAvailable;
    // Array to hold all drawn cards
    private ArrayList<SetCard> mSetHand;
    // Deck of shuffled SET cards
    private SetDeck mCurrentDeck;
    // Location of available sets in hand
    private ArrayList<Triplet<Integer, Integer, Integer>> mLocationOfSets;
    // List of found sets
    private List<Triplet<SetCard, SetCard, SetCard>> mFoundSets;
    // Detect game over
    private boolean mGameOver;
    // Detect hand overflow
    private boolean mOverflow = false;

    /**
     * Constructor to initialize a game of SET
     */
    public SetGame(){
        init();
    }

    /**
     * Initialize the game by creating a deck and drawing cards
     */
    private void init(){

        // Create a new deck
        mCurrentDeck = new SetDeck();
        mSetHand = new ArrayList<>();
        mFoundSets = new ArrayList<>();
        mGameOver = false;

        // Draw 16 cards
        for (int i = 0; i < 12; i++){
            mSetHand.add(mCurrentDeck.drawCard());
        }

        // Look for SETs in the current hand
        analyzeSets();
    }

    public SetCard getSetCard(int location){
        return mSetHand.get(location);
    }

    /**
     * Claim 3 cards are a set. If the set is valid, remove and replace the 3 cards
     * and return true. If the set is invalid, return false.
     * @param firstIndex
     * @param secondIndex
     * @param thirdIndex
     * @return True if they were a SEt, false otherwise.
     */
    public boolean claimSet(int firstIndex, int secondIndex, int thirdIndex){
        SetCard firstCard = mSetHand.get(firstIndex);
        SetCard secondCard = mSetHand.get(secondIndex);
        SetCard thirdCard = mSetHand.get(thirdIndex);

        boolean isValid = isValidSet(
                firstCard,
                secondCard,
                thirdCard
        );

        if (isValid){ // Successful SET call

            // Store found SET
            mFoundSets.add(
                    new Triplet<>(
                            firstCard,
                            secondCard,
                            thirdCard
                    ));

            // Draw new cards or rearrange cards if deck is empty
            if (!mCurrentDeck.isEmpty() && !mOverflow){
                // Replace cards in hand if we have cards in the deck and we're not in overflow
                mSetHand.set(firstIndex, mCurrentDeck.drawCard());
                mSetHand.set(secondIndex, mCurrentDeck.drawCard());
                mSetHand.set(thirdIndex, mCurrentDeck.drawCard());
            } else {
                // Remove pulled cards in descending order to avoid out of bounds exceptions
                mSetHand.remove(thirdIndex);
                mSetHand.remove(secondIndex);
                mSetHand.remove(firstIndex);

                mOverflow = false;
            }

            // Recalculate available sets
            analyzeSets();

            // If no sets and deck is empty, game over
            if (mNumSetsAvailable == 0){
                // Game over
                mGameOver = true;
            }

        }

        return isValid;
    }

    /**
     * This method looks at the drawn hand, counts the available sets and stores
     * their location.
     * First card is at index i, second card is index j
     * Iterations look like the following
     * 1. ij00
     * 2. i0j0
     * 3. i00j
     * 4. 0ij0
     * 5. 0i0j
     * ...
     */
    private void analyzeSets(){
        int setsFound = 0;
        mLocationOfSets = new ArrayList<>();

        for (int i = 0; i < mSetHand.size(); i++){
            for (int j = i+1; j < mSetHand.size(); j++){
                // Generate 3rd card required to complete the set
                SetCard thirdCard = mSetHand.get(i).getThirdCard(mSetHand.get(j));
                // Check if the 3rd card is in the current hand
                int k = isInHand(j, thirdCard);
                if (k > 0){
                    setsFound++;
                    // Store the location of the set
                    mLocationOfSets.add(new Triplet<>(i, j, k));
                }
            }
        }

        // Store the number of available sets
        mNumSetsAvailable = setsFound;

        // If there are no sets and we still have cards in the deck, add 3 cards and analyze again
        if (setsFound == 0 && !mCurrentDeck.isEmpty()){

            mSetHand.add(mCurrentDeck.drawCard());
            mSetHand.add(mCurrentDeck.drawCard());
            mSetHand.add(mCurrentDeck.drawCard());

            mOverflow = true;
            analyzeSets();
        }
    }

    public int getNumAvailableSets(){
        return mNumSetsAvailable;
    }

    public ArrayList<Triplet<Integer, Integer, Integer>> getLocationOfSets(){
        return mLocationOfSets;
    }

    /**
     * Returns a list possible sets in a hand in the form of an List of Triplets.
     * @returna A list of Triplets containing 3 integers, each integer being
     * the index of a card required to make that set.
     */
    public List<Triplet<SetCard, SetCard, SetCard>> getFoundSets(){
        return mFoundSets;
    }

    public ArrayList<SetCard> getSetHand(){
        return mSetHand;
    }

    public int getHandSize(){
        return mSetHand.size();
    }

    public SetDeck getSetDeck(){ return mCurrentDeck; }

    public int getDeckSize(){
        return mCurrentDeck.getCount();
    }

    public boolean getIsGameOver(){
        return mGameOver;
    }

    public boolean getIsOverflow() {
        return mOverflow;
    }

    /**
     * Checks if the card required to complete a set is in the current hand. Returns it's index
     * if it exists, -1 if it doesn't exist.
     * @param secondIndex Index of the second card
     * @param thirdCard Third card to search for
     * @return Index of the 3rd card if it is available, -1 if it isn't
     */
    private int isInHand(int secondIndex, SetCard thirdCard){
        int cardIndex = -1;

        // We can assume we've found any sets that can be completed
        // before the second index.
        for (int i = secondIndex; i < mSetHand.size(); i++){
            if (mSetHand.get(i).isEqualTo(thirdCard)){
                cardIndex = i;
            }
        }

        return cardIndex;
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

    public class Triplet<T, U, V> {

        private final T first;
        private final U second;
        private final V third;

        public Triplet(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getFirst() { return first; }
        public U getSecond() { return second; }
        public V getThird() { return third; }
    }

}
