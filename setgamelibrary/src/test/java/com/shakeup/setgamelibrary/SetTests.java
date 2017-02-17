package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Set;

import sun.rmi.runtime.Log;

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
                CardFill.STRIPED
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

        Assert.assertEquals(81, testDeck.getCount());

        // Draw a single card
        testDeck.drawCard();

        Assert.assertEquals(80, testDeck.getCount());

        // Draw the rest of the cards
        for(int i = 0; i < 80; i++){
            testDeck.drawCard();
        }

        Assert.assertTrue(testDeck.isEmpty());
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
        int numSets = testGame.getNumAvailableSets();
        ArrayList<SetGame.Triplet<Integer, Integer, Integer>> locationOfSets = testGame.getLocationOfSets();

        for (SetGame.Triplet<Integer, Integer, Integer> set : locationOfSets) {
            boolean isValid = testGame.isValidSet(
                    testGame.getSetCard(set.getFirst()),
                    testGame.getSetCard(set.getSecond()),
                    testGame.getSetCard(set.getThird())
            );
            Assert.assertTrue(isValid);
        }
    }
}
