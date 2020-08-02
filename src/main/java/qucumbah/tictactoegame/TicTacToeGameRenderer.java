package qucumbah.tictactoegame;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TicTacToeGameRenderer {
  private TicTacToeGameRenderer() {

  }

  public static void render(Canvas canvas, boolean[][] crosses, boolean[][] noughts) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    double cellWidth = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;

    gc.setStroke(Color.BLACK);

    for (int row = 0; row < 3; row += 1) {
      for (int col = 0; col < 3; col += 1) {
        drawCell(gc, cellWidth, row, col);

        if (crosses[row][col]) {
          drawCross(gc, cellWidth, row, col);
        }

        if (noughts[row][col]) {
          drawNought(gc, cellWidth, row, col);
        }
      }
    }
  }

  private static void drawCell(GraphicsContext gc, double cellWidth, int row, int col) {
    gc.strokeRect(col * cellWidth, row * cellWidth, cellWidth, cellWidth);
  }

  private static void drawCross(GraphicsContext gc, double cellWidth, int row, int col) {
    gc.strokeLine(
        col * cellWidth,
        row * cellWidth,
        (col + 1) * cellWidth,
        (row + 1) * cellWidth
    );
    gc.strokeLine(
        (col + 1) * cellWidth,
        row * cellWidth,
        col * cellWidth,
        (row + 1) * cellWidth
    );
  }

  private static void drawNought(GraphicsContext gc, double cellWidth, int row, int col) {
    gc.strokeOval(col * cellWidth, row * cellWidth, cellWidth, cellWidth);
  }
}
