package neuralnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


// MAS NEURONAS -> MENOR LEARNING RATE

public class NeuralNetwork {

	private int hiddenSize;
	private int inputSize;
	private double[][] weightsItoH;
	private double[] weightsHtoO;
	private double[] ah;
	private double[] ai;
	private double LEARNING_RATE;
	private static final double E = 0.01;

	/**
	 * Creates a MLPNN with specified input layer size, hidden
	 * layer size and an output layers size of 1. This neural network
	 * is trained via backpropagation.
	 * @param learningRate	Value of the learning rate to be used in
	 * 						backpropagation.
	 * @param inputSize		Size of the input layer.
	 * @param hiddenSize	Size of the hidden layer.
	 */
	public NeuralNetwork(double learningRate, int inputSize, int hiddenSize) {
		defaultInit(learningRate, inputSize, hiddenSize);
	}

	/**
	 * Creates a MLPNN using the LR, layer sizes and weights
	 * specified in a file.	
	 * @param filename					Name of the file where the LR, layer.
	 * 									sizes and weights are saved.
	 * @param defLr						Default learning rate to use if an error occurs.
	 * @param defInSize					Default input layer size to use if an error occurs.
	 * @param defHiSize					Default hidden layer size to use if an error occurs.
	 * @throws FileNotFoundException	Thrown if the file is not found.
	 */
	public NeuralNetwork(String filename, double defLr, int defInSize, int defHiSize)
			throws FileNotFoundException {
		File file = new File(filename);
		Scanner in = new Scanner(file);
		try {
			this.LEARNING_RATE = in.nextDouble();
			this.inputSize     = in.nextInt();
			this.hiddenSize    = in.nextInt();
		} catch (Exception e) {
			in.close();
			defaultInit(defLr, defInSize, defHiSize);
			return;
		}
		init();
		loadWeights(in, this.LEARNING_RATE, this.inputSize, this.hiddenSize);
		in.close();
	}

	/**
	 * Initialize attributes.
	 */
	private void init() {
		this.weightsItoH = new double[this.inputSize][this.hiddenSize];
		this.weightsHtoO = new double[this.hiddenSize];
		this.ai 		 = new double[this.inputSize];
		this.ah 		 = new double[this.hiddenSize];
		ah[this.hiddenSize - 1]   = 1.0; // Bias units
		ai[this.inputSize  - 1]   = 1.0;
	}
	
	/**
	 * Default attributes initialization.
	 * @param learningRate	Value of the learning rate to be used in
	 * 						backpropagation.
	 * @param inputSize		Size of the input layer.
	 * @param hiddenSize	Size of the hidden layer.
	 */
	private void defaultInit(double learningRate, int inputSize, int hiddenSize) {
		this.LEARNING_RATE = learningRate;
		this.inputSize   = inputSize + 1;
		this.hiddenSize  = hiddenSize + 1;
		init();
		randomizeWeights();
	}

	/**
	 * Use this method to load the weights from a file.
	 * Expected file format:
	 * 		Learning rate <space> Input size <space> Hidden size.
	 * 		All weights from input layer to hidden layer.
	 * 		All weights from hidden layer to output layer.
	 * If the file format is wrong or its data is wrong,
	 * the weights are randomized.
	 * @param filename					The name of the file to be laoded.	
	 * @throws FileNotFoundException 	Thrown when the file is not found.
	 */
	public void loadWeights(String filename) {
		try {
			Scanner in = new Scanner(new File(filename));
			double lr = in.nextDouble();
			int inSize = in.nextInt();
			int hiSize = in.nextInt();
			loadWeights(in, lr, inSize, hiSize);
			in.close();
		} catch (Exception e) {
			randomizeWeights();
		}
	}

	/**
	 * Loads weights.
	 * @param in		Scanner of file where weights are contained.
	 * @param lr		Learning rate.
	 * @param inSize	Input layer size.
	 * @param hiSize	Hidden layer size.
	 */
	private void loadWeights(Scanner in, double lr, int inSize, int hiSize) {
		if (lr != LEARNING_RATE || inputSize != inSize || hiSize != hiddenSize) {
			randomizeWeights();
			return;
		}
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				weightsItoH[i][j] = in.nextDouble();
		for (int j = 0; j < hiddenSize; j++)
			weightsHtoO[j] = in.nextDouble();
	}

	/**
	 * Use this method to load the weights from a file.
	 * Output file format:
	 * 		Learning rate <space> Input size <space> Hidden size
	 * 		All weights from input layer to hidden layer.
	 * 		All weights from hidden layer to output layer.
	 * @param filename		Name of the file where the weights are going to be saved.	
	 * @throws IOException	Thrown if an I/O error occurs.
	 */
	public void saveWeights(String filename) throws IOException {
		FileWriter f = new FileWriter(new File(filename));
		f.write(LEARNING_RATE + " " + inputSize + " " + hiddenSize + "\n");
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				f.write(String.format("%f\n", weightsItoH[i][j]));
		for (int j = 0; j < hiddenSize; j++)
			f.write(String.format("%f\n", weightsHtoO[j]));
		f.close();
	}

	/**
	 * Use this method to set all weights to random.
	 */
	public void randomizeWeights() {
		for (int j = 0; j < this.hiddenSize; j++) {
			weightsHtoO[j] = rand(-1.0, 1.0);
			for (int i = 0; i < this.inputSize; i++)
				weightsItoH[i][j] = rand(-1.0, 1.0);
		}
	}

