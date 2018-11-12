package u1.a3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class DoubleDigest {
	// Test
	static int[] fragmentlengthsA = {1, 2, 3, 4}; // Quersumme ergibt Sequenzlänge
	static int[] fragmentlengthsB = {4, 6}; // Quersumme ergibt Sequenzlänge
	static int[] fragmentlengthsAB = {1, 1, 2, 2, 4}; // Quersumme ergibt Sequenzlänge
	
	// Aufgab 3.2
//	static int[] fragmentlengthsA = {1, 3, 5, 8}; // Quersumme ergibt Sequenzlänge
//	static int[] fragmentlengthsB = {2, 3, 5, 7}; // Quersumme ergibt Sequenzlänge
//	static int[] fragmentlengthsAB = {1, 1, 2, 2, 3, 4, 4}; // Quersumme ergibt Sequenzlänge

	public static void main(String[] args) {
		
		// convert all fragment arrays to List
		List<Integer> a = new ArrayList<Integer>();
		for (int i : fragmentlengthsA)
		    a.add(i);
		List<Integer> b = new ArrayList<Integer>();
		for (int i : fragmentlengthsB)
		    b.add(i);
		List<Integer> ab = new ArrayList<Integer>();
		for (int i : fragmentlengthsAB)
		    ab.add(i);
	
		// create all permutations for the fragments A; size is facorial of a.size()
		Collection<List<Integer>> pa = CollectionUtils.permutations(a);
		print(pa); // print all permutation for A sequence
		

		// create all permutations for the fragments B; size is facorial of b.size()
		Collection<List<Integer>> pb = CollectionUtils.permutations(b);
		print(pb); // print all permutations for B sequece
		
		// create the combination of both permutation collections; size is pa.size() * pb.size()
		Collection<List<Integer>> pab = combine(pa, pb);
		
		// create workingSet with the help of of pab containg all possible combinations
		Collection<FragmentValidator> workingSet = new ArrayList<FragmentValidator>(pab.size());
		for(List<Integer> elem : pab)
			workingSet.add(new FragmentValidator(elem, fragmentlengthsAB, fragmentlengthsA.length));
		
		// evalute validity fr each combination and set flag internally
		for(FragmentValidator elem : workingSet) {
			elem.eval(false); // no logging
		}
		
		// count the valid sequnces and print them on the console
		int valid = 0;
		for(FragmentValidator elem : workingSet) {
			if (elem.isValid()) {
//				elem.printFragA(); // print Fragment for enzym A only
//				elem.printFragB(); // print Fragment for enzym B only
				elem.printCombination();
				System.out.println("---------------");
				valid++;
			}
		}
		
		// print results
		System.out.println("All:     " + workingSet.size());
		System.out.println("Valid:   " + valid);
		System.out.println("Invalid: " + (workingSet.size() - valid));
			
	}
	
	private static Collection<List<Integer>> combine(Collection<List<Integer>> pa, Collection<List<Integer>> pb) {
		Collection<List<Integer>> pab = new ArrayList<List<Integer>>();
		for (List<Integer> elempa : pa) {
			for (List<Integer> elempb : pb) {
				List<Integer> elem = new ArrayList<Integer>(elempa.size());
				for (Integer val : elempa)
					elem.add(val);
				elem.addAll(elempb);
				pab.add(elem);
			}
		}
		
		return pab;
	}

	private static void print(Collection<List<Integer>> pa) {
		for (List<Integer> elem : pa) {
			String s = "";
			String delimiter = ", ";
			for(Integer i : elem) {
					s += i + delimiter;
			}
			System.out.print(s.substring(0, s.length()-delimiter.length()));
			System.out.println();
		}
		
		System.out.println(pa.size());
	}

}
