package com.example.numberanalyzer;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class ImageReader {
  @SneakyThrows
  public double[][] getInputs(String pathname) {
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

  public int[] getInputValues(String pathname) {
    File[] imagesFiles = Objects.requireNonNull(new File(pathname).listFiles());
    int[] digits = new int[imagesFiles.length];
    for (int i = 0; i < imagesFiles.length; i++) {
      digits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
    }
    return digits;
  }
}
