package com.example.numberanalyzer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

public class Main {
  public static final double LEARNING_RATE = 0.001;
  public static final UnaryOperator<Double> SIGMOID = x -> 1 / (1 + Math.exp(-x));
  public static final UnaryOperator<Double> D_SIGMOID = y -> y * (1 - y);
  public static final String TRAIN_PATH = "src/main/resources/train";
  public static final int EPOCHS = 1000;
  public static final int BATCH_SIZE = 100;
  private static final ImageReader imageReader = new ImageReader();

  public static void main(String[] args) {
    NeuralNetwork nn = new NeuralNetwork(LEARNING_RATE, SIGMOID, D_SIGMOID, 784, 512, 128, 32, 10);
    List<FeedData> feed = imageReader.getFeed(TRAIN_PATH);

    for (int i = 1; i < EPOCHS; i++) {
      int correctAnswersCount = 0;
      double errorSquareSum = 0;
      for (int j = 0; j < BATCH_SIZE; j++) {
        int randomIndex = new Random().nextInt(feed.size());
        FeedData randomFeed = feed.get(randomIndex);

        double[] outputs = nn.feedForward(randomFeed.input());
        correctAnswersCount += getCorrectIncrement(randomFeed.correctAnswers(), outputs);
        errorSquareSum += getSquareError(randomFeed.correctAnswers(), outputs);

        nn.backpropagation(randomFeed.input(), randomFeed.correctAnswers());
      }
      System.out.printf(
          "epoch: %d. correct: %d. error: %s%n", i, correctAnswersCount, errorSquareSum);
    }
    new Thread(new FormDigits(nn)).start();
  }

  private static int getCorrectIncrement(double[] correctAnswers, double[] outputs) {
    return getMaxIn(correctAnswers) == getMaxIn(outputs) ? 1 : 0;
  }

  private static int getMaxIn(double[] array) {
    List<Double> list = Arrays.stream(array).boxed().toList();
    return list.indexOf(Collections.max(list));
  }

  private static double getSquareError(double[] correctAnswers, double[] outputs) {
    double error = 0;
    for (int i = 0; i < outputs.length; i++) {
      error += Math.pow(correctAnswers[i] - outputs[i], 2);
    }
    return error;
  }
}
