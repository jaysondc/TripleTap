package com.shakeup.setofthree.MultiplayerGame;

import com.shakeup.setofthree.SetGame.GameContract;

/**
 * Created by Jayson on 3/20/2017.
 */

public class MultiplayerGameContract extends GameContract {
    /**
     * Methods that need to be implemented by the Multiplayer Game View
     */
    interface View extends GameContract.View {

        void onPlayerButtonClick(int playerId);

        void startPlayerCountdown(int playerId);

    }

    /**
     * Methods that need to be implemented by the Multiplayer Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void playerButtonClick(int playerId);

    }
}
