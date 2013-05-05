import java.util.HashMap;

import neuralnetwork.NeuralNetwork;



public class Shared {
	public static DrawWin drawWin;
	public static DrawPanel drawPanel;
	public static RecognitionWin recognitionWin;
	public static NeuralNetwork neuralNet;
	private static final HashMap<Integer, Double> digits;
	
	static {
		digits = new HashMap<Integer, Double>();
		digits.put(0, 0.7);
		digits.put(1, 0.1);
		digits.put(2, 0.4);
		digits.put(3, 0.6);
		digits.put(4, 0.3);
		digits.put(5, 0.5);
		digits.put(6, 0.8);
		digits.put(7, 0.2);
		digits.put(8, 1.0);
		digits.put(9, 0.9);
	}
	
	public static double getDigit(int d) {
		return digits.get(d);
	}
	
}
