package u2.a1;

public class Pair {
	private String sI;
	private String sJ;
	private int overlappingScore;
	private String s0;
	
	public Pair(String i, String j) {
		sI = i;
		sJ = j;
		process();
	}
	
	public void process() {
		
		
		int maxScore = sI.length() > sJ.length() ? sJ.length() : sI.length(); 
		
		int sIFrontScore = 0;
		String sIFrontString = sI + sJ;
		int sJFrontScore = 0;
		String sJFrontString = sJ + sI;
		for (int score = 1; score < maxScore; score++) {
			boolean allMatchedForSI = true;
			boolean allMatchedForSJ = true;
			for (int i = 0; i < score; i++) {
				// test for leading aString
				if (sI.charAt(sI.length() - score + i) != sJ.charAt(i)) {
					allMatchedForSI = false;
//					break;
				}
				
				// test for leading bString
				if (sJ.charAt(sJ.length() - score + i) != sI.charAt(i)) {
					allMatchedForSJ = false;
//					break;
				}
				
				if (!allMatchedForSI && !allMatchedForSJ)
					break;
			}
			
			if (allMatchedForSI) {
				sIFrontScore = score;
				sIFrontString = sI + sJ.substring(score, sJ.length());
			}
			
			if (allMatchedForSJ) {
				sJFrontScore = score;
				sJFrontString = sJ + sI.substring(score, sI.length());
			}
		}
		
		if (sIFrontScore > sJFrontScore) {
			overlappingScore = sIFrontScore;
			s0 = sIFrontString;
		} else {
			overlappingScore = sJFrontScore;
			s0 = sJFrontString;
		}
		
	}

	public String getA() {
		return sI;
	}

	public String getB() {
		return sJ;
	}

	public int getOverlappingScore() {
		return overlappingScore;
	}

	public String getOverlap() {
		return s0;
	}
	
}
