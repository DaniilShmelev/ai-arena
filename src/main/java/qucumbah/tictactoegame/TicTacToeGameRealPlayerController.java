package qucumbah.tictactoegame;

import qucumbah.RealPlayerController;
import qucumbah.UserIO;

public class TicTacToeGameRealPlayerController extends RealPlayerController {
  public TicTacToeGameRealPlayerController(TicTacToeGamePlayer player, UserIO userIO) {
    super(player, userIO);
  }

  @Override
  public boolean[] getActions(double[] vision, double reward) {
    boolean[] actions = new boolean[9];

    int[] keyCodesForActions = new int[] { 100, 101, 102, 103, 104, 105, 106, 107, 108 };
    while (true) {
      boolean[] pressedKeys = userIO.getPressedKeysCopy();

      for (int actionIndex = 0; actionIndex < 9; actionIndex += 1) {
        int keyCodeForThisAction = keyCodesForActions[actionIndex];

        if (pressedKeys[keyCodeForThisAction]) {
          actions[actionIndex] = true;
          return actions;
        }
      }

      try {
        userIO.awaitKeyPress();
      } catch (InterruptedException exception) {
        return actions;
      }
    }
  }
}
