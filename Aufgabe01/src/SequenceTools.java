import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SequenceTools {

    private static final String filePathToFastaFolder = "/Users/rlubaschewski/Desktop/uni/BIM/Aufgabe01/assets/fasta";

    public static String getMatureMRNA(String sequence, String[] exons, boolean keepFirstIntron) {
        String matureMRNA = "";
        List<String> introns = new LinkedList<>();
        int lastExonIndex = 0;
        for (int i = 0; i < exons.length; i++) {
            int exonIndex = sequence.indexOf(exons[i]);
            if (exonIndex != 1) {
                if (lastExonIndex != 0 && lastExonIndex < exonIndex) {
                    introns.add(sequence.substring(lastExonIndex + 1, exonIndex - 1));
                }
                lastExonIndex = sequence.lastIndexOf(exons[i]);
                if (keepFirstIntron && introns.size() == 1) {
                    matureMRNA += introns.get(0);
                }
                matureMRNA += exons[i];
            }
        }
        StringBuilder builder = new StringBuilder(matureMRNA);
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == 'A') {
                builder.setCharAt(i, 'U');
            } else if (builder.charAt(i) == 'T') {
                builder.setCharAt(i, 'A');
            } else if (builder.charAt(i) == 'C') {
                builder.setCharAt(i, 'G');
            } else if (builder.charAt(i) == 'G') {
                builder.setCharAt(i, 'C');
            }
        }
        return builder.toString();
    }

    public static String mRNAtoProtein(String mrna, boolean log) {
        String protein = "";
        String proteinStartToStop = "";
        int start = 0, stop = 0;
        for (int i = 0; i < mrna.length() - 2; i++) {
            protein += CodonTranslater.codon2aa(mrna.substring(i, i + 3));
        }
        for (int i = 0; i < protein.length(); i++) {
            if (start > 0 && stop > 0 && stop > start) {
                proteinStartToStop = protein.substring(start, stop);
                break;
            }
            if (protein.charAt(i) == 'M') {
                if (log)
                    System.out.println("The 5' untranslated region of the mRNA starts at index 0 and ends at index " + (i - 1));
                start = i;
            } else if (protein.charAt(i) == '*') {
                stop = i;
                if (stop > start && start > 0 && log) {
                    System.out.println("The 3' untranslated region of the mRNA starts at index " + i + " and ends at index " + protein.length());
                }
            }
        }
        return proteinStartToStop;
    }

    public static void main(String[] args) {
        List<String> fasta = FastaUtilizer.getArrayFromFasta(filePathToFastaFolder + "/NR1H4-BIM-UEBUNG.fasta");
        String sequence = fasta.get(0);
        String[] exons = new String[fasta.size() - 1];
        for (int i = 1; i < fasta.size(); i++) {
            exons[i - 1] = fasta.get(i);
        }
        String matureMRNA = getMatureMRNA(sequence, exons, false);
        String[] commentsRNA = {"mature mRNA of NR1H4"};
        String[] contentsRNA = {matureMRNA};
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-reife-mRNA.fasta", commentsRNA, contentsRNA);
        String protein = mRNAtoProtein(matureMRNA, true);
        String[] commentsProtein = {"Primary Protein Sequence of NR1H4"};
        String[] contentsProtein = {protein};
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-Protein.fasta", commentsProtein, contentsProtein);
        String mRNAWithFirstIntron = getMatureMRNA(sequence, exons, true);
        String proteinWithFirstIntron = mRNAtoProtein(mRNAWithFirstIntron, false);
        String[] commentsRNAWithIntron = {"Protein with first Intron kept"};
        String[] contentsRNAWithIntron = {proteinWithFirstIntron};
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-Protein-Variante.fasta", commentsRNAWithIntron, contentsRNAWithIntron);
    }

}
