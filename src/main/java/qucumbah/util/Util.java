package qucumbah.util;

import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.JFXPanel;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import qucumbah.networks.TrainingExample;

public class Util {
  private static final Thread mainThread = Thread.currentThread();

  public static Thread getMainThread() {
    return mainThread;
  }

  private static boolean javaFXToolkitInitialized = false;

  public static boolean isJavaFXToolkitInitialized() {
    return javaFXToolkitInitialized;
  }

  public static void initializeJavaFXToolkit() {
    new JFXPanel();
    javaFXToolkitInitialized = true;
  }

  public static <T> List<T> pickRandomly(List<T> from, int numberOfElementsToPick) {
    if (numberOfElementsToPick > from.size()) {
      throw new IllegalArgumentException("Can't pick " + numberOfElementsToPick + "elements"
          + " from a list of size " + from.size());
    }

    List<T> pickedElements = new ArrayList<>(numberOfElementsToPick);
    int leftToPick = numberOfElementsToPick;
    int elementsLeft = from.size();
    for (T element : from) {
      double probabilityOfBeingPicked = (double)leftToPick / elementsLeft;
      if (Math.random() < probabilityOfBeingPicked) {
        pickedElements.add(element);
        leftToPick -= 1;
      }

      if (leftToPick == 0) {
        break;
      }

      elementsLeft -= 1;
    }

    return pickedElements;
  }

  public static <T> void putInRandomLocation(T element, List<T> to) {
    int randomLocation = (int)(to.size() * Math.random());
    to.set(randomLocation, element);
  }

  public static DataSet createDataSet(List<TrainingExample> examples) {
    double[][] inputs = new double[examples.size()][];
    double[][] outputs = new double[examples.size()][];

    for (int i = 0; i < examples.size(); i += 1) {
      inputs[i] = examples.get(i).inputs;
      outputs[i] = examples.get(i).outputs;
    }

    return new DataSet(Nd4j.create(inputs), Nd4j.create(outputs));
  }

  public static int maxIndex(double[] array) {
    int maxIndex = -1;

    for (int i = 0; i < array.length; i += 1) {
      if (maxIndex == -1 || array[i] > array[maxIndex]) {
        maxIndex = i;
      }
    }

    return maxIndex;
  }

  public static double maxValue(double[] array) {
    double maxValue = Double.NEGATIVE_INFINITY;

    for (int i = 1; i < array.length; i += 1) {
      if (array[i] > maxValue) {
        maxValue = array[i];
      }
    }

    return maxValue;
  }
}
