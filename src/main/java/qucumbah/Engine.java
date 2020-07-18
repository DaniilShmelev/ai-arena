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

  public Engine(Game game, List<PlayerController> controllers, int ticksPerSecond) {
    this.game = game;
    this.controllers = controllers;
    this.ticksPerSecond = ticksPerSecond;
    this.isEnded = false;
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

      boolean[] controlledPlayerActions = controller.getActions(
          controlledPlayerVision, controlledPlayerReward);
      controlledPlayer.setActions(controlledPlayerActions);

      if (controller.hasUserIO()) {
        Platform.runLater(() -> {
          Canvas renderCanvas = controller.getUserIO().getCanvas();
          game.renderPlayerPOV(renderCanvas, controlledPlayer);
        });
      }
    }

    game.executeGameTick();

    if (game.isEnded()) {
      Player player = controllers.get(0).player;
      double averageReward = game.getTotalRewardForPlayer(player) / game.getTickNumber();
      System.out.println("Average reward: " + averageReward);

      if (Math.abs(averageReward - 1.0) < 0.001) {
        System.out.println("The AI is now perfect");
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
