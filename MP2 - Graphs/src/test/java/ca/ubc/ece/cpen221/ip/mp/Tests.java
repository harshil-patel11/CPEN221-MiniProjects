package ca.ubc.ece.cpen221.ip.mp;

import ca.ubc.ece.cpen221.ip.core.Image;
import ca.ubc.ece.cpen221.ip.core.Rectangle;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;

public class Tests {

    @Test
    public void test_Red() {
        Image green = new Image("resources/custom-tests/green255.png");
        Image blackImage = new Image (900, 600);
        ImageTransformer t = new ImageTransformer(green);
        Image output = t.red();

        double cosineSimilarity = ImageProcessing.cosineSimilarity(blackImage, output);
        assertTrue(cosineSimilarity > 0.95);
    }

    @Test
    public void test_Mirroring2() {
        Image originalImg = new Image("resources/custom-tests/undo.png");
        Image expectedImg = new Image("resources/custom-tests/undo-mirror.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.mirror();
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_Mirroring3() {
        Image originalImg = new Image("resources/custom-tests/arrow-right.png");
        Image expectedImg = new Image("resources/custom-tests/arrow-right-mirror.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.mirror();
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_Negative2() {
        Image originalImg = new Image("resources/custom-tests/tree.png");
        Image expectedImg = new Image("resources/custom-tests/tree-negative.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.negative();
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_Negative3() {
        Image originalImg = new Image("resources/custom-tests/sunny-day.png");
        Image expectedImg = new Image("resources/custom-tests/sunny-day-negative.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.negative();
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_Posterize2() {
        Color[][] origMatrix = {{new Color(0, 90, 180), new Color(63, 79, 229)},
            {new Color(5, 250, 101), new Color(109, 20, 125)}};

        Color[][] expMatrix = {{new Color(32, 96, 222), new Color(32, 96, 222)},
            {new Color(32, 222, 96), new Color(96, 32, 96)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image posterized = t.posterize();

        Color[][] outputMatrix = getMatrixFromImage(posterized);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_Posterize3() {
        Color[][] origMatrix =
            {{new Color(60, 100, 200), new Color(50, 40, 70), new Color(200, 80, 150)},
                {new Color(180, 41, 65), new Color(0, 64, 128), new Color(129, 255, 67)},
                {new Color(80, 92, 135), new Color(69, 42, 0), new Color(12, 13, 59)}};

        Color[][] expMatrix =
            {{new Color(32, 96, 222), new Color(32, 32, 96), new Color(222, 96, 222)},
                {new Color(222, 32, 96), new Color(32, 32, 96), new Color(222, 222, 96)},
                {new Color(96, 96, 222), new Color(96, 32, 32), new Color(32, 32, 32)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image posterized = t.posterize();

        Color[][] outputMatrix = getMatrixFromImage(posterized);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_Clip2() {
        Image originalImg = new Image("resources/22090.jpg");
        Image expectedImg = new Image("resources/22090.jpg");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.clip(new Rectangle(0, 0, 480, 320));
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_Weathering2() {
        Color[][] origMatrix = {{new Color(100, 50, 150), new Color(50, 100, 70)}};
        Color[][] expMatrix = {{new Color(50, 50, 70), new Color(50, 50, 70)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image weathered = t.weather();

        Color[][] outputMatrix = getMatrixFromImage(weathered);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_Weathering3() {
        Color[][] origMatrix = {{new Color(100, 60, 0), new Color(50, 80, 0)},
            {new Color(80, 10, 0), new Color(40, 70, 0)},
            {new Color(20, 60, 0), new Color(60, 50, 0)}};
        Color[][] expMatrix = {{new Color(40, 10, 0), new Color(40, 10, 0)},
            {new Color(20, 10, 0), new Color(20, 10, 0)},
            {new Color(20, 10, 0), new Color(20, 10, 0)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image weathered = t.weather();

        Color[][] outputMatrix = getMatrixFromImage(weathered);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_Denoise() {
        Color[][] origMatrix = {{new Color(100, 50, 150), new Color(50, 100, 70)}};
        Color[][] expMatrix = {{new Color(75, 75, 110), new Color(62, 87, 90)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image denoised = t.denoise();

        Color[][] outputMatrix = getMatrixFromImage(denoised);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_Denoise2() {
        Color[][] origMatrix = {{new Color(100, 0, 0), new Color(50, 0, 0)},
            {new Color(80, 0, 0), new Color(0, 0, 0)}};
        Color[][] expMatrix = {{new Color(65, 0, 0), new Color(57, 0, 0)},
            {new Color(61, 0, 0), new Color(59, 0, 0)}};

        Image image = getImageFromMatrix(origMatrix);
        ImageTransformer t = new ImageTransformer(image);
        Image denoised = t.denoise();

        Color[][] outputMatrix = getMatrixFromImage(denoised);
        assertArrayEquals(outputMatrix, expMatrix);
    }

    @Test
    public void test_BlockPaint1() {
        Image originalImg = new Image("resources/216053.jpg");
        Image expectedImg = new Image("resources/tests/216053-seurat-3x3.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.blockPaint(3);
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_BlockPaint2() {
        Image originalImg = new Image("resources/95006.jpg");
        Image expectedImg = new Image("resources/tests/95006-seurat-4x4.png");
        ImageTransformer t = new ImageTransformer(originalImg);
        Image outputImage = t.blockPaint(4);
        assertEquals(expectedImg, outputImage);
    }

    @Test
    public void test_CosineSimilarity1() {
        Image image1 = new Image("resources/16052.jpg");
        Image image2 = new Image("resources/16052.jpg");
        double cosineSimilarity = ImageProcessing.cosineSimilarity(image1, image2);
        assertTrue(cosineSimilarity > 0.99);
    }

    @Test
    public void test_CosineSimilarity2() {
        Image image1 = new Image("resources/custom-tests/blackblue.jpg");
        Image image2 = new Image("resources/20008.jpg");
        double cosineSimilarity = ImageProcessing.cosineSimilarity(image1, image2);
        assertTrue(cosineSimilarity < 0.05);
    }

    @Test
    public void test_CosineSimilarity3() {
        Image blackImage = new Image(321, 481);
        Image image2 = new Image("resources/26031.jpg");
        double cosineSimilarity = ImageProcessing.cosineSimilarity(blackImage, image2);
        assertTrue(cosineSimilarity < 0.02);
    }

    @Test
    public void test_CosineSimilarity4() {
        Image redImage = new Image("resources/custom-tests/red255.png");
        Image greenImage = new Image("resources/custom-tests/green255.png");
        double cosineSimilarity = ImageProcessing.cosineSimilarity(redImage, greenImage);
        assertTrue(cosineSimilarity > 0.99);
    }

    @Test
    public void test_Rotate1() {
        Image originalImg = new Image("resources/12003.jpg");
        Image expectedImg = new Image("resources/tests/12003-r30.png");
        ImageTransformer orig = new ImageTransformer(originalImg);
        ImageTransformer exp = new ImageTransformer(expectedImg);
        Image origOutputImage = orig.rotate(30);
        Image expOutputImage = exp.clip(
            new Rectangle(0, 0, origOutputImage.width() - 1, origOutputImage.height() - 1));

        double cosineSimilarity = ImageProcessing.cosineSimilarity(expOutputImage, origOutputImage);
        assertTrue(cosineSimilarity > 0.98);
    }

    @Test
    public void test_Rotate2() {
        Image originalImg = new Image("resources/12003.jpg");
        Image expectedImg = new Image("resources/tests/12003-r45.png");
        ImageTransformer orig = new ImageTransformer(originalImg);
        ImageTransformer exp = new ImageTransformer(expectedImg);
        Image origOutputImage = orig.rotate(45);
        Image expOutputImage = exp.clip(
            new Rectangle(0, 0, origOutputImage.width() - 1, origOutputImage.height() - 1));

        double cosineSimilarity = ImageProcessing.cosineSimilarity(expOutputImage, origOutputImage);
        assertTrue(cosineSimilarity > 0.98);
    }

    @Test
    public void test_Rotate3() {
        Image originalImg = new Image("resources/12003.jpg");
        Image expectedImg = new Image("resources/tests/12003-r75.png");
        ImageTransformer orig = new ImageTransformer(originalImg);
        ImageTransformer exp = new ImageTransformer(expectedImg);
        Image origOutputImage = orig.rotate(75);
        Image expOutputImage = exp.clip(
            new Rectangle(0, 0, origOutputImage.width() - 1, origOutputImage.height() - 1));

        double cosineSimilarity = ImageProcessing.cosineSimilarity(expOutputImage, origOutputImage);
        assertTrue(cosineSimilarity > 0.98);
    }

    @Test
    public void test_Rotate4() {
        Image originalImg = new Image("resources/12003.jpg");
        Image expectedImg = new Image("resources/tests/12003-r180.png");
        ImageTransformer orig = new ImageTransformer(originalImg);
        ImageTransformer exp = new ImageTransformer(expectedImg);
        Image origOutputImage = orig.rotate(180);
        Image expOutputImage = exp.clip(
            new Rectangle(0, 0, origOutputImage.width() - 1, origOutputImage.height() - 1));

        double cosineSimilarity = ImageProcessing.cosineSimilarity(expOutputImage, origOutputImage);
        assertTrue(cosineSimilarity > 0.98);
    }

    @Test
    public void testDFT1() {
        int[][] intensities = {{100}};
        Image intensityImage = getImageFromIntensities(intensities);
        ImageTransformer t = new ImageTransformer(intensityImage);
        DFTOutput actual = t.dft();

        double[][] expectedAmplitudes = {{100.0}};
        double[][] expectedPhaseAngles = {{0.0}};
        DFTOutput expected = new DFTOutput(expectedAmplitudes, expectedPhaseAngles);

        assertEquals(actual, expected);
    }

    @Test
    public void testDFT2() {
        int[][] intensities = {{200, 50}};
        Image intensityImage = getImageFromIntensities(intensities);
        ImageTransformer t = new ImageTransformer(intensityImage);
        DFTOutput actual = t.dft();

        double[][] expectedAmplitudes = {{250.0, 150.0}};
        double[][] expectedPhaseAngles = {{0.0, 0.0}};
        DFTOutput expected = new DFTOutput(expectedAmplitudes, expectedPhaseAngles);

        assertEquals(actual, expected);
    }

    @Test
    public void testDFT3() {
        int[][] intensities = {{13, 28, 199, 43}};
        Image intensityImage = getImageFromIntensities(intensities);
        ImageTransformer t = new ImageTransformer(intensityImage);
        DFTOutput actual = t.dft();

        double[][] expectedAmplitudes =
            {{283.0, Math.sqrt(186.0 * 186.0 + 225.0), 141.0, Math.sqrt(186.0 * 186.0 + 225.0)}};
        double[][] expectedPhaseAngles = {{Math.atan2(0.0, 283.0), Math.atan2(15.0, -186.0),
            Math.atan2(0.0, 141.0), Math.atan2(-15.0, -186.0)}};
        DFTOutput expected = new DFTOutput(expectedAmplitudes, expectedPhaseAngles);

        assertEquals(actual, expected);
    }

    @Test
    public void testDFT4() {
        int[][] intensities = {{0, 100}, {200, 50}};
        Image intensityImage = getImageFromIntensities(intensities);
        ImageTransformer t = new ImageTransformer(intensityImage);
        DFTOutput actual = t.dft();

        double[][] expectedAmplitudes = {{350.0, 50.0}, {150.0, 250.0}};
        double[][] expectedPhaseAngles = {{0.0, 0.0}, {-Math.PI, -Math.PI}};
        DFTOutput expected = new DFTOutput(expectedAmplitudes, expectedPhaseAngles);

        assertEquals(actual, expected);
    }

    @Test
    public void testDFT5() {
        int[][] intensities = {{0, 200}, {100, 50}};
        Image intensityImage = getImageFromIntensities(intensities);
        ImageTransformer t = new ImageTransformer(intensityImage);
        DFTOutput actual = t.dft();

        double[][] expectedAmplitudes = {{350.0, 150.0}, {50.0, 250.0}};
        double[][] expectedPhaseAngles = {{0.0, -Math.PI}, {0.0, -Math.PI}};
        DFTOutput expected = new DFTOutput(expectedAmplitudes, expectedPhaseAngles);

        assertEquals(actual, expected);
    }

    @Test
    public void test_GreenCherry() {
        Image greenCherry = new Image("resources/custom-tests/chery-green.png");
        Image blackCherry = new Image("resources/custom-tests/chery-black.png");
        ImageTransformer transform = new ImageTransformer(greenCherry);
        Image blackImage = new Image(50, 50);
        Color green = new Color(14, 209, 69);

        Image output = transform.greenScreen(green, blackImage);
        double cosineSimilarity = ImageProcessing.cosineSimilarity(output, blackCherry);
        assertTrue(cosineSimilarity > 0.97);
    }

    @Test
    public void test_GreenMountain() {
        Image greenMountain = new Image("resources/custom-tests/mountain-green.png");
        Image blueMountain = new Image("resources/custom-tests/mountain-green-blue.png");
        ImageTransformer transform = new ImageTransformer(greenMountain);
        Image blueImage = new Image("resources/custom-tests/blue.png");
        Color green = new Color(14, 209, 69);

        Image output = transform.greenScreen(green, blueImage);
        double cosineSimilarity = ImageProcessing.cosineSimilarity(output, blueMountain);
        assertTrue(cosineSimilarity > 0.97);
    }

    @Test
    public void test_GreenScreenTiling() {
        Image fullyGreen = new Image("resources/custom-tests/fully-green.png");
        Image tiledFullyGreen = new Image("resources/custom-tests/tiled-fully-green.png");
        Image cctv = new Image("resources/custom-tests/cctv.png");
        ImageTransformer transform = new ImageTransformer(fullyGreen);
        Color green = new Color(14, 209, 69);

        Image output = transform.greenScreen(green, cctv);
        double cosineSimilarity = ImageProcessing.cosineSimilarity(output, tiledFullyGreen);
        assertTrue(cosineSimilarity > 0.97);
    }

    @Test
    public void test_TextAlignImage1() {
        Image image = new Image("resources/custom-tests/TestTextImage1.png");
        Image expectedImage = new Image("resources/custom-tests/TestTextImage1.png");

        ImageTransformer transform = new ImageTransformer(image);
        Image output = transform.alignTextImage();

        transform = new ImageTransformer(expectedImage);

        ImageTransformer transformClip = new ImageTransformer(output);
        Rectangle middleRectangle = new Rectangle(expectedImage.width() / 4,
            expectedImage.height() / 4, expectedImage.width() * 3 / 4,
            expectedImage.height() * 3 / 4);

        Image clippedActualImage = transformClip.clip(middleRectangle);
        Image clippedExpectedImage = transform.clip(middleRectangle);

        double cosineSimilarity =
            ImageProcessing.cosineSimilarity(clippedActualImage, clippedExpectedImage);

        assertTrue(cosineSimilarity > 0.95);
    }

    @Test
    public void test_TextAlignImage2() {
        Image image = new Image("resources/custom-tests/TestTextImage3-(-15deg).png");
        Image expectedImage = new Image("resources/custom-tests/TestTextImage3.png");

        ImageTransformer transform = new ImageTransformer(image);
        Image output = transform.alignTextImage();

        transform = new ImageTransformer(expectedImage);

        ImageTransformer transformClip = new ImageTransformer(output);
        Rectangle middleRectangle = new Rectangle(expectedImage.width() / 4,
            expectedImage.height() / 4, expectedImage.width() * 3 / 4,
            expectedImage.height() * 3 / 4);

        Image clippedActualImage = transformClip.clip(middleRectangle);
        Image clippedExpectedImage = transform.clip(middleRectangle);

        double cosineSimilarity =
            ImageProcessing.cosineSimilarity(clippedActualImage, clippedExpectedImage);

        assertTrue(cosineSimilarity > 0.95);
    }

    @Test
    public void test_TextAlignImage3() {
        Image image = new Image("resources/custom-tests/TestTextImage3-25deg.png");
        Image expectedImage = new Image("resources/custom-tests/TestTextImage3.png");

        ImageTransformer transform = new ImageTransformer(image);
        Image output = transform.alignTextImage();

        transform = new ImageTransformer(expectedImage);

        ImageTransformer transformClip = new ImageTransformer(output);
        Rectangle middleRectangle = new Rectangle(expectedImage.width() / 4,
            expectedImage.height() / 4, expectedImage.width() * 3 / 4,
            expectedImage.height() * 3 / 4);

        Image clippedActualImage = transformClip.clip(middleRectangle);
        Image clippedExpectedImage = transform.clip(middleRectangle);

        double cosineSimilarity =
            ImageProcessing.cosineSimilarity(clippedActualImage, clippedExpectedImage);

        assertTrue(cosineSimilarity > 0.95);
    }


    @Test
    public void test_TextAlignImage4() {
        Image image = new Image("resources/custom-tests/TestTextImage3-(-70deg).png");
        Image expectedImage = new Image("resources/custom-tests/TestTextImage3.png");

        ImageTransformer transform = new ImageTransformer(image);
        Image output = transform.alignTextImage();

        transform = new ImageTransformer(expectedImage);

        ImageTransformer transformClip = new ImageTransformer(output);
        Rectangle middleRectangle = new Rectangle(expectedImage.width() / 4,
            expectedImage.height() / 4, expectedImage.width() * 3 / 4,
            expectedImage.height() * 3 / 4);

        Image clippedActualImage = transformClip.clip(middleRectangle);
        Image clippedExpectedImage = transform.clip(middleRectangle);

        double cosineSimilarity =
            ImageProcessing.cosineSimilarity(clippedActualImage, clippedExpectedImage);

        assertTrue(cosineSimilarity > 0.95);
    }
    
    @Test
    public void test_TextAlignImage5() {
        Image image = new Image("resources/custom-tests/TestTextImage3-80deg.png");
        Image expectedImage = new Image("resources/custom-tests/TestTextImage3.png");

        ImageTransformer transform = new ImageTransformer(image);
        Image output = transform.alignTextImage();

        transform = new ImageTransformer(expectedImage);

        ImageTransformer transformClip = new ImageTransformer(output);
        Rectangle middleRectangle = new Rectangle(expectedImage.width() / 4,
            expectedImage.height() / 4, expectedImage.width() * 3 / 4,
            expectedImage.height() * 3 / 4);

        Image clippedActualImage = transformClip.clip(middleRectangle);
        Image clippedExpectedImage = transform.clip(middleRectangle);

        double cosineSimilarity =
            ImageProcessing.cosineSimilarity(clippedActualImage, clippedExpectedImage);

        assertTrue(cosineSimilarity > 0.95);
    }

    /**
     * A helper method that converts a matrix of RGB values to an image.
     *
     * @param matrix - the Color matrix containing RGB values as each element.
     * @return - the image output of the matrix.
     */
    private Image getImageFromMatrix(Color[][] matrix) {
        int width = matrix[0].length;
        int height = matrix.length;

        Image newImage = new Image(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                newImage.set(col, row, matrix[row][col]);
            }
        }

        return newImage;
    }

    /**
     * A helper method that converts an image to a matrix of RGB values.
     *
     * @param image - the image to be converted to a matrix.
     * @return - the Color matrix output containing RGB values as each element.
     */
    private Color[][] getMatrixFromImage(Image image) {
        Color[][] colorMatrix = new Color[image.height()][image.width()];

        for (int row = 0; row < image.height(); row++) {
            for (int col = 0; col < image.width(); col++) {
                colorMatrix[row][col] = image.get(col, row);
            }
        }

        return colorMatrix;
    }

    /**
     * Creates a gray image out of a matrix of intensities
     *
     * @param intensities the matrix containing the intensity of each of the pixels
     * @return an Image with the same dimensions as the matrix, where each pixel has an intensity
     * corresponding to the intensity of the element in intensities with the same position
     * <p>
     * requires that the values in intensity are between 0-255
     */
    private Image getImageFromIntensities(int[][] intensities) {
        Image intensityImage = new Image(intensities[0].length, intensities.length);
        Color pixelColor;
        int intensity;

        for (int i = 0; i < intensityImage.height(); i++) {
            for (int j = 0; j < intensityImage.width(); j++) {
                intensity = intensities[i][j];
                pixelColor = new Color(intensity, intensity, intensity);

                intensityImage.set(j, i, pixelColor);
            }
        }

        return intensityImage;
    }

}
