import java.math.BigInteger;

public class BigMath {

	// TODO
	public static BigInteger gcd(BigInteger a, BigInteger b) //valid for positive integers.
	{
		while (b.compareTo(new BigInteger("0")) == 1) {
			BigInteger c = a.mod(b);
			a = b;
			b = c;
		}
		return a;
	}

	// TODO
	public static int legendre(BigInteger N, BigInteger p) {
		BigInteger a = new BigInteger("0");
		BigInteger ret = new BigInteger("1");

		a = N.mod(p);
		long pu;
		pu = p.longValue();
		long power = (pu - 1) / 2;

		while (power > 0) {
			if (power % 2 == 1) {
				ret = ret.multiply(a);
				ret = ret.mod(p);
			}

			a = a.multiply(a);
			a = a.mod(p);
			power = power / 2;
		}

		BigInteger temp = new BigInteger("0");

		temp = ret.subtract(p);

		if (temp.compareTo(new BigInteger("-1")) == 0) {
			ret = ret.subtract(p);
		}

		return ret.intValue();
	}

	public static boolean divisible(BigInteger a, BigInteger b) {

		return false;
	}

    public static boolean unevenDivider(BigInteger bigInteger, BigInteger bigInteger1) {
        return false;
    }
}
