package cs107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides tools to compare fingerprint.
 */
public class Fingerprint {

	/**
	 * The number of pixels to consider in each direction when doing the linear
	 * regression to compute the orientation.
	 */
	public static final int ORIENTATION_DISTANCE = 16;

	/**
	 * The maximum distance between two minutiae to be considered matching.
	 */
	public static final int DISTANCE_THRESHOLD = 5;

	/**
	 * The number of matching minutiae needed for two fingerprints to be considered
	 * identical.
	 */
	public static final int FOUND_THRESHOLD = 20;

	/**
	 * The distance between two angle to be considered identical.
	 */
	public static final int ORIENTATION_THRESHOLD = 20;

	/**
	 * The offset in each direction for the rotation to test when doing the
	 * matching.
	 */
	public static final int MATCH_ANGLE_OFFSET = 2;

	/**
	 * Returns an array containing the value of the 8 neighbours of the pixel at
	 * coordinates <code>(row, col)</code>.
	 * <p>
	 * The pixels are returned such that their indices corresponds to the following
	 * diagram:<br>
	 * ------------- <br>
	 * | 7 | 0 | 1 | <br>
	 * ------------- <br>
	 * | 6 | _ | 2 | <br>
	 * ------------- <br>
	 * | 5 | 4 | 3 | <br>
	 * ------------- <br>
	 * <p>
	 * If a neighbours is out of bounds of the image, it is considered white.
	 * <p>
	 * If the <code>row</code> or the <code>col</code> is out of bounds of the
	 * image, the returned value should be <code>null</code>.
	 *
	 * @param image array containing each pixel's boolean value.
	 * @param row   the row of the pixel of interest, must be between
	 *              <code>0</code>(included) and
	 *              <code>image.length</code>(excluded).
	 * @param col   the column of the pixel of interest, must be between
	 *              <code>0</code>(included) and
	 *              <code>image[row].length</code>(excluded).
	 * @return An array containing each neighbours' value.
	 */
	public static boolean[] getNeighbours(boolean[][] image, int row, int col) {
		assert (image != null); // special case that is not expected (the image is supposed to have been checked
								// earlier)
		// knowing the image is a rectangle (all the rows have the same length, same for
		// columns)
		int rowLength = image.length;
		int colLength = image[0].length;

		// if the pixel of coordinates (row,col) does not exist (out of bound)
		if ((row < 0 || row >= rowLength) || (col < 0 || col >= colLength)) {
			return null;
		}
		// the array is read line by line = the neighbours are out of order in the
		// temporary array
		boolean[] temp = new boolean[9]; // length is 9 because it contains p and its 8 neighbours
		int k = 0;
		for (int i = row - 1; i <= row + 1; ++i) {
			for (int j = col - 1; j <= col + 1; ++j) {
				// if the neighbour does not exist, we set its value to false
				if ((i < 0 || i >= rowLength) || (j < 0 || j >= colLength)) {
					temp[k] = false;
				} else { // else we take the boolean value of the neighbour
					temp[k] = image[i][j];
				}
				k++;
			}
		}
		// temp = {p7, p0, p1, p6, p, p2, p5, p4, p3}
		// put in order p0 to p7 in the final tab getNeighbours (excluding p)
		boolean[] neighbours = { temp[1], temp[2], temp[5], temp[8], temp[7], temp[6], temp[3], temp[0] };

		return neighbours;
	}

	/**
	 * Computes the number of black (<code>true</code>) pixels among the neighbours
	 * of a pixel.
	 *
	 * @param neighbours array containing each pixel value. The array must respect
	 *                   the convention described in
	 *                   {@link #getNeighbours(boolean[][], int, int)}.
	 * @return the number of black neighbours.
	 */
	public static int blackNeighbours(boolean[] neighbours) {
		assert (neighbours != null);
		int blackNeighbours = 0;
		// for every black neighbour in the array neighbours : add 1 to the total
		for (int i = 0; i < neighbours.length; ++i) {
			if (neighbours[i] == true) {
				blackNeighbours++;
			}
		}

		return blackNeighbours;
	}

