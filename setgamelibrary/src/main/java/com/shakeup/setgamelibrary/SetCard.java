package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;
import com.sun.org.apache.xpath.internal.operations.Equals;

/**
 * Created by Jayson on 2/15/2017.
 *
 * Set cards have 4 characteristics: shape, color, count, fill.
 * These should be set when the card is created and never changed.
 */

public class SetCard {
    private CardShape mShape;
    private CardColor mColor;
    private CardCount mCount;
    private CardFill mFill;

    public SetCard(CardShape shape, CardColor color, CardCount count, CardFill fill){
        mShape = shape;
        mColor = color;
        mCount = count;
        mFill = fill;
    }


    /**
     *
     * @param card
     * @return
     */
    public boolean isEqualTo(SetCard card){
        return (
                card.getShape() == mShape &&
                        card.getColor() == mColor &&
                        card.getCount() == mCount &&
                        card.getFill() == mFill
        );
    }

    public CardShape getShape(){
        return mShape;
    }

    public CardColor getColor(){
        return mColor;
    }

    public CardCount getCount(){
        return mCount;
    }

    public CardFill getFill(){
        return mFill;
    }

    public String toString(){
        return String.format("%s, %s, %s, %s", mShape, mColor, mCount, mFill);
    }

}

