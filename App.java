import java.io.FileNotFoundException;

import neuralnetwork.NeuralNetwork;


public class App {
	
	public static void main(String[] args) throws FileNotFoundException {
		Shared.neuralNet = new NeuralNetwork("res/nn_weights.txt", 0.3, 100, 10);
		System.out.println(Shared.neuralNet);
		Shared.recognitionWin = new RecognitionWin();
		Shared.drawWin = new DrawWin();
	}
}
