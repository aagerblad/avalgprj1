/**
 * Created by Andreas on 2014-11-17.
 */
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.BitSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        //Test.testLegendre();
        Test.testQS();

        int rows = 10; int columns = 10;

        Random r = new Random();
        BitSet[] matrix = new BitSet[rows];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new BitSet(columns);
            matrix[i].set(r.nextInt(10));
//            matrix[i].set(9);
        }


        System.err.println("Old matrix");
        Gauss.printMatrix(matrix, rows, columns);

        matrix = Gauss.gaussEliminate(matrix, rows, columns);

        System.err.println("Gaussed");
        Gauss.printMatrix(matrix, rows, columns);

        BitSet freeVar = Gauss.getFreeVariables(matrix, rows, columns);
        System.err.println("Free variables: ");
        Gauss.printBitSet(freeVar, columns);

        BitSet nullspace = null;
        while (true) {
            nullspace = Gauss.calcNullSpace(matrix, rows, columns, freeVar, nullspace);
            System.err.println("Null space vector: ");
            Gauss.printBitSet(nullspace, columns);
            if (nullspace.isEmpty()) {
                break;
            }
        }

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
