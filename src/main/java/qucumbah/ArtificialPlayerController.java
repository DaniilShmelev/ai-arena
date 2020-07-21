package qucumbah;

import qucumbah.game.Player;
import qucumbah.networks.ArtificialBrain;
import qucumbah.util.Util;

public class ArtificialPlayerController extends PlayerController {
  private ArtificialBrain brain;

  public ArtificialPlayerController(Player player, ArtificialBrain brain) {
    super(player);
    this.brain = brain;
  }

  private double[] prevVision;
  private double[] possibleActionsForPrevVision;

  private int curActionIndex;

  @Override
  public void showNewGameState(double[] curVision, double rewardForPrevAction) {
    double[] possibleActionsForCurVision = brain.getPossibleActions(curVision);

    if (!gameFirstTick) {
      brain.memorizeStateTransition(
          prevVision,
          possibleActionsForPrevVision,
          possibleActionsForCurVision,
          rewardForPrevAction
      );
    }

    curActionIndex = Util.maxIndex(possibleActionsForCurVision);

    prevVision = curVision;
    possibleActionsForPrevVision = possibleActionsForCurVision;
  }

  @Override
  public boolean[] getActions() {
    boolean[] actions = new boolean[player.getActionsLength()];
    actions[curActionIndex] = true;

    return actions;
  }
}
