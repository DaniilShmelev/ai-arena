package qucumbah;

import qucumbah.game.Player;

public abstract class PlayerController {
  public final Player player;
  protected UserIO userIO;

  public PlayerController(Player player) {
    this.player = player;
  }

  public boolean hasUserIO() {
    return userIO != null;
  }

  public UserIO getUserIO() {
    return userIO;
  }

  public void setUserIO(UserIO userIO) {
    this.userIO = userIO;
  }

  protected boolean gameFirstTick;
  public void setGameFirstTick(boolean newValue) {
    gameFirstTick = newValue;
  }

  public abstract void showNewGameState(double[] vision, double rewardForStateTransition);

  public abstract boolean[] getActions();
}
