package u1.a3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentValidator {
	
	private int splitIndex; // index where B begins
	private int sequenceLength = 0;
	private int[] fragmentlengthsAB;
	private int[] fragmentlengthsA; 
	private int[] fragmentlengthsB;
	private List<Integer> combination;
	private boolean valid = true;
	private int[] fragSeq;
	

	public FragmentValidator(List<Integer> combination, int[] fragmentlengthsAB, int splitIndex) {
		this.combination = combination;
		this.splitIndex = splitIndex;
		this.fragmentlengthsAB = fragmentlengthsAB;
		
		int j = 0;
		for (Integer el : combination)
			if (j++ < splitIndex)
				sequenceLength += el;
			else
				break;
		
		fragmentlengthsA = new int[splitIndex];
		fragmentlengthsB = new int[combination.size() - splitIndex];
		
		for (int i = 0; i < combination.size(); i++) {
			if (i < splitIndex)
				fragmentlengthsA[i] = combination.get(i);
			else
				fragmentlengthsB[i - splitIndex] = combination.get(i);
		}
		
	}
	
	public boolean isValid() {
		return valid;
	}

	public void eval(boolean debug) {
		this.fragSeq = buildFragSeq();
		if (debug) {
			System.out.print("Combination: ");
			printCombination();
			System.out.print("Cutposition Sequence: ");
			printFragSeq();
		}

		int[] fragmentlengthsAB = new int[this.fragmentlengthsAB.length];
		for (int i = 0; i < fragmentlengthsAB.length; i++)
			fragmentlengthsAB[i] = this.fragmentlengthsAB[i];
		
		int n = 0; // delete counter
		for (int i = 1; i < fragSeq.length; i++) {
			int val = fragSeq[i] - fragSeq[i - 1];
			int res = delete(fragmentlengthsAB, val);
			if (res == val) {
				n++;
			}
			if (debug) {
				System.out.print(val + "(" + res + ") | ");
			}
		}
		
		if (n == fragmentlengthsAB.length)
			valid = true;
		else
			valid = false;
		if (debug) {
			System.out.println(n + " von " + fragmentlengthsAB.length + " entfernt => " + valid);
			System.out.println("--------------------------------------");
		}
	}

	private int delete(int[] fragmentlengthsAB, int val) {
		for (int i = 0; i < fragmentlengthsAB.length; i++) { 
			if (fragmentlengthsAB[i] == val) {
				fragmentlengthsAB[i] = -1;
				return val;
			}
		}
		return -1;
	}

	private int[] buildFragSeq() {
		List<Integer> a = new ArrayList<Integer>();
		int prev = 0;
		for(int i = 0; i < splitIndex - 1; i++) {
			Integer val = combination.get(i);
			if (i == 0)
				prev = 0;
			else
				prev = a.get(i - 1);
			a.add(prev + val);
			prev = val;
		}
		
		List<Integer> b = new ArrayList<Integer>();
		prev = 0;
		for(int i = splitIndex; i < combination.size() - 1; i++) {
			Integer val = combination.get(i);
			if (i == splitIndex)
				prev = 0;
			else
				prev = b.get(i - splitIndex - 1);
			b.add(prev + val);
			prev = val;
		}
		
		List<Integer> fraqSeq = a;
		fraqSeq.add(0);
		fraqSeq.add(sequenceLength);
		for (Integer val : b)
			if (!fraqSeq.contains(val))
				fraqSeq.add(val);
		
		int[] fraqSeqArray = new int[fraqSeq.size()];
		for (int i = 0; i < fraqSeqArray.length; i++)
			fraqSeqArray[i] = fraqSeq.get(i);
		
		Arrays.sort(fraqSeqArray);
		
		return fraqSeqArray;
	}

	public void printCombination() {
		String s = "";
		String delimiter = ", ";
		String split = "| ";
		int j = 0;
		for(Integer i : combination) {
			if (j != splitIndex - 1)
				s += i + delimiter;
			else
				s += i + split;
				j++;
		}
		System.out.print(s.substring(0, s.length()-delimiter.length()));
		System.out.println();
	}
	
	public void printFragA() {
		String s = "";
		String delimiter = ", ";
		for(int i = 0; i < fragmentlengthsA.length; i++) {
				s += fragmentlengthsA[i] + delimiter;
		}
		System.out.print(s.substring(0, s.length()-delimiter.length()));
		System.out.println();
		
	}
	
	public void printFragB() {
		String s = "";
		String delimiter = ", ";
		for(int i = 0; i < fragmentlengthsB.length; i++) {
				s += fragmentlengthsB[i] + delimiter;
		}
		System.out.print(s.substring(0, s.length()-delimiter.length()));
		System.out.println();
		
	}	

	public void printFragSeq() {
		String s = "";
		String delimiter = ", ";
		for(int i = 0; i < fragSeq.length; i++) {
				s += fragSeq[i] + delimiter;
		}
		System.out.print(s.substring(0, s.length()-delimiter.length()));
		System.out.println();
		
	}
	
}
