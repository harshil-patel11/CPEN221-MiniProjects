package ca.ubc.ece.cpen221.ip.mp;

import ca.ubc.ece.cpen221.ip.core.Image;
import ca.ubc.ece.cpen221.ip.core.ImageProcessingException;
import ca.ubc.ece.cpen221.ip.core.Rectangle;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This datatype (or class) provides operations for transforming an image.
 *
 * <p>The operations supported are:
 * <ul>
 *     <li>The {@code ImageTransformer} constructor generates an instance of an image that
 *     we would like to transform;</li>
 *     <li></li>
 * </ul>
 * </p>
 */

public class ImageTransformer {

    private final Image image;
    private final int width;
    private final int height;

    /**
     * Creates an ImageTransformer with an image. The provided image is
     * <strong>never</strong> changed by any of the operations.
     *
     * @param img is not null
     */
    public ImageTransformer(Image img) {
        image = img;
        height = img.height();
        width = img.width();
    }

    /**
     * Computes the value of the DFT a specified pixel in an image
     *
     * @param col   the column of the pixel in the image
     * @param row   the row of the pixel in the image
     * @param image the image to examine in the computation of the DFT
     * @return an array containing [a, b] where a is the real component and b is the imaginary
     * component of the DFT value of the pixel
     */
    private static double[] fourierOfPixel(int col, int row, Image image) {
        double[] fourierComponents;
        double sumOfReal = 0.0;
        double sumOfImaginary = 0.0;

        for (int i = 0; i < image.width(); i++) {
            for (int j = 0; j < image.height(); j++) {
                fourierComponents = computeFourierComponents(col, row, i, j, image.width(),
                    image.height(), image.get(i, j).getRed());

                sumOfReal += fourierComponents[0];
                sumOfImaginary += fourierComponents[1];
            }
        }

        double[] sums = {sumOfReal, sumOfImaginary};
        return sums;
    }

    /**
     * Computes the contribution of a pixel (the secondary pixel) in an image to the DFT of another
     * main pixel in the image
     *
     * @param mainCol      the column of the main pixel
     * @param mainRow      the row of the main pixel
     * @param secondaryCol the column of the secondary pixel
     * @param secondaryRow the row of the secondary pixel
     * @param imageWidth   the width of the image of which the DFT is being calculated
     * @param imageHeight  the height of the image of which the DFT is being calculated
     * @param intensity    the intensity of the main pixel
     * @return an array containing [a, b] where a is the real component and b is the imaginary
     * component of the secondary pixel's contribution to the DFT of the main pixel
     */
    private static double[] computeFourierComponents(int mainCol, int mainRow, int secondaryCol,
                                                     int secondaryRow, int imageWidth,
                                                     int imageHeight, int intensity) {
        double[] fourierComponents = new double[2];

        double theta = 2 * Math.PI * ((double) mainCol * secondaryCol / imageWidth +
            (double) mainRow * secondaryRow / imageHeight);

        fourierComponents[0] = intensity * Math.cos(theta);
        fourierComponents[1] = intensity * -Math.sin(theta);

        return fourierComponents;
    }

    /**
     * Determines the slope of a line determined by y-values gleaned from columns in a matrix/image
     *
     * @param points the y-values corresponding to each column of a matrix/image
     * @return the slope of the line containing the values in points
     */
    private static double slopeOfBestFitLineFromColumns(int[] points) {
        double numerator = getAverage(Arrays.copyOfRange(points, points.length / 2,
            points.length - 1)) - getAverage(Arrays.copyOf(points, points.length / 2));
        double denominator = (points.length - 1) / 2.0;

        return -numerator / denominator;
    }

    /**
     * Determines the slope of a line determined by x-values gleaned from rows in a matrix/image
     *
     * @param points the x-values corresponding to each row of a matrix/image
     * @return the slope of the line containing the values in points
     */
    private static double slopeOfBestFitLineFromRows(int[] points) {
        double numerator = (points.length - 1) / 2.0;
        double denominator = getAverage(Arrays.copyOfRange(points, points.length / 2,
            points.length - 1)) - getAverage(Arrays.copyOf(points, points.length / 2));

        return -numerator / denominator;
    }

    /**
     * Creates an array containing the x-values with the highest average of it, and its neighbours
     * in the same row.
     *
     * @param matrix        the matrix to examine the rows of
     * @param rowsToExamine the number of rows to examine
     * @return an array, each index of which corresponds to a row in matrix and contains the value
     * of the index in that matrix, of that row, around which the average value is the highest. The
     * last value of the array contains a value representing how high the average values of the
     * elements is.
     */
    private static int[] rowValues(double[][] matrix, int rowsToExamine) {
        double maxAverageValue = 0.0;
        double currentAverage;
        double averageMaxAverage = 0.0;
        int seqSize = 5;
        double[] currentSequence = new double[seqSize];
        int[] maxAverageIndices = new int[rowsToExamine + 1];

        for (int i = 0; i < rowsToExamine; i++) {
            for (int j = seqSize / 2; j < matrix[0].length - seqSize / 2; j++) {
                for (int k = -seqSize / 2; k <= seqSize / 2; k++) {
                    currentSequence[0] = Math.log(1 + matrix[i][j + k]);
                }

                currentAverage = getAverage(currentSequence);

                if (currentAverage > maxAverageValue) {
                    maxAverageIndices[i] = j;
                    maxAverageValue = currentAverage;
                }
            }

            averageMaxAverage += maxAverageValue;
            maxAverageValue = 0.0;
        }

        maxAverageIndices[rowsToExamine] = (int) averageMaxAverage;

        return maxAverageIndices;
    }

