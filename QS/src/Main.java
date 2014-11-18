/**
 * Created by Andreas on 2014-11-17.
 */
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.BitSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        //Test.testLegendre();
//        Test.testPollardBrent();
        BigInteger n = new BigInteger("92070546547344938001");
//        Test.testPollardBrent(n);
        Test.testQS();
//        try {
//            n = Test.testTrialDivision(n);
//
//            System.err.println("Pollard!!------\n");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


//        BigInteger N = new BigInteger("100000");
//
//        BigInteger B = calcBase(N);
//
//        LinkedList<BigInteger> legendrePrimes = new LinkedList<BigInteger>();
//        long[] primes = Primes.getPrimes();
//        for (int i = 0; i < primes.length; i++) {
//            BigInteger p = new BigInteger(String.valueOf(primes[i]));
//            if (B.compareTo(p) > 0)
//                break;
//
//            if (BigMath.legendre(B, p) == 1)
//                legendrePrimes.add(p);
//        }
//
//        int numOfLP = legendrePrimes.size();
//        LinkedList<BigInteger> smoothNumbers = new LinkedList<BigInteger>();
//        while(smoothNumbers.size() < numOfLP) {
//            //TODO find smooth numbers
//        }
//
//        BitSet[] matrix = initializeMatrix(numOfLP, legendrePrimes, smoothNumbers);
//
//        BigInteger[] squares = findSquares(matrix);

    }

    private static BigInteger[] findSquares(BitSet[] matrix) {
        BitSet foo = new BitSet();
//        foo.set

        return new BigInteger[1];
    }

    public static BitSet[] initializeMatrix(int numOfLP, LinkedList<BigInteger> legendrePrimes,
                                             LinkedList<BigInteger> smoothNumbers) {

        BitSet[] matrix = new BitSet[numOfLP];

        for (int i = 0; i < matrix.length; i++){
            BitSet row = new BitSet(numOfLP);
            for (int j = 0; j < legendrePrimes.size(); j++) {
                row.set(j, BigMath.unevenDivider(smoothNumbers.get(i), legendrePrimes.get(j)));
            }
            matrix[i] = row;
        }

        return matrix;
    }


    //TODO
    private static BigInteger calcBase(BigInteger n) {
        return null;
    }
}
