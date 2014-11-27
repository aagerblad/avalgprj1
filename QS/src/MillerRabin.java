import java.math.BigInteger;

import java.util.Random;
import java.math.BigInteger;

public class MillerRabin
{

    private static boolean millerrabin(BigInteger n) {

        if (n.compareTo(new BigInteger("3")) == 0)
            return true;

        //if n.compareTo()

        Random r = new Random();
        BigInteger temp;

        do {
            temp = new BigInteger(n.bitLength()-1, r);
        } while (temp.compareTo(BigInteger.ONE) <= 0);

        if (!BigMath.gcd(temp, n).equals(BigInteger.ONE)) return false;


        BigInteger base = n.subtract(BigInteger.ONE);

        // largest power of 2 that divides n-1
        int k=0;
        while ( (base.mod(new BigInteger("2"))).equals(BigInteger.ZERO)) {
            base = base.divide(new BigInteger("2"));
            k++;
        }

        BigInteger current = temp.modPow(base,n);

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

    public static boolean isPrime(BigInteger n, int numTimes) {

        for (int i=0; i<numTimes; i++)
            if (!millerrabin(n)) return false;

        return true;
    }
}
