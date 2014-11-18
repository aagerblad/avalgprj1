import java.math.BigInteger;
import java.util.ArrayList;

public class Test {

	public static void testLegendre() {


		System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("2")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("3")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("5")) == -1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("7")) == -1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("11")) == -1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("13")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("17")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("19")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("23")) == -1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("29")) == 1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("31")) == -1);
        System.out.println(BigMath.legendre(new BigInteger("87463"), new BigInteger("37")) == -1);



	}
   public static void testQS() {
       QS qs = new QS(new BigInteger("87463"));

       qs.calculateBase();
       qs.calculateFactorBase();

       //System.out.println(hej.get(4));



   }


}
