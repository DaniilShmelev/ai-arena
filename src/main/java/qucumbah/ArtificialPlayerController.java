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

  @Override
  public boolean[] getActions(double[] curVision, double rewardForPrevAction) {
    double[] possibleActionsForCurVision = brain.getPossibleActions(curVision);
    int curActionIndex = Util.maxIndex(possibleActionsForCurVision);

    boolean firstTick = player.getGame().getTickNumber() == 0;

    if (!firstTick) {
      brain.memorizeStateTransition(
          prevVision,
          possibleActionsForPrevVision,
          possibleActionsForCurVision,
          rewardForPrevAction
      );
    }

    boolean[] actions = new boolean[player.getActionsLength()];
    actions[curActionIndex] = true;

    prevVision = curVision;
    possibleActionsForPrevVision = possibleActionsForCurVision;

    return actions;
  }
}
