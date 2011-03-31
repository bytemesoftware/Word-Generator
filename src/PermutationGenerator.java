/**
 * This class generates permutaions of an array of elements
 * I did not create this class I got it from
 * http://www.merriampark.com/perm.htm
 * created by Michael Gilleland
 *
 * I appreciate his work to create this class.
 */

//--------------------------------------
// Systematically generate permutations. 
//--------------------------------------

import java.math.BigInteger;

public class PermutationGenerator {

  private int[] a;
  private BigInteger numLeft;
  private BigInteger total;

  //-----------------------------------------------------------
  // Constructor. WARNING: Don't make n too large.
  // Recall that the number of permutations is n!
  // which can be very large, even when n is as small as 20 --
  // 20! = 2,432,902,008,176,640,000 and
  // 21! is too big to fit into a Java long, which is
  // why we use BigInteger instead.
  //----------------------------------------------------------

  public PermutationGenerator (int n) {

    //TODO: Rewrite class to only use integers insteas of BigInteger

    //precomputed factorials to increase speed
    BigInteger[] factorials = {new BigInteger("1") , new BigInteger("1") , new BigInteger("2") , new BigInteger("6") , new BigInteger("24") , new BigInteger("120") , new BigInteger("720") , new BigInteger("5040") , new BigInteger("40320") , new BigInteger("362880") , new BigInteger("3628800")};

    //the call to generate the factorials
    //BigInteger[] factorials = getFactorial(n);
    
    //list of precomputed factorials in a int array
    //int factorials[] = {1,1,2,6,24,120,720,5040,40320,362880,3628800};

    if (n < 1) {
      throw new IllegalArgumentException ("Min 1");
    }
    a = new int[n];
    total = factorials[n];
    reset ();
  }

  //------
  // Reset
  //------

  public void reset () {
    for (int i = 0; i < a.length; i++) {
      a[i] = i;
    }
    numLeft = new BigInteger (total.toString ());
  }

  //------------------------------------------------
  // Return number of permutations not yet generated
  //------------------------------------------------

  public BigInteger getNumLeft () {
    return numLeft;
  }

  //------------------------------------
  // Return total number of permutations
  //------------------------------------

  public BigInteger getTotal () {
    return total;
  }

  //-----------------------------
  // Are there more permutations?
  //-----------------------------

  public boolean hasMore () {
    return numLeft.compareTo (BigInteger.ZERO) == 1;
  }

  //------------------
  // Compute factorial
  //------------------

  private static BigInteger getFactorial (int n) {
    BigInteger fact = BigInteger.ONE;
    for (int i = n; i > 1; i--) {
      fact = fact.multiply (new BigInteger (Integer.toString (i)));
    }
    return fact;
  }

  //--------------------------------------------------------
  // Generate next permutation (algorithm from Rosen p. 284)
  //--------------------------------------------------------

  public int[] getNext () {

    if (numLeft.equals (total)) {
      numLeft = numLeft.subtract (BigInteger.ONE);
      return a;
    }

    int temp;

    // Find largest index j with a[j] < a[j+1]

    int j = a.length - 2;
    while (a[j] > a[j+1]) {
      j--;
    }

    // Find index k such that a[k] is smallest integer
    // greater than a[j] to the right of a[j]

    int k = a.length - 1;
    while (a[j] > a[k]) {
      k--;
    }

    // Interchange a[j] and a[k]

    temp = a[k];
    a[k] = a[j];
    a[j] = temp;

    // Put tail end of permutation after jth position in increasing order

    int r = a.length - 1;
    int s = j + 1;

    while (r > s) {
      temp = a[s];
      a[s] = a[r];
      a[r] = temp;
      r--;
      s++;
    }

    numLeft = numLeft.subtract (BigInteger.ONE);
    return a;

  }

}
