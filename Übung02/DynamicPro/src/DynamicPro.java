import net.gumbix.dynpro.DynPro;

public class DynamicPro  {
    private String[][] matrix;

    public void initializeMatrix(String seq1, String seq2) {
        this.matrix = new String[seq1.length() + 1][seq2.length() + 1];
        // fill in first sequence
        for (int i = 0; i < this.matrix[0].length; i++) {
            if (i == 0) {
                this.matrix[0][i] = "";
            } else {
                this.matrix[0][i] = "" + seq1.charAt(i - 1);
            }
        }
        // fil in second sequence
        for (int i = 1; i < this.matrix.length; i++) {
            this.matrix[i][0] = "" + seq2.charAt(i - 1);
        }

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

    public DynamicPro(String seq1, String seq2) {
        this.initializeMatrix(seq1, seq2);
    }

    public static void main(String[] args) {
        DynamicPro test = new DynamicPro("CGATCCTGT", "CATCGCCTT");
        test.fillPlots();
        test.printMatrix();
    }

}
