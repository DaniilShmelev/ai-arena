package qucumbah;

import qucumbah.game.Player;

public abstract class RealPlayerController extends PlayerController {
  public RealPlayerController(Player player, UserIO userIO) {
    super(player);
    setUserIO(userIO);
  }

  protected double[] vision;
  protected double rewardForStateTransition;

  @Override
  public void showNewGameState(double[] vision, double rewardForStateTransition) {
    this.vision = vision;
    this.rewardForStateTransition = rewardForStateTransition;
  }
}
