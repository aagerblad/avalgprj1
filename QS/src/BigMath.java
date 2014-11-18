import java.math.BigInteger;

public class BigMath {

	public static BigInteger gcd(BigInteger a, BigInteger b) //valid for positive integers.
	{
		while (b.compareTo(new BigInteger("0")) == 1) {
			BigInteger c = a.mod(b);
			a = b;
			b = c;
		}
		return a;
	}

    public static int legendre(BigInteger a, BigInteger p) {
        if (a.remainder(p).equals(BigInteger.ZERO)) {
            return 0;
        }

        BigInteger exponent = p.subtract(BigInteger.ONE);
        exponent = exponent.divide(BigInteger.valueOf(2));
        BigInteger result = a.modPow(exponent, p); // 1 <= result <= p - 1

        if (result.equals(BigInteger.ONE)) {
            return 1;
        } else if (result.equals(p.subtract(BigInteger.ONE))) {
            return -1;
        } else {
            throw new ArithmeticException("Error computing the Legendre symbol.");
        }
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
            else a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }

    // TODO funkar denna verkligen?
    public static int[] shanksTonelli(BigInteger N, BigInteger p) {
        int prime = p.intValue();
        int Q = prime - 1;
        int S = 0;


        while (Q % 2 == 0) {
            S++;
            Q = Q / 2;
        }
        BigInteger R;
        if (S == 1) {     // p was 3, cool cool.
            R = N.modPow(BigInteger.valueOf((prime + 1) / 4), BigInteger.valueOf(prime));
        } else {

            int residue = 0;
            int Z = 2;
            while (true) {
                residue = legendre(BigInteger.valueOf(Z), new BigInteger("" + prime));
                if (residue == -1) {
                    break;
                }
                Z++;
            }

            BigInteger C = BigInteger.valueOf(Z).modPow(BigInteger.valueOf(Q), BigInteger.valueOf(prime));

            R = N.modPow(BigInteger.valueOf((Q + 1) / 2), BigInteger.valueOf(prime));

            BigInteger t = N.modPow(BigInteger.valueOf(Q), BigInteger.valueOf(prime));

            int M = S;

            while (t.compareTo(BigInteger.ONE) == 1) {
                int temp = 1;
                int i = 1;
                while (i < M) {
                    temp *= 2;

                    if (t.modPow(BigInteger.valueOf(temp), BigInteger.valueOf(prime)).equals(BigInteger.ONE)) {
                        break;
                    }
                    i++;
                }
                if (i == M && i != 1) {
                    i--;
                }


                BigInteger b = C.modPow(BigInteger.valueOf((long) Math.pow(2, M - i - 1)), BigInteger.valueOf(prime));
                R = R.multiply(b).mod(BigInteger.valueOf(prime));
                t = t.multiply(b).multiply(b).mod(BigInteger.valueOf(prime));
                C = b.multiply(b).mod(BigInteger.valueOf(prime));
                M = i;
            }
        }

        int[] result = new int[]{ R.intValue(), prime - R.intValue() };
        BigInteger sqrtN = sqrt(N);
        for (int i = 0; i < 2; i++) {
            result[i] = (int) ((result[i] - sqrtN.longValue()) % prime);
            if (result[i] < 0) {
                result[i] += prime;
            }
        }

        return result;
    }

    public static BigInteger OLDshanksTonelli(BigInteger N, BigInteger p) {
        BigInteger y, b, t;
        BigInteger retValue;
        int m;
        int s = 0;

        if (BigMath.divisible(N, p)) {
            retValue = new BigInteger("0");
            return retValue;
        }

        if (BigMath.legendre(N, p) == -1) {
            return null;
        }

        y = new BigInteger("2");

        BigInteger ONE = new BigInteger("1");
        while (BigMath.legendre(y, p) != -1)
            y = y.add(ONE);

        retValue = p.subtract(ONE);

        while (retValue.testBit(s))
            s++;
        retValue = retValue.shiftRight(s);

        y = y.modPow(retValue, p);

        retValue.shiftRight(1);
        b = N.modPow(retValue, p);

        retValue = N.multiply(b);

        retValue = retValue.mod(p);

        b = retValue.multiply(b);
        b = b.mod(p);

        while (b.compareTo(ONE) == 1) {
            t = b.multiply(b);
            t = t.mod(p);

            for (m = 1; t.compareTo(ONE) == 1; m++) {
                t = t.multiply(t);
                t = t.mod(p);
            }

            t = new BigInteger("0");

            t = t.setBit(s - m - 1);
            t = y.modPow(t, p);
            y = t.multiply(t);

            s = m;
            retValue = retValue.multiply(t);

            retValue = retValue.mod(p);

        }

        return retValue;

    }

    public static double log(BigInteger val) {
        int blex = val.bitLength() - 1022; // any value in 60..1023 is ok
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * Math.log(2.0) : res;
    }

    // TODO
	public static boolean divisible(BigInteger a, BigInteger b) {

		return false;
	}

    // TODO
    public static boolean unevenDivider(BigInteger bigInteger, BigInteger bigInteger1) {
        return false;
    }
}