    /**
     * Creates an array containing the y-values with the highest average of it, and its neighbours
     * in the same column.
     *
     * @param matrix           the matrix to examine the columns of
     * @param columnsToExamine the number of columns to examine
     * @return an array, each index of which corresponds to a column in matrix and contains the value
     * of the index in that matrix, of that column, around which the average value is the highest.
     * The last value of the array contains a value representing how high the average values of the
     * elements is.
     */
    private static int[] columnValues(double[][] matrix, int columnsToExamine) {
        double maxAverageValue = 0.0;
        double currentAverage;
        int seqSize = 5;
        double[] currentSequence = new double[seqSize];
        int[] maxAverageIndices = new int[columnsToExamine + 1];
        double averageMaxAverage = 0.0;

        for (int i = 0; i < columnsToExamine; i++) {
            for (int j = seqSize / 2; j < matrix.length - seqSize / 2; j++) {
                for (int k = -seqSize / 2; k <= seqSize / 2; k++) {
                    currentSequence[0] = Math.log(1 + matrix[j + k][i]);
                }

                currentAverage = getAverage(currentSequence);

                if (currentAverage > maxAverageValue) {
                    maxAverageIndices[i] = j;
                    maxAverageValue = currentAverage;
                }
            }

            averageMaxAverage += maxAverageValue;
            maxAverageValue = 0.0;
        }

        maxAverageIndices[columnsToExamine] = (int) averageMaxAverage;

        return maxAverageIndices;
    }

    /**
     * Finds the average value of the elements in an array
     *
     * @param array the array to examine
     * @return the average value of the array
     */
    private static double getAverage(double[] array) {
        double sum = 0.0;

        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }

