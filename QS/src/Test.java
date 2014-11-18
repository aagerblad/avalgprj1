import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

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
       // 874654698127381723817123512312312312312873123563 // funkar inte
       // 874812123123563 // bra print för null vector
       QS qs = new QS(new BigInteger("87465435123112312312312212312312123123122312312873123563"));

       qs.calculateBase();
       qs.calculateFactorBase();

       int rows = qs.getSmoothNumberSize();
       int columns = qs.getFactorBase().size();
       BitSet[] matrix = new BitSet[rows];

       qs.generateSmoothNumbers(matrix);

       //Gauss.printMatrix(matrix, rows, columns);

       matrix = Gauss.gaussEliminate(matrix, rows, columns);

       BitSet freeVar = Gauss.getFreeVariables(matrix, rows, columns);
       System.err.println("---------------------------------------------");
       System.err.println("---------------------------------------------");
       System.err.println("---------------------------------------------");
       //System.err.println("Free variables: ");
       //Gauss.printBitSet(freeVar, columns);
       //Gauss.printMatrix(matrix, rows, columns);

       BitSet nullspace = null;
       while (true) {
           nullspace = Gauss.calcNullSpace(matrix, rows, columns, freeVar, nullspace);
           //System.err.println("Null space vector: ");
           //Gauss.printBitSet(nullspace, columns);
           if (nullspace.isEmpty()) {
               break;
           }
       }





       //System.out.println(hej.get(4));



   }


}
