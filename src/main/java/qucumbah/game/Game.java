package qucumbah.game;

import java.util.HashMap;
import java.util.Map;
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

  public void start() {

  }

  public void restart() {
    tickNumber = 0;
    resetTotalReward();
    ended = false;
  }

  private Map<Player, Double> rewards = new HashMap<>();

  public double getRewardForPlayer(Player player) {
    return rewards.getOrDefault(player, 0.0);
  }

  private Map<Player, Double> totalRewards = new HashMap<>();

  public double getTotalRewardForPlayer(Player player) {
    return totalRewards.getOrDefault(player, 0.0);
  }

  protected void resetTotalReward() {
    totalRewards.clear();
  }

  protected void assignReward(Player player, double reward) {
    rewards.put(player, reward);
    totalRewards.merge(player, reward, (oldValue, newValue) -> oldValue + newValue);
  }

  public abstract void addPlayer(Player player);

  public abstract void renderPlayerPOV(Canvas canvas, Player player);
}