	/**
	 * Computes the number of white to black transitions among the neighbours of
	 * pixel.
	 *
	 * @param neighbours array containing each pixel value. The array must respect
	 *                   the convention described in
	 *                   {@link #getNeighbours(boolean[][], int, int)}.
	 * @return the number of white to black transitions.
	 */
	public static int transitions(boolean[] neighbours) {
		assert (neighbours != null);
		int transitions = 0;
		for (int i = 0; i < 8; ++i) {
			// counting the white to black transitions between each pixel and the following one
			if (!neighbours[i] && neighbours[(i + 1) % 8]) { // when i=7, (i+1)%8=0, allowing us to compare p7 with p0
				transitions++;
			}
		}

		return transitions;
	}

	/**
	 * Returns <code>true</code> if the images are identical and false otherwise.
	 *
	 * @param image1 array containing each pixel's boolean value.
	 * @param image2 array containing each pixel's boolean value.
	 * @return <code>True</code> if they are identical, <code>false</code>
	 *         otherwise.
	 */
	public static boolean identical(boolean[][] image1, boolean[][] image2) {
		assert (image1 != null);
		assert (image2 != null);
		// if the images are not of the same dimension, they are not identical
		if ((image1.length != image2.length) || (image1[0].length != image2[0].length)) {
			return false;
		} else {
			for (int i = 0; i < image1.length; ++i) { // use the row length of either image1 or image2 as they are equal
				for (int j = 0; j < image1[0].length; ++j) { // same with column length
					// if two pixels are different :
					if (image1[i][j] != image2[i][j]) {
						return false;
						// the loop for comparison is stopped and the method return false
					}
				}
			}
			// all the pixels have been compared and are the same, meaning
			// image1[i][j]==image2[i][j] for every i and j
			return true;
		}
	}

