import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.LinkedList;

/**
 * Created by Andreas on 2014-11-18.
 */
public class TrialDivision {

    public static LinkedList<BigInteger> trialDivision(BigInteger n) throws FileNotFoundException {

        FileInputStream file = new FileInputStream("/Users/tobbew92/Documents/workspace/avalgprj1/QS/src/Primenumbers");
        Kattio io = new Kattio(file);

        LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
        while (io.hasMoreTokens()) {
            long p = io.getLong();
            while (isDivisible(n, BigInteger.valueOf(p))) {
                n = n.divide(BigInteger.valueOf(p));
                factors.add(BigInteger.valueOf(p));
            }

            if (n.compareTo(BigInteger.ONE) == 0) {
                return factors;
            }
        }

        return factors;
    }

    public static boolean isDivisible(BigInteger isThisNumberDivisible, BigInteger byThisNumber)
    {
        return isThisNumberDivisible.mod(byThisNumber).equals(BigInteger.ZERO);
    }

    public static BigInteger divide(BigInteger n, LinkedList<BigInteger> factors) {
        for (BigInteger f : factors)
            n = n.divide(f);
        return n;
    }
}
