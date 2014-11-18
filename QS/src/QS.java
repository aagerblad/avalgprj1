import java.math.BigInteger;
import java.util.BitSet;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created with IntelliJ IDEA.
 * User: tobbew92
 * Date: 17/11/14
 * Time: 20:56
 * To change this template use File | Settings | File Templates.
 */


public class QS {

    private BigInteger N;
    private BigInteger sqrtN;
    private int B;

    //private final int OFFSET = 5; // So we get linear relations
    private final int SMOOTH_OFFSET = 1;
    private int SIEVE_INTERVAL;

    LinkedHashSet<Integer> primes;
    LinkedHashSet<Integer> factorBase;
    LinkedHashSet<BigInteger> smoothNumbers;

    int[]      sieveSolution;
    int[]      sievePrimeOffset;
    float[]    sievePrimeLog;
    float[]    primeLogValues;


    public QS(BigInteger N) {
        this.N = N;
        sqrtN = BigMath.sqrt(N);
        primes = new LinkedHashSet<Integer>();
        factorBase = new LinkedHashSet<Integer>();
        SIEVE_INTERVAL = (int) Math.pow(N.bitLength() / 10, 5);
    }

    // Get "optimal" B, using function from course page
    public void calculateBase() {
        double lg = 2 * Math.log(sqrtN.longValue());
        double lglg = lg * Math.log(lg);
        double exponent = 0.5 * Math.sqrt(lglg);
        double v = Math.pow(Math.E, exponent);
        int b = (int) Math.round(v);
        B = b; //TODO maybe add scale factor for B

    }

    public void calculateFactorBase() {
        sieveOfEra();
        //factorBase.add(2);
        System.out.println("B = " + B);
        System.out.print("Factor base = ");
        for (int prime : primes) {

            int quadraticResidue = BigMath.legendre(N, new BigInteger("" + prime));
            if (quadraticResidue == 1) {
                System.out.print(prime + " ");
                factorBase.add(prime);
            }

        }
        System.out.println();
    }

    // Generate primes up to B
    private void sieveOfEra() {
        int upperBoundSquareRoot = (int) Math.sqrt(B);
        boolean[] candidates = new boolean[B + 1];
        for (int m = 2; m <= upperBoundSquareRoot; m++) {
            if (!candidates[m]) {
                primes.add(m);
                for (int k = m * m; k <= B; k += m)
                    candidates[k] = true;
            }
        }
        for (int m = upperBoundSquareRoot; m <= B; m++)
            if (!candidates[m])
                primes.add(m);
    }


    // Initialized outside since they are used in recursion, don't want to overload the stack
    byte[] smoothRow;
    Integer[] fBaseArray;

