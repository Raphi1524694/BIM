import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import scala.Function2;
import scala.Option;
import scala.Some;
import java.util.*;

public class aufgabe2 extends DynProJava<Integer> {

    public static void main (String[] args)
    {
        int elements[] = {1, 3, 5, 6, 9};
        int elementCounter = elements.length;
        int fragmentLength = 58;

        int[][] solutionMatrix =  createSolutionMatrix(elements, elementCounter, fragmentLength);
        System.out.println("");
        System.out.println("The solutionMatrix looks like this");
        System.out.println(Arrays.deepToString(solutionMatrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        int[] result = readSolutionMatrix(solutionMatrix);
        System.out.println("");
        System.out.println("The solutionArray looks like this:");
        for(int i = 0; i<result.length; i++){
            System.out.print(result[i] + ",");
        }
    }

    static int[][] createSolutionMatrix(int elements[], int elementCounter, int fragmentLength)
    {
        int matrix[][] = new int[fragmentLength + 1][fragmentLength + 1];

        // Initialize matrix
        for (int i = 0; i<=fragmentLength; i++ )
            for(int j = 0; j<=fragmentLength; j++) {
               if(j==0){
                   //We use Integer.Max_Value to mark values as infinite
                    matrix[j][i] = Integer.MAX_VALUE;
                }
                else{
                    matrix[j][i]=0;
                }
            }
            matrix[0][0]=0;

        // Compute minimum elements required for all
        for (int i = 1; i <= fragmentLength; i++)
        {
            // Go through all elements smaller than i
            for (int j = 0; j < elementCounter; j++)
                if (elements[j] <= i)
                {
                    //go back in matrix history to check if precomputed values can be used
                    int minElements = matrix[0][i-elements[j]];
                    //
                    if (minElements != Integer.MAX_VALUE && minElements + 1 < matrix[0][i]) {
                        matrix[0][i] = minElements + 1;
                        //clone old matrix values and add own
                            for (int k = 0; k <= fragmentLength; k++) {
                                if (minElements != 0) {
                                    matrix[i][k] = matrix[i - elements[j]][k];
                                }
                                else{
                                    matrix[i][k] = 0;
                                }



                            }

                        matrix[i][elements[j]]++;
                    }
                    }
                }
        return matrix;

    }

    static int[] readSolutionMatrix(int[][] matrix){
        int[] result = new int[matrix[0][matrix.length-1]];
        int resultCounter = 0;
        for(int i=1; i< matrix.length; i++){
            int tempCounter = matrix[matrix.length-1][i];
                while(tempCounter!=0){
                    result[resultCounter] = i;
                    tempCounter--;
                    resultCounter++;
                }

        }
        return result;
    }

    private String[] items;
    private int[] weights;
    private int[] values;
    private int capacity;
    private int elements[] = {9, 6, 5, 1};
    private int elementCounter = elements.length;
    private int fragmentLength = 10;


    @Override
    public int n() {
        return fragmentLength;
    }

    @Override
    public int m() {
        return fragmentLength;
    }

    @Override
    public double value(Idx idx, Integer d) {
        return d * values[idx.i()];
    }

    /**
     * If the remaining capacity (idx.j) plus the weight that could be taken
     * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
     * If not, we can only skip it (={0}).
     */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.j() + weights[idx.i()] <= capacity) {
            return new Integer[]{0, 1};
        } else {
            return new Integer[]{0};
        }
    }

    /**
     * The prev. state is the previous item (idx.i-1) and the prev. capacity.
     * The prev. capacity is the remaining capacity (idx.j) plus weight that was
     * taken (or plus 0 if it was skipped).
     */
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        if (idx.i() > 0) {
            Idx pidx = new Idx(idx.i() - 1, idx.j() + d * weights[idx.i()]);
            return new Idx[]{pidx};
        } else {
            return new Idx[]{};
        }
    }

    /**
     * Defines whether the minimum or maximum is calculated.
     *
     * @return
     */
    @Override
    public Function2 extremeFunction() {
        return this.MAX(); // oder MIN()
    }

    /**
     * Provide row labels, i.e. each row gets a short description.
     *
     * @return Array of size n with the labels.
     */
    @Override
    public Option<String[]> rowLabels() {
        return new Some(items);
    }

    /**
     * Provide column labels, i.e. each columns gets a short description.
     * In this case, the column labels are the same as the column index.
     *
     * @return Array of size m with the labels.
     */
    @Override
    public Option<String[]> columnLabels() {
        String[] cArray = new String[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            cArray[i] = "" + i;
        }
        return new Some(cArray);
    }
}
