import java.math.BigInteger;

public class ShanksTonelli {

	public BigInteger calculate(BigInteger N, BigInteger p) {
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

}