        return sum / array.length;
    }

    /**
     * Finds the average value of the elements in an array
     *
     * @param array the array to examine
     * @return the average value of the array
     */
    private static double getAverage(int[] array) {
        double sum = 0.0;

        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }

        return sum / array.length;
    }

    public static void main(String[] args) {

    }

    /**
     * Obtain the grayscale version of the image.
     *
     * @return the grayscale version of the instance.
     */
    public Image grayscale() {
        Image gsImage = new Image(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Color color = image.get(col, row);
                Color gray = Image.toGray(color);
                gsImage.set(col, row, gray);
            }
        }
        return gsImage;
    }

    /**
     * Obtain a version of the image with only the red colours.
     *
     * @return a reds-only version of the instance.
     */
    public Image red() {
        Image redImage = new Image(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                int originalPixel = image.getRGB(col, row);
                int alpha = (originalPixel >> 24) & 0xFF;
                int red = (originalPixel >> 16) & 0xFF;
                int desiredColor = (alpha << 24) | (red << 16) | (0 << 8) | (0);
                redImage.setRGB(col, row, desiredColor);
            }
        }
        return redImage;
    }

    /**
     * Returns the mirror image of an instance.
     * This method flips an image horizontally.
     *
     * @return the mirror image of the instance.
     */
    public Image mirror() {
        Image mirrorImage = new Image(image);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = image.get(width - j - 1, i);
                mirrorImage.set(j, i, color);
            }
        }
        return mirrorImage;
    }

    /**
     * <p>Returns the negative version of an instance.<br />
     * If the colour of a pixel is (r, g, b) then the colours of the same pixel
     * in the negative of the image are (255-r, 255-g, 255-b).</p>
     *
     * @return the negative of the instance.
     */
    public Image negative() {
        Image negativeImage = new Image(image);
        int[] pixelRGB = new int[3];

        for (int i = 0; i < negativeImage.height(); i++) {
            for (int j = 0; j < negativeImage.width(); j++) {
                pixelRGB[0] = 255 - negativeImage.get(j, i).getRed();
                pixelRGB[1] = 255 - negativeImage.get(j, i).getGreen();
                pixelRGB[2] = 255 - negativeImage.get(j, i).getBlue();

                negativeImage.set(j, i, new Color(pixelRGB[0], pixelRGB[1], pixelRGB[2]));
            }
        }

        return negativeImage;
    }

    /**
     * <p>Returns the posterized version of an instance.<br />
     * For each pixel, each colour is analyzed independently to produce a new image as follows:
     * <ul>
     * <li>if the value of the colour is between 0 and 64 (limits inclusive), set it to 32;</li>
     * <li>if the value of the colour is between 65 and 128, set it to 96;</li>
     * <li>if the value of the colour is between 129 and 255, set it to 222.</li>
     * </ul>
     * </p>
     *
     * @return the posterized version of the instance.
     */
    public Image posterize() {
        Image posterizeImage = new Image(width, height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = image.get(j, i);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red = posterizeColor(red);
                green = posterizeColor(green);
                blue = posterizeColor(blue);

                Color modifiedColor = new Color(red, green, blue);
                posterizeImage.set(j, i, modifiedColor);
            }
        }

        return posterizeImage;
    }

    /**
     * Analyzes each color independently and returns a new color based on given boundaries
     *
     * @param color numerical value of one of RGB colors. Must be in the range 0 <= color <= 255.
     * @return returns 32, if color is between 0 and 64 (inclusive),
     * returns 96 if it is between 64 and 128 (inclusive), and
     * returns 222 if it is between 128 and 255 (inclusive).
     */
    private int posterizeColor(int color) {
        if (color <= 64) {
            return 32;
        } else if (color <= 128) {
            return 96;
        } else if (color <= 255) {
            return 222;
        }
        return 0;
    }

    /**
     * Clip the image given a rectangle that represents the region to be retained.
     *
     * @param clippingBox is not null.
     * @return a clipped version of the instance.
     * @throws ImageProcessingException if the clippingBox does not fit completely
     *                                  within the image.
     */
    public Image clip(Rectangle clippingBox) {
        int width = clippingBox.xBottomRight - clippingBox.xTopLeft;
        int height = clippingBox.yBottomRight - clippingBox.yTopLeft;

        Image clipImage = new Image(width + 1, height + 1);

        for (int i = clippingBox.yTopLeft; i <= height + clippingBox.yTopLeft; i++) {
            for (int j = clippingBox.xTopLeft; j <= width + clippingBox.xTopLeft; j++) {
                Color color = image.get(j, i);
                clipImage.set(j - clippingBox.xTopLeft, i - clippingBox.yTopLeft, color);
            }
        }
        return clipImage;
    }

    /**
     * Denoise an image by replacing each pixel by the median value of that pixel and
     * all its neighbouring pixels. During this process, each colour channel is handled
     * separately.
     *
     * @return a denoised version of the instance.
     */
    public Image denoise() {
        Image denoiseImage = new Image(image);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                HashMap<String, ArrayList<Integer>> rgb =
                    getNeighbouringPixels(col - 1, row - 1, col + 1, row + 1, denoiseImage);

                int size = rgb.get("red").size();
                int redMedian, greenMedian, blueMedian;

                if (size % 2 == 0) {
                    redMedian =
                        (rgb.get("red").get(size / 2 - 1) + rgb.get("red").get(size / 2)) / 2;
                    greenMedian =
                        (rgb.get("green").get(size / 2 - 1) + rgb.get("green").get(size / 2)) / 2;
                    blueMedian =
                        (rgb.get("blue").get(size / 2 - 1) + rgb.get("blue").get(size / 2)) / 2;
                } else {
                    redMedian = rgb.get("red").get(size / 2);
                    greenMedian = rgb.get("green").get(size / 2);
                    blueMedian = rgb.get("blue").get(size / 2);
                }

                Color medianColor = new Color(redMedian, greenMedian, blueMedian);
                denoiseImage.set(col, row, medianColor);
            }
        }
        return denoiseImage;
    }

    /**
     * @param minCol the lowest column index's pixel to count within the average
     * @param minRow the lowest row index's pixel to count within the average
     * @param maxCol the highest column index's pixel to count within the average
     * @param maxRow the highest row index's pixel to count within the average
     * @param image  an object of type Image
     * @return HashMap with String Keys: red, green, blue. Values: ArrayList of Integers
     * corresponding to pixel color values in ascending order
     */
    private HashMap<String, ArrayList<Integer>> getNeighbouringPixels(int minCol, int minRow,
                                                                      int maxCol, int maxRow,
                                                                      Image image) {

        HashMap<String, ArrayList<Integer>> rgb = new HashMap<>();
        ArrayList<Integer> red = new ArrayList<>();
        ArrayList<Integer> green = new ArrayList<>();
        ArrayList<Integer> blue = new ArrayList<>();

        for (int i = minRow; i <= maxRow; i++) {
            if (i >= 0 && i < height) {
                for (int j = minCol; j <= maxCol; j++) {
                    if (j >= 0 && j < width) {
                        Color color = image.get(j, i);
                        red.add(color.getRed());
                        green.add(color.getGreen());
                        blue.add(color.getBlue());
                    }
                }
            }
        }
        Collections.sort(red);
        Collections.sort(green);
        Collections.sort(blue);
        rgb.put("red", red);
        rgb.put("green", green);
        rgb.put("blue", blue);

        return rgb;
    }

    /**
     * Returns a weathered version of the image by replacing each pixel by the minimum value
     * of that pixel and all its neighbouring pixels. During this process, each colour channel
     * is handled separately.
     *
     * @return a weathered version of the image.
     */
    public Image weather() {
        Image weatherImage = new Image(image);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                HashMap<String, ArrayList<Integer>> rgb = getNeighbouringPixels(col - 1, row - 1,
                    col + 1, row + 1, image);

                int redMin = rgb.get("red").get(0);
                int greenMin = rgb.get("green").get(0);
                int blueMin = rgb.get("blue").get(0);

                Color minColor = new Color(redMin, greenMin, blueMin);
                weatherImage.set(col, row, minColor);
            }

        }
        return weatherImage;
    }

    /**
     * Return a block paint version of the instance by treating the image as a
     * sequence of squares of a given size and replacing all pixels in a square
     * by the average value of all pixels in that square.
     * During this process, each colour channel is handled separately.
     *
     * @param blockSize the dimension of the square block, > 1.
     * @return the block paint version of the instance.
     * When the original image is not a perfect multiple of blockSize * blockSize,
     * the bottom rows and right columns are obtained by averaging the pixels that
     * fit the smaller rectangular regions. For example, if we have a 642 x 642 size
     * original image and the block size is 4 x 4 then the bottom two rows will use
     * 2 x 4 blocks, the rightmost two columns will use 4 x 2 blocks, and the
     * bottom-right corner will use a 2 x 2 block.
     */
    public Image blockPaint(int blockSize) {
        Image blockPaintImage = new Image(image);

        int bottomCornerWidth = width % blockSize;
        int bottomCornerHeight = height % blockSize;

        for (int i = 0; i < height; i += blockSize) {
            for (int j = 0; j <= width - blockSize; j += blockSize) {
                if (i + blockSize <= height) {
                    setRegionColorsToAverage(i, j, blockSize, blockSize, blockPaintImage);
                }

                if (i + blockSize >= height && j + blockSize < width && bottomCornerHeight > 0) {
                    setRegionColorsToAverage(height - bottomCornerHeight, j, blockSize,
                        bottomCornerHeight, blockPaintImage);
                }
            }

            if (bottomCornerWidth > 0 && i + blockSize <= height) {
                setRegionColorsToAverage(i, width - bottomCornerWidth, bottomCornerWidth,
                    blockSize, blockPaintImage);
            }
        }

        if (bottomCornerHeight * bottomCornerWidth > 0) {
            setRegionColorsToAverage(height - bottomCornerHeight, width - bottomCornerWidth,
                bottomCornerWidth, bottomCornerHeight, blockPaintImage);
        }

        return blockPaintImage;
    }

    /**
     * Sets the color of each pixel in a rectangular region of an image to the average of the
     * colors. The average of thecolors is the color with r,g,b where r is the average of all red
     * values, g is the average of all green values, and b is the average of all blue values
     *
     * @param startRow Row of the top corner of the region
     * @param startCol Column of the top corner of the region
     * @param width    Width of the region
     * @param height   Height of the region
     * @param image    Image to set the region color to
     */
    private void setRegionColorsToAverage(int startRow, int startCol, int width, int height,
                                          Image image) {
        HashMap<String, ArrayList<Integer>> region;
        Color regionColor;

        int averageRed;
        int averageGreen;
        int averageBlue;

        region =
            getNeighbouringPixels(startCol, startRow, startCol + width - 1, startRow + height - 1,
                image);

        averageRed = getAverage(region.get("red"));
        averageGreen = getAverage(region.get("green"));
        averageBlue = getAverage(region.get("blue"));

        regionColor = new Color(averageRed, averageGreen, averageBlue);

        setColorOfRegion(startRow, startCol, width, height, regionColor, image);
    }

    /**
     * Sets the colour of all pixels in a specified rectangular region of an image to a specified
     * color
     *
     * @param startRow The row of the top left corner of the region; must be within the width of
     *                 image
     * @param startCol The column of the top left corner of the region; must be within the height of
     *                 image
     * @param width    Width of the region to colour
     * @param height   Height of the region to color
     * @param color    Color to set the pixels in the region
     * @param image    Image to change
     */
    private void setColorOfRegion(int startRow, int startCol, int width, int height, Color color,
                                  Image image) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.set(j + startCol, i + startRow, color);
            }
        }
    }

    /**
     * Returns the average of the values in an ArrayList of Integers
     *
     * @param values The ArrayList of integers with the values to get the average of
     * @return An integer that represents the average of the integers in the ArrayList
     */
    private int getAverage(ArrayList<Integer> values) {
        int sum = 0;

        for (Integer i : values) {
            sum += i;
        }

        return sum / values.size();
    }

    /**
     * Rotate an image by the given angle (degrees) about the centre of the image.
     * The centre of an image is the pixel at (width/2, height/2). The new regions
     * that may be created are given the colour white (<code>#ffffff</code>) with
     * maximum transparency (alpha = 255).
     *
     * @param degrees the angle to rotate the image by, 0 <= degrees <= 360.
     * @return a rotate version of the instance.
     */
    public Image rotate(double degrees) {
        double radians = Math.toRadians(degrees);
        HashMap<String, Integer> imgSize = rotationImageSize(degrees);

        int newWidth = imgSize.get("width");
        int newHeight = imgSize.get("height");

        Image rotateImage = new Image(newWidth, newHeight);

        for (int col = 0; col < newWidth; col++) {
            for (int row = 0; row < newHeight; row++) {
                int original_x = (int) ((col - newWidth / 2) * Math.cos(radians) +
                    (row - newHeight / 2) * Math.sin(radians) + width / 2);
                int original_y = (int) (-(col - newWidth / 2) * Math.sin(radians) +
                    (row - newHeight / 2) * Math.cos(radians) + height / 2);
                if (original_x >= 0 && original_y >= 0 &&
                    original_x < width &&
                    original_y < height) {
                    rotateImage.set(col, row, image.get(original_x, original_y));
                } else {
                    rotateImage.set(col, row, Color.WHITE);
                }
            }
        }
        return rotateImage;
    }

    /**
     * @param degrees the angle to rotate the image by, 0 <= degrees <= 360.
     * @return HashMap with keys <b>width</b> and <b>height</b> representing the size
     * of the image after rotation
     */
    private HashMap<String, Integer> rotationImageSize(double degrees) {
        HashMap<String, Integer> size = new HashMap<>();
        double radians = Math.toRadians(degrees);
        int newWidth;
        int newHeight;

        if (degrees <= 90.00 || (degrees > 180 && degrees <= 270)) {
            newWidth = (int) Math.abs((width * Math.cos(radians) +
                height * Math.sin(radians)));
            newHeight = (int) Math.abs((width * Math.sin(radians) +
                height * Math.cos(radians)));
        } else {
            newWidth = (int) Math.abs((width * Math.cos(radians) -
                height * Math.sin(radians)));
            newHeight = (int) Math.abs((width * Math.sin(radians) -
                height * Math.cos(radians)));
        }

        size.put("width", newWidth);
        size.put("height", newHeight);

        return size;
    }

    /**
     * Compute the discrete Fourier transform of the image and return the
     * amplitude and phase matrices as a DFTOutput instance.
     *
     * @return the amplitude and phase of the DFT of the instance.
     */
    public DFTOutput dft() {
        Image greyImage = new Image(grayscale());
        double[][] amplitude = new double[greyImage.height()][greyImage.width()];
        double[][] phase = new double[greyImage.height()][greyImage.width()];

        double[] pixelFourierComponents;
        double realComponent;
        double imaginaryComponent;
        int intensity;

        for (int i = 0; i < greyImage.width(); i++) {
            for (int j = 0; j < greyImage.height(); j++) {
                intensity = greyImage.get(i, j).getRed();

                pixelFourierComponents = fourierOfPixel(i, j, greyImage);
                realComponent = pixelFourierComponents[0];
                imaginaryComponent = pixelFourierComponents[1];

                amplitude[j][i] = Math.sqrt(Math.pow(realComponent, 2.0) +
                    Math.pow(imaginaryComponent, 2.0));

                if (realComponent != 0) {
                    phase[j][i] = Math.atan2(imaginaryComponent, realComponent);
                } else if (imaginaryComponent == 0) {
                    phase[j][i] = 0.0;
                } else if (imaginaryComponent > 0) {
                    phase[j][i] = Math.PI / 2;
                } else {
                    phase[j][i] = -Math.PI / 2;
                }
            }
        }

        return new DFTOutput(amplitude, phase);
    }

    /**
     * Replaces a background screen with a provided image.
     * <p>
     * This operation identifies the largest connected region of the image that matches
     * <code>screenColour</code> exactly. This operation determines a rectangle that bounds
     * the "green screen" region and overlays the <code>backgroundImage</code> over that
     * rectangle by aligning the top-left corner of the image with the top-left corner of the
     * rectangle. After determining the screen region, all pixels in that region matching
     * <code>screenColour</code> are replaced with corresponding pixels from
     * <code>backgroundImage</code>.
     * <p>
     * If <code>backgroundImage</code> is smaller
     * than the screen then the image is tiled over the screen.
     *
     * @param screenColour    the colour of the background screen, is not null
     * @param backgroundImage the image to replace the screen with, is not null
     * @return an image with provided image replacing the background screen
     * of the specified colour, tiling the screen with the background image if the
     * background image is smaller than the screen size.
     */
    public Image greenScreen(Color screenColour, Image backgroundImage) {
        Image greenScreenImage = new Image(image);
        HashSet<Point> points = getLargestRegion(screenColour);
        Rectangle boundingRectangle = boundingRect(points);

        Image tiledImage = tiledBackground(boundingRectangle, backgroundImage);
        HashMap<String, Integer> minMax = minMaxXY(points);
        HashSet<Point> boundedPoints = rectPoints(minMax);

        int xMin = minMax.get("xMin");
        int yMin = minMax.get("yMin");

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point p = new Point(col, row);
                if (boundedPoints.contains(p)) {
                    Color color = tiledImage.get(col - xMin, row - yMin);
                    Color rectColor = image.get(col, row);

                    if (rectColor.equals(screenColour)) {
                        greenScreenImage.set(col, row, color);
                    }
                }
            }
        }

        return greenScreenImage;
    }

    /**
     * Adds all possible points into a HashSet within the range xMin to xMax
     * and yMin to yMax (inclusive).<br>
     * <p>
     * Key and Value Pairs: "xMin" - minimum X value, "yMin" - minimum Y value,
     * "xMax" - maximum X value, "yMax" - maximum Y value.
     * </p>
     *
     * @param pointsMap HashMap of type <b>String, Integer</b>
     * @return a HashSet of the bounded points, limits inclusive
     */
    private HashSet<Point> rectPoints(HashMap<String, Integer> pointsMap) {
        HashSet<Point> boundedPoints = new HashSet<>();
        int xMin = pointsMap.get("xMin");
        int yMin = pointsMap.get("yMin");
        int xMax = pointsMap.get("xMax");
        int yMax = pointsMap.get("yMax");

        for (int row = yMin; row <= yMax; row++) {
            for (int col = xMin; col <= xMax; col++) {
                Point p = new Point(col, row);
                boundedPoints.add(p);
            }
        }

        return boundedPoints;
    }

    /**
     * Takes a Rectangle object bounding the largest greenscreen region and
     * the background image and creates an image larger than the rectangle with
     * the tiled background image.
     *
     * @param rect            - a Rectangle object
     * @param backgroundImage - a background image.
     * @return returns a tiled image which is bigger than or equal to the size of <code>rect</code>.
     */
    private Image tiledBackground(Rectangle rect, Image backgroundImage) {
        int rectWidth = rect.xBottomRight - rect.xTopLeft;
        int rectHeight = rect.yBottomRight - rect.yTopLeft;
        int backgroundImageWidth = backgroundImage.width();
        int backgroundImageHeight = backgroundImage.height();
        int widthRatio = (int) Math.ceil((double) rectWidth / backgroundImageWidth);
        int heightRatio = (int) Math.ceil((double) rectHeight / backgroundImageWidth);
        Image tiledImage =
            new Image(widthRatio * backgroundImageWidth, heightRatio * backgroundImageHeight);

        int countRows = 0;
        int countCols = 0;
        while (countRows < heightRatio) {
            int rowIndex = 0, colIndex = 0;

            for (int row = countRows * backgroundImageHeight;
                 row < backgroundImageHeight + countRows * backgroundImageHeight; row++) {
                for (int col = countCols * backgroundImageWidth;
                     col < backgroundImageWidth + countCols * backgroundImageWidth; col++) {
                    Color bgColor = backgroundImage.get(colIndex, rowIndex);
                    tiledImage.set(col, row, bgColor);
                    colIndex++;
                }
                rowIndex++;
                colIndex = 0;
            }
            countCols++;
            if (countCols == widthRatio) {
                countCols = 0;
                countRows++;
            }
        }

        return tiledImage;
    }

    /**
     * @param points - Hashset of minimum and maximum X and Y values.
     * @return returns a Rectangle with coordinates <b>xMin, yMin, xMax, yMax</b> from set of points.
     */
    private Rectangle boundingRect(HashSet<Point> points) {
        HashMap<String, Integer> minMax = minMaxXY(points);
        int xMin = minMax.get("xMin");
        int yMin = minMax.get("yMin");
        int xMax = minMax.get("xMax");
        int yMax = minMax.get("yMax");

        return new Rectangle(xMin, yMin, xMax, yMax);
    }

    /**
     * Finds the minimum and maximum X and Y values in a given HashSet of Points
     *
     * @param points - Hashset of points.
     * @return return the HashMap of minimum and maximum x, y values of the HashSet of points
     * with keys <b>xMin, yMin, xMax, yMax </b>.
     */
    private HashMap<String, Integer> minMaxXY(HashSet<Point> points) {
        Iterator<Point> it = points.iterator();
        Point initPoint = it.next();
        int xMin = (int) initPoint.getX();
        int xMax = (int) initPoint.getX();
        int yMin = (int) initPoint.getY();
        int yMax = (int) initPoint.getY();

        HashMap<String, Integer> minMax = new HashMap<>();

        for (Point p : points) {
            int x = (int) p.getX();
            int y = (int) p.getY();

            if (xMin > x) {
                xMin = x;
            }
            if (xMax < x) {
                xMax = x;
            }
            if (yMin > y) {
                yMin = y;
            }
            if (yMax < y) {
                yMax = y;
            }
        }
        minMax.put("xMin", xMin);
        minMax.put("yMin", yMin);
        minMax.put("xMax", xMax);
        minMax.put("yMax", yMax);
        return minMax;
    }

    /**
     * This method scans an image for regions that contain the <code>screenColor</code> and
     * finds the largest connected region
     *
     * @param screenColour the colour of the background screen, is not null
     * @return a HashSet representing the largest region of <code>screenColor</code>
     * contained within an image
     */
    private HashSet<Point> getLargestRegion(Color screenColour) {
        int group = 0;
        boolean[][] trueMatrix = getMatrix(screenColour);
        boolean[][] exempt = new boolean[height][width];

        HashMap<Integer, HashSet<Point>> regions = new HashMap<>();
        HashSet<Point> largestSet = new HashSet<>();

        regions.put(group, largestSet);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (trueMatrix[row][col] && !exempt[row][col]) {
                    group++;

                    HashSet<Point> points = new HashSet<>();
                    HashSet<Point> checkedPoints = new HashSet<>(1);
                    getRegion(col, row, trueMatrix, points, checkedPoints, exempt);
                    regions.put(group, checkedPoints);
                }
            }
        }

        for (int i = 0; i < regions.size(); i++) {
            if (regions.get(i).size() > largestSet.size()) {
                largestSet = regions.get(i);
            }
        }

        return largestSet;
    }

    /**
     * This method gets a connected region in an image based on indices of the <code>trueMatrix</code>
     * by checking for neighbouring <b>"true"</b> values in the matrix using the
     * <b>getNeighbouringPoints</b> method.
     * <p>
     * <b>Modifies:</b> <br>
     * <code>points</code>: Points in a region are added into the HashSet
     * <code>checkedPoints</code>: Points in a region are added into the HashSet
     * <code>exempt</code>: for every Point that exists in the HashSet of points, the corresponding
     * X and Y values of that point are mapped to <b>true</b> in the matrix
     * </p>
     *
     * @param col           the first column from which to start checking for a region
     * @param row           the first row from which to start checking for a region
     * @param trueMatrix    a 2-D matrix containing either <b>true</b> or <b>false</b> values
     * @param points        an empty HashSet of type Point
     * @param checkedPoints an empty HashSet of type Point
     * @param exempt        an empty 2-D matrix
     */
    private void getRegion(int col, int row, boolean[][] trueMatrix, HashSet<Point> points,
                           HashSet<Point> checkedPoints, boolean[][] exempt) {
        int nCol = col;
        int nRow = row;
        ArrayList<Point> orderedPointsArray = new ArrayList<>(1);
        getNeighbouringPoints(nCol, nRow, trueMatrix, points, orderedPointsArray);

        while (points.size() <= width * height) {

            Point newPt = new Point(nCol, nRow);
            checkedPoints.add(newPt);

            if (points.size() == checkedPoints.size()) {
                break;
            }

            Point point = orderedPointsArray.get(checkedPoints.size());
            nRow = point.y;
            nCol = point.x;
            exempt[nRow][nCol] = true;
            getNeighbouringPoints(nCol, nRow, trueMatrix, points, orderedPointsArray);
        }
    }

    /**
     * This method gets a region in an image based on indices of the <code>trueMatrix</code>
     * by checking for neighbouring <b>"true"</b> values in the matrix using the
     * <b>getNeighbouringPoints</b> method.
     * <p>
     * <b>Modifies:</b> <br>
     * <code>points</code>: Points in a region are added into the HashSet
     * <code>orderedPointsArray</code>: new/unique Points in a region are added into the ArrayList
     * </p>
     *
     * @param col                the first column from which to start checking for a region
     * @param row                the first row from which to start checking for a region
     * @param trueMatrix         a 2-D matrix containing either <b>true</b> or <b>false</b> values
     * @param points             an empty HashSet of type Point
     * @param orderedPointsArray a copy of the <code>points</code> HashSet but ordered in an ArrayList
     */
    private void getNeighbouringPoints(int col, int row, boolean[][] trueMatrix,
                                       HashSet<Point> points, ArrayList<Point> orderedPointsArray) {

        for (int i = row - 1; i <= row + 1; i++) {
            if (i >= 0 && i < height) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (j >= 0 && j < width) {
                        Point p = new Point(j, i);
                        if (!points.contains(p) && trueMatrix[i][j]) {
                            points.add(p);
                            orderedPointsArray.add(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param screenColor the colour of the background screen, is not null
     * @return a matrix which has indices set to true if a corresponding pixel in the image
     * contains <code>screenColor</code> and false otherwise.
     */
    private boolean[][] getMatrix(Color screenColor) {
        boolean[][] matrix = new boolean[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                matrix[row][col] = screenColor.equals(image.get(col, row));
            }
        }
        return matrix;
    }

    /**
     * Align (appropriately rotate) an image of text that was improperly aligned.
     * This transformation can work properly only with text images rotated between -89 and 89
     * degrees from the normal, although it works best when between around -80 and 80 degrees. Has
     * difficulties when there are two perpendicular lines with high amplitudes though (from dft
     * visualization, that is)
     *
     * @return the aligned image.
     */
    public Image alignTextImage() {
        ImageTransformer rotationImageTransformer = new ImageTransformer(image);

        DFTOutput imageDFT = rotationImageTransformer.dft();

        double[][] shiftedAmplitudes = fftShift(imageDFT.getAmplitude().getMatrix());
        modifyDFTAmplitudesForImage(shiftedAmplitudes);

        double[][] examinationMatrix = getMiddleRegion(shiftedAmplitudes,
            shiftedAmplitudes[0].length / 2, shiftedAmplitudes.length / 2);

        double theta = getAngleOfRotation(examinationMatrix);
        Image rotatedImage = rotationImageTransformer.rotate(theta);

        return rotatedImage;
    }

    /**
     * Copies the matrix elements in the middle of an inputted matrix. The size of this region is
     * specified by the width and height parameters
     *
     * @param matrix the matrix to copy from
     * @param width  the width of the middle region
     * @param height the height of the middle region
     * @return a copy of the middle region of the inputted matrix
     * <p>
     * requires that width and height are less than or equal to the height and width of the
     * inputted matrix
     */
    private double[][] getMiddleRegion(double[][] matrix, int width, int height) {
        double[][] middleRegion = new double[height][width];
        int startRow = (matrix.length - height) / 2;
        int startCol = (matrix[0].length - width) / 2;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                middleRegion[i][j] = matrix[startRow + i][startCol + j];
            }
        }

        return middleRegion;
    }

    /**
     * Applies a logarithmic transformation to the elements in a matrix. Facilitates conversion of
     * the matrix to an image
     *
     * @param amplitudeMatrix the matrix to rescale the elements of
     */
    private void modifyDFTAmplitudesForImage(double[][] amplitudeMatrix) {
        double transformConstant = (255.0 / Math.log(1 + maxInMatrix(amplitudeMatrix)));

        for (int i = 0; i < amplitudeMatrix.length; i++) {
            for (int j = 0; j < amplitudeMatrix[0].length; j++) {
                amplitudeMatrix[i][j] = Math.log(1.0 + amplitudeMatrix[i][j]) * transformConstant;
            }
        }
    }

    /**
     * Creates a grayscale image out of a matrix of intensities
     *
     * @param matrix the matrix to base the image off of
     * @return a grayscale Image where each pixel's intensity matches the value of the corresponding
     * element in matrix
     * <p>
     * requires that the values in matrix lie between 0-255
     */
    public Image toImage(double[][] matrix) {
        Image intensityImage = new Image(matrix[0].length, matrix.length);
        Color pixelColor;
        int intensity;
        int logTransformConstant = (int) (255.0 / Math.log(1 + maxInMatrix(matrix)));

        for (int i = 0; i < intensityImage.height(); i++) {
            for (int j = 0; j < intensityImage.width(); j++) {
                intensity = (int) Math.log(1.0 + matrix[i][j]) * logTransformConstant;
                pixelColor = new Color(intensity, intensity, intensity);

                intensityImage.set(j, i, pixelColor);
            }
        }

        return intensityImage;
    }

    /**
     * Returns the value of the largest matrix element
     *
     * @param matrix the matrix to analyze
     * @return the largest value in the matrix
     */
    private double maxInMatrix(double[][] matrix) {
        double max = 0.0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                }
            }
        }

        return max;
    }

    /**
     * Finds the angle of rotation of an image, given a matrix containing its shifted dft amplitudes,
     * and determines the proper angle by which to rotate the image such that it is realigned.
     *
     * @param dftAmplitudes the dft amplitudes of the image
     * @return the angle by which the image needs to be rotated in order to be realigned
     */
    private double getAngleOfRotation(double[][] dftAmplitudes) {
        int height = dftAmplitudes.length;
        int width = dftAmplitudes[0].length;
        double slope;
        int numToExamine = 10;

        if (columnValues(dftAmplitudes, numToExamine)[numToExamine] >
            rowValues(dftAmplitudes, numToExamine)[numToExamine]) {
            int[] columnIndices = columnValues(dftAmplitudes, width);

            slope = slopeOfBestFitLineFromColumns(columnIndices);
        } else {
            int[] rowIndices = rowValues(dftAmplitudes, height);

            slope = slopeOfBestFitLineFromRows(rowIndices);
        }

        if (slope > 0) {
            return (2 * Math.PI - (Math.PI / 2 - Math.atan(slope))) * 360 / (2 * Math.PI);
        } else {
            return (Math.PI / 2 + Math.atan(slope)) * 360 / (2 * Math.PI);
        }
    }

    /**
     * Shifts zero-frequency component to center of spectrum.
     * Makes four different quadrants (like a graph) by spliting up the matrix and swaps
     * quadrants 1 and 3, and 2 and 4.
     *
     * @param matrix a 2D matrix of type double
     * @return a 2D matrix with the zero-frequency component at center
     */
    private double[][] fftShift(double[][] matrix) {
        int height = matrix.length;
        int width = matrix[0].length;
        double[][] modifiedMatrix = new double[height][width];

        double[][] quad1Matrix, quad2Matrix, quad3Matrix, quad4Matrix;

        int oddWidth = 0, oddHeight = 0;

        if (height % 2 == 1) {
            oddHeight = 1;
        }
        if (width % 2 == 1) {
            oddWidth = 1;
        }

        quad1Matrix = new double[height / 2 + oddHeight][width / 2 + oddWidth];
        quad2Matrix = new double[height / 2][width / 2];
        quad3Matrix = new double[height / 2 + oddHeight][width / 2 + oddWidth];
        quad4Matrix = new double[height / 2][width / 2];

        for (int row = 0; row < height / 2 + oddHeight; row++) {
            for (int col = width / 2; col < width; col++) {
                quad1Matrix[row][col - width / 2] = matrix[row][col];
            }
        }

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width / 2; col++) {
                quad2Matrix[row][col] = matrix[row][col];
            }
        }

        for (int row = height / 2; row < height; row++) {
            for (int col = 0; col < width / 2 + oddWidth; col++) {
                quad3Matrix[row - height / 2][col] = matrix[row][col];
            }
        }

        for (int row = height / 2 + oddHeight; row < height; row++) {
            for (int col = width / 2 + oddWidth; col < width; col++) {
                quad4Matrix[row - height / 2 - oddHeight][col - width / 2 - oddWidth] =
                    matrix[row][col];
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (row < height / 2 && col < width / 2) {
                    modifiedMatrix[row][col] = quad4Matrix[row][col];
                }
                if (row < height / 2 + oddHeight && col >= width / 2) {
                    modifiedMatrix[row][col] = quad3Matrix[row][col - width / 2];
                }
                if (row >= height / 2 && col < width / 2 + oddWidth) {
                    modifiedMatrix[row][col] =
                        quad1Matrix[row - height / 2][col];
                }
                if (row >= (height / 2 + oddHeight) && col >= width / 2 + oddWidth) {
                    modifiedMatrix[row][col] =
                        quad2Matrix[row - height / 2 - oddHeight][col - width / 2 - oddWidth];
                }
            }
        }

        return modifiedMatrix;
    }
}
