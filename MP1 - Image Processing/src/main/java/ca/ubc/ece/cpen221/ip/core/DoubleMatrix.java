package ca.ubc.ece.cpen221.ip.core;

import java.awt.Color;
import java.util.Arrays;

/**
 * This datatype represents a matrix of <code>double</code> values.
 * <p>
 * The constant fields <code>width</code> and <code>height</code> provide
 * the dimensions of the matrix, and it is possible to compare two DoubleMatrices for
 * equality using a set <code>epsilon</code> value of 10^(-7).
 */
public final class DoubleMatrix {
    public static final double epsilon = 1e-7;
    public final int columns;
    public final int rows;
    private final double[][] matrix;

    /*
        Abstraction Function:
            DoubleMatrix represents a matrix of double-precision values.
            The matrix has dimensions height * width.

        Representation Invariant:
            rows >= 1
            columns >= 1
            matrix.length == rows
            matrix[0].length == columns
     */

    /**
     * Create an instance of DoubleMatrix from a two-dimensional array.
     *
     * @param _input is not null, and _input has at least one row and at least one column.
     */
    public DoubleMatrix(double[][] _input) {
        if (_input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }

        rows = _input.length;
        if (rows == 0) {
            throw new IllegalArgumentException("matrix has to have at least one row");
        }

        columns = _input[0].length;
        if (columns == 0) {
            throw new IllegalArgumentException("matrix has to have at least one column");
        }

        matrix = Arrays.stream(_input).map(double[]::clone).toArray(double[][]::new);
    }

    /**
     * Creates a grayscale image where the intensity of each pixel depends on the value of the
     * pixel in the corresponding location of the instance variable matrix
     *
     * @return the grayscale image where the intensity of each pixel depends on the value of the
     * pixel in the corresponding location of the instance variable matrix
     */
    public Image toImage() {
        Image intensityImage = new Image(matrix[0].length, matrix.length);
        Color pixelColor;
        int intensity;
        int logTransformConstant = (int) (255.0 / Math.log(1 + maxInMatrix()));

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
     * Finds and returns the largest value of any element in the instance variable matrix
     *
     * @return the highest value contained in matrix
     */
    private double maxInMatrix() {
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


    public double[][] getMatrix() {
        return matrix;
    }

    /**
     * Return the entry at (row, col)
     *
     * @param row the row of the entry to return, 0 <= row < rows
     * @param col the column of the entry to return, 0 <= col < columns
     * @return the entry at location (row, col)
     */
    public double get(int row, int col) {
        return matrix[row][col];
    }

    /**
     * Obtain the number of rows in the matrix
     *
     * @return the number of rows in the matrix
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtain the number of columns in the matrix
     *
     * @return the number of columns in the matrix
     */
    public int getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DoubleMatrix)) {
            return false;
        }
        DoubleMatrix other = (DoubleMatrix) o;
        if (columns != other.columns || rows != other.rows) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (Math.abs(matrix[i][j] - other.matrix[i][j]) > epsilon) {
                    System.out.println("Expected: " + other.matrix[i][j]);
                    System.out.println("Actual: " + matrix[i][j]);

                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return columns * rows;
    }
}
