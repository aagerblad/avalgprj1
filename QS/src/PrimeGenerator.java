import java.math.BigInteger;

/**
 * Created by Andreas on 2014-11-18.
 */
public class PrimeGenerator {

    public static void main(String[] args) {


        long l = 3;
        int c = 1;
        while(l < 500000) {
            l += 2;

            if (BigInteger.valueOf(l).isProbablePrime(100)) {
                System.err.print(" " + l);
                c += 1;
            }

            if (c > 20) {
//                System.err.println("");
                c = 1;
            }
        }
    }
}
