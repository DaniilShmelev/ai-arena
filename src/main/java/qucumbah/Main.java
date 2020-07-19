package qucumbah;

import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import qucumbah.networks.ArtificialBrain;
import qucumbah.networks.ArtificialBrainBuilder;
import qucumbah.testgame.TestGame;
import qucumbah.testgame.TestGamePlayer;

public class Main {
  public static void main(String[] args) {
    var game = new TestGame();
    var player = new TestGamePlayer();

    game.addPlayer(player);

    Layer layer0 = new DenseLayer.Builder()
        .nIn(player.getVisionLength())
        .nOut(8)
        .build();
    Layer layer1 = new OutputLayer.Builder()
        .activation(Activation.SIGMOID)
        .nIn(8)
        .nOut(player.getActionsLength())
        .lossFunction(LossFunction.SQUARED_LOSS)
        .build();

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
        .weightInit(WeightInit.XAVIER)
        .activation(Activation.RELU)
        .updater(new Sgd(0.05))
        .list()
        .layer(0, layer0)
        .layer(1, layer1)
        .build();

    MultiLayerNetwork network = new MultiLayerNetwork(conf);
    network.init();

    ArtificialBrain brain = new ArtificialBrainBuilder()
        .setNetwork(network)
        .setDiscountFactor(0)
        .setTrainingMode(true)
        .build();

    var aiController = new ArtificialPlayerController(player, brain);
    // aiController.setUserIO(new UserIO());

    List<PlayerController> playerControllers = new ArrayList<>();
    playerControllers.add(aiController);

    new Engine(game, playerControllers, -1).start();
  }
}
