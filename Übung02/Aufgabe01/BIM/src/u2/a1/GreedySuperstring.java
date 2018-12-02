package u2.a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GreedySuperstring {
	
	public static void main(String[] args) {
		
		String[] files = {"./resources/u2/text-fragmente.txt", "./resources/u2/DNA-fragmente-1.txt",
				"./resources/u2/DNA-fragmente-2.txt", "./resources/u2/DNA-fragmente-3.txt"};
		
		String[] T = args;
		
		for (String file : files) {
		System.out.println(file);
		// Aufgabe 1.2
		T = readFile(file);
		// Aufgabe 1.3
//		T = readFile("./resources/u2/DNA-fragmente-1.txt");
		// Aufgabe 1.3
//		T = readFile("./resources/u2/DNA-fragmente-2.txt");
		// Aufgabe 1.3
//		T = readFile("./resources/u2/DNA-fragmente-3.txt");
		
		
		T = distinctT(T);
		
		System.out.println("Minimiert Menge T");
		printT(T);
		System.out.println();
		
		String greedSuperString = buildGreedySuperString(T);
		System.out.println("Länge: " + greedSuperString.length() + " | " + greedSuperString);
		System.out.println("_____________________________________________________________________________________________");
		}
		
	}

	private static String buildGreedySuperString(String[] t) {
		System.out.println("Bilde Greedy Super String");
		List<String> T = new ArrayList<String>(Arrays.asList(t));
		
		while(T.size() > 1) {
			List<Pair> pairs = buildPairs(T);
			Pair pair = getPairWithHighestOverlappingScore(pairs);
//			pair.process();
			T.remove(pair.getA());
			T.remove(pair.getB());
//			System.out.println(pair.getOverlap());
//			System.out.print(pair.getA() + "; " + pair.getB() + " | ");
//			printT(t);
//			System.out.println();
			T.add(pair.getOverlap());
		}
		return T.get(0);
	}

	private static List<Pair> buildPairs(List<String> t) {
		List<Pair> pairs = new ArrayList<Pair>();
		for(int i = 0; i < t.size(); i++) {
			for(int j = i + 1; j < t.size(); j++) {
				Pair pair = new Pair(t.get(i), t.get(j));
				pairs.add(pair);
			}
		}
		
		return pairs;
	}

	private static Pair getPairWithHighestOverlappingScore(List<Pair> pairs) {
		
		int index = -1;
		int score = -1;
		
		for(int i = 0; i < pairs.size(); i++) {
			int pairScore = pairs.get(i).getOverlappingScore();
			if (pairScore > score) {
				score = pairScore;
				index = i;
			}
		}
		
		return pairs.get(index);
	}

	private static String[] distinctT(String[] t) {
		Arrays.sort(t);
		Arrays.sort(t, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.length() - o1.length();
			}
		});
		
		int[] removeIndexes = new int[t.length];
		int removeCounter = 0;
		for(int i = 0; i < t.length; i++) {
			if (removeIndexes[i] == 1)
				continue;
			
			String elem = t[i];
			for(int j = 0; j < t.length; j++) {
				if (removeIndexes[j] == 1)
					continue;
				if (i == j)
					continue;
				
				if (elem.contains(t[j])) {
					removeIndexes[j] = 1;
					t[j] = null;
					removeCounter++;
				}
				
			}
		}
		
		String[] distict = new String[t.length - removeCounter];
		
		for(int i = 0, j = 0; i < t.length && j < distict.length; i++) {
			if (t[i] != null)
				distict[j++] = t[i];
		}
		
		return distict;
		
	}
	
	private static void printT(String[] set) {
		String s = "";
		String delimiter = ", ";
		for(int i = 0; i < set.length; i++) {
				s += set[i] + delimiter;
		}
		System.out.print(s.substring(0, s.length()-delimiter.length()));
		System.out.println();	
	}
	
	private static String[] readFile(String path) {
		BufferedReader br = null;
		FileReader fr = null;
		List<String> ls = new ArrayList<String>();
		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				ls.add(sCurrentLine);
//				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		String[] res = new String[ls.size()];
		return ls.toArray(res);
	}
}
