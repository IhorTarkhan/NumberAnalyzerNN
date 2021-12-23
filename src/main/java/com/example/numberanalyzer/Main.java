package com.example.numberanalyzer;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class Main {
  public static final int EPOCHS = 1000;
  public static final int BATCH_SIZE = 100;
  public static final double LEARNING_RATE = 0.001;
  public static final UnaryOperator<Double> SIGMOID = x -> 1 / (1 + Math.exp(-x));

  public static void main(String[] args) {
    UnaryOperator<Double> dSigmoid = y -> y * (1 - y);
    NeuralNetwork nn = new NeuralNetwork(LEARNING_RATE, SIGMOID, dSigmoid, 784, 512, 128, 32, 10);

    String pathname = "train";
    double[][] inputs = getInputs(pathname);
    int[] digits = getInputValues(pathname);

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

    FormDigits f = new FormDigits(nn);
    new Thread(f).start();
  }

  @SneakyThrows
  private static double[][] getInputs(String pathname) {
    File[] imagesFiles = Objects.requireNonNull(new File(pathname).listFiles());
    double[][] inputs = new double[imagesFiles.length][784];
    for (int i = 0; i < imagesFiles.length; i++) {
      BufferedImage read = ImageIO.read(imagesFiles[i]);
      for (int x = 0; x < 28; x++) {
        for (int y = 0; y < 28; y++) {
          inputs[i][x + y * 28] = (read.getRGB(x, y) & 0xff) / 255.0;
        }
      }
    }
    return inputs;
  }

  private static int[] getInputValues(String pathname) {
    File[] imagesFiles = Objects.requireNonNull(new File(pathname).listFiles());
    int[] digits = new int[imagesFiles.length];
    for (int i = 0; i < imagesFiles.length; i++) {
      digits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
    }
    return digits;
  }
}
