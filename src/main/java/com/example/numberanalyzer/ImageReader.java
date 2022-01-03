package com.example.numberanalyzer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageReader {
  public List<FeedData> getFeed(String folderName) {
    File[] imagesFiles = new File(folderName).listFiles();
    if (imagesFiles == null) {
      throw new NullPointerException();
    }
    return Arrays.stream(imagesFiles)
        .map(
            input -> {
              try {
                BufferedImage read = ImageIO.read(input);
                double[] feedData =
                    Arrays.stream(
                            read.getRGB(
                                0, 0, read.getWidth(), read.getHeight(), null, 0, read.getWidth()))
                        .mapToDouble(rgb -> new Color(rgb).getBlue() / 255.0)
                        .toArray();
                double[] digits = new double[10];
                digits[Integer.parseInt(input.getName().charAt(10) + "")] = 1;
                return new FeedData(feedData, digits);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
        .toList();
  }
}
