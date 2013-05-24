

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


// MAS NEURONAS -> MENOR LEARNING RATE

public class NeuralNetwork {

	private int hiddenSize, inputSize, outputSize, iters;
	private double[][] weightsItoH;
	private double[][] weightsHtoO;
	private double[] ah;
	private double[] ai;
	private double[] ao;
	private double LEARNING_RATE;
	private static final double E = 0.001;

	/**
	 * Creates a MLPNN with specified input layer size, hidden
	 * layer size and an output layers size of 1. This neural network
	 * is trained via backpropagation.
	 * @param learningRate	Value of the learning rate to be used in
	 * 						backpropagation.
	 * @param inputSize		Size of the input layer.
	 * @param hiddenSize	Size of the hidden layer.
	 * @param outputSize	Size of the hidden layer.
	 */
	public NeuralNetwork(double learningRate, int inputSize, int hiddenSize, int outputSize) {
		defaultInit(learningRate, inputSize, hiddenSize, outputSize);
	}

	/**
	 * Creates a MLPNN using the LR, layer sizes and weights
	 * specified in a file.	
	 * @param filename					Name of the file where the LR, layer.
	 * 									sizes and weights are saved.
	 * @param defLr						Default learning rate to use if an error occurs.
	 * @param defInSize					Default input layer size to use if an error occurs.
	 * @param defHiSize					Default hidden layer size to use if an error occurs.
	 * @param defOutSize				Default output layer size to use if an error occurs.
	 * @throws FileNotFoundException	Thrown if the file is not found.
	 */
	public NeuralNetwork(String filename, double defLr, int defInSize, int defHiSize, int defOutSize)
			throws FileNotFoundException {
		File file = new File(filename);
		Scanner in = new Scanner(file);
		try {
			this.LEARNING_RATE = in.nextDouble();
			this.inputSize     = in.nextInt();
			this.hiddenSize    = in.nextInt();
			this.outputSize    = in.nextInt();
		} catch (Exception e) {
			in.close();
			defaultInit(defLr, defInSize, defHiSize, defOutSize);
			return;
		}
		init();
		loadWeights(in, this.LEARNING_RATE, this.inputSize, this.hiddenSize, this.outputSize);
		in.close();
	}

	/**
	 * Initialize attributes.
	 */
	private void init() {
		this.weightsItoH = new double[this.inputSize][this.hiddenSize];
		this.weightsHtoO = new double[this.hiddenSize][this.outputSize];
		this.ai 		 = new double[this.inputSize];
		this.ah 		 = new double[this.hiddenSize];
		this.ao 		 = new double[this.outputSize];
		ah[this.hiddenSize - 1]   = 1.0; // Bias units
		ai[this.inputSize  - 1]   = 1.0;
		iters = 0;
	}
	