	/**
	 * Sigmoid function that is used (tanh).
	 * @param x	Input value.
	 * @return	tanh(x).
	 */
	private double sigmoid(double x) {
		//return 1./(1 + Math.exp(-x));
		return Math.tanh(x);
	}

	/**
	 * Derivative of sigmoid function.
	 * @param y	An activation value.
	 * @return	1 - y^2
	 */
	private double dSigmoid(double y) {
		//return y * (1 - y);
		return 1 - y*y;
	}

	/**
	 * Return a random number between a and b.
	 * @param a	Lower bound.
	 * @param b	Upper bound.
	 * @return	Random number in range [a,b).
	 */
	private double rand(double a, double b) {
		return a + (b - a) * Math.random();
	}

	/**
	 * Perform forward propagtion through the NN.
	 * @param inputs	Activation values for input layer.
	 * @return			Activation value of output layer.
	 */
	private double forwardPropagation(int[] inputs) {
		for (int i = 0; i < inputSize - 1; i++)
			ai[i] = inputs[i];
		for (int j = 0; j < hiddenSize - 1; j++) {
			ah[j] = 0.0;
			for (int i = 0; i < inputSize; i++)
				ah[j] += weightsItoH[i][j] * ai[i];
			ah[j] = sigmoid(ah[j]);
		}
		double out = 0.0;
		for (int j = 0; j < hiddenSize; j++)
			out += ah[j] * weightsHtoO[j];
		return sigmoid(out);
	}

	/**
	 * Perform backpropagation algorithm to update the weights
	 * and train the NN.
	 * @param error		The error (expected - output) where output.
	 * @param output	The output layer neuron actiavation value.
	 */
	private void backPropagation(double error, double output) {
		double deltak = dSigmoid(output) * error;

		// Compute delta for hidden layer neurons
		double[] deltaj = new double[hiddenSize];
		for (int j = 0; j < hiddenSize; j++)
			deltaj[j] = dSigmoid(ah[j]) * deltak * weightsHtoO[j];

		// Update weights from input to hidden layer
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				weightsItoH[i][j] += LEARNING_RATE * deltaj[j] * ai[i];

		// Update weights from hidden to output layer
		for (int j = 0; j < hiddenSize; j++)
			weightsHtoO[j] += LEARNING_RATE * deltak * ah[j];
	}

	/**
	 * Train the neural network for iterLimit iterations or until for
	 * each pattern input abs(expected - output) <= 0.01
	 * @param inputs	List of all input patterns to be used.
	 * @param outputs	Expected output for each input pattern.
	 * @param iterLimit	Limit of iterations.
	 * @return Iterations performed.
	 */
	public int train(int[][] inputs, double[] outputs, int iterLimit) {
		int success = 0, iters = 0;
		while (success < inputs.length && iters < iterLimit) {
			for (int i = 0; i < inputs.length; i++) {
				double output = forwardPropagation(inputs[i]);
				double error = outputs[i] - output;
				if (Math.abs(error) > E) {
					backPropagation(error, output);
					success = 0;
				}
				else success++;
			}
			iters++;
		}
		return iters;
	}
	
	/**
	 * Run the neural network with pattern as input.
	 * @param pattern	Input pattern.
	 * @return			Output layer neuron activation value.
	 */
	public double eval(int[] pattern) {
		return forwardPropagation(pattern);
	}

	/**
	 * Test the NN with specified inputs and expected outputs.
	 * @param inputs	List of all input patterns to be used.
	 * @param outputs	Expected output for each input pattern.
	 * @param print		If true, print the expected output and the output
	 * 					and at the end, print the success rate and the mean
	 * 					square error.
	 * @return			Array where index 0 = success rate, and index
	 * 					1 = mean square error.
	 */
	public double[] test(int[][] inputs, double[] outputs, boolean print) {
		double[] r = {0.0, 0.0};
		for (int i = 0; i < inputs.length; i++) {
			double x = forwardPropagation(inputs[i]);
			if (print) System.out.println("Expected: " + outputs[i] + " Result: " + x);
			if (Math.abs(outputs[i] - x) <= E) r[0] += 1.0/inputs.length;
			r[1] += (outputs[i] - x)*(outputs[i] - x)/(double)inputs.length;
		}
		if (print) {
			System.out.println("Success: " + r[0]*100 + "%");
			System.out.println("MSE:     " + String.format("%.8f", r[1]));
		}
		return r;
	}

	/**
	 * Unit test. Train the NN to perform XOR operations.
	 * @param args None expected
	 */
	public static void main(String[] args) {
		int[][] inputs = {
				{0, 0},
				{0, 1},
				{1, 0},
				{1, 1}
		};
		double[] outputs = {0, 1, 1, 0};
		NeuralNetwork nn = new NeuralNetwork(0.5, 2, 2);
		System.out.println(nn.train(inputs, outputs, 10000));
		nn.test(inputs, outputs, true);
	}
	
	public String toString() {
		String s = "Weights I->H = " + Arrays.deepToString(weightsItoH);
		s += "\nWeights H->O = " + Arrays.toString(weightsHtoO);
		s += "\nLearning Rate = " + LEARNING_RATE;
		return s;
	}

}
