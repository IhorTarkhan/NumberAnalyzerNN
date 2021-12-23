package com.example.numberanalyzer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ImageReaderTest {
  public static final String TEST_TRAIN = "src/test/resources/image-reader/";
  public static final String TEST_TRAIN_CSV = TEST_TRAIN + "csv/";
  public static final String TEST_TRAIN_IMAGES = TEST_TRAIN + "images/";
  ImageReader imageReader = new ImageReader();

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  @SneakyThrows
  void getInputsTest() {
    double[][] expectation =
        Files.readAllLines(Paths.get(TEST_TRAIN_CSV + "inputs.csv"), StandardCharsets.UTF_8)
            .stream()
            .map(l -> Arrays.stream(l.split(",")).mapToDouble(Double::valueOf).toArray())
            .toArray(double[][]::new);
    double[][] real = imageReader.getInputs(TEST_TRAIN_IMAGES);
    assertArrayEquals(expectation, real);
  }

  @Test
  @SneakyThrows
  void getInputValues() {
    int[] expectation =
        Arrays.stream(
                Files.readString(Paths.get(TEST_TRAIN_CSV + "digits.csv"), StandardCharsets.UTF_8)
                    .trim()
                    .split(","))
            .mapToInt(Integer::valueOf)
            .toArray();
    int[] real = imageReader.getCorrectAnswers(TEST_TRAIN_IMAGES);
    assertArrayEquals(expectation, real);
  }
}
