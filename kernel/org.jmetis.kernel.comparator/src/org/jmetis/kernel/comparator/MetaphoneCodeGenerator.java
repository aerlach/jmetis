package org.jmetis.kernel.comparator;

/**
 * {@code MetaphoneCodeGenerator} produces better matches than the Soundex
 * algorithm. An input word is reduced to a 1 to 4 character code using
 * relatively simple phonetic rules for typical spoken English.
 * <ul>
 * <li>Metaphone reduces the alphabet to 16 consonant sounds:</br> B X S K J T F
 * H L M N P R 0 W Y</br> Note: 0 is not an O but a zero - representing the 'th'
 * sound.</li>
 * <li>Metaphone uses the following transformation rules:</br> Doubled letters
 * except "c" -&gt; drop 2nd letter.</br> Vowels are only kept when they are the
 * first letter.</br>
 * <table>
 * <tr>
 * <td>B</td>
 * <td>-&gt;</td>
 * <td>B</td>
 * <td>unless at the end of a word after <i>M</i> as in <i>dumb</i></td>
 * </tr>
 * <tr>
 * <td>C</td>
 * <td>-&gt;</td>
 * <td>X</td>
 * <td>(sh) if -CIA- or -CH-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>S</td>
 * <td>if -CI-, -CE- or -CY-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>K</td>
 * <td>otherwise, including -SCH-</td>
 * </tr>
 * <tr>
 * <td>D</td>
 * <td>-&gt;</td>
 * <td>J</td>
 * <td>if in -DGE-, -DGY- or -DGI--</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>T</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>F</td>
 * <td>-&gt;</td>
 * <td>F</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>G</td>
 * <td>-&gt;</td>
 * <td></td>
 * <td>silent if in -GH- and not at end or before a vowel-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>in -GN- or -GNED- (also see DGE etc. above)</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>J</td>
 * <td>if before I, E, or Y if not double GG</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>K</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>H</td>
 * <td>-&gt;</td>
 * <td></td>
 * <td>silent if after vowel and no vowel follows</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>H</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>J</td>
 * <td>-&gt;</td>
 * <td>J</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>K</td>
 * <td>-&gt;</td>
 * <td></td>
 * <td>silent if after C</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>K</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>L</td>
 * <td>-&gt;</td>
 * <td>L</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>M</td>
 * <td>-&gt;</td>
 * <td>M</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>N</td>
 * <td>-&gt;</td>
 * <td>N</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>P</td>
 * <td>-&gt;</td>
 * <td>F</td>
 * <td>if before H</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>P</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>Q</td>
 * <td>-&gt;</td>
 * <td>K</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>R</td>
 * <td>-&gt;</td>
 * <td>R</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>S</td>
 * <td>-&gt;</td>
 * <td>X</td>
 * <td>(sh) if before H or in -SIO- or -SIA-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>S</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>T</td>
 * <td>-&gt;</td>
 * <td>X</td>
 * <td>(sh) if -TIA- or -TIO-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>0</td>
 * <td>(th) if before H</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>silent if in -TCH-</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>T</td>
 * <td>otherwise</td>
 * </tr>
 * <tr>
 * <td>V</td>
 * <td>-&gt;</td>
 * <td>F</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>W</td>
 * <td>-&gt;</td>
 * <td></td>
 * <td>silent if not followed by a vowel</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>W</td>
 * <td>if followed by a vowel</td>
 * </tr>
 * <tr>
 * <td>X</td>
 * <td>-&gt;</td>
 * <td>KS</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Y</td>
 * <td>-&gt;</td>
 * <td></td>
 * <td>silent if not followed by a vowel</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>Y</td>
 * <td>if followed by a vowel</td>
 * </tr>
 * <tr>
 * <td>Z</td>
 * <td>-&gt;</td>
 * <td>S</td>
 * <td></td>
 * </tr>
 * </table>
 * <br>
 * Initial Letter Exceptions:<br>
 * <table>
 * <tr>
 * <td>KN-, GN-, PN, AE- or WR--</td>
 * <td>-&gt;</td>
 * <td>drop first letter</td>
 * </tr>
 * <tr>
 * <td>X-</td>
 * <td>-&gt;</td>
 * <td>change to S</td>
 * </tr>
 * <tr>
 * <td>WH-</td>
 * <td>-&gt;</td>
 * <td>change to W</td>
 * </tr>
 * </table>
 * </li>
 * </ul>
 * See Lawrence Philips in the December 1990 issue of Computer Language
 * "Hanging on the Metaphone" pp 39 - 43.
 */
public class MetaphoneCodeGenerator implements IPhoneticCodeGenerator {

	public MetaphoneCodeGenerator() {
		super();
	}

	protected final char getCharacterAt(String string, int length, int index) {
		return 0 <= index && index < length ? Character.toUpperCase(string
				.charAt(index)) : '\0';
	}

