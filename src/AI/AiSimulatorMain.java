package AI;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import Game.Move;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Gene;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.InputConnection;
import Neurons.InputNeuron;
import Neurons.NeuralLayer;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Search.A_StarNode;
import Search.A_StarSearch;
import Search.ArtifactSearch;
import Search.Container;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AiSimulatorMain  extends Application {
	
	StackPane stackPane = new StackPane();
	Game game; 
	AiSimulator pacSim ;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		layoutWindow();
		simulate();
        Scene scene = new Scene(stackPane, 750, 750);
       // scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
        
	}

	public void layoutWindow (){
		try {
			game = new Game(5, 1);
		} catch (IOException e) {}
		stackPane.getChildren().add(game.getGrid().getCanvas());
		for (GenericAgent agent : game.getAgents().get(GenericAgent.GHOST)){
			stackPane.getChildren().add(agent.getGraphic());}
		for (GenericAgent agent : game.getAgents().get(GenericAgent.PACMAN)){
			stackPane.getChildren().add(agent.getGraphic());}
		stackPane.setAlignment(Pos.TOP_LEFT);
	}
	
	public void simulate () throws DuplicateNeuronID_Exception{
		
		
		NeuralNetwork net1 =  new NeuralNetwork(4, 2, 1, 6);
		NeuralNetwork net2 = new NeuralNetwork(4, 4, 0, 0);
		
		InputReader2 reader1= new InputReader2(game);
		InputReader reader2= new InputReader(game);
		
		net1.setInputReader(reader1);
		net1.setBiaInputs(-1);
		net2.setInputReader(reader2);
		
		AgentEvolver2 e = new AgentEvolver2(game,net1, 40, GenericAgent.PACMAN,-1,1);
		AgentEvolver e2 = new AgentEvolver(game,net2, 200, GenericAgent.GHOST,0,1);
		
//		Random rand = new Random();
//		int g =rand.nextInt(4);
//		for (int i = 0; i < g; i++){
//			NeuralLayer layer = net1.getHiddenLayers().get(0);
//			Neuron neuron = layer.getNeurons().get(rand.nextInt(layer.getNeurons().size()));
//			neuron.setOutputValue(0);
//		}
		e.getAlgorithm().setMutationRate(0.08);
		e2.getAlgorithm().setMutationRate(0.05);
		pacSim = new AiSimulator(game, net1, e.getAlgorithm(), net2, e2.getAlgorithm());
		pacSim.setGhostSimulationProperties(0,2, 100, 0.5);
		pacSim.setPacSimulationProperties(7,0, 100, 1);
		pacSim.setSimulationProperties(5000, 100,35);
		pacSim.startThread("Pacman Simulation",true);	
	}
	
	
	
	
  public static void main(String[] args) {
	 launch(args);

  }
}
