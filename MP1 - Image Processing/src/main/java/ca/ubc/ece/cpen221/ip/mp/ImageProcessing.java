package ca.ubc.ece.cpen221.ip.mp;

import ca.ubc.ece.cpen221.ip.core.Image;

import java.util.Vector;
import java.awt.Color;

/**
 * This class provides some simple operations involving
 * more than one image.
 */
public class ImageProcessing {
    /**
     * Compute the cosine similarity between two images.
     *
     * @param img1: the first image, is not null.
     * @param img2: the second image, is not null and matches img1 in dimensions.
     * @return the cosine similarity between the Images
     * referenced by img1 and img2.
     */
    public static double cosineSimilarity(Image img1, Image img2) {
        Image greyImage1 = grayscale(img1);
        Image greyImage2 = grayscale(img2);

        double[] v1 = intensityVector(greyImage1);
        double[] v2 = intensityVector(greyImage2);

        double v1Length = Math.sqrt(dotProduct(v1, v1));
        double v2Length = Math.sqrt(dotProduct(v2, v2));

        if (v1Length == 0 && v2Length == 0) {
            return 1;
        } else if (v1Length == 0 || v2Length == 0) {
            return 0;
        }

        double cosineSimilarity = dotProduct(v1, v2) / (v1Length * v2Length);

        return cosineSimilarity;
    }

    /**
     * Computes the dot product of two vectors, represented as integer arrays. V1 should be the
     * same size as v2
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product of the first and second vectors
     */
    private static double dotProduct(double[] v1, double[] v2) {
        double sum = 0;

        for (int i = 0; i < v1.length && i < v2.length; i++) {
            sum += (v1[i] * v2[i]);
        }

        return sum;
    }

    /**
     * Returns an array of integers representing the intensity value of each pixel in a grayscaled
     * image
     *
     * @param image the image to save the intensity values of
     * @return an integer array containing the intensities of the inputted image's pixels, in order
     * from left to right, top to bottom (the order you would read in, in english)
     */
    private static double[] intensityVector(Image image) {
        double[] intensityVector = new double[image.width() * image.height()];

        for (int i = 0; i < image.height(); i++) {
            for (int j = 0; j < image.width(); j++) {
                intensityVector[i * image.width() + j] = image.get(j, i).getGreen();
            }
        }

        return intensityVector;
    }

    /**
     * Obtain the grayscale version of the image.
     *
     * @return the grayscale version of the instance.
     */
    private static Image grayscale(Image image) {
        Image gsImage = new Image(image.width(), image.height());
        for (int col = 0; col < image.width(); col++) {
            for (int row = 0; row < image.height(); row++) {
                Color color = image.get(col, row);
                Color gray = Image.toGray(color);
                gsImage.set(col, row, gray);
            }
        }
        return gsImage;
    }

}