	/**
	 * Default attributes initialization.
	 * @param learningRate	Value of the learning rate to be used in
	 * 						backpropagation.
	 * @param inputSize		Size of the input layer.
	 * @param hiddenSize	Size of the hidden layer.
	 * @param outputSize	Size of the output layer.
	 */
	private void defaultInit(double learningRate, int inputSize, int hiddenSize, int outputSize) {
		this.LEARNING_RATE = learningRate;
		this.inputSize   = inputSize + 1;
		this.hiddenSize  = hiddenSize + 1;
		this.outputSize  = outputSize;
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
			int outSize = in.nextInt();
			loadWeights(in, lr, inSize, hiSize, outSize);
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
	 * @param outSize	Output layer size.
	 */
	private void loadWeights(Scanner in, double lr, int inSize, int hiSize, int outSize) {
		if (lr != LEARNING_RATE || inputSize != inSize || hiSize != hiddenSize || outSize != outputSize) {
			randomizeWeights();
			return;
		}
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				weightsItoH[i][j] = in.nextDouble();
		for (int j = 0; j < hiddenSize; j++)
			for (int k = 0; k < outputSize; k++)
			weightsHtoO[j][k] = in.nextDouble();
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
		f.write(LEARNING_RATE + " " + inputSize + " " + hiddenSize + " " + outputSize + "\n");
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				f.write(String.format("%f\n", weightsItoH[i][j]));
		for (int j = 0; j < hiddenSize; j++)
			for (int k = 0; k < outputSize; k++)
				f.write(String.format("%f\n", weightsHtoO[j][k]));
		f.close();
	}

	/**
	 * Use this method to set all weights to random.
	 */
	public void randomizeWeights() {
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				weightsItoH[i][j] = rand(-1.0, 1.0);
		for (int j = 0; j < hiddenSize; j++)
			for (int k = 0; k < outputSize; k++)
				weightsHtoO[j][k] = rand(-1.0, 1.0);
	}

	/**
	 * Sigmoid function that is used (tanh).
	 * @param x	Input value.
	 * @return	logistic(x).
	 */
	private double sigmoid(double x) {
		return 1./(1 + Math.exp(-x));
		//return Math.tanh(x);
	}

	/**
	 * Derivative of sigmoid function.
	 * @param y	An activation value.
	 * @return	y * (1 - y)
	 */
	private double dSigmoid(double y) {
		return y * (1 - y);
		//return 1 - y*y;
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
	 * Perform forward propagation through the NN.
	 * @param inputs	Activation values for input layer.
	 * @return			Activation value of output layer.
	 */
	private void forwardPropagation(int[] inputs) {
		// Compute activations for input layer neurons
		for (int i = 0; i < inputSize - 1; i++)
			ai[i] = inputs[i];

		// Compute activations for hidden layer neurons
		for (int j = 0; j < hiddenSize - 1; j++) {
			ah[j] = 0.0;
			for (int i = 0; i < inputSize; i++)
				ah[j] += weightsItoH[i][j] * ai[i];
			ah[j] = sigmoid(ah[j]);
		}

		// Compute activations for output layer neurons
		for (int k = 0; k < outputSize; k++) {
			ao[k] = 0.0;
			for (int j = 0; j < hiddenSize; j++)
				ao[k] += ah[j] * weightsHtoO[j][k];
			ao[k] = sigmoid(ao[k]);
		}
	}

	/**
	 * Perform backpropagation algorithm to update the weights
	 * and train the NN.
	 * @param error		The error (expected - output) where output.
	 * @param output	The output layer neuron activation value.
	 */
	private void backPropagation(double[] errors) {
		// Compute delta for output layer neuron
		double[] deltak = new double[outputSize];
		for (int k = 0; k < outputSize; k++)
			deltak[k] = dSigmoid(ao[k]) * errors[k];
		
		// Compute delta for hidden layer neurons
		double[] deltaj = new double[hiddenSize];
		for (int j = 0; j < hiddenSize; j++)
			for (int k = 0; k < outputSize; k++)
				deltaj[j] += dSigmoid(ah[j]) * deltak[k] * weightsHtoO[j][k];

		// Update weights from input to hidden layer
		for (int i = 0; i < inputSize; i++)
			for (int j = 0; j < hiddenSize; j++)
				weightsItoH[i][j] += LEARNING_RATE * deltaj[j] * ai[i];

		// Update weights from hidden to output layer
		for (int j = 0; j < hiddenSize; j++)
			for (int k = 0; k < outputSize; k++)
				weightsHtoO[j][k] += LEARNING_RATE * deltak[k] * ah[j];
	}

	/**
	 * Train the neural network for iterLimit iterations or until for
	 * each pattern input abs(expected - output) <= 0.01
	 * @param inputs	List of all input patterns to be used.
	 * @param outputs	Expected output for each input pattern.
	 * @param iterLimit	Limit of iterations.
	 */
	public void train(int[][] inputs, int[][] outputs, int iterLimit) {
		for (int c = 0; c < iterLimit; c++, iters++)
			for (int i = 0; i < inputs.length; i++) {
				forwardPropagation(inputs[i]);
				double[] errors = new double[outputSize];
				for (int k = 0; k < outputSize; k++)
					errors[k] = outputs[i][k] - ao[k];
				backPropagation(errors);
			}
	}
	
	/**
	 * Run the neural network with pattern as input.
	 * @param pattern	Input pattern.
	 * @return			Neuron fired.
	 */
	public int eval(int[] pattern) {
		forwardPropagation(pattern);
		return interpret();
	}
	
	/**
	 * Maximum activation value.
	 * @return	Neuron index.
	 */
	private int interpret() {
		if (outputSize == 1) return (ao[0] < 0.5)? 0 : 1;
		int index = 0;
		double max = ao[0];
		for (int k = 1; k < outputSize; k++)
			if (ao[k] > max) {
				max = ao[k]; index = k;
			}
		return index;
	}
	
	/**
	 * Find the neuron with the maximum activation value.
	 * @return	Neuron index.
	 */
	private int maxIndex(int[] pattern) {
		int index = 0;
		double max = pattern[0];
		for (int k = 1; k < outputSize; k++)
			if (pattern[k] > max) {
				max = pattern[k]; index = k;
			}
		return index;
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
	public double[] test(int[][] inputs, int[][] outputs, boolean print) {
		double[] r = {0.0, 0.0};
		System.out.println("Iterations: " + iters);
		for (int i = 0; i < inputs.length; i++) {
			int x = eval(inputs[i]);
			int expected = maxIndex(outputs[i]);
			if (print) System.out.println("Expected: " + expected + " " + Arrays.toString(outputs[i]) +
										  " Result: " + x + " " + Arrays.toString(ao));
			for (int k = 0; k < outputSize; k++)
				r[1] += (outputs[i][k] - ao[k]) * (outputs[i][k] - ao[k]);
			if (expected == x) r[0] += 1.0/inputs.length;
			r[1] += (expected - x)*(expected - x)/(double)inputs.length;
		}
		r[1] *= 0.5;
		if (print) {
			System.out.println("Success rate:  " + r[0]*100 + "%");
			System.out.println("Squared Error: " + String.format("%.8f", r[1]));
			// ERROR = 0.5 * sum(norm(expected - output)**2)
		}
		return r;
	}
	
	/**
	 * Get the iterations that have been made to train the NN.
	 * @return	Number of iterations performed
	 */
	public int iters() {
		return iters;
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
		int[][] outputs = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
		NeuralNetwork nn = new NeuralNetwork(0.3, 2, 5, 4);
		nn.train(inputs, outputs, 10000);
		nn.test(inputs, outputs, true);
	}
	
	public String toString() {
		String s = "Weights I->H = " + Arrays.deepToString(weightsItoH);
		s += "\nWeights H->O = " + Arrays.toString(weightsHtoO);
		s += "\nLearning Rate = " + LEARNING_RATE;
		return s;
	}

}
