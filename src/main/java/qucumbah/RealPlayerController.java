package qucumbah;

import qucumbah.game.Player;

public abstract class RealPlayerController extends PlayerController {
  public RealPlayerController(Player player, UserIO userIO) {
    super(player);
    setUserIO(userIO);
  }
}
