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

        void startFindSetCountdown(int playerId);

        void setEnablePlayerButton(int playerId, boolean enable);

        void setActivePlayer(int playerId);

        void onPunishPlayer(int playerId);

        void setGameState(int gameState);

    }

    /**
     * Methods that need to be implemented by the Multiplayer Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void playerButtonClick(int playerId);

        void playerButtonSuccess(int playerId);

        void playerButtonPunish(int playerId);

    }
}
