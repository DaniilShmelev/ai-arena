package qucumbah;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import qucumbah.game.Game;
import qucumbah.game.Player;
import qucumbah.util.Util;

public class Engine {
  private Game game;
  private List<PlayerController> controllers;
  private int ticksPerSecond;
  private boolean isEnded;

  private int gamesPlayed;
  private int gamesToPlay;

  public Engine(
      Game game,
      List<PlayerController> controllers,
      int ticksPerSecond,
      int gamesToPlay
  ) {
    this.game = game;
    this.controllers = controllers;
    this.ticksPerSecond = ticksPerSecond;
    this.isEnded = false;

    this.gamesPlayed = 0;
    this.gamesToPlay = gamesToPlay;
  }

  public Engine(
      Game game,
      List<PlayerController> controllers,
      int ticksPerSecond
  ) {
    this(game, controllers, ticksPerSecond, -1);
  }

  public void start() {
    if (Thread.currentThread() != Util.getMainThread()) {
      throw new IllegalThreadStateException("The engine has to be run on the main thread");
    }

    game.start();

    while (!isEnded) {
      update();
    }
  }

  private void update() {
    long tickStart = System.currentTimeMillis();

    for (PlayerController controller : controllers) {
      Player controlledPlayer = controller.player;
      double[] controlledPlayerVision = controlledPlayer.getVision();
      double controlledPlayerReward = controlledPlayer.getReward();

      controller.setGameFirstTick(game.getTickNumber() == 0);
      controller.showNewGameState(controlledPlayerVision, controlledPlayerReward);
      controlledPlayer.setActions(controller.getActions());

      if (controller.hasUserIO()) {
        Platform.runLater(() -> {
          Canvas renderCanvas = controller.getUserIO().getCanvas();
          game.renderPlayerPOV(renderCanvas, controlledPlayer);
        });
      }
    }

    game.executeGameTick();

    if (game.isEnded()) {
      for (PlayerController controller : controllers) {
        Player controlledPlayer = controller.player;
        double[] controlledPlayerVision = controlledPlayer.getVision();
        double controlledPlayerReward = controlledPlayer.getReward();

        controller.showNewGameState(controlledPlayerVision, controlledPlayerReward);
      }

      gamesPlayed += 1;
      System.out.println("Game number " + gamesPlayed + " has ended");

      for (int playerIndex = 0; playerIndex < controllers.size(); playerIndex += 1) {
        Player player = controllers.get(playerIndex).player;
        double totalRewardForThisPlayer = game.getTotalRewardForPlayer(player);

        System.out.println(
            "Total reward for player " + playerIndex + ": " + totalRewardForThisPlayer);
      }

      if (gamesPlayed == gamesToPlay) {
        stop();
        return;
      }

      game.restart();
    }

    if (ticksPerSecond == -1) {
      return;
    }

    long tickEnd = System.currentTimeMillis();

    long intendedTickDuration = 1000 / ticksPerSecond;
    long actualTickDuration = tickEnd - tickStart;

    if (actualTickDuration < intendedTickDuration) {
      try {
        Thread.sleep(intendedTickDuration - actualTickDuration);
      } catch (InterruptedException exception) {
        // It's ok to suppress this exception here since this method is guaranteed to run on the
        // main thread
      }
    }
  }

  public void stop() {
    isEnded = true;
  }
}