	public String toEncodedValue(String initialString) {
		int initialStringSize;
		if (initialString == null
				|| (initialStringSize = initialString.length()) == 0) {
			return "";
		}
		char currentChar = this.getCharacterAt(initialString,
				initialStringSize, 0);
		char nextChar = this
				.getCharacterAt(initialString, initialStringSize, 1);
		int initialStringIndex = 2;
		char[] encodedValue = new char[4];
		int encodedStringIndex = 0;
		switch (currentChar) {
		case 'A':
			if (nextChar == 'E') {
				encodedValue[encodedStringIndex++] = 'E';
				initialStringIndex = 4;
				break;
			}
		case 'E':
		case 'I':
		case 'O':
		case 'U':
			encodedValue[encodedStringIndex++] = currentChar;
			initialStringIndex = 3;
			break;
		case 'G':
		case 'K':
		case 'P':
			if (nextChar == 'N') {
				encodedValue[encodedStringIndex++] = 'N';
				initialStringIndex = 4;
			}
			break;
		case 'W':
			if (nextChar == 'R') {
				encodedValue[encodedStringIndex++] = 'R';
				initialStringIndex = 4;
			} else if (nextChar == 'H') {
				encodedValue[encodedStringIndex++] = 'W';
				initialStringIndex = 4;
			}
			break;
		case 'X':
			encodedValue[encodedStringIndex++] = 'S';
			initialStringIndex = 3;
			break;
		}
		if (initialStringIndex == 3) {
			currentChar = nextChar;
			nextChar = this.getCharacterAt(initialString, initialStringSize, 2);
		} else if (initialStringIndex == 4) {
			currentChar = this.getCharacterAt(initialString, initialStringSize,
					2);
			nextChar = this.getCharacterAt(initialString, initialStringSize, 3);
		}
		while (currentChar != '\0' && encodedStringIndex < 4) {
			if (currentChar != 'C' && currentChar == nextChar) {
				initialStringIndex++;
			} else {
				switch (currentChar) {
				case 'B':
				case 'F':
				case 'J':
				case 'K':
				case 'L':
				case 'N':
				case 'R':
					encodedValue[encodedStringIndex++] = currentChar;
					break;
				case 'C':
					if (nextChar == 'E' || nextChar == 'Y') {
						encodedValue[encodedStringIndex++] = 'S';
					} else if (nextChar == 'H') {
						encodedValue[encodedStringIndex++] = 'X';
					} else if (nextChar == 'I') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						encodedValue[encodedStringIndex++] = nextChar == 'A' ? 'X'
								: 'S';
					} else {
						if (nextChar == 'K') {
							nextChar = this.getCharacterAt(initialString,
									initialStringSize, initialStringIndex++);
						}
						encodedValue[encodedStringIndex++] = 'K';
					}
					break;
				case 'D':
					if (nextChar == 'G') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex + 1);
						if (nextChar == 'E' || nextChar == 'I'
								|| nextChar == 'Y') {
							encodedValue[encodedStringIndex++] = 'J';
						}
						nextChar = 'G';
						break;
					}
					encodedValue[encodedStringIndex++] = 'T';
					break;
				case 'G':
					if (nextChar == 'G') {
						encodedValue[encodedStringIndex++] = 'K';
					} else if (nextChar != 'N') {
						if (nextChar == 'H') {
							nextChar = this.getCharacterAt(initialString,
									initialStringSize, initialStringIndex++);
							if (nextChar == 'A' || nextChar == 'E'
									|| nextChar == 'I' || nextChar == 'O'
									|| nextChar == 'U') {
								break;
							}
						}
						encodedValue[encodedStringIndex++] = 'J';
					}
					break;
				case 'H':
					if (nextChar == 'A' || nextChar == 'E' || nextChar == 'I'
							|| nextChar == 'O' || nextChar == 'U') {
						char previousChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex - 3);
						if (previousChar == 'C' || previousChar == 'S'
								|| previousChar == 'P' || previousChar == 'T'
								|| previousChar == 'G') {
							encodedValue[encodedStringIndex++] = 'H';
						}
					}
					break;
				case 'M':
					encodedValue[encodedStringIndex++] = 'M';
					if (nextChar == 'B'
							&& initialStringIndex + 1 == initialStringSize) {
						nextChar = '\0';
					}
					break;
				case 'P':
					encodedValue[encodedStringIndex++] = nextChar == 'H' ? 'F'
							: 'P';
					break;
				case 'Q':
					encodedValue[encodedStringIndex++] = 'K';
					break;
				case 'S':
					if (nextChar == 'C') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						if (nextChar == 'H') {
							encodedValue[encodedStringIndex++] = 'S';
							encodedValue[encodedStringIndex++] = 'K';
						} else {
							nextChar = 'C';
							initialStringIndex--;
						}
					} else if (nextChar == 'H') {
						encodedValue[encodedStringIndex++] = 'X';
					} else if (nextChar == 'I') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						if (nextChar == 'A' || nextChar == 'O') {
							encodedValue[encodedStringIndex++] = 'X';
						}
					} else {
						encodedValue[encodedStringIndex++] = 'S';
					}
					break;
				case 'T':
					if (nextChar == 'H') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						encodedValue[encodedStringIndex++] = '0';
					} else if (nextChar == 'I') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						if (nextChar == 'A' || nextChar == 'O') {
							encodedValue[encodedStringIndex++] = 'X';
						}
					} else if (nextChar == 'C') {
						nextChar = this.getCharacterAt(initialString,
								initialStringSize, initialStringIndex++);
						if (nextChar == 'H') {
							encodedValue[encodedStringIndex++] = 'X';
						} else {
							nextChar = 'C';
							initialStringIndex--;
						}
					} else {
						encodedValue[encodedStringIndex++] = 'T';
					}
					break;
				case 'V':
					encodedValue[encodedStringIndex++] = 'F';
					break;
				case 'W':
				case 'Y':
					if (nextChar == 'A' || nextChar == 'E' || nextChar == 'I'
							|| nextChar == 'O' || nextChar == 'U') {
						encodedValue[encodedStringIndex++] = currentChar;
					}
					break;
				case 'X':
					encodedValue[encodedStringIndex++] = 'K';
					encodedValue[encodedStringIndex++] = 'S';
					break;
				case 'Z':
					encodedValue[encodedStringIndex++] = 'S';
					break;
				default:
				}
			}
			currentChar = nextChar;
			nextChar = this.getCharacterAt(initialString, initialStringSize,
					initialStringIndex++);
		}
		return new String(encodedValue);
	}

}