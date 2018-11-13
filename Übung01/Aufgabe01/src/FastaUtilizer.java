import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FastaUtilizer {

    public static List<String> getListFromFasta(String path) {
        List<String> fasta = new LinkedList<>();
        String fastaBlock = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.charAt(0) != '>') {
                    fastaBlock += line;
                } else {
                    if (fastaBlock.length() != 0) {
                        fasta.add(fastaBlock);
                    }
                    fastaBlock = "";
                }
            }
            fasta.add(fastaBlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fasta;
    }

    public static void writeFastaFile(String path, String[] comments, String[] contents) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < comments.length; i++) {
                writer.write(">");
                writer.write(comments[i]);
                for (int j = 0; j < contents[i].length(); j++) {
                    if (j % 59 == 0) {
                        writer.newLine();
                    }
                    writer.write(contents[i].charAt(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
