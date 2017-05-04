package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayson on 2/15/2017.
 *
 * Class for testing functions of the Set Game Library
 */

public class SetTests {

    @Test
    public void createCardTest(){
        SetCard testCard = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );
        Assert.assertEquals(CardShape.DIAMOND, testCard.getShape());
        Assert.assertEquals(CardColor.GREEN, testCard.getColor());
        Assert.assertEquals(CardCount.ONE, testCard.getCount());
        Assert.assertEquals(CardFill.OPEN, testCard.getFill());
    }

    @Test
    public void cardToStringTest(){
        SetCard testCard = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );

        Assert.assertNotNull(testCard.toString());
    }

    @Test
    public void cardEqualToTest(){
        SetCard testCard1 = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );

        SetCard testCard2 = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );

        Assert.assertTrue(testCard1.isEqualTo(testCard2));
    }

    @Test
    public void getThirdCardTest(){
        SetCard testCard1 = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );

        SetCard testCard2 = new SetCard(
                CardShape.OVAL,
                CardColor.GREEN,
                CardCount.TWO,
                CardFill.OPEN
        );

        SetCard thirdCard = testCard1.getThirdCard(testCard2);

        // Compliment
        Assert.assertEquals(CardShape.SQUIGGLE, thirdCard.getShape());
        // Same
        Assert.assertEquals(CardColor.GREEN, thirdCard.getColor());
        // Compliment
        Assert.assertEquals(CardCount.THREE, thirdCard.getCount());
        // Same
        Assert.assertEquals(CardFill.OPEN, thirdCard.getFill());


        // Test again with a diferent card
        testCard2 = new SetCard(
                CardShape.DIAMOND,
                CardColor.PURPLE,
                CardCount.ONE,
                CardFill.STRIPE
        );

        thirdCard = testCard1.getThirdCard(testCard2);

        // Same
        Assert.assertEquals(CardShape.DIAMOND, thirdCard.getShape());
        // Compliment
        Assert.assertEquals(CardColor.RED, thirdCard.getColor());
        // Same
        Assert.assertEquals(CardCount.ONE, thirdCard.getCount());
        // Compliment
        Assert.assertEquals(CardFill.SOLID, thirdCard.getFill());
    }

    @Test
    public void setDeckTest(){
        SetGame.SetDeck testDeck = new SetGame().new SetDeck();

        // Create a discard pile
        ArrayList<SetCard> discardPile = new ArrayList<>();

        Assert.assertEquals(81, testDeck.getCount());

        // Draw a single card
        discardPile.add(testDeck.drawCard());

        Assert.assertEquals(80, testDeck.getCount());

        // Draw the rest of the cards
        for(int i = 0; i < 80; i++){
            discardPile.add(testDeck.drawCard());
        }

        Assert.assertTrue(testDeck.isEmpty());

        testDeck.refillDeck(discardPile);
        Assert.assertEquals(81, testDeck.getCount());
    }

    @Test
    public void isValidSetTest(){
        SetGame testGame = new SetGame();


        SetCard testCard1 = new SetCard(
                CardShape.DIAMOND,
                CardColor.GREEN,
                CardCount.ONE,
                CardFill.OPEN
        );

        SetCard testCard2 = new SetCard(
                CardShape.OVAL,
                CardColor.GREEN,
                CardCount.TWO,
                CardFill.OPEN
        );

        SetCard testCard3Valid = new SetCard(
                CardShape.SQUIGGLE,
                CardColor.GREEN,
                CardCount.THREE,
                CardFill.OPEN
        );

        SetCard testCard3Invalid = new SetCard(
                CardShape.OVAL,
                CardColor.GREEN,
                CardCount.THREE,
                CardFill.OPEN
        );

        Assert.assertTrue(testGame.isValidSet(testCard1, testCard2, testCard3Valid));
        Assert.assertFalse(testGame.isValidSet(testCard1, testCard2, testCard3Invalid));
    }

    @Test
    public void setAnalysisTest(){
        // Test whether the set analysis is correct. Detect and validate all found sets in a hand.
        SetGame testGame = new SetGame();
        int detectedSets = testGame.getNumAvailableSets();
        ArrayList<SetGame.Triplet> locationOfSets =
                testGame.getLocationOfSets();
        int countedSets = 0;

        for (SetGame.Triplet set : locationOfSets) {
            boolean isValid = testGame.isValidSet(
                    testGame.getSetCard(set.getFirst()),
                    testGame.getSetCard(set.getSecond()),
                    testGame.getSetCard(set.getThird())
            );
            countedSets++;
            Assert.assertTrue(isValid);
        }

        Assert.assertEquals(detectedSets, countedSets);
    }

    @Test
    public void setAnalysisEmptyTest(){
        // Test whether the set analysis is correct. Detect and validate all found sets in a hand.
        SetGame testGame = new SetGame();

        // Set SetHand to be empty
        testGame.setSetHand(new ArrayList<SetCard>());
        // Create a new deck and empty it
        SetGame.SetDeck emptyDeck = testGame.new SetDeck();
        while(!emptyDeck.isEmpty()){
            emptyDeck.drawCard();
        }

        // Set the empty deck as the game's deck
        testGame.setSetDeck(emptyDeck);
        // Re-analyze sets
        testGame.analyzeSets();

        int detectedSets = testGame.getNumAvailableSets();

        Assert.assertEquals(0, detectedSets);
    }

    @Test
    public void setFullGameTest(){
        SetGame testGame = new SetGame();
        ArrayList<SetGame.Triplet> locationOfSets;
        int detectedSets = 0;
        int handSize = 0;
        int deckSize = 0;
        int foundSets = 0;

        // Test playing a game and pulling sets until the deck is empty or
        // there are no more sets in the hand
        while (!testGame.getIsGameOver()){

            // Test pulling sets from the deck
            SetGame.Triplet foundSet;
            foundSet = testGame.getLocationOfSets().get(0);

            // Claim this set
            if( testGame.claimSet(
                    foundSet.getFirst(),
                    foundSet.getSecond(),
                    foundSet.getThird()
                    )) {
                foundSets++;
            }

            detectedSets = testGame.getNumAvailableSets();
            handSize = testGame.getHandSize();
            deckSize = testGame.getDeckSize();

            System.out.println("Sets detected: " + detectedSets);
            System.out.println("Hand size: " + handSize);
            System.out.println("Deck size: " + deckSize);
        }

        // Get all the sets we found
        List<SetGame.SetTriplet> allFoundSets = testGame.getFoundSets();
        // Get the leftover deck which should be empty
        List<SetCard> leftoverDeck = testGame.getSetDeck().getDeckArray();

        Assert.assertEquals(0, leftoverDeck.size());
        Assert.assertEquals(foundSets, allFoundSets.size());
        Assert.assertEquals(0, deckSize);
        Assert.assertEquals(0, detectedSets);
        Assert.assertTrue(testGame.getIsGameOver());

    }

    @Test
    public void multipleFullGamesTest(){
        int numberOfRuns = 1000;

        for( int i = 0; i < numberOfRuns; i++ ){
            setFullGameTest();
        }
    }
}
