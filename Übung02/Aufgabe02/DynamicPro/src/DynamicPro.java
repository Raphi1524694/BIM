import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import scala.Function2;
import scala.Option;
import scala.Some;



public class DynamicPro extends DynProJava<Integer> {

    public static void main(String[] args) {
        char[] seq1 = ("-"+"CGATCCTGT").toCharArray();
        char[] seq2 = ("-"+"CATCGCCTT").toCharArray();
        int match = 1;
        int mismatch= -1;
        int gap = -1;
        DynamicPro dp = new DynamicPro(seq1, seq2, match, mismatch, gap);
        // The maximum is expected at the last item (n-1)
        // with no capacity left (0);
    }

    private String[] items;
    private char[] seq1;
    private char[] seq2;
    private int match;
    private int mismatch;
    private int gap;

    public DynamicPro(char[] seq1, char[] seq2, int match, int mismatch,
                    int gap) {
        this.seq1 = seq1;
        this.seq2 = seq2;
        this.match = match;
        this.mismatch = mismatch;
        this.gap = gap;
        this.initializeMatrix(seq1, seq2);
        System.out.println("Ergebnismatrix:");
        this.printMatrix();
        String[] alignements = this.traceback(this.m()-1, this.n()-1);
        this.drawAlignements(alignements);
    }
    //Augabe 2.2
    public void drawAlignements(String[] alignements){
        System.out.println("Alignements:");
        System.out.println(alignements[0]);
        for(int i = 0; i< alignements[0].length(); i++) {
            if(alignements[0].charAt(i) == alignements[1].charAt(i)) {
                    System.out.print("*");
            }
            else{
                System.out.print(" ");
            }
        }
        System.out.println("\n"+alignements[1]);
    }

    private String[][] matrix;

    public void initializeMatrix(char[] seq1, char[] seq2) {
        this.matrix = new String[this.m()][this.n()];
        // fill in first sequence
        for (int i = 2; i < this.n(); i++) {
            this.matrix[0][i] = i == 0 ? "" : "" + seq2[i-1];
        }
        // fil in second sequence
        for (int i = 2; i < this.m(); i++) {
            this.matrix[i][0] = "" + seq1[i - 1];
        }

        matrix[0][0] = "-";
        matrix[0][1] = "-";
        matrix[1][0] = "-";
        matrix[1][1] = "0";


        //use gap to fill first row
        for (int i = 2; i < this.m() ;i++)
        {
            matrix[i][1] = Integer.toString(Integer.parseInt(matrix[i-1][1]) + this.gap);
        }

        //use gap to fill first column
        for (int i = 2; i < this.n(); i++)
        {
            matrix[1][i] = Integer.toString(Integer.parseInt(matrix[1][i - 1]) + this.gap);
        }

        //calculate matrix
        for (int i = 2; i < m();i++)
        {
            for (int j = 2; j < n();j++)
            {
                int D = Integer.parseInt(matrix[i-1][j-1]) + compare(seq1[i-1], seq2[j-1]);
                int L= Integer.parseInt(matrix[i - 1][j]) + gap;
                int U = Integer.parseInt(matrix[i][j - 1]) + gap;

                matrix[i][j] = Integer.toString(decisions(new Idx (U, Math.max(L, D)))[0]);
            }}
    }

    public String[] traceback(int i, int j){
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
        while(i!=1 && j !=1){
            //diagonal one
            if(Integer.parseInt(matrix[i-1][j-1])>= Math.max(Integer.parseInt(matrix[i-1][j]),Integer.parseInt(matrix[i][j-1]))){
                String[] temp = traceback(i-1, j-1);
                result[0]= temp[0] + matrix[i][0];
                result[1]= temp[1] + matrix[i][0];
                return result;
            }
            //left one
            else if(Integer.parseInt(matrix[i-1][j]) >= Integer.parseInt(matrix[i][j-1])){
                String[] temp =  traceback(i-1, j);
                result[0]=  temp[0] + '_';
                result[1]=  temp[1] + matrix[i][0];
                return result;
            }
            //up one
            else {
                String[] temp = traceback(i, j-1);
                result[0]= temp[0] + matrix[0][j];
                result[1]= temp[1] + '_';
                return result;
            }
        }
        return result;



    }

    public void printRow(String[] row) {
        for (int i = 0; i < row.length; i++) {
            System.out.print(row[i]);
            System.out.print("\t");
        }
    }

    public void printMatrix() {
        for (String[] row : this.matrix) {
            this.printRow(row);
            System.out.println();
        }
    }

    public void fillPlots() {
        for (int i = 1; i < this.matrix.length; i++) {
            for (int j = 1; j < this.matrix[i].length; j++) {
                this.matrix[i][j] = (this.matrix[i][0]).equals(this.matrix[0][j]) ? "1" : "0";
            }
        }
    }

    @Override
    public int n() {
        return seq2.length + 1;
    }

    @Override
    public int m() {
        return seq2.length + 1;
    }

    @Override
    public double value(Idx idx, Integer d) {
        return 1;
    }

    /**
     * If the remaining capacity (idx.j) plus the weight that could be taken
     * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
     * If not, we can only skip it (={0}).
     */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.i()> idx.j()){
            return new Integer[]{idx.i()};
        } else {
            return new Integer[]{idx.j()};
        }
    }

    /**
     * The prev. state is the previous item (idx.i-1) and the prev. capacity.
     * The prev. capacity is the remaining capacity (idx.j) plus weight that was
     * taken (or plus 0 if it was skipped).
     */
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {

        return new Idx[]{};

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
        return new Some(seq1);
    }

    /**
     * Provide column labels, i.e. each columns gets a short description.
     * In this case, the column labels are the same as the column index.
     *
     * @return Array of size m with the labels.
     */
    @Override
    public Option<String[]> columnLabels() {
        return new Some(seq2);
    }

    public int compare(char a, char b) {
        return (a==b) ? match : mismatch;
    }


    }