	/**
	 * Internal method used by {@link #thin(boolean[][])}.
	 *
	 * @param image array containing each pixel's boolean value.
	 * @param step  the step to apply, Step 0 or Step 1.
	 * @return A new array containing each pixel's value after the step.
	 */
	public static boolean[][] thinningStep(boolean[][] image, int step) {
		int rowLength = image.length;
		int colLength = image[0].length;

		boolean[][] temp = new boolean[rowLength][colLength];
		// we create a duplicate of the original image
		for (int i = 0; i < rowLength; ++i) {
			for (int j = 0; j < colLength; ++j) {
				temp[i][j] = image[i][j];
			}
		}
		for (int i = 0; i < rowLength; ++i) {
			for (int j = 0; j < colLength; ++j) {
				boolean[] neighbours = getNeighbours(image, i, j); // get the neighbours of the pixel
				int blackNeighbours = blackNeighbours(neighbours); // count its black neighbours
				int transitions = transitions(neighbours); // count the transitions from white to black
				// if the pixel is black
				if (image[i][j]) {
					if (neighbours != null) {
						// if the number of black neighbours is between 2 and 6
						if (2 <= blackNeighbours && blackNeighbours <= 6) {
							// if there is only one transition
							if (transitions == 1) {
								// step 1 (only the last two conditions differs from step 2)
								if (step == 0) {
									// if p0 or p2 or p4 is white
									if (neighbours[0] == false || neighbours[2] == false || neighbours[4] == false) {
										// if p2 or p4 or p6 is white
										if (neighbours[2] == false || neighbours[4] == false
												|| neighbours[6] == false) {
											temp[i][j] = false; // set the pixel to false
										}
									}
								}
								// step 2
								else if (step == 1) {
									// if p0 or p2 or p6 is white
									if (neighbours[0] == false || neighbours[2] == false || neighbours[6] == false) {
										// if p0 or p4 or p6 is white
										if (neighbours[0] == false || neighbours[4] == false
												|| neighbours[6] == false) {
											temp[i][j] = false; // set the pixel to false
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return temp;
	}

	/**
	 * Compute the skeleton of a boolean image.
	 *
	 * @param image array containing each pixel's boolean value.
	 * @return array containing the boolean value of each pixel of the image after
	 *         applying the thinning algorithm.
	 */
	public static boolean[][] thin(boolean[][] image) {
		assert (image != null);

		int rowLength = image.length;
		int colLength = image[0].length;
		boolean[][] tempImage = new boolean[rowLength][colLength];
		boolean[][] finalImage = new boolean[rowLength][colLength];

		// we create a duplicate of the original image
		for (int i = 0; i < rowLength; ++i) {
			for (int j = 0; j < colLength; ++j) {
				tempImage[i][j] = image[i][j];
			}
		}
		// we apply the two first steps once
		finalImage = thinningStep(tempImage, 0);
		finalImage = thinningStep(finalImage, 1);

		// while the temporary image and the final one are not identical
		while (!identical(tempImage, finalImage)) {
			// we replace the tempImage by the previous finalImage
			for (int i = 0; i < rowLength; ++i) {
				for (int j = 0; j < colLength; ++j) {
					tempImage[i][j] = finalImage[i][j];
				}
			}
			// then we can modify finalImage with thinningStep
			finalImage = thinningStep(tempImage, 0);
			finalImage = thinningStep(finalImage, 1);
			// when the tempImage (i.e finalImage minus the last two steps of thinningStep)
			// is identical to the final version finalImage, we exit the loop
		}

		return finalImage;
	}

	/**
	 * Computes all pixels that are connected to the pixel at coordinate
	 * <code>(row, col)</code> and within the given distance of the pixel.
	 *
	 * @param image    array containing each pixel's boolean value.
	 * @param row      the first coordinate of the pixel of interest.
	 * @param col      the second coordinate of the pixel of interest.
	 * @param distance the maximum distance at which a pixel is considered.
	 * @return An array where <code>true</code> means that the pixel is within
	 *         <code>distance</code> and connected to the pixel at
	 *         <code>(row, col)</code>.
	 */
	public static boolean[][] connectedPixels(boolean[][] image, int row, int col, int distance) {
		assert (image != null);

		// if the pixel is out of bound
		if (row < 0 || row >= image.length || col < 0 || col >= image[0].length) {
			return null;
		}

		boolean[][] connectedPixels = new boolean[image.length][image[0].length];// we create a table named
																					// connectedPixels which has the
																					// same size as the table image and
																					// it is filled by false
		boolean newConnected = true;

		while (newConnected) { // while we keep finding new connected pixels at each passing
			newConnected = false;
			for (int rowPixel = 0; rowPixel < image.length; ++rowPixel) {
				for (int colPixel = 0; colPixel < image[0].length; ++colPixel) {
					// if the pixel is black
					if (image[rowPixel][colPixel]) {
						// if it's the square of size 2*distance+1 centered on the minutia of
						// coordinates row, col
						if ((row - distance <= rowPixel) && (rowPixel <= row + distance) && (col - distance <= colPixel)
								&& (colPixel <= col + distance) && (!(connectedPixels[rowPixel][colPixel]))) {
							if ((row - 1 <= rowPixel) && (rowPixel <= row + 1) && (col - 1 <= colPixel)
									&& (colPixel <= col + 1)) { // if the pixel is a neighbour of the minutiae
								connectedPixels[rowPixel][colPixel] = true; // then it's a connected pixel
								newConnected = true; // found a new pixel, the loop does not stop
							} else { // if the pixel is not a neighbour, we check if its connected to a neighbour
								for (int i = 0; i < connectedPixels.length; ++i) {
									for (int j = 0; j < connectedPixels[0].length; ++j) {
										if (connectedPixels[i][j] == true) { // if there's a connected pixel of
																				// coordinates i,j
											if ((i - 1 <= rowPixel) && (rowPixel <= i + 1) && (j - 1 <= colPixel)
													&& (colPixel <= j + 1)) { // that is a neighbour of our pixel
																				// [rowPixel][colPixel]
												connectedPixels[rowPixel][colPixel] = true; // our pixel is a connected
																							// pixel
												newConnected = true; // found a new pixel, the loop does not stop
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return connectedPixels;

	}

	/**
	 * Computes the slope of a minutia using linear regression.
	 *
	 * @param connectedPixels the result of
	 *                        {@link #connectedPixels(boolean[][], int, int, int)}.
	 * @param row             the row of the minutia.
	 * @param col             the col of the minutia.
	 * @return the slope.
	 */
	public static double computeSlope(boolean[][] connectedPixels, int row, int col) {
		assert (connectedPixels != null);

		double slope = 0.0;// we initialize slope
		ArrayList<Double> X = new ArrayList<Double>();// arraylist of double named X for the coordinates X
		ArrayList<Double> Y = new ArrayList<Double>();// arraylist of double named Y for the coordinates Y
		for (int i = 0; i < connectedPixels.length; i++) {// go through all the rows of connectedPixels
			for (int j = 0; j < connectedPixels[0].length; j++) {// go through all the columns of connectedPixels
				if (connectedPixels[i][j]) {// if true we change the coordinates of the pixel putting the minutia as our
											// origin
					double rowY = row - i;
					Y.add(rowY);
					double colX = j - col;
					X.add(colX);
				}
			}
		}
		int v;
		double sumX2 = 0.0;// sum of the square of all x coordinates
		for (v = 0; v < X.size(); ++v) {
			sumX2 = sumX2 + (X.get(v) * X.get(v));
		}
		int w;
		double sumY2 = 0.0;// sum of the square of all y coordinates
		for (w = 0; w < Y.size(); ++w) {
			sumY2 = sumY2 + (Y.get(w) * Y.get(w));
		}
		int m;
		double sumXY = 0.0;// we do the sum of xy
		for (m = 0; m < X.size() && m < Y.size(); ++m) {
			sumXY = sumXY + (X.get(m) * Y.get(m));
		}
		if (sumX2 == 0.0) {
			slope = Double.POSITIVE_INFINITY;
		} else if (sumX2 >= sumY2) {// the equation of the slope if the sum of the square of all x coordinates are
									// upper or equal to the sum of the square of all y coordinates
			slope = sumXY / sumX2;
		} else if (sumX2 < sumY2) {// the other equation of the slope if sumX2 is lower than the sumY2
			slope = sumY2 / sumXY;
		}

		return slope;
	}

	/**
	 * Computes the orientation of a minutia in radians.
	 * 
	 * @param connectedPixels the result of
	 *                        {@link #connectedPixels(boolean[][], int, int, int)}.
	 * @param row             the row of the minutia.
	 * @param col             the col of the minutia.
	 * @param slope           the slope as returned by
	 *                        {@link #computeSlope(boolean[][], int, int)}.
	 * @return the orientation of the minutia in radians.
	 */
	public static double computeAngle(boolean[][] connectedPixels, int row, int col, double slope) {
		assert (connectedPixels != null);

		double angle = 0.0;
		int Up = 0;
		int Down = 0;
		// we change the coordinates according to the minutia as the center
		for (int i = 0; i < connectedPixels.length; ++i) {
			for (int j = 0; j < connectedPixels[0].length; ++j) {
				if (connectedPixels[i][j]) {
					double y = row - i;
					double x = j - col;
					// if the slope is equal to zero, we look the ups and downs with the
					// perpendicular axis
					if (slope == 0.0) {
						if (x > 0) {
							Up++;
						} else {
							Down++;
						}

					} else if (slope == Double.POSITIVE_INFINITY) {// if slope is equal to infinity, we can already look
																	// the ups and downs with the perpendicular axis
						if (y > 0) {
							Up++;
						} else {
							Down++;
						}
					} else {
						if (y >= (-1 / slope) * x) {
							Up++;
						} else {
							Down++;
						}
					}
				}
			} // we can now find the angle
			if (slope == Double.POSITIVE_INFINITY) {
				if (Up > Down) {
					angle = Math.PI / 2;
				} else {
					angle = -Math.PI / 2;
				}
			} else {
				angle = Math.atan(slope);
				if ((Up > Down && angle < 0.0) || (Down >= Up && angle >= 0.0)) {
					angle = angle + Math.PI;
				}
			}
		}
		return angle;
	}

	/**
	 * Computes the orientation of the minutia that the coordinate <code>(row,
	 * col)</code>.
	 *
	 * @param image    array containing each pixel's boolean value.
	 * @param row      the first coordinate of the pixel of interest.
	 * @param col      the second coordinate of the pixel of interest.
	 * @param distance the distance to be considered in each direction to compute
	 *                 the orientation.
	 * @return The orientation in degrees.
	 */
	public static int computeOrientation(boolean[][] image, int row, int col, int distance) {
		assert (image != null);

		int orientationAngle = 0; // initialize orientationAngle
		boolean[][] connectedPixels = connectedPixels(image, row, col, distance); // get the connected pixels of the
																					// minutia in the image
		double slope = computeSlope(connectedPixels, row, col); // get the slope of connectedPixels
		double angle = computeAngle(connectedPixels, row, col, slope); // get the angle

		double angleDegree = Math.round(Math.toDegrees(angle));// change the angle from radians to degrees
		orientationAngle = (int) angleDegree; // convert to integer

		if (orientationAngle < 0) {// if negative we add 360degrees to the angle to have a positive angle
			orientationAngle += 360;
		}
		return orientationAngle;

	}

	/**
	 * Extracts the minutiae from a thinned image.
	 *
	 * @param image array containing each pixel's boolean value.
	 * @return The list of all minutiae. A minutia is represented by an array where
	 *         the first element is the row, the second is column, and the third is
	 *         the angle in degrees.
	 * @see #thin(boolean[][])
	 */
	public static List<int[]> extract(boolean[][] image) {
		List<int[]> extract = new ArrayList<int[]>();// list of arrays of integers

		for (int row = 1; row < image.length - 1; ++row) { // go through all the rows of image
			for (int col = 1; col < image[0].length - 1; ++col) { // go through all the columns of image
				if (image[row][col]) {
					boolean[] neighbours = getNeighbours(image, row, col);
					int transitions = transitions(neighbours);
					if (transitions == 1 || transitions == 3) {// if it is 1 or 3 it is a minutia
						int[] coordinates = new int[3];// we set the size of the tables of integers at 3
						coordinates[0] = row;// we put the coordinate of its row in the first element of the table
						coordinates[1] = col;// we put the coordinate of its column in the second element of the table
						int orientationAngle = 0;
						orientationAngle = computeOrientation(image, row, col, ORIENTATION_DISTANCE);
						coordinates[2] = orientationAngle;// we put the orientation of the minutia in the third element
															// of the table
						extract.add(coordinates);// we add this table in the list named extract
					}
				}
			}
		}
		return extract;

	}

	/**
	 * Applies the specified rotation to the minutia.
	 *
	 * @param minutia   the original minutia.
	 * @param centerRow the row of the center of rotation.
	 * @param centerCol the col of the center of rotation.
	 * @param rotation  the rotation in degrees.
	 * @return the minutia rotated around the given center.
	 */
	public static int[] applyRotation(int[] minutia, int centerRow, int centerCol, int rotation) {
		// convert from degrees to radians
		double radRotation = rotation * (Math.PI / 180);

		int x = minutia[1] - centerCol; // col is minutia[1], apply a translation of centerCol
		int y = centerRow - minutia[0]; // row is minutia[0], apply a translation of centerRow
		double newX = x * Math.cos(radRotation) - y * Math.sin(radRotation); // apply a rotation on x
		double newY = x * Math.sin(radRotation) + y * Math.cos(radRotation); // apply a rotation on y
		int newRow = (int) (Math.round(centerRow - newY)); // round the new row obtained and convert to int
		int newCol = (int) (Math.round(newX + centerCol)); // same with the new column
		int newOrientation = (int) ((minutia[2] + rotation) % 360); // orientation is minutia[2], apply a rotation

		int[] newMinutia = { newRow, newCol, newOrientation };
		return newMinutia;
	}

	/**
	 * Applies the specified translation to the minutia.
	 *
	 * @param minutia        the original minutia.
	 * @param rowTranslation the translation along the rows.
	 * @param colTranslation the translation along the columns.
	 * @return the translated minutia.
	 */
	public static int[] applyTranslation(int[] minutia, int rowTranslation, int colTranslation) {
		// knowing that minutia contains row, column and orientation in this order
		int newRow = minutia[0] - rowTranslation; // row-rowTranslation
		int newCol = minutia[1] - colTranslation; // col-colTranslation
		int[] newMinutia = { newRow, newCol, minutia[2] }; // we keep the same orientation

		return newMinutia;
	}

	/**
	 * Computes the row, column, and angle after applying a transformation
	 * (translation and rotation).
	 *
	 * @param minutia        the original minutia.
	 * @param centerCol      the column around which the point is rotated.
	 * @param centerRow      the row around which the point is rotated.
	 * @param rowTranslation the vertical translation.
	 * @param colTranslation the horizontal translation.
	 * @param rotation       the rotation.
	 * @return the transformed minutia.
	 */
	public static int[] applyTransformation(int[] minutia, int centerRow, int centerCol, int rowTranslation,
			int colTranslation, int rotation) {
		assert (minutia != null);

		int[] newMinutia = new int[3];
		// we apply successively applyRotation and applyTranslation on the minutia to
		// get a new one
		newMinutia = applyRotation(minutia, centerRow, centerCol, rotation);
		newMinutia = applyTranslation(newMinutia, rowTranslation, colTranslation);
		return newMinutia;
	}

	/**
	 * Computes the row, column, and angle after applying a transformation
	 * (translation and rotation) for each minutia in the given list.
	 *
	 * @param minutiae       the list of minutiae.
	 * @param centerCol      the column around which the point is rotated.
	 * @param centerRow      the row around which the point is rotated.
	 * @param rowTranslation the vertical translation.
	 * @param colTranslation the horizontal translation.
	 * @param rotation       the rotation.
	 * @return the list of transformed minutiae.
	 */
	public static List<int[]> applyTransformation(List<int[]> minutiae, int centerRow, int centerCol,
			int rowTranslation, int colTranslation, int rotation) {
		List<int[]> newMinutiae = new ArrayList<int[]>(); // new array list for the minutiae after transformation
		int[] newMinutia = new int[3];
		for (int i = 0; i < minutiae.size(); ++i) { // for every minutia in the list minutiae
			// apply the transformation
			newMinutia = applyTransformation(minutiae.get(i), centerRow, centerCol, rowTranslation, colTranslation,
					rotation);
			newMinutiae.add(newMinutia); // add the transformed minuitia to the new list
		}
		return newMinutiae;
	}

	/**
	 * Counts the number of overlapping minutiae.
	 *
	 * @param minutiae1      the first set of minutiae.
	 * @param minutiae2      the second set of minutiae.
	 * @param maxDistance    the maximum distance between two minutiae to consider
	 *                       them as overlapping.
	 * @param maxOrientation the maximum difference of orientation between two
	 *                       minutiae to consider them as overlapping.
	 * @return the number of overlapping minutiae.
	 */
	public static int matchingMinutiaeCount(List<int[]> minutiae1, List<int[]> minutiae2, int maxDistance,
			int maxOrientation) {
		// initialize matchingMinutiaeCount at 0
		int matchingMinutiaeCount = 0;

		for (int i = 0; i < minutiae1.size(); ++i) {
			int[] m1 = minutiae1.get(i); // get the ith entry of minutiae1
			for (int j = 0; j < minutiae2.size(); ++j) {
				int[] m2 = minutiae2.get(j); // get the jth the entry of minutiae2
				// using the euclidean distance formula :
				double distance = Math.sqrt((Math.pow(m1[0] - m2[0], 2)) + (Math.pow(m1[1] - m2[1], 2)));

				// check if the conditions for a matching minuitia are fulfilled
				if (distance <= maxDistance && (Math.abs(m1[2] - m2[2])) <= maxOrientation) {
					matchingMinutiaeCount++; // found a match, add 1 to the count
					break; // stop comparing
				}
			}
		}
		return matchingMinutiaeCount;

	}

	/**
	 * Compares the minutiae from two fingerprints.
	 *
	 * @param minutiae1 the list of minutiae of the first fingerprint.
	 * @param minutiae2 the list of minutiae of the second fingerprint.
	 * @return Returns <code>true</code> if they match and <code>false</code>
	 *         otherwise.
	 */
	public static boolean match(List<int[]> minutiae1, List<int[]> minutiae2) {
		// we compare each minutia m1 of minutiae1 to every minuitia m2 of minutiae2
		for (int i = 0; i < minutiae1.size(); ++i) {
			int[] m1 = minutiae1.get(i); // ith entry of minutiae1
			for (int j = 0; j < minutiae2.size(); ++j) {
				int[] m2 = minutiae2.get(j); // jth entry of minutiae2

				int rotation = Math.abs(m2[2] - m1[2]);
				for (int r = (rotation - MATCH_ANGLE_OFFSET); r <= (rotation + MATCH_ANGLE_OFFSET); ++r) {
					// we apply a transformation to the second list of minutiae
					List<int[]> newMinutiae2 = applyTransformation(minutiae2, m1[0], m1[1], m2[0] - m1[0],
							m2[1] - m1[1], r);
					if (matchingMinutiaeCount(minutiae1, newMinutiae2, DISTANCE_THRESHOLD,
							ORIENTATION_THRESHOLD) >= FOUND_THRESHOLD) { // if there are enough matching minutiae
						return true;
					}
				}
			}
		}

		return false; // there are not enough matching minuitae between the two lists
	}

}
