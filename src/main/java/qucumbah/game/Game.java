package qucumbah.game;

import javafx.scene.canvas.Canvas;

public abstract class Game {
  protected int tickNumber;

  public void executeGameTick() {
    tickNumber += 1;
  }

  public int getTickNumber() {
    return tickNumber;
  }

  private boolean ended = false;
  public boolean isEnded() {
    return ended;
  }

  protected void end() {
    ended = true;
  }

  public void start() { }

  public void restart() {
    tickNumber = 0;
    resetTotalReward();
    ended = false;
  }

  public abstract void addPlayer(Player player);

  public abstract double getRewardForPlayer(Player player);

  public abstract double getTotalRewardForPlayer(Player player);

  public abstract void resetTotalReward();

  public abstract void renderPlayerPOV(Canvas canvas, Player player);
}
