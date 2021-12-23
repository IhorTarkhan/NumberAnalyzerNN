package com.example.numberanalyzer;

import java.util.function.UnaryOperator;

public class Executor {
  public static final double LEARNING_RATE = 0.001;
  public static final UnaryOperator<Double> SIGMOID = x -> 1 / (1 + Math.exp(-x));
  public static final UnaryOperator<Double> D_SIGMOID = y -> y * (1 - y);
  public static final String TRAIN_PATH = "src/main/resources/train";
  public static final int EPOCHS = 1000;
  public static final int BATCH_SIZE = 100;
  private final ImageReader imageReader = new ImageReader();

  public void start() {
    NeuralNetwork nn = new NeuralNetwork(LEARNING_RATE, SIGMOID, D_SIGMOID, 784, 512, 128, 32, 10);

    double[][] inputs = imageReader.getInputs(TRAIN_PATH);
    int[] digits = imageReader.getInputValues(TRAIN_PATH);

    for (int i = 1; i < EPOCHS; i++) {
      int right = 0;
      double errorSum = 0;
      for (int j = 0; j < BATCH_SIZE; j++) {
        int imgIndex = (int) (Math.random() * inputs.length);
        double[] targets = new double[10];
        int digit = digits[imgIndex];
        targets[digit] = 1;

        double[] outputs = nn.feedForward(inputs[imgIndex]);
        int maxDigit = 0;
        double maxDigitWeight = -1;
        for (int k = 0; k < 10; k++) {
          if (outputs[k] > maxDigitWeight) {
            maxDigitWeight = outputs[k];
            maxDigit = k;
          }
        }
        if (digit == maxDigit) {
          right++;
        }
        for (int k = 0; k < 10; k++) {
          errorSum += (targets[k] - outputs[k]) * (targets[k] - outputs[k]);
        }
        nn.backpropagation(targets);
      }
      System.out.println("epoch: " + i + ". correct: " + right + ". error: " + errorSum);
    }

    new Thread(new FormDigits(nn)).start();
  }
}
