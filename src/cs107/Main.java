package cs107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class will not be graded. You can use it to test your program.
 */
public class Main {

  /**
   * Main entry point of the program.
   *
   * @param args the command lines arguments of the program.
   */
  public static void main(String[] args) {
    //---------------------------
    // Tests functions separately
    //---------------------------
	//System.out.println("Uncomment the function calls in Main.main to test your implementation.");
	//System.out.println("The provided tests are not complete. You have to write your own tests.");
    //testGetNeighbours();
	//testBlackNeighbours();
	//testTransitions();
	//testIdentical();
    //testConnectedPixels1();
    //testConnectedPixels2();
    //testConnectedPixels3();
	//testComputeSlope();
    //testOrientation();
    //testApplyRotation();
    //testApplyTranslation();
	//testThinningStep();
	//testThin();
    //testThin2();
    //testWithSkeleton();
    
    //testDrawSkeleton("1_1"); //draw skeleton of fingerprint 1_1.png
    //testDrawSkeleton("1_2"); //draw skeleton of fingerprint 1_2.png
    //testDrawSkeleton("2_1"); //draw skeleton of fingerprint 2_1.png

    //testDrawMinutiae("1_1"); //draw minutiae of fingerprint 1_1.png
    //testDrawMinutiae("1_2"); //draw minutiae of fingerprint 1_2.png
    //testDrawMinutiae("2_1"); //draw minutiae of fingerprint 2_1.png
	
    //---------------------------
    // Test overall functionality
    //---------------------------
	//compare 1_1.png with 1_2.png: they are supposed to match
    //testCompareFingerprints("1_1", "1_2", true);  //expected match: true
    
    //compare 1_1.png with 2_1.png: they are not supposed to match
    //testCompareFingerprints("1_1", "2_1", false); //expected match: false

    //compare 1_1 with all other images of the same finger
    //testCompareAllFingerprints("1_1", 1, true);

    //compare 1_1 with all images of finger 2
    //testCompareAllFingerprints("1_1", 2, false);

    //compare 1_1 with all images of finger 3 to 16
    //for (int f = 3; f <= 16; f++) {
    //    testCompareAllFingerprints("1_1", f, false);
    //}
	  
	//System.out.println("End");
	  
  }

  /**
   * This function is here to help you test the functionalities of
   * getNeighbours. You are free to modify and/or delete it.
   */
  public static void testGetNeighbours() {
    System.out.print("testGetNeighbours 1: ");
    boolean[][] image = {{true}};
    boolean[] neighbours = Fingerprint.getNeighbours(image, 0, 0);
    boolean[] expected = {false, false, false, false,
                          false, false, false, false};
    if (arrayEqual(neighbours, expected)) {
      System.out.println("OK");
    } else {
      System.out.println("ERROR");
      System.out.print("Expected: ");
      printArray(expected);
      System.out.print("Computed: ");
      printArray(neighbours);
    }

    System.out.print("testGetNeighbours 2: ");
    boolean[][] image2 = {{true, true}};
    boolean[] neighbours2 = Fingerprint.getNeighbours(image2, 0, 0);
    boolean[] expected2 = {false, false, true,  false,
                           false, false, false, false};
    if (arrayEqual(neighbours2, expected2)) {
      System.out.println("OK");
    } else {
      System.out.println("ERROR");
      System.out.print("Expected: ");
      printArray(expected2);
      System.out.print("Computed: ");
      printArray(neighbours2);
    }
  }
  
  public static void testBlackNeighbours() {
	  System.out.print("testBlackNeighbours 1 : ");
	  boolean[] neighbours = {true, false, false, true, false, true, true, true};
	  int expected = 5;
	  int blackNeighbours = Fingerprint.blackNeighbours(neighbours);
	  if (blackNeighbours == expected) {
		  System.out.println("OK");
	  }
	  else {
		  System.out.println("ERROR");
		  System.out.println("Expected: " + expected);
		  System.out.println("Computed: " + blackNeighbours);
	  }
	  
	  System.out.print("testBlackNeighbours 2 : ");
	  boolean[] neighbours2 = {true, true, false, false, false, true, true, false};
	  int expected2 = 4;
	  int blackNeighbours2 = Fingerprint.blackNeighbours(neighbours2);
	  if (blackNeighbours2 == expected2) {
		  System.out.println("OK");
	  }
	  else {
		  System.out.println("ERROR");
		  System.out.println("Expected: " + expected2);
		  System.out.println("Computed: " + blackNeighbours2);
	  }
	 
  }
  
