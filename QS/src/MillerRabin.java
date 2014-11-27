import java.math.BigInteger;

import java.util.Random;
import java.math.BigInteger;

public class MillerRabin
{
    public static boolean isPrime(BigInteger n, int iterations) {

        for (int i = 0; i < iterations; i++)
            if (!millerrabin(n))
                return false;

        return true;
    }

    private static boolean millerrabin(BigInteger n) {

        if (n.compareTo(new BigInteger("3")) == 0)
            return true;

        //if n.compareTo()

        Random r = new Random();
        BigInteger a;

        do {
            a = new BigInteger(n.bitLength()-1, r);     //randomize
        } while (a.compareTo(BigInteger.ONE) <= 0);

        if (!BigMath.gcd(a, n).equals(BigInteger.ONE))
            return false;


        BigInteger base = n.subtract(BigInteger.ONE);

        // largest power of 2 that divides n-1
        int k=0;
        while ( (base.mod(new BigInteger("2"))).equals(BigInteger.ZERO)) {
            base = base.divide(new BigInteger("2"));
            k++;
        }

        BigInteger current = a.modPow(base,n);

        if (current.equals(BigInteger.ONE))
            return true;

        for (int i=0; i < k; i++) {
            if (current.equals(n.subtract(BigInteger.ONE)))
                return true;
            else
                current = current.modPow(new BigInteger("2"), n);
        }

        return false;
    }

}
