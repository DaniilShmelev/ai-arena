package qucumbah.networks;

import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.nn.api.Layer.TrainingMode;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import qucumbah.util.Util;

public class ArtificialBrain {
  private int longTermMemoryMaxSize;
  private int shortTermMemoryMaxSize;

  private int numberOfExamplesToSaveToLongTermMemory;
  private int numberOfTrainingExamplesToPickFromLongTermMemory;

  private List<TrainingExample> longTermMemory;
  private List<TrainingExample> shortTermMemory;

  private double randomActionProbability;
  private double discountFactor;

  private MultiLayerNetwork network;

  public ArtificialBrain(
      int longTermMemoryMaxSize,
      int shortTermMemoryMaxSize,
      int numberOfExamplesToSaveToLongTermMemory,
      int numberOfTrainingExamplesToPickFromLongTermMemory,
      double randomActionProbability,
      double discountFactor,
      MultiLayerNetwork network
  ) {
    this.longTermMemoryMaxSize = longTermMemoryMaxSize;
    this.shortTermMemoryMaxSize = shortTermMemoryMaxSize;

    this.longTermMemory = new ArrayList<TrainingExample>(longTermMemoryMaxSize);
    this.shortTermMemory = new ArrayList<TrainingExample>(shortTermMemoryMaxSize);

    this.numberOfExamplesToSaveToLongTermMemory = numberOfExamplesToSaveToLongTermMemory;
    this.numberOfTrainingExamplesToPickFromLongTermMemory =
        numberOfTrainingExamplesToPickFromLongTermMemory;

    this.discountFactor = discountFactor;
    this.randomActionProbability = randomActionProbability;

    this.network = network;
  }

  public void memorizeStateTransition(
      double[] prevVision,
      double[] possibleActionsForPrevVision,
      double[] possibleActionsForCurVision,
      double rewardForTransition
  ) {
    double curStateMaxQ = Util.maxValue(possibleActionsForCurVision);
    double targetQ = rewardForTransition + discountFactor * curStateMaxQ;
    int prevStateBestActionIndex = Util.maxIndex(possibleActionsForPrevVision);

    possibleActionsForPrevVision[prevStateBestActionIndex] = targetQ;
    shortTermMemory.add(new TrainingExample(prevVision, possibleActionsForPrevVision));

    if (shortTermMemory.size() == shortTermMemoryMaxSize) {
      train();

      List<TrainingExample> examplesToSaveToLongTermMemory =
          Util.pickRandomly(shortTermMemory, numberOfExamplesToSaveToLongTermMemory);

      for (TrainingExample trainingExample : examplesToSaveToLongTermMemory) {
        if (longTermMemory.size() < longTermMemoryMaxSize) {
          longTermMemory.add(trainingExample);
        } else {
          Util.putInRandomLocation(trainingExample, longTermMemory);
        }
      }

      shortTermMemory.clear();
    }
  }

  private void train() {
    List<TrainingExample> trainingExamples = new ArrayList<>(
        shortTermMemory.size() + numberOfTrainingExamplesToPickFromLongTermMemory);
    trainingExamples.addAll(shortTermMemory);
    if (longTermMemory.size() >= numberOfTrainingExamplesToPickFromLongTermMemory) {
      trainingExamples.addAll(
          Util.pickRandomly(longTermMemory, numberOfTrainingExamplesToPickFromLongTermMemory));
    }

    network.fit(Util.createDataSet(trainingExamples));
  }

  public double[] getPossibleActions(double[] vision) {
    INDArray encodedVision = Nd4j.create(new double[][] { vision });
    INDArray encodedActions = network.output(encodedVision);

    double[] possibleActions = encodedActions.toDoubleMatrix()[0];

    if (Math.random() < randomActionProbability) {
      double bestActionValue = Util.maxValue(possibleActions);
      int randomActionIndex = (int)(Math.random() * possibleActions.length);
      possibleActions[randomActionIndex] = bestActionValue + 0.01;
    }

    return possibleActions;
  }
}
