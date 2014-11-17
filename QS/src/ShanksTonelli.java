import java.math.BigInteger;

public class ShanksTonelli {

	private BigInteger x;
	private BigInteger y;

	public void calculate(BigInteger N, BigInteger p) {
		BigInteger y, b, t;
		BigInteger retValue;
		int r, m;

		long i;
		int s = 0;

		//		mpz_t w, n_inv, y;
		//        unsigned int i, s;

		if (BigMath.divisible(N, p)) {
			retValue = new BigInteger("0");
			return;
		}

		if (BigMath.legendre(N, p) == -1) {
			return;
		}

		y = new BigInteger("2");

		BigInteger ONE = new BigInteger("1");
		while (BigMath.legendre(y, p) != -1)
			y = y.add(ONE);

		retValue = p.subtract(ONE);

		//		 mpz_t y, b, t;
		//		   unsigned int r, m;
		//		   
		//		   if (mpz_divisible_p(arg, prime)) {
		//		     mpz_set_ui(result, 0);
		//		     return 1;
		//		   }
		//		   if (mpz_legendre(arg, prime) == -1)
		//		     return -1;
		//		   
		//			mpz_init(b);
		//			mpz_init(t);
		//			mpz_init_set_ui(y, 2);
		//			while (mpz_legendre(y, prime) != -1)
		//				mpz_add_ui(y, y, 1);

		//			mpz_sub_ui(result, prime, 1);

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

			for (int m = 1; t.compareTo(ONE) == 1; m++) {

			}

		}
		//			r = mpz_scan1(result, 0);
		//			mpz_rshift(result, result, r);
		//
		//			mpz_powm(y, y, result, prime);
		//
		//			mpz_rshift(result, result, 1);
		//			mpz_powm(b, arg, result, prime);
		//
		//			mpz_mul(result, arg, b);
		//			mpz_mod(result, result, prime);
		//
		//			mpz_mul(b, result, b);
		//			mpz_mod(b, b, prime);
		//
		//			while (mpz_cmp_ui(b, 1)) {
		//
		//				mpz_mul(t, b, b);
		//				mpz_mod(t, t, prime);
		//				for (m = 1; mpz_cmp_ui(t, 1); m++) {
		//					mpz_mul(t, t, t);
		//					mpz_mod(t, t, prime);
		//				}
		//
		//				mpz_set_ui(t, 0);
		//				mpz_setbit(t, r - m - 1);
		//				mpz_powm(t, y, t, prime);
		//
		//				mpz_mul(y, t, t);
		//
		//				r = m;
		//
		//				mpz_mul(result, result, t);
		//				mpz_mod(result, result, prime);
		//
		//				mpz_mul(b, b, y);
		//				mpz_mod(b, b, prime);
		//			}
		//
		//			mpz_clear(y);
		//			mpz_clear(b);
		//			mpz_clear(t);
		//			return 1;
		//		}

	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}
}
