package qucumbah;

import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import qucumbah.util.Util;

public class UserIO extends Application {
  private Canvas canvas = new Canvas(800, 600);

  public UserIO() {
    if (!Util.isJavaFXToolkitInitialized()) {
      Util.initializeJavaFXToolkit();
    }

    Platform.runLater(() -> start(new Stage()));
  }

  @Override
  public void start(Stage stage) {
    var root = new BorderPane();
    root.setCenter(canvas);
    var scene = new Scene(root, 800, 600);

    scene.setOnKeyPressed((keyEvent) -> setKeyPressed(keyEvent.getCode().getCode(), true));
    scene.setOnKeyReleased((keyEvent) -> setKeyPressed(keyEvent.getCode().getCode(), false));

    stage.setScene(scene);
    stage.setTitle("AI Arena");
    stage.show();

    stage.setOnCloseRequest((event) -> System.exit(0));
  }

  public Canvas getCanvas() {
    return canvas;
  }

  private boolean[] pressedKeys = new boolean[128];

  public synchronized boolean[] getPressedKeysCopy() {
    return Arrays.copyOf(pressedKeys, pressedKeys.length);
  }

  private synchronized void setKeyPressed(int keyCode, boolean isPressed) {
    if (keyCode < 0 || keyCode >= pressedKeys.length) {
      return;
    }

    if (isPressed) {
      notifyAll();
    }

    pressedKeys[keyCode] = isPressed;
  }

  public void awaitKeyPress() throws InterruptedException {
    wait();
  }
}