  public static void testTransitions() {
	  System.out.print("testTransitions 1 : ");
	  boolean[] neighbours = {true, false, false, true, false, true, true, false};
	  int expected = 3;
	  int transitions = Fingerprint.transitions(neighbours);
	  if (transitions == expected) {
		  System.out.println("OK");
	  }
	  else {
		  System.out.println("ERROR");
		  System.out.println("Expected: " + expected);
		  System.out.println("Computed: " + transitions);
	  }
	  
	  System.out.print("testTransitions 2 : ");
	  boolean[] neighbours2 = {false, false, true, false, true, true, false, true};
	  int expected2 = 3;
	  int transitions2 = Fingerprint.transitions(neighbours2);
	  if (transitions2 == expected2) {
		  System.out.println("OK");
	  }
	  else {
		  System.out.println("ERROR");
		  System.out.println("Expected: " + expected2);
		  System.out.println("Computed: " + transitions2);
	  }
  }
  
  public static void testIdentical() {
	System.out.print("testIdentical: ");
	boolean[][] image1 = {{true, false, false, true},
	                     {false, false, true, true},
	                     {false, true, true, false},
	                     {false, false, false, false}};
	boolean[][] image2 = {{true, false, false, true},
    					 {false, false, true, true},
    					 {false, true, true, false}};
	boolean[][] image3 = {{true, false, false, true},
            			 {false, false, true, true},
            			 {false, true, true, false},
            			 {false, false, true, false}};
	if (arrayEqual(image1, image1)==Fingerprint.identical(image1, image1)) {
		System.out.println("OK 1");	
	}
	if (arrayEqual(image1, image2)==Fingerprint.identical(image1, image2)) {
		System.out.println("OK 2");	
	}
	if (arrayEqual(image1, image3)==Fingerprint.identical(image1, image3)) {
		System.out.println("OK 3");	
	} 
  }

