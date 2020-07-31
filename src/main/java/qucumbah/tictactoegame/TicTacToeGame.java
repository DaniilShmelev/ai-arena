package qucumbah.tictactoegame;

import javafx.scene.canvas.Canvas;
import qucumbah.game.Game;
import qucumbah.game.Player;

public class TicTacToeGame extends Game {
  private TicTacToeGamePlayer[] players = new TicTacToeGamePlayer[2];

  private boolean isCrossesTurn;
  private int crossesPlayerIndex;

  private boolean[][] crosses;
  private boolean[][] noughts;

  public TicTacToeGame() {
    restart();
  }

  @Override
  public void restart() {
    super.restart();

    crossesPlayerIndex = (int)(Math.random() * 2);

    crosses = new boolean[3][3];
    noughts = new boolean[3][3];

    isCrossesTurn = true;
  }

  public boolean isCrossesTurn() {
    return isCrossesTurn;
  }

  public boolean isPlayerCrosses(TicTacToeGamePlayer player) {
    return player == players[crossesPlayerIndex];
  }

  public double[] getVisionForPlayer(TicTacToeGamePlayer player) {
    if (players[0] != player && players[1] != player) {
      throw new IllegalArgumentException("This player isn't in the game");
    }

    boolean isCurrentPlayerCrosses = isPlayerCrosses(players[getCurrentPlayerIndex()]);

    boolean playersTurn = (
        isCrossesTurn && isCurrentPlayerCrosses
        || !isCrossesTurn && !isCurrentPlayerCrosses
    );

    double[] vision = new double[19];

    vision[0] = playersTurn ? 1.0 : 0.0;

    boolean[][] currentPlayerFigures = isCurrentPlayerCrosses ? crosses : noughts;
    boolean[][] enemyPlayerFigures = isCurrentPlayerCrosses ? noughts : crosses;

    for (int i = 0; i < 9; i += 1) {
      int row = i / 3;
      int col = i % 3;
      vision[1 + i] = currentPlayerFigures[row][col] ? 1.0 : 0.0;
      vision[1 + 9 + i] = enemyPlayerFigures[row][col] ? 1.0 : 0.0;
    }

    return vision;
  }

  @Override
  public void executeGameTick() {
    super.executeGameTick();

    int currentPlayerIndex = getCurrentPlayerIndex();

    TicTacToeGamePlayer currentPlayer = players[currentPlayerIndex];
    TicTacToeGamePlayer enemyPlayer = players[1 - currentPlayerIndex];

    boolean[] currentPlayerActions = currentPlayer.getActions();

    int row = -1;
    int col = -1;
    for (int i = 0; i < 9; i += 1) {
      if (currentPlayerActions[i]) {
        row = i / 3;
        col = i % 3;
        break;
      }
    }

    boolean noCellSpecified = row == -1;
    boolean specifiedCellIsTaken = crosses[row][col] || noughts[row][col];

    final double lossReward = 0;
    final double winReward = 1;
    final double idleReward = 0.07;

    if (noCellSpecified || specifiedCellIsTaken) {
      assignReward(currentPlayer, lossReward);
      assignReward(enemyPlayer, idleReward);
      end();
      return;
    }

    if (isCrossesTurn) {
      crosses[row][col] = true;
    } else {
      noughts[row][col] = true;
    }

    boolean moveLeadToVictory = (
        isCrossesTurn && checkIfCrossesWon()
        || !isCrossesTurn && checkIfNoughtsWon()
    );

    // Victory (loss == victory of enemy player)
    if (moveLeadToVictory) {
      assignReward(currentPlayer, winReward);
      assignReward(enemyPlayer, lossReward);
      end();
      return;
    }

    // Tie
    if (tickNumber == 9) {
      assignReward(currentPlayer, lossReward);
      assignReward(enemyPlayer, lossReward);
      end();
      return;
    }

    // Game keeps going
    assignReward(currentPlayer, idleReward);
    assignReward(enemyPlayer, idleReward);
    isCrossesTurn = !isCrossesTurn;
  }

  private boolean checkIfCrossesWon() {
    return hasFilledLine(crosses);
  }

  private boolean checkIfNoughtsWon() {
    return hasFilledLine(noughts);
  }

  private boolean hasFilledLine(boolean[][] field) {
    // Rows
    for (int row = 0; row < 3; row += 1) {
      if (field[row][0] && field[row][1] && field[row][2]) {
        return true;
      }
    }

    // Columns
    for (int col = 0; col < 3; col += 1) {
      if (field[0][col] && field[1][col] && field[2][col]) {
        return true;
      }
    }

    // Main diagonal
    if (field[0][0] && field[1][1] && field[2][2]) {
      return true;
    }

    // Secondary diagonal
    if (field[0][2] && field[1][1] && field[2][0]) {
      return true;
    }

    return false;
  }

  private int getCurrentPlayerIndex() {
    return isCrossesTurn ? crossesPlayerIndex : 1 - crossesPlayerIndex;
  }

  @Override
  public void addPlayer(Player player) {
    if (!(player instanceof TicTacToeGamePlayer)) {
      throw new IllegalArgumentException("Player has to be of class TestGamePlayer");
    }

    if (players[0] == null) {
      players[0] = (TicTacToeGamePlayer)player;
    } else if (players[1] == null) {
      players[1] = (TicTacToeGamePlayer)player;
    } else {
      throw new IllegalArgumentException("This game is full");
    }

    player.registerGame(this);
  }

  @Override
  public void renderPlayerPOV(Canvas canvas, Player player) {
    if (player != players[0] && player != players[1]) {
      throw new IllegalArgumentException("The player isn't in this game");
    }

    TicTacToeGameRenderer.render(canvas, crosses, noughts);
  }
}
