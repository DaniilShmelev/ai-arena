package qucumbah.testgame;

import java.util.Arrays;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import qucumbah.game.Game;
import qucumbah.game.Player;

public class TestGame extends Game {
  private TestGamePlayer player;
  private double reward;
  private double totalReward;

  public TestGame() {
    setRandomGameState();
  }

  @Override
  public void addPlayer(Player player) {
    if (this.player != null) {
      throw new IllegalArgumentException("There is already a player in the game");
    }

    if (!(player instanceof TestGamePlayer)) {
      throw new IllegalArgumentException("Player has to be of class TestGamePlayer");
    }

    this.player = (TestGamePlayer)player;
    player.registerGame(this);

    reward = 0;
  }

  @Override
  public double getRewardForPlayer(Player player) {
    if (player != this.player) {
      throw new IllegalArgumentException("Given player isn't playing this game");
    }

    return reward;
  }

  @Override
  public double getTotalRewardForPlayer(Player player) {
    if (player != this.player) {
      throw new IllegalArgumentException("Given player isn't playing this game");
    }

    return totalReward;
  }

  @Override
  public void resetTotalReward() {
    totalReward = 0;
  }

  private double[] gameState = new double[2];
  public double[] getGameState() {
    return Arrays.copyOf(gameState, gameState.length);
  }

  private void setRandomGameState() {
    gameState[0] = Math.random() > 0.5 ? 1 : 0;
    gameState[1] = Math.random() > 0.5 ? 1 : 0;
  }

  @Override
  public void executeGameTick() {
    super.executeGameTick();

    boolean state1 = gameState[0] > 0.5;
    boolean state2 = gameState[1] > 0.5;

    boolean[] playerActions = player.getActions();
    boolean rightAction = state1 ^ state2;
    boolean playerAction = playerActions[1];

    reward = (playerAction == rightAction) ? 1.0 : 0.0;
    totalReward += reward;

    setRandomGameState();

    if (tickNumber == 100) {
      end();
    }
  }

  @Override
  public void renderPlayerPOV(Canvas canvas, Player player) {
    if (player != TestGame.this.player) {
      throw new IllegalArgumentException("Given player isn't playing this game");
    }

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.setStroke(Color.BLACK);
    gc.strokeText("Game state: " + gameState[0] + " ^ " + gameState[1], 20, 40);
    gc.strokeText("Player choice: " + player.getActions()[1], 20, 60);
  }
}
