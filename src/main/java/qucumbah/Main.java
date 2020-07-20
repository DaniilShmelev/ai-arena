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
import qucumbah.tictactoegame.TicTacToeGame;
import qucumbah.tictactoegame.TicTacToeGamePlayer;

public class Main {
  public static void main(String[] args) {
    // launchTicTacToeGame();
    launchTestGame();
  }

  private static void launchTicTacToeGame() {
    var game = new TicTacToeGame();
    var player1 = new TicTacToeGamePlayer();
    var player2 = new TicTacToeGamePlayer();

    game.addPlayer(player1);
    game.addPlayer(player2);

    ArtificialBrain brain1 = createFreshTicTacToeBrain();
    ArtificialBrain brain2 = createFreshTicTacToeBrain();

    ArtificialPlayerController controller1 = new ArtificialPlayerController(player1, brain1);
    ArtificialPlayerController controller2 = new ArtificialPlayerController(player2, brain2);

    // controller1.setUserIO(new UserIO());

    List<PlayerController> playerControllers = new ArrayList<>();
    playerControllers.add(controller1);
    playerControllers.add(controller2);

    new Engine(game, playerControllers, -1).start();
  }

  private static ArtificialBrain createFreshTicTacToeBrain() {
    var tempTicTacToeGamePlayer = new TicTacToeGamePlayer();

    int numberOfInputs = tempTicTacToeGamePlayer.getVisionLength();
    int numberOfOutputs = tempTicTacToeGamePlayer.getActionsLength();

    Layer layer0 = new DenseLayer.Builder()
        .nIn(numberOfInputs)
        .nOut(32)
        .build();
    Layer layer1 = new DenseLayer.Builder()
        .nIn(32)
        .nOut(16)
        .build();
    Layer layer2 = new OutputLayer.Builder()
        .activation(Activation.SIGMOID)
        .nIn(16)
        .nOut(numberOfOutputs)
        .lossFunction(LossFunction.SQUARED_LOSS)
        .build();

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
        .weightInit(WeightInit.XAVIER)
        .activation(Activation.RELU)
        .updater(new Sgd(0.05))
        .list()
        .layer(0, layer0)
        .layer(1, layer1)
        .layer(2, layer2)
        .build();

    MultiLayerNetwork network = new MultiLayerNetwork(conf);
    network.init();

    return new ArtificialBrainBuilder()
        .setNetwork(network)
        .setDiscountFactor(0.8)
        .setTrainingMode(true)
        .build();
  }

  private static void launchTestGame() {
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
