package com.shakeup.setofthree.SetGame;

import com.shakeup.setgamelibrary.SetCard;

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
    interface View {

        void displayGame(ArrayList<SetCard> setHand);

        // Some actions to interact with the SetGame

    }

    /**
     * Methods that need to be implemented by the Game Presenter
     */
    interface UserActionsListener {

        void initGame();

        void submitSet(int indexOne, int indexTwo, int indexThree);

    }

}
