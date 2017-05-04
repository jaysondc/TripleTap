package com.shakeup.setgamelibrary;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;

import java.util.ArrayList;
import java.util.Arrays;

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

    // Empty constructor for Parceler support
    public SetCard(){
    }

    public SetCard(CardShape shape, CardColor color, CardCount count, CardFill fill){
        mShape = shape;
        mColor = color;
        mCount = count;
        mFill = fill;
    }


    /**
     * Checks if 2 cards are equal to each other
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


    /**
     * This takes a second SetCard and returns a 3rd SetCard that would
     * complete the set.
     */
    public SetCard getThirdCard(SetCard second){
        CardShape thirdShape = getThirdShape(mShape, second.getShape());
        CardColor thirdColor = getThirdColor(mColor, second.getColor());
        CardCount thirdCount = getThirdCount(mCount, second.getCount());
        CardFill thirdFill = getThirdFill(mFill, second.getFill());

        return new SetCard(thirdShape, thirdColor, thirdCount, thirdFill);
    }

    /**
     * THIRD FEATURE CALCULATORS
     * Returns compliment if they are the different, same if they are the same
     */
    
    private CardShape getThirdShape(CardShape first, CardShape second){
        if (first == second){
            return first;
        } else {
            ArrayList<CardShape> shapes =
                    new ArrayList<CardShape>(Arrays.asList(CardShape.values()));
            shapes.remove(first);
            shapes.remove(second);
            return shapes.get(0);
        }
    }

    private CardColor getThirdColor(CardColor first, CardColor second){
        if (first == second){
            return first;
        } else {
            ArrayList<CardColor> colors =
                    new ArrayList<CardColor>(Arrays.asList(CardColor.values()));
            colors.remove(first);
            colors.remove(second);
            return colors.get(0);
        }
    }

    private CardCount getThirdCount(CardCount first, CardCount second){
        if (first == second){
            return first;
        } else {
            ArrayList<CardCount> counts =
                    new ArrayList<CardCount>(Arrays.asList(CardCount.values()));
            counts.remove(first);
            counts.remove(second);
            return counts.get(0);
        }
    }

    private CardFill getThirdFill(CardFill first, CardFill second){
        if (first == second){
            return first;
        } else {
            ArrayList<CardFill> fills =
                    new ArrayList<CardFill>(Arrays.asList(CardFill.values()));
            fills.remove(first);
            fills.remove(second);
            return fills.get(0);
        }
    }

    /**
     * END THIRD FEATURE CALCULATORS
     */



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

    /**
     * Generates a long representation of the card contents
     * @return Card ID in the form of a long
     */
    public long getId(){
        long id = 0;
        id += mShape.ordinal() * 1000;
        id += mColor.ordinal() * 100;
        id += mCount.ordinal() * 10;
        id += mFill.ordinal();

        return id;
    }

}

