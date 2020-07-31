package qucumbah.testgame;

import qucumbah.game.Player;

public class TestGamePlayer extends Player {
  public TestGamePlayer() {
    super(2, 2);
  }

  @Override
  public boolean canMakeAMove() {
    return true;
  }

  @Override
  public double getReward() {
    return game.getRewardForPlayer(this);
  }

  @Override
  public double[] getVision() {
    return ((TestGame)game).getGameState();
  }
}