    public boolean generateSmoothNumbers(BitSet[] matrix) {
        int sieveSize;
        if (factorBase.contains(2)) {
            sieveSize = factorBase.size() * 2 - 1;
        } else {
            sieveSize = factorBase.size() * 2;
        }

        // Many arrays storing information for reach factor base position
        sievePrimeLog = new float[sieveSize];
        sievePrimeOffset = new int[sieveSize];
        sieveSolution = new int[sieveSize];
        primeLogValues = new float[sieveSize];

        int pos = 0;
        // Solve the quadratic congruent equation for every factor in the factor base
        for (int prime : factorBase) {
            if (prime == 2) { //Special case for 2
                sievePrimeOffset[pos] = prime;
                sievePrimeLog[pos] = (float) Math.log(prime);
                BigInteger Q = sqrtN.multiply(sqrtN).subtract(N);

                if (Q.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
                    sieveSolution[pos] = 0;
                else
                    sieveSolution[pos] = 1;

                primeLogValues[pos] = (float) Math.log(prime);

                pos++;
            } else {
                int[] xArray = BigMath.shanksTonelli(N, BigInteger.valueOf(prime));
                float logPrime = (float) Math.log(prime);
                for (int x : xArray) {  // loops twice max
                    sievePrimeOffset[pos] = prime;
                    sievePrimeLog[pos] = logPrime;
                    sieveSolution[pos] = x;
                    primeLogValues[pos] = (float) Math.log(prime);
                    pos++;
                }
            }
        }


        smoothNumbers = new LinkedHashSet<BigInteger>(factorBase.size() + SMOOTH_OFFSET);

        float[] qValues = new float[SIEVE_INTERVAL];

        initQValues(0, qValues, N);

        smoothRow = new byte[factorBase.size()];
        fBaseArray =  factorBase.toArray(new Integer[factorBase.size()]);

        // Returns false if it fails.
        return sieve(0, smoothNumbers, qValues, matrix);
    }



    private boolean sieve(long offset, LinkedHashSet<BigInteger> smoothingNumbers, float[] qLogValues, BitSet[] matrix) {

        int baseSize = factorBase.size();
        // iterate through the solutions 'x' and 'y'
        for (int i = 0; i < sieveSolution.length; i++) {

            long x = sieveSolution[i];
            int prime = sievePrimeOffset[i];
            while (x < offset + qLogValues.length) {
                sieveSolution[i] += prime;
                int relativeX = (int) (x - offset);

                if (qLogValues[relativeX] < Float.POSITIVE_INFINITY) {
                    qLogValues[relativeX] -= primeLogValues[i];

                    if (qLogValues[relativeX] < (float) Math.log(fBaseArray[fBaseArray.length - 1])) {
                        BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(x))).multiply(sqrtN.add(BigInteger.valueOf(x)))).subtract(N);
                        long q = 0;
                        boolean useLong = false;
                        for (int col = 0; col < baseSize; col++) {

                            if (useLong || qValue.bitLength() < 64) {
                                if(!useLong){
                                    q = qValue.longValue();
                                    useLong = true;
                                }

                                smoothRow[col] = 0;
                                long p = fBaseArray[col];

                                while (q % p == 0) {
                                    q = q / p;
                                    smoothRow[col] = (byte) (smoothRow[col] == 1 ? 0 : 1);
                                }
                                if (q == 1) {
                                    matrix[smoothingNumbers.size()] = Gauss.byteArrayToBitSet(smoothRow);
                                    smoothRow = new byte[smoothRow.length];
                                    smoothingNumbers.add(BigInteger.valueOf(x));
                                    break;
                                }
                            } else {
                                smoothRow[col] = 0;
                                long p = fBaseArray[col];

                                BigInteger largePrime = BigInteger.valueOf(p);
                                BigInteger[] qr = qValue.divideAndRemainder(largePrime);
                                while (qr[1].equals(BigInteger.ZERO)) {
                                    qValue = qr[0];
                                    smoothRow[col] = (byte) (smoothRow[col] == 1 ? 0 : 1);
                                    qr = qValue.divideAndRemainder(largePrime);
                                }
                                if (qValue.equals(BigInteger.ONE)) {
                                    matrix[smoothingNumbers.size()] = Gauss.byteArrayToBitSet(smoothRow);
                                    smoothRow = new byte[smoothRow.length];
                                    smoothingNumbers.add(BigInteger.valueOf(x));
                                    break;
                                }
                            }
                        }


                        if (smoothingNumbers.size() >= baseSize + SMOOTH_OFFSET) {
                            return true;
                        }

                        qLogValues[relativeX] = Float.POSITIVE_INFINITY;
                    }
                }


                x = sieveSolution[i];
            }
        }

        long newOffsetX = offset + qLogValues.length;
        initQValues(newOffsetX, qLogValues, N);
        return sieve(newOffsetX, smoothNumbers, qLogValues, matrix);

    }

    // TODO kolla upp vad ddetta är
    private void initQValues(long offsetX, float[] qLogValues, BigInteger N) {
        BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(offsetX + qLogValues.length - 1))).multiply(sqrtN.add(BigInteger.valueOf(offsetX + qLogValues.length - 1)))).subtract(N);
        float qValueForLastX = (float) Math.log(qValue.doubleValue());
        for (int x = 0; x < qLogValues.length; x++) {
            qLogValues[x] = qValueForLastX;
        }
    }

    public void setB(int b) {
        B = b;
    }

    public int getB() {
        return B;
    }

    public LinkedHashSet<Integer> getFactorBase() {
        return factorBase;
    }
}
