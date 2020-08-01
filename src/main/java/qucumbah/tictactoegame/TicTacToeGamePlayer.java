package qucumbah.tictactoegame;

import qucumbah.game.Player;

public class TicTacToeGamePlayer extends Player {
  public TicTacToeGamePlayer() {
    super(18, 9);
  }

  @Override
  public boolean canMakeAMove() {
    TicTacToeGame game = (TicTacToeGame)this.game;
    return (
        game.isCrossesTurn() && game.isPlayerCrosses(this)
        || !game.isCrossesTurn() && !game.isPlayerCrosses(this)
    );
  }

  @Override
  public double getReward() {
    return game.getRewardForPlayer(this);
  }

  @Override
  public double[] getVision() {
    return ((TicTacToeGame)game).getVisionForPlayer(this);
  }
}
