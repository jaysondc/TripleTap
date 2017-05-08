package com.shakeup.setofthree.SetGame;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setgamelibrary.SetGame;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This class specifies the interaction between the Game View and the Presenter
 */

public class GameContract {

    /**
     * Methods that need to be implemented by the Game View
     */
    public interface View {

        void displayGame(ArrayList<SetCard> setHand);

        // Some actions to interact with the SetGame
        void updateSetHand(
                boolean isOverflow,
                int deckSize);

        void onSetSuccess();

        void onSetFailure();

        void onSetCardClicked();

        void showGameOver();

        void highlightCard(int index);

        void selectCard(int index);

        void setGameClickable(boolean isClickable);

        void clearChoices();

        void clearHighlights();

    }

    /**
     * Methods that need to be implemented by the Game Presenter
     */
    public interface UserActionsListener {

        void initGame(SetGame game);

        void onSubmitSet(int indexOne, int indexTwo, int indexThree);

        void setSetGame(SetGame game);

        SetGame getSetGame();

        void onSetCardClick();

        void onSetFailure();

        void onSetSuccess();

        void onGameOver();

        void highlightValidSet();

        void onShowHintClick();

        ArrayList<SetGame.Triplet> getSetLocations();

    }

}
