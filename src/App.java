import java.io.FileNotFoundException;



public class App {
	
	public static void main(String[] args) throws FileNotFoundException {
		Shared.neuralNet = new NeuralNetwork("res/nn_weights.txt", 0.3, 100, 25, 10);
		System.out.println(Shared.neuralNet);
		Shared.recognitionWin = new RecognitionWin();
		Shared.drawWin = new DrawWin();
	}
}
