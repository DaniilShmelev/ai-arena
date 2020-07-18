package qucumbah.game;

public abstract class Player {
  protected Player(int visionLength, int actionsLength) {
    this.visionLength = visionLength;
    this.actionsLength = actionsLength;
  }

  protected Game game;
  public void registerGame(Game game) {
    this.game = game;
  }

  public abstract double getReward();

  protected int visionLength;
  public final int getVisionLength() {
    return visionLength;
  }

  public abstract double[] getVision();

  protected int actionsLength;
  public final int getActionsLength() {
    return actionsLength;
  }

  protected boolean[] actions;
  public void setActions(boolean[] actions) {
    this.actions = actions;
  }
  public boolean[] getActions() {
    return actions;
  }
}
