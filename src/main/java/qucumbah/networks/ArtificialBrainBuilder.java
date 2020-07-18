package qucumbah.networks;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class ArtificialBrainBuilder {
  private int longTermMemoryMaxSize = 100;
  private int shortTermMemoryMaxSize = 50;

  private int numberOfExamplesToSaveToLongTermMemory = 5;
  private int numberOfTrainingExamplesToPickFromLongTermMemory = 10;

  private double discountFactor = 0.9;

  private MultiLayerNetwork network;

  public ArtificialBrainBuilder setLongTermMemoryMaxSize(int newSize) {
    longTermMemoryMaxSize = newSize;
    return this;
  }

  public ArtificialBrainBuilder setShortTermMemoryMaxSize(int newSize) {
    shortTermMemoryMaxSize = newSize;
    return this;
  }

  public ArtificialBrainBuilder setNumberOfExamplesToSaveToLongTermMemory(int newNumber) {
    numberOfExamplesToSaveToLongTermMemory = newNumber;
    return this;
  }

  public ArtificialBrainBuilder setNumberOfTrainingExamplesToPickFromLongTermMemory(int newNumber) {
    numberOfTrainingExamplesToPickFromLongTermMemory = newNumber;
    return this;
  }

  public ArtificialBrainBuilder setDiscountFactor(double newDiscountFactor) {
    discountFactor = newDiscountFactor;
    return this;
  }

  public ArtificialBrainBuilder setNetwork(MultiLayerNetwork newNetwork) {
    network = newNetwork;
    return this;
  }

  public ArtificialBrain build() {
    return new ArtificialBrain(
        longTermMemoryMaxSize,
        shortTermMemoryMaxSize,
        numberOfExamplesToSaveToLongTermMemory,
        numberOfTrainingExamplesToPickFromLongTermMemory,
        discountFactor,
        network
    );
  }
}
