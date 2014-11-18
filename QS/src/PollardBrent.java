import java.math.BigInteger;

/**
 * Created by Andreas on 2014-11-18.
 */
public class PollardBrent {

    public static BigInteger pollardBrent(BigInteger N) {
        BigInteger x_fixed = new BigInteger("2");
        int cycle_size = 2;
        BigInteger x = new BigInteger("2");
        BigInteger h = new BigInteger("1");

        while (h.compareTo(new BigInteger("1")) == 0) {
            int count = 1;
            int brentCounter = 1;
            while (count <= cycle_size && h.compareTo(new BigInteger("1")) == 0) {
                x = g(x, N);
                count++;
                brentCounter++;
                if (brentCounter > 100) {
                    h = BigMath.gcd(x.subtract(x_fixed), N);
                    brentCounter = 1;
                }
            }

            cycle_size *= 2;
            x_fixed = x;
        }

        return h;
    }

    private static BigInteger g(BigInteger x, BigInteger n) {
        // (x * x + 1) mod n
        return  x.multiply(x).add(new BigInteger("1")).mod(n);
    }
}
