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

  public abstract boolean[] getActions(double[] vision, double reward);
}
