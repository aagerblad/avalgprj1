import java.util.BitSet;

/**
 * Created by Andreas on 2014-11-17.
 */
public class Gauss {

//import java.math.*;

    //Fields:
    //no fields atm


    //Methods
    /**
     * Constructor
     */



    public BitSet getFreeVariables(BitSet[] matrix) {
        int r = matrix.length;
        int c = matrix[0].length();
        if (c > r) {
            System.err.println("Matrix is wide...");
            return null;
        }

        BitSet res = new BitSet(c);

        //Warning. We assume that the matrix is "high" or square. Might crash otherwise.
        for(int i = c-1; i >= 0; i--) {	//Lower rows should be all 0 anyway

            boolean bitValue = matrix[i].get(i);

            if (bitValue) {	//Bound
                //System.err.println("Bound!");
                res.clear(i);
            } else {	//Free
                //System.err.println("Free!");
                res.set(i);
            }

        }

        return res;
    }

    public BitSet[] transpose(final BitSet[] src) {
        int r = src.length;
        int c = src[0].length();

        BitSet[] res = new BitSet[c];
        for (int i = 0;i<c;i++) {
            res[i] = new BitSet(r);
        }

        for (int i = 0;i<c;i++) {
            for (int j = 0;j<r;j++) {
                res[i].set(j, src[j].get(i));
            }
        }

        return res;
    }

    //Prev denotes the solution given by the previous call to this function
    //Free denotes the free variables, calculated by getFreeVariables();
    public BitSet calcNullSpace(BitSet[] matrix, final BitSet free, final BitSet prev) {
        int c = matrix[0].length();

        if (free == null || free.isEmpty()) {	//Return zero-vector
            BitSet ret = new BitSet(c);
            ret.clear();
            return ret;
        }

        BitSet variables;

        int lastRow;
        BitSet lr;

        if (prev == null || prev.isEmpty()) {
            lastRow = c-1;
            variables = new BitSet(c);
            variables.set(0,c);
        } else {
            lr = (BitSet)prev.clone();
            lr.and(free);
            //lastRow = lr.length()-1;
            lastRow = lr.nextSetBit(0);

            if (lastRow == -1) {
                lastRow++;
            }

            variables = (BitSet)prev.clone();

            variables.set(0,lastRow);
            variables.clear(lastRow);

            lastRow--;
        }

        BitSet bsTmp;

        for(int i = lastRow; i >= 0; i--) {	//Lower rows should be all 0 anyway

            bsTmp = (BitSet)matrix[i].clone();
            //Set the variables
            bsTmp.and(variables);

            if (!free.get(i)) {	//Bound
                //System.err.println("Bound!" + " i is " + i);
                //Determine what we need to set it to
                int bitVal = bsTmp.cardinality();
                //System.err.println("bitVal is: " + bitVal);
                bitVal = (bitVal+1) % 2;

                if (bitVal == 0) {
                    variables.clear(i);
                } else {
                    variables.set(i);
                }

            } else {	//Free, set to 1 as default for now
                //System.err.println("Free!");
                variables.set(i);
            }

        }

        return variables;
    }

    public BitSet[] gaussEliminate(final BitSet[] src) {
        int r = src.length;
        int c = src[0].length();

        BitSet[] res;
        res = (BitSet[])src.clone();	//Use this if you don't want to modify input
        //res = src;	//Use this if you want to modify the input instead
        BitSet tmp;
        int cNoBit = 0;
        int x = -1;
        for (int j = 0;j<c;j++) {
            x = -1;
            for (int i = j-cNoBit;i<r;i++) {
                if (res[i].get(j) == true) {
                    if (x < 0) {
                        x = i;
                    } else {
                        res[i].xor(res[x]);
                    }
                }
            }

            if (x > -1) {
                tmp = (BitSet)res[j-cNoBit].clone();
                res[j-cNoBit] = (BitSet)res[x].clone();
                res[x] = (BitSet)tmp.clone();
            } else {
                cNoBit++;
            }
        }


        //Rearrange for a nicer diagonal

        for (int j = c-1;j>=0;j--) {
            if (res[j].get(j) == true) {	//No rearranging needed.
                break;
            }

            if (res[j].isEmpty()) {	//0-row. Just continue
                continue;
            }

            int bitPos = res[j].nextSetBit(0);

            tmp = (BitSet)res[j].clone();
            res[j] = new BitSet(c);
            res[j].clear();
            res[bitPos] = (BitSet)tmp.clone();

        }



        return res;
    }

    public void printMatrix(BitSet[] matrix, int r, int c) {
        for (int i = 0;i<r;i++) {
            printBitSet(matrix[i], c);
        }
    }

    public void printBitSet(BitSet bs, int c) {
        if (bs != null) {
            for (int j = 0;j<c;j++) {
                System.err.print(" " + (bs.get(j) == true ? 1 : 0));
            }
            System.err.println();
        } else {
            System.err.println("null");
        }
    }
}