  /**
   * This function is here to help you test the functionalities of
   * connectedPixels. You are free to modify and/or delete it.
   */
  public static void testConnectedPixels1() {
    System.out.print("testConnectedPixels1: ");
    boolean[][] image = {{true, false, false, true},
                         {false, false, true, true},
                         {false, true, true, false},
                         {false, false, false, false}};
    boolean[][] expected = {{false, false, false, true},
                            {false, false, true, true},
                            {false, true, true, false},
                            {false, false, false, false}};
    boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 10);
    if (arrayEqual(connectedPixels, expected)) {
      System.out.println("OK");
    } else {
      System.out.println("ERROR");
      System.out.print("Expected: ");
      printArray(expected);
      System.out.print("Computed: ");
      printArray(connectedPixels);
    }
  }
  

  /**
   * This function is here to help you test the functionalities of
   * connectedPixels. You are free to modify and/or delete it.
   */
  public static void testConnectedPixels2() {
    System.out.print("testConnectedPixels2: ");
    boolean[][] image = {{true, false, false, true},
                         {false, false, true, true},
                         {false, true, true, false},
                         {false, false, false, false}};
    boolean[][] expected = {{false, false, false, false},
                            {false, false, true,  false},
                            {false, true,  true,  false},
                            {false, false, false, false}};
    boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 1);
    if (arrayEqual(connectedPixels, expected)) {
      System.out.println("OK");
    } else {
      System.out.println("ERROR");
      System.out.print("Expected: ");
      printArray(expected);
      System.out.print("Computed: ");
      printArray(connectedPixels);
    }
  }
  

  /**
   * This function is here to help you test the functionalities of
   * connectedPixels. You are free to modify and/or delete it.
   */
  public static void testConnectedPixels3() {
    System.out.print("testConnectedPixels3: ");
    boolean[][] image = {{true,  false, false, true,  true},
                         {true,  false, true,  true,  false},
                         {true,  true,  false, false, false},
                         {false, true,  false, true,  false}};
    boolean[][] expected = {{true,  false, false, true,  false},
                            {true,  false, true,  true,  false},
                            {true,  true,  false, false, false},
                            {false, true,  false, false, false}};
    boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 2);
    if (arrayEqual(connectedPixels, expected)) {
      System.out.println("OK");
    } else {
      System.out.println("ERROR");
      System.out.print("Expected: ");
      printArray(expected);
      System.out.print("Computed: ");
      printArray(connectedPixels);
    }
  }
  
  public static void testComputeSlope() {
	  System.out.println("testComputeSlope");

	  //infinity case
	  boolean[][] connectedPixels1 = {{true, false, false, false, false},
              						 {true, false, false,  false, false},
              						 {true, false,  false,  false, false},
              						 {true, false, false, false, false}};
	  
	  double slope1 =Fingerprint.computeSlope(connectedPixels1, 1, 0);
	  System.out.println("Slope : " + slope1 + " Expected slope : Infinity ");
	  
	  //x2 >= y2
	  boolean[][] connectedPixels2 = {{false, false, false, true, false},
                 					 {false, false, true,  true, false},
                 					 {false, true,  true,  false, false},
                 					 {false, false, false, false, false}};

	  double slope2 =Fingerprint.computeSlope(connectedPixels2, 2, 1);
	  System.out.println("Slope : " + slope2 + " Expected slope : 0.7");
	  
	  //x2<y2
	  boolean[][] connectedPixels3 = {{false, false, false, false},
				 					 {false, false, true, false},
				 					 {false, true,  true,  false},
				 					 {true, true, false, false},
				 					 {false, false, false, false}};
				 					 

	  double slope3 =Fingerprint.computeSlope(connectedPixels3, 1, 2);
	  System.out.println("Slope : " + slope3 + " Expected slope : 10/7 ");
	  
	  boolean[][] connectedPixels4 = {{true, false, false, false},
				 					 {false, true, false, false},
				 					 {false, false,  true,  false},
				 					 {false, false, true, false},
				 					 {false, false, false, true}};
				 

	  double slope4 =Fingerprint.computeSlope(connectedPixels4, 4, 4);
	  System.out.println("Slope : " + slope4 + " Expected slope : 45 ");
	  
	  
  }

  /**
   * This function is here to help you test the functionalities of
   * computeOrientation. You are free to modify and/or delete it.
   */
  public static void testOrientation() {
    boolean[][] image = {{true, false, false, true},
                         {false, false, true, true},
                         {false, true, true, false},
                         {false, false, false, false}};
    int angle = Fingerprint.computeOrientation(image, 2, 1, 3);
    System.out.println("Expected angle: 35\t Computed angle: " + angle);
  }

  /**
   * This function is here to help you test the functionalities of
   * applyRotation. You are free to modify and/or delete it.
   */
  public static void testApplyRotation() {
    // minutia, centerRow, centerCol, rotation)
    int[] minutia = new int[] {1, 3, 10};
    int[] result = Fingerprint.applyRotation(minutia, 0, 0, 0);
    System.out.println("Expected: 1,3,10");
    System.out.print("Computed: ");
    printArray(result);

    result = Fingerprint.applyRotation(minutia, 10, 5, 0);
    System.out.println("Expected: 1,3,10");
    System.out.print("Computed: ");
    printArray(result);

    result = Fingerprint.applyRotation(minutia, 0, 0, 90);
    System.out.println("Expected: -3,1,100");
    System.out.print("Computed: ");
    printArray(result);

    result = Fingerprint.applyRotation(new int[] {0, 3, 10}, 0, 0, 90);
    System.out.println("Expected: -3,0,100");
    System.out.print("Computed: ");
    printArray(result);

    result = Fingerprint.applyRotation(new int[] {3, 0, 10}, 0, 0, 90);
    System.out.println("Expected: 0,3,100");
    System.out.print("Computed: ");
    printArray(result);
  }

  /**
   * This function is here to help you test the functionalities of
   * applyTranslation. You are free to modify and/or delete it.
   */
  public static void testApplyTranslation() {
    // minutia, rowTranslation, colTranslation)
    int[] result = Fingerprint.applyTranslation(new int[] {1, 3, 10}, 0, 0);
    System.out.println("Expected: 1,3,10");
    System.out.print("Computed: ");
    printArray(result);

    result = Fingerprint.applyTranslation(new int[] {1, 3, 10}, 10, 5);
    System.out.println("Expected: -9,-2,10");
    System.out.print("Computed: ");
    printArray(result);
  }
  
  public static void testThinningStep() {

	  boolean[][] image = new boolean[3][3];
	  image[1][1] = true; //minutia

	  //create all 256 possible patterns
	  for (int i = 0; i < 256; i++) {
		  image[0][0] = ((i >> 0 & 0x1) ==1);
		  image[0][1] = ((i >> 1 & 0x1) ==1);
		  image[0][2] = ((i >> 2 & 0x1) ==1);
		  image[1][0] = ((i >> 3 & 0x1) ==1);
		  image[1][2] = ((i >> 4 & 0x1) ==1);
		  image[2][0] = ((i >> 5 & 0x1) ==1);
		  image[2][1] = ((i >> 6 & 0x1) ==1);
		  image[2][2] = ((i >> 7 & 0x1) ==1);
		  boolean[][] result1 = Fingerprint.thinningStep(image, 0);
		  System.out.println("---- " + i + ": Thinning Step 0 ------");
		  printArray(result1);
		  boolean[][] result2 = Fingerprint.thinningStep(image, 0);
		  System.out.println("---- " + i + ": Thinning Step 1 ------");
		  printArray(result2);
	  }
  }



  /**
   * This function is here to help you test the functionalities of extract.
   * It will read the first fingerprint and extract the minutiae. It will save
   * the thinned version as skeleton_1_1.png and a version where the minutiae
   * are drawn on top as minutiae_1_1.png. You are free to modify and/or delete
   * it.
   */
  public static void testThin() {
	    boolean[][] image1 = Helper.readBinary("resources/test_inputs/1_1_small.png");
	    boolean[][] skeleton1 = Fingerprint.thin(image1);
	    Helper.writeBinary("skeleton_1_1_small.png", skeleton1);
	    System.out.print("Done");
  }
  
  public static void testThin2() {
	    boolean[][] image1 = Helper.readBinary("resources/fingerprints/1_1.png");
	    boolean[][] skeleton1 = Fingerprint.thin(image1);
	    Helper.writeBinary("skeleton_1_1.png", skeleton1);
	    Helper.show(skeleton1, "testThin2");
	    System.out.println("Done");
}

  public static void testDrawSkeleton(String name) {
	    boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name + ".png");
	    boolean[][] skeleton1 = Fingerprint.thin(image1);
	    Helper.writeBinary("skeleton_" + name + ".png", skeleton1);
  }

  public static void testDrawMinutiae(String name) {
    boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name + ".png");
    boolean[][] skeleton1 = Fingerprint.thin(image1);
    List<int[]> minutia1 = Fingerprint.extract(skeleton1);
    int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
    Helper.drawMinutia(colorImageSkeleton1, minutia1);
    Helper.writeARGB("minutiae_" + name + ".png", colorImageSkeleton1);
  }

  /**
   * This function is here to help you test the functionalities of extract
   * without using the function thin. It will read the first fingerprint and
   * extract the minutiae. It will save a version where the minutiae are drawn
   * on top as minutiae_skeletonTest.png. You are free to modify and/or delete
   * it.
   */
  public static void testWithSkeleton() {
    boolean[][] skeleton1 = Helper.readBinary("resources/test_inputs/skeletonTest.png");
    List<int[]> minutiae1 = Fingerprint.extract(skeleton1);
    List<int[]> expected = new ArrayList<int[]>();
    expected.add(new int[] {39, 21, 264});
    expected.add(new int[] {53, 33, 270});

    System.out.print("Expected minutiae: ");
    printMinutiae(expected);
    System.out.print("Computed minutiae: ");
    printMinutiae(minutiae1);

    // Draw the minutiae on top of the thinned image
    int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
    Helper.drawMinutia(colorImageSkeleton1, minutiae1);
    Helper.writeARGB("minutiae_skeletonTest.png", colorImageSkeleton1);
  }

  public static void printMinutiae(List<int[]> minutiae) {
    for (int[] minutia : minutiae) {
      System.out.print("[");
      for (int j = 0; j < minutia.length; j++) {
        System.out.print(minutia[j]);
        if (j != minutia.length - 1)
          System.out.print(", ");
      }
      System.out.println("],");
    }
  }
	  
	  
  /**
   * This function is here to help you test the overall functionalities. It will
   * compare the fingerprint in the file name1.png with the fingerprint in the
   * file name2.png. The third parameter indicates if we expected a match or not.
   */
  public static void testCompareFingerprints(String name1, String name2, boolean expectedResult) {
	    boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name1 + ".png");
	    // Helper.show(Helper.fromBinary(image1), "Image1");
	    boolean[][] skeleton1 = Fingerprint.thin(image1);
	    //Helper.writeBinary("skeleton_" + name1 + ".png", skeleton1);
	    List<int[]> minutiae1 = Fingerprint.extract(skeleton1);
	    //printMinutiae(minutiae1);

	    //int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
	    //Helper.drawMinutia(colorImageSkeleton1, minutiae1);
	    //Helper.writeARGB("./minutiae_" + name1 + ".png", colorImageSkeleton1);

	    boolean[][] image2 = Helper.readBinary("resources/fingerprints/" + name2 + ".png");
	    boolean[][] skeleton2 = Fingerprint.thin(image2);
	    List<int[]> minutiae2 = Fingerprint.extract(skeleton2);

	    //int[][] colorImageSkeleton2 = Helper.fromBinary(skeleton2);
	    //Helper.drawMinutia(colorImageSkeleton2, minutiae2);
	    //Helper.writeARGB("./minutiae_" + name2 + ".png", colorImageSkeleton2);

	    boolean isMatch = Fingerprint.match(minutiae1, minutiae2);
	    System.out.print("Compare " + name1 + " with " + name2);
	    System.out.print(". Expected match: " + expectedResult);
	    System.out.println(" Computed match: " + isMatch);
	  }

  /**
   * This function is here to help you test the overall functionalities. It will
   * compare the fingerprint in the file <code>name1.png</code> with all the eight 
   * fingerprints of the given finger (second parameter). 
   * The third parameter indicates if we expected a match or not.
   */
  public static void testCompareAllFingerprints(String name1, int finger, boolean expectedResult) {
	  for (int i = 1; i <= 8; i++) {
		  testCompareFingerprints(name1, finger + "_" + i, expectedResult);
	  }
  }

  /*
   * Helper functions to print and compare arrays
   */
  public static boolean arrayEqual(boolean[] array1, boolean[] array2) {
    if (array1 == null && array2 == null)
      return true;
    if (array1 == null || array2 == null)
      return false;
    if (array1.length != array2.length)
      return false;

    for (int i = 0; i < array1.length; i++) {
      if (array1[i] != array2[i])
        return false;
    }
    return true;
  }

  public static boolean arrayEqual(boolean[][] array1, boolean[][] array2) {
    if (array1 == null && array2 == null)
      return true;
    if (array1 == null || array2 == null)
      return false;
    if (array1.length != array2.length)
      return false;

    for (int i = 0; i < array1.length; i++) {
      if (!arrayEqual(array1[i], array2[i]))
        return false;
    }
    return true;
  }

  public static void printArray(boolean[][] array) {
    for (boolean[] row : array) {
      for (boolean pixel : row) {
        System.out.print(pixel + ",");
      }
      System.out.println();
    }
  }

  public static void printArray(boolean[] array) {
    for (boolean pixel : array) {
      System.out.print(pixel + ",");
    }
    System.out.println();
  }

  public static void printArray(int[] array) {
    for (int pixel : array) {
      System.out.print(pixel + ",");
    }
    System.out.println();
  }
}
