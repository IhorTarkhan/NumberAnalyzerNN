package com.example.numberanalyzer;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageReader {
  @SneakyThrows
  public double[][] getInputs(String folderName) {
    File[] imagesFiles = new File(folderName).listFiles();
    if (imagesFiles == null) {
      throw new NullPointerException();
    }
    List<double[]> collect =
        Arrays.stream(imagesFiles)
            .map(
                input -> {
                  try {
                    BufferedImage read = ImageIO.read(input);
                    return Arrays.stream(
                            read.getRGB(
                                0, 0, read.getWidth(), read.getHeight(), null, 0, read.getWidth()))
                        .mapToDouble(rgb -> new Color(rgb).getBlue() / 255.0)
                        .toArray();
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                })
            .toList();

    return collect.toArray(new double[collect.size()][]);
  }

  public int[] getCorrectAnswers(String folderName) {
    File[] imagesFiles = Objects.requireNonNull(new File(folderName).listFiles());
    int[] digits = new int[imagesFiles.length];
    for (int i = 0; i < imagesFiles.length; i++) {
      digits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
    }
    return digits;
  }

  //  public List<FeedData> getFeed(String folderName) {}

  // ToDo combine response in one Generic DTO
}
