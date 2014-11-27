import java.math.BigInteger;

import java.util.Scanner;
import java.util.Random;
import java.math.BigInteger;

public class MillerRabin
{

    public static boolean isPrime(BigInteger n, int iterations)
    {
        if (n.compareTo(new BigInteger("0")) ==  0 || n.compareTo(new BigInteger("1")) == 0)
            return false;

        if (n.compareTo(new BigInteger("2")) == 0 || n.compareTo(new BigInteger("5")) ==  0 || n.compareTo(new BigInteger("7")) == 0)
            return true;

        if (n.mod(new BigInteger("2")).compareTo(new BigInteger("0")) == 0)
            return false;

        BigInteger s = n.subtract(new BigInteger("1"));

        while (s.mod(new BigInteger("2")).compareTo(new BigInteger("0")) == 0)
            s = s.divide(new BigInteger("2"));

        for (int i = 0; i < iterations; i++)
        {
            BigInteger a = new BigInteger(n.bitLength(), new Random());
            a = a.mod(n.subtract(new BigInteger("1")).add(new BigInteger("1")));
            BigInteger temp = s;

            BigInteger mod = a.modPow(temp, n);
            while (temp.compareTo(n.subtract(new BigInteger("1"))) != 0 && mod.compareTo(new BigInteger("1")) != 0 && mod.compareTo(n.subtract(new BigInteger("1"))) != 0) {
                mod = mulMod(mod, mod, n);
                temp = temp.multiply(new BigInteger("2"));
            }


            if (mod.compareTo(n.subtract(new BigInteger("1"))) != 0 && temp.mod(new BigInteger("2")).compareTo(new BigInteger("0")) == 0) {
                    return false;
               }

        }
        return true;
    }

    private static BigInteger mulMod(BigInteger a, BigInteger b, BigInteger mod)
    {
        return a.multiply(b).mod(mod);
    }
}
