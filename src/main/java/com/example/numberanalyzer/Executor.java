package com.example.numberanalyzer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
    int[] correctAnswers = imageReader.getCorrectAnswers(TRAIN_PATH);

    for (int i = 1; i < EPOCHS; i++) {
      int correctAnswersCount = 0;
      double errorSquareSum = 0;
      for (int j = 0; j < BATCH_SIZE; j++) {
        int randomImg = new Random().nextInt(inputs.length);

        double[] outputs = nn.feedForward(inputs[randomImg]);
        List<Double> outputsList = Arrays.stream(outputs).boxed().toList();
        int maxPredicted = outputsList.indexOf(Collections.max(outputsList));
        int maxCorrect = correctAnswers[randomImg];

        if (maxCorrect == maxPredicted) {
          correctAnswersCount++;
        }
        errorSquareSum += getSquareError(outputs, correctAnswers[randomImg]);

        double[] targets = new double[10];
        targets[correctAnswers[randomImg]] = 1;
        nn.backpropagation(inputs[randomImg], targets);
      }
      System.out.printf("epoch: %d. correct: %d. error: %s%n", i, correctAnswersCount, errorSquareSum);
    }
    new Thread(new FormDigits(nn)).start();
  }

  private double getSquareError(double[] outputs, int correctAnswer) {
    double error = 0;
    for (int i = 0; i < outputs.length; i++) {
      if (i == correctAnswer) {
        error += Math.pow(1 - outputs[i], 2);
      } else {
        error += Math.pow(outputs[i], 2);
      }
    }
    return error;
  }
}
