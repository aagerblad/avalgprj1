import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Andreas on 2014-11-18.
 */
public class PollardBrent {

    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE  = new BigInteger("1");
    private final static BigInteger TWO  = new BigInteger("2");

    public static BigInteger rho(BigInteger N) {
        BigInteger divisor;

        Random random = new Random();
        BigInteger c  = new BigInteger(N.bitLength(), random);
        BigInteger x  = new BigInteger(N.bitLength(), random);
        BigInteger y = x;

        // check divisibility by 2
        if (N.mod(TWO).compareTo(ZERO) == 0) return TWO;

        int count = 0;
        divisor = ONE;

        long t = System.currentTimeMillis();
        int iterations = 1;
        do {
            iterations++;
            x = g(x, c, N);
            y = g(y, c, N);
            y = g(y, c, N);

            count++;
            if (count > 10) {
                count = 0;
                divisor = BigMath.gcd(x.subtract(y), N);
            }
//            divisor = x.subtract(y).gcd(N);
        } while((divisor.compareTo(ONE)) == 0);

        long t2 = System.currentTimeMillis()-t;

        System.out.println("Time: " + t2);
        System.out.println("Iterations: " + iterations);

        if (t2 != 0)
            System.out.println("I/T: " + (iterations/t2));

        return divisor;
    }

    public static LinkedList<BigInteger> factor(BigInteger N) {

        LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
        LinkedList<BigInteger> queue = new LinkedList<BigInteger>();

        queue.add(N);

        while (!queue.isEmpty()) {
            BigInteger n = queue.pop();


            if (n.compareTo(ONE) == 0)
                continue;

            if (n.isProbablePrime(20)) {
//            if (MillerRabin.isPrime(N, 20)) {
//                System.out.println("Found factor: " + n.toString());
                factors.add(n);
                continue;
            }

            BigInteger divisor = rho(n);

            queue.add(divisor);
            queue.add(n.divide(divisor));

//            factors.addAll(factor(divisor));
//            factors.addAll(factor(N.divide(divisor)));

        }




        return factors;
    }


    public static void main(String[] args) {
        BigInteger N = new BigInteger(args[0]);
        factor(N);
    }


//    public static LinkedList<BigInteger> findAllFactors(BigInteger n) {
//        LinkedList<BigInteger> q = new LinkedList<BigInteger>();
//        LinkedList<BigInteger> factors = new LinkedList<BigInteger>();
//        if (n.compareTo(BigInteger.ONE) == 0) {
//            factors.add(n);
//            return factors;
//        }
//        q.add(n);
//
//        while (!q.isEmpty()) {
//            BigInteger cn = q.pop();
////            if (MillerRabin.isPrime(cn))
//            if (cn.isProbablePrime(10)) {
//                factors.add(cn);
//                continue;
//            }
//            BigInteger factor = new BigInteger("1");
//            BigInteger x_0 = new BigInteger("2");
//            while (factor.compareTo(BigInteger.valueOf(1)) == 0  || factor.compareTo(cn) == 0) {
//                System.err.println("Updating x_0");
//                factor = PollardBrent.pollardBrent(cn, x_0);
//                x_0.add(BigInteger.valueOf(1));
//            }
//
//            q.add(factor);
//            q.add(cn.divide(factor));
//
//        }
//        return factors;
//    }
//
//    private static BigInteger pollardBrent(BigInteger N, BigInteger x) {
//        if (N.compareTo(BigInteger.ONE) == 0)
//            return N;
//        BigInteger x_fixed;
//        int cycle_size = 100;
//
//        BigInteger y = nextRandomBigInteger(N);
//        BigInteger c = nextRandomBigInteger(N);
//        BigInteger m = nextRandomBigInteger(N);
//
//        BigInteger g = new BigInteger("1");
//        BigInteger r = new BigInteger("1");
//        BigInteger q = new BigInteger("1");
//        BigInteger ys = y;
//
//        while (g.compareTo(new BigInteger("1")) == 0) {
//            x = y;
//            for (int i = 0; r.compareTo(BigInteger.valueOf(i)) > 0; i++) {
//                y = g(y, N);
//            }
//
//            BigInteger k = BigInteger.ZERO;
//            while (k.compareTo(r) < 0 && g.compareTo(BigInteger.ONE) == 0) {
//                ys = y;
//                for (int i = 0; i < Math.min(m.longValue(), r.subtract(k).longValue()); i++) {
//                    y = g(y, N);
//                    q = q.multiply(x.subtract(y).abs()).mod(N);
//                }
//                g = BigMath.gcd(q, N);
//                k = k.add(m);
//            }
//            r = r.multiply(BigInteger.valueOf(2));
//
//        }
//
//        if (g.compareTo(N) == 0) {
//            while (true) {
//                ys = g(ys, N);
//                g = BigMath.gcd(x.subtract(ys).abs(), N);
//                if (g.compareTo(BigInteger.ONE) > 0)
//                    break;
//            }
//        }
//
//        return g;
//    }
//
//    public static BigInteger nextRandomBigInteger(BigInteger n) {
//        Random rand = new Random();
//        BigInteger result = new BigInteger(n.bitLength(), rand);
//        while( result.compareTo(n) >= 0 ) {
//            result = new BigInteger(n.bitLength(), rand);
//        }
//        return result;
//    }

    private static BigInteger g(BigInteger x, BigInteger c, BigInteger n) {
        // (x * x + 1) mod n
        return  x.multiply(x).add(c).mod(n);
    }
}
