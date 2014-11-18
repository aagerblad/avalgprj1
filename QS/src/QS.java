import java.math.BigInteger;
import java.util.*;

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
    LinkedList<Integer> factorBase;
    LinkedList<BigInteger> smoothNumbers;

    int[]      sieveSolution;
    int[]      sievePrimeOffset;
    float[]    sievePrimeLog;
    float[]    primeLogValues;


    public QS(BigInteger N) {
        this.N = N;
        sqrtN = BigMath.sqrt(N);
        primes = new LinkedHashSet<Integer>();
        factorBase = new LinkedList<Integer>();
        SIEVE_INTERVAL = (int) Math.pow(N.bitLength() / 10, 5);
    }

    // Get "optimal" B, using function from course page
    public void calculateBase() {
        double lg = 2 * BigMath.log(sqrtN);
        System.out.println("lg: " + lg);
        double lglg = lg * Math.log(lg);
        System.out.println("lglg: " + lglg);
        double exponent = 0.5 * Math.sqrt(lglg);
        System.out.println("exp: " + exponent);
        double v = Math.pow(Math.E, exponent);
        System.out.println("v: " + v);
        int b = (int) Math.round(v);
        B = b * 10; //TODO maybe add scale factor for B
        //B = 300000;

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


        smoothNumbers = new LinkedList<BigInteger>();

        float[] qValues = new float[SIEVE_INTERVAL];

        initQValues(0, qValues, N);

        smoothRow = new byte[factorBase.size()];

        fBaseArray =  factorBase.toArray(new Integer[factorBase.size()]);

        //int i = 0;
        //for (Integer f : factorBase) {
            //System.out.println("factorBase: " + f);
            //System.out.println("Fbase: " + fBaseArray[i]);

          //  i++;
        //}

        // Returns false if it fails.
        return sieve(smoothNumbers, qValues, matrix);
    }

    public int getSmoothNumberSize() {
        return factorBase.size() + SMOOTH_OFFSET;
    }



    private boolean sieve(LinkedList<BigInteger> smoothingNumbers, float[] qLogValues, BitSet[] matrix) {
        System.out.println("Sieving...");
        long offset = 0;
        int baseSize = factorBase.size();
        boolean continueSieving = true;
        long t = 0;
        stop:
        while (continueSieving)   {

        t = System.currentTimeMillis();

            // iterate through the solutions 'x' and 'y'
            for (int i = 0; i < sieveSolution.length; i++) {

            long x = sieveSolution[i];
                int prime = sievePrimeOffset[i];
                //long hejsan = offset + qLogValues.length;
                //t = System.currentTimeMillis();
                while (x < offset + qLogValues.length) {
                    sieveSolution[i] += prime;
                    //System.out.println("Offset: " + x);
                    //System.out.println("x: " + offset);

                    int relativeX = (int) (x - offset);

                    //if (qLogValues[relativeX] < Float.POSITIVE_INFINITY) {
                        qLogValues[relativeX] -= primeLogValues[i];

                        if (qLogValues[relativeX] < (float) Math.log(fBaseArray[fBaseArray.length - 1])) {
                            //System.out.println(qLogValues[relativeX]);
                            //System.out.println(qLogValues[relativeX]);

                            BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(x))).multiply(sqrtN.add(BigInteger.valueOf(x)))).subtract(N);

                            for (int col = 0; col < baseSize; col++) {

                                    smoothRow[col] = 0;
                                    long p = fBaseArray[col];


                                //System.out.println(p);
                                //System.out.println(qValue);
                                BigInteger largePrime = BigInteger.valueOf(p);
                                BigInteger[] qr = qValue.divideAndRemainder(largePrime);   //ret 2 values
                                if (qLogValues[relativeX] < 0.2) {

                                   // System.out.println(qLogValues[relativeX]);
                                }
                                //System.out.println("Sieve time: " + (System.currentTimeMillis() - t));
                                while (qr[1].equals(BigInteger.ZERO)) {
                                        //System.out.println("bajs");
                                        qValue = qr[0];
                                        smoothRow[col] = (byte) (smoothRow[col] == 1 ? 0 : 1);
                                        qr = qValue.divideAndRemainder(largePrime);
                                    }
                                //System.out.println(qValue);
                                // TODO får man göra sådär? med qLogValues < 1?????????????????????
                                    if (qValue.equals(BigInteger.ONE)) {
                                        matrix[smoothingNumbers.size()] = Gauss.byteArrayToBitSet(smoothRow);
                                        smoothRow = new byte[smoothRow.length];
                                        System.out.println("added smmoth: " + smoothingNumbers.size());
                                        smoothingNumbers.add(BigInteger.valueOf(x));
                                        break;
                                    }

                            }


                            if (smoothingNumbers.size() >= baseSize + SMOOTH_OFFSET) {
                                continueSieving = false;
                                break stop;
                                //return true;
                            }

                            qLogValues[relativeX] = Float.POSITIVE_INFINITY;
                            //System.out.println("HEJSAN");
                        //} else {
                        //}
                    }


                    x = sieveSolution[i];
                }
            }

            offset = offset + qLogValues.length;
            initQValues(offset, qLogValues, N);
            //System.out.println("Sieve time: " + (System.currentTimeMillis() - t));

        }
       // return sieve(smoothNumbers, qLogValues, matrix);
       return true;
    }

    // TODO kolla upp vad ddetta är
    private void initQValues(long offsetX, float[] qLogValues, BigInteger N) {
        //System.out.println(qLogValues[0]);
        BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(offsetX + qLogValues.length - 1))).multiply(sqrtN.add(BigInteger.valueOf(offsetX + qLogValues.length - 1)))).subtract(N);
        float qValueForLastX = (float)BigMath.log(qValue);
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

    public LinkedList<Integer> getFactorBase() {
        return factorBase;
    }

    public LinkedList<BigInteger> getSmoothNumbers() {
        return smoothNumbers;
    }
}
