/**
 * Created by Andreas on 2014-11-17.
 */
import java.math.BigInteger;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {


        BigInteger N = new BigInteger("100000");

        BigInteger B = calcBase(N);

        LinkedList<BigInteger> legendrePrimes = new LinkedList<BigInteger>();
        long[] primes = Primes.getPrimes();
        for (int i = 0; i < primes.length; i++) {
            BigInteger p = new BigInteger(String.valueOf(primes[i]));
            if (B.compareTo(p) > 0)
                break;

            if (BigMath.legendre(B, p) == 1)
                legendrePrimes.add(p);
        }

    }

    //TODO
    private static BigInteger calcBase(BigInteger n) {
        return null;
    }
}
