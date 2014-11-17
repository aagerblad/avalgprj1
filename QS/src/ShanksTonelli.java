import java.math.BigInteger;

public class ShanksTonelli {

	private BigInteger x;
	private BigInteger y;

	public void calculate(BigInteger N, BigInteger p) {
		BigInteger w, n_inv, y;

		long i, s;

		//		mpz_t w, n_inv, y;
		//        unsigned int i, s;

		if (BigMath.divisible(n, p)) {

		}
		if (mpz_divisible_p(n, p)) {
			mpz_set_ui(q, 0);
			return 1;
		}

		if (mpz_tstbit(p, 1) == 1) {
			mpz_set(q, p);
			mpz_add_ui(q, q, 1);
			mpz_fdiv_q_2exp(q, q, 2);
			mpz_powm(q, n, q, p);
			return 1;
		}
		mpz_init(y);
		mpz_init(w);
		mpz_init(n_inv);

		mpz_set(q, p);
		mpz_sub_ui(q, q, 1);
		s = 0;
		while (mpz_tstbit(q, s) == 0)
			s++;
		mpz_fdiv_q_2exp(q, q, s);
		mpz_set_ui(w, 2);
		while (mpz_legendre(w, p) != -1)
			mpz_add_ui(w, w, 1);
		mpz_powm(w, w, q, p);
		mpz_add_ui(q, q, 1);
		mpz_fdiv_q_2exp(q, q, 1);
		mpz_powm(q, n, q, p);
		mpz_invert(n_inv, n, p);
		for (;;) {
			mpz_powm_ui(y, q, 2, p);
			mpz_mul(y, y, n_inv);
			mpz_mod(y, y, p);
			i = 0;
			while (mpz_cmp_ui(y, 1) != 0) {
				i++;
				mpz_powm_ui(y, y, 2, p);
			}
			if (i == 0) {
				return 1;
			}
			if (s - i == 1) {
				mpz_mul(q, q, w);
			} else {
				mpz_powm_ui(y, w, 1 << (s - i - 1), p);
				mpz_mul(q, q, y);
			}
			mpz_mod(q, q, p);
		}

		mpz_clear(w);
		mpz_clear(n_inv);
		mpz_clear(y);
		return 0;

	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}
}
