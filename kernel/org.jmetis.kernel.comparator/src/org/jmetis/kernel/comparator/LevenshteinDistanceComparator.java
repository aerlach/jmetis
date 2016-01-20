package org.jmetis.kernel.comparator;

import java.util.Comparator;

/**
 * Levenshtein distance (LD) is a measure of the similarity between two strings,
 * which we will refer to as the source string (s) and the target string (t).
 * The distance is the number of deletions, insertions, or substitutions
 * required to transform s into t. For example, - If s is "test" and t is
 * "test", then LD(s,t) = 0, because no transformations are needed. The strings
 * are already identical. - If s is "test" and t is "tent", then LD(s,t) = 1,
 * because one substitution (change "s" to "n") is sufficient to transform s
 * into t. The greater the Levenshtein distance, the more different the strings
 * are. Levenshtein distance is named after the Russian scientist Vladimir
 * Levenshtein, who devised the algorithm in 1965. Step Description 1 Set n to
 * be the length of s. Set m to be the length of t. If n = 0, return m and exit.
 * If m = 0, return n and exit. Construct a matrix containing 0..m rows and 0..n
 * columns. 2 Initialize the first row to 0..n. Initialize the first column to
 * 0..m. 3 Examine each character of s (i from 1 to n). 4 Examine each character
 * of t (j from 1 to m). 5 If s[i] equals t[j], the cost is 0. If s[i] doesn't
 * equal t[j], the cost is 1. 6 Set cell d[i,j] of the matrix equal to the
 * minimum of: a. The cell immediately above plus 1: d[i-1,j] + 1. b. The cell
 * immediately to the left plus 1: d[i,j-1] + 1. c. The cell diagonally above
 * and to the left plus the cost: d[i-1,j-1] + cost. 7 After the iteration steps
 * (3, 4, 5, 6) are complete, the distance is found in cell d[n,m].
 */
public class LevenshteinDistanceComparator implements Comparator<String> {

	/**
	 * Constructs a new {@code LevenshteinDistanceComparator} instance.
	 */
	public LevenshteinDistanceComparator() {
		super();
	}

	public boolean areSimilar(String firstString, String secondString) {
		return this.areSimilar(firstString, secondString, this
				.getDefaultMaxSize(), this.getDefaultThreshold());
	}

	public boolean areSimilar(String firstString, String secondString,
			int threshold) {
		return this.areSimilar(firstString, secondString, this
				.getDefaultMaxSize(), threshold);
	}

	public boolean areSimilar(String firstString, String secondString,
			int maxSize, int threshold) {
		return this.distanceFor(firstString, secondString, maxSize) <= threshold;
	}

	protected int getDefaultMaxSize() {
		return 20;
	}

	protected int getDefaultThreshold() {
		return 3;
	}

	protected int distanceFor(String firstString, String secondString,
			int maxSize) {
		int firstSize = firstString.length();
		int secondSize = secondString.length();
		if (firstSize == 0) {
			return secondSize;
		}
		if (secondSize == 0) {
			return firstSize;
		}
		firstSize = Math.min(firstSize, maxSize);
		secondSize = Math.min(secondSize, maxSize);
		int[][] distance = new int[firstSize + 1][secondSize + 1];
		for (int i = 0; i <= firstSize; i++) {
			distance[i][0] = i;
		}
		for (int i = 0; i <= secondSize; i++) {
			distance[0][i] = i;
		}
		for (int i = 1; i <= firstSize; i++) {
			char firstCh = firstString.charAt(i - 1);
			for (int j = 1; j <= secondSize; j++) {
				char secondCh = secondString.charAt(j - 1);
				int cost = firstCh == secondCh ? 0 : 1;
				distance[i][j] = Math.min(Math.min(distance[i - 1][j],
						distance[i][j - 1]) + 1, distance[i - 1][j - 1] + cost);
			}
		}
		return distance[firstSize][secondSize];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String firstValue, String secondValue) {
		// TODO Auto-generated method stub
		return 0;
	}
}