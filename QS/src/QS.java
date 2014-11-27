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

    //private final int OFFSET = 5; // So we get linear relations, minimum 1
    private final int SMOOTH_OFFSET = 1;
    private int SIEVE_INTERVAL;

    LinkedHashSet<Integer> primes;
    LinkedList<Integer> factorBase;
    LinkedList<BigInteger> smoothNumbers;


    public QS(BigInteger N) {
        this.N = N;
        sqrtN = BigMath.sqrt(N);
        primes = new LinkedHashSet<Integer>();
        factorBase = new LinkedList<Integer>();
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
        B = b * 1; //TODO maybe add scale factor for B
        SIEVE_INTERVAL = B;
        //B = 50000;

    }

    public void calculateFactorBase() {
        sieveOfEra();
        System.out.println("B = " + B);
        //System.out.print("Factor base = ");
        for (int prime : primes) {

            int quadraticResidue = BigMath.legendre(N, new BigInteger("" + prime));
            if (quadraticResidue == 1) {
                //System.out.print(prime + " ");
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
    int[]      sieveSolution;
    int[]      sievePrimeOffset;
    float[]    sievePrimeLog;
    float[]    primeLogValues;

    public boolean generateSmoothNumbers(BitSet[] matrix) {
        int sieveSize;
        if (factorBase.get(0) == 2)
            sieveSize = factorBase.size() * 2 - 1;
         else
            sieveSize = factorBase.size() * 2;


        // Many arrays storing information for reach factor base position
        sievePrimeLog = new float[sieveSize];
        sievePrimeOffset = new int[sieveSize];
        sieveSolution = new int[sieveSize];
        primeLogValues = new float[sieveSize];

        int i = 0;
        // Solve the quadratic congruent equation for every factor in the factor base
        for (int prime : factorBase) {
            if (prime == 2) { //Special case for 2
                sievePrimeOffset[i] = prime;
                sievePrimeLog[i] = (float) Math.log(prime);
                BigInteger Q = sqrtN.multiply(sqrtN).subtract(N);

                if (Q.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
                    sieveSolution[i] = 0;
                else
                    sieveSolution[i] = 1;

                primeLogValues[i] = (float) Math.log(prime);

                i++;
                //continue;
            } else {
                int[] congruenceSolutions = BigMath.shanksTonelli(N, BigInteger.valueOf(prime));
                float logPrime = (float) Math.log(prime);

                for (int x : congruenceSolutions) {  // loops twice max
                    sievePrimeOffset[i] = prime; sievePrimeLog[i] = logPrime;
                    sieveSolution[i] = x; primeLogValues[i] = (float) Math.log(prime);
                    i++;
                }
            }
        }


        smoothNumbers = new LinkedList<BigInteger>();

        float[] qValues = new float[SIEVE_INTERVAL];

        // Get Q(x)
        BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(qValues.length - 1))).multiply(sqrtN.add(BigInteger.valueOf(qValues.length - 1)))).subtract(N);
        float qValueForLastX = (float)BigMath.log(qValue);
        for (int x = 0; x < qValues.length; x++) {
            qValues[x] = qValueForLastX;
        }

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



    private boolean sieve(LinkedList<BigInteger> smoothingNumbers, float[] logsQ, BitSet[] matrix) {
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
                //long hejsan = offset + logsQ.length;
                //t = System.currentTimeMillis();
                while (x < offset + logsQ.length) {
                    sieveSolution[i] += prime;
                    //System.out.println("Offset: " + x);
                    //System.out.println("x: " + offset);

                    int xNew = (int) (x - offset);

                    logsQ[xNew] -= primeLogValues[i];

                    if (logsQ[xNew] < (float) Math.log(fBaseArray[fBaseArray.length - 1])) {
                        //System.out.println(logsQ[t xNew]);
                        //System.out.println(logsQ[t xNew]);

                        BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(x))).multiply(sqrtN.add(BigInteger.valueOf(x)))).subtract(N);

                        for (int col = 0; col < baseSize; col++) {

                            smoothRow[col] = 0;
                            long p = fBaseArray[col];


                            //System.out.println(p);
                            //System.out.println(qValue);
                            BigInteger largePrime = BigInteger.valueOf(p);
                            BigInteger[] qr = qValue.divideAndRemainder(largePrime);   //ret 2 values
                            if (logsQ[xNew] < 0.2) {

                                // System.out.println(logsQ[t xNew]);
                            }
                            //System.out.println("Sieve time: " + (System.currentTimeMillis() - t));
                            while (qr[1].equals(BigInteger.ZERO)) {
                                //System.out.println("bajs");
                                qValue = qr[0];
                                smoothRow[col] = (byte) (smoothRow[col] == 1 ? 0 : 1);
                                qr = qValue.divideAndRemainder(largePrime);
                            }
                            //System.out.println(qValue);
                            // TODO får man göra sådär? med logsQ < 1?????????????????????       t.ex.
                            // ta en närmare titt på detta
                            if (qValue.equals(BigInteger.ONE) || logsQ[xNew] < 1) {
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
                        }

                        logsQ[xNew] = Float.POSITIVE_INFINITY;
                        //System.out.println("HEJSAN");

                    }


                    x = sieveSolution[i];
                }
            }

            offset = offset + logsQ.length;
            // Get the Q(x)
            BigInteger qValue = ((sqrtN.add(BigInteger.valueOf(offset + logsQ.length - 1))).multiply(sqrtN.add(BigInteger.valueOf(offset + logsQ.length - 1)))).subtract(N);
            float qValueForLastX = (float)BigMath.log(qValue);
            for (int x = 0; x < logsQ.length; x++) {
                logsQ[x] = qValueForLastX;
            }

            //System.out.println("Sieve time: " + (System.currentTimeMillis() - t));

        }
        return true;
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
