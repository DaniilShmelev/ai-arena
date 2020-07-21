package qucumbah.tictactoegame;

import qucumbah.game.Player;

public class TicTacToeGamePlayer extends Player {
  public TicTacToeGamePlayer() {
    super(19, 9);
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
