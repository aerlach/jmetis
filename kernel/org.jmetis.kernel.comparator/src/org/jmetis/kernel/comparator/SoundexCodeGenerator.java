package org.jmetis.kernel.comparator;

/**
 * The Soundex phonetic code algorithm (first applied to the 1880 census)
 * reduces an input word to a letter and three numbers such as S543,
 * representing words like Smalltalk. The letter is always the first letter of
 * the input word, whether it is a vowel or a consonant. The remaining vowels
 * (A, E, I, O, and U) as well as W, Y, and H are disregarded. The numbers of
 * the next three consonants of the input word are assigned according to the
 * coding guidelines:
 * <table>
 * <tr>
 * <td>B, P, F, V</td>
 * <td>-&gt;</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>C, S, K, G, J, Q, X, Z</td>
 * <td>-&gt;</td>
 * <td>2</td>
 * </tr>
 * <tr>
 * <td>D, T</td>
 * <td>-&gt;</td>
 * <td>3</td>
 * </tr>
 * <tr>
 * <td>L</td>
 * <td>-&gt;</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>M, N</td>
 * <td>-&gt;</td>
 * <td>5</td>
 * </tr>
 * <tr>
 * <td>R</td>
 * <td>-&gt;</td>
 * <td>6</td>
 * </tr>
 * </table>
 * If there are not three consonants following the initial letter, zeroes are
 * used to complete the three-digit code.</br> If the input word has any double
 * letters, they are treated as one letter.</br> If the input word has different
 * side-by-side letters that receive the same number, they are treated as one
 * letter.</br> See Donald Knuth in
 * "The Art Of Computer Programming, vol. 3: Sorting And Searching",
 * Addison-Wesley (1973), pp. 391-392.
 */
public class SoundexCodeGenerator implements IPhoneticCodeGenerator {
	private transient char[] codeTable;

	public SoundexCodeGenerator() {
		super();
		this.setCodeTable(this.newCodeTable());
	}

	protected char getCodeFor(char[] codeTable, char ch) {
		return 'A' <= ch && ch <= 'Z' ? codeTable[ch - 'A'] : 'a' <= ch
				&& ch <= 'z' ? codeTable[ch - 'a'] : '0';
	}

	protected void setCodeTable(char[] newCodeTable) {
		this.codeTable = newCodeTable;
	}

	protected char[] getCodeTable() {
		return this.codeTable;
	}

	protected char[] newCodeTable() {
		return new char[] { '0', '1', '2', '3', '0', '1', '2', '0', '0', '2',
				'2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '1',
				'0', '2', '0', '2' };
	}

	public String toEncodedValue(String initialString) {
		int initialStringSize;
		if (initialString == null
				|| (initialStringSize = initialString.length()) == 0) {
			return new String(new char[] { '0', '0', '0', '0' });
		}
		char[] codeTable = this.getCodeTable();
		char[] encodedValue = new char[] { '0', '0', '0', '0' };
		encodedValue[0] = Character.toUpperCase(initialString.charAt(0));
		char previousCode = this.getCodeFor(codeTable, encodedValue[0]);
		int encodedStringIndex = 1;
		int initialStringIndex = 1;
		while (initialStringIndex < initialStringSize) {
			char currentCode = this.getCodeFor(codeTable, initialString
					.charAt(initialStringIndex++));
			if (currentCode != '0' && currentCode != previousCode) {
				encodedValue[encodedStringIndex++] = currentCode;
				if (encodedStringIndex >= 4) {
					return new String(encodedValue);
				}
			}
			previousCode = currentCode;
		}
		return new String(encodedValue);
	}

}