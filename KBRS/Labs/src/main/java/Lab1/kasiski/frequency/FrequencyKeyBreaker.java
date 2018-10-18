package Lab1.kasiski.frequency;

import Lab1.kasiski.frequency.analysis.FrequencyAnalysis;
import Lab1.kasiski.frequency.analysis.impl.EnglishFrequencyAnalysis;
import Lab1.kasiski.utils.VigenereStringUtils;
import Lab1.vigenere.VigenereCipher;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.IntStream;

public class FrequencyKeyBreaker {

	private final FrequencyAnalysis analysis = new EnglishFrequencyAnalysis();

	/**
	 * Find the key of a vigenere. Since we know the key length, we can split our text into X columns (X being the key length).
	 * And trying to frequency analysis on each column with char from a to z. Keeping the best match on each column.
	 * This will give us the key.
	 *
	 * @param cypher
	 * @param keyLength
	 * @return
	 */
	public String breakKey(final CharSequence cypher, final Integer keyLength) {

		return IntStream.range(0, keyLength)
				// For each index from 0 to keylength -1, we generate a subtext composed of one letter peeked every X chars (where X is the keylength)
				.mapToObj(shift -> VigenereStringUtils.peekAndLeap(keyLength, shift, cypher))
				// For this subtring, we find the alphabetical character that would the better decrypt the vigenere.
				.map(this::findBestShiftToMatchEnglishFrequency).map(Map.Entry::getKey)
				// Assemble all the chars to create the key
				.reduce((i, j) -> String.join("", i, j)).get();
	}

	/**
	 * Goes through all possible character 'from a to z' and try to find the best to decrypt, so that the result would
	 * match english letter frequency as much as possible.
	 *
	 * @param subText
	 * @return
	 */
	Map.Entry<String, Double> findBestShiftToMatchEnglishFrequency(final String subText) {
		// For each characters in 'a' to 'z'
		return IntStream.rangeClosed(0, 25)
				//We uncypher the text with vigenere (with a key of length 1, so it's caesar) and calculate the deviation from english letter frequency
				.mapToObj(i -> {
					final String currentShift = Character.toString((char) ('a' + i));
					final String uncyphered = new String(VigenereCipher.decrypt(subText, currentShift));

					return new AbstractMap.SimpleEntry<>(currentShift, analysis.deviationFromLanguage(uncyphered));
				})
				// the best match is our minimum deviation, so we keep this one only
				.min(Comparator.comparingDouble(Map.Entry::getValue)).get();
	}

}
