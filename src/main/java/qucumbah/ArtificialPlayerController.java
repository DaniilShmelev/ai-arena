package qucumbah;

import qucumbah.game.Player;
import qucumbah.networks.ArtificialBrain;
import qucumbah.util.Util;

public class ArtificialPlayerController extends PlayerController {
  private ArtificialBrain brain;
  private boolean trainingMode = false;

  public ArtificialPlayerController(Player player, ArtificialBrain brain) {
    super(player);
    this.brain = brain;
  }

  public void setTrainingMode(boolean trainingMode) {
    this.trainingMode = trainingMode;
  }

  private boolean firstTick = true;
  private double[] prevVision;
  private double[] possibleActionsForPrevVision;

  @Override
  public boolean[] getActions(double[] curVision, double rewardForPrevAction) {
    double[] possibleActionsForCurVision = brain.getPossibleActions(curVision);
    int curActionIndex = Util.maxIndex(possibleActionsForCurVision);

    if (trainingMode && !firstTick) {
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
    firstTick = false;

    return actions;
  }
}
