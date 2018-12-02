import java.util.LinkedList;
import java.util.List;

public class SequenceTools {

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
            if (matureMRNA.charAt(i) == 'T') {
                builder.setCharAt(i, 'U');
            }
        }
        return builder.toString();
    }

    String getCorrectProtein(String mRNA) {
        int index = mRNA.indexOf("AUG");
        System.out.println("Start Codon found at " + index);
        String protein = "";
        for (int index; index < mRNA.length(); index += 3) {
            if (i > mRNA.length()) {
                break;
            }
            String codon = mRNA.substring(index, index + 3);
            protein += CodonTranslater.codon2aa(codon);
        }
        return protein;
    }

    public static String mRNAtoProtein(String mrna, boolean log) {
        String protein = "";
        String proteinStartToStop = "";
        boolean firstStartCodonFound = false;
        int start = 0, stop = 0;
        for (int i = 0; i < mrna.length() - 2; i++) {
            String codon = mrna.substring(i, i + 3);
            if (codon == "AUG") {
                System.out.println(i);
            }
            protein += CodonTranslater.codon2aa(codon);
        }
        for (int i = 0; i < protein.length(); i++) {
            if (start > 0 && stop > 0 && stop > start) {
                proteinStartToStop = protein.substring(start, stop);
                break;
            }
            if (protein.charAt(i) == 'M' && !firstStartCodonFound) {
                if (log) {
                    firstStartCodonFound = true;
                    System.out.println("The 5' untranslated region of the mRNA starts at index 0 and ends at index " + (i - 1));
                }
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
        String filePathToFastaFolder = System.getProperty("user.dir") + "/assets/fasta";
        // extract information from original fasta file
        List<String> fasta = FastaUtilizer.getListFromFasta(filePathToFastaFolder + "/NR1H4-BIM-UEBUNG.fasta");
        String sequence = fasta.get(0);
        String[] exons = new String[fasta.size() - 1];
        for (int i = 1; i < fasta.size(); i++) {
            exons[i - 1] = fasta.get(i);
        }
        // transcribe to mature mRNA
        String matureMRNA = getMatureMRNA(sequence, exons, false);
        String[] commentsRNA = {"mature mRNA of NR1H4"};
        String[] contentsRNA = {matureMRNA};
        // create new file and write generated mRNA
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-reife-mRNA.fasta", commentsRNA, contentsRNA);
        // translate to protein (primary structure)
        String protein = mRNAtoProtein(matureMRNA, true);
        String[] commentsProtein = {"Primary Protein Sequence of NR1H4"};
        String[] contentsProtein = {protein};
        // create new file and write amino acid sequence
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-Protein.fasta", commentsProtein, contentsProtein);
        // mRNA with first intron kept
        String mRNAWithFirstIntron = getMatureMRNA(sequence, exons, true);
        String proteinWithFirstIntron = mRNAtoProtein(mRNAWithFirstIntron, false);
        String[] commentsRNAWithIntron = {"Protein with first Intron kept"};
        String[] contentsRNAWithIntron = {proteinWithFirstIntron};
        // write protein with first intron kept in new fasta file
        FastaUtilizer.writeFastaFile(filePathToFastaFolder + "/NR1H4-Protein-Variante.fasta", commentsRNAWithIntron, contentsRNAWithIntron);
    }

}
