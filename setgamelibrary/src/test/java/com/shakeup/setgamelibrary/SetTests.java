package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

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

}
