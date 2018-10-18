package Lab1.kasiski.frequency.analysis;

import Lab1.exception.MyException;
import Lab1.kasiski.utils.VigenereStringUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public abstract class FrequencyAnalysis {

	private final Map<String, Double> letterFrequency;

	protected FrequencyAnalysis(final Map<String, Double> letterFrequency) {

		if (Objects.isNull(letterFrequency)) {
			throw new MyException("letterFrequency is null");
		}
		if (letterFrequency.size() != 26) {
			throw new MyException("letterFrequency Map has to contains 26 characters.");
		}
		final Double sumOfFrequency = letterFrequency.values().stream().mapToDouble(Double::doubleValue).sum();
		if ((sumOfFrequency > 0.97D) && (sumOfFrequency < 1.03D)) {
			this.letterFrequency = letterFrequency;
		} else {
			throw new MyException("Sum of the frequency have to be close to one, '" + sumOfFrequency + "' given.");
		}

	}

	/**
	 * Calculate the difference between english letter frequency and the given text letter frequencies.
	 * value between 0 and 1 (0 means the text match the english's characters frequency perfectly).
	 */
	public Double deviationFromLanguage(final String text) {
		// Count how much time each character is repeated
		final Map<String, Long> textFrequencies = VigenereStringUtils.countRepetitionByCharacters(text);

		// If a character was missing from the table, add it with 0 occurrences.
		for (char c = 'a'; c <= 'z'; ++c) {
			final String currentChar = String.valueOf(c);
			if (!textFrequencies.containsKey(currentChar)) {
				textFrequencies.put(currentChar, 0L);
			}
		}

		return textFrequencies.entrySet().stream()
				// Turn character count into their frequency (based on total amount of letters in text)
				.map(entry -> {
					final Double charFrequency = text.length() > 0 ? entry.getValue() / (double) text.length() : 0;
					return new AbstractMap.SimpleEntry<>(entry.getKey(), charFrequency);
				})
				// Then calculate difference between english letters occurrence frequency and our frequency
				.mapToDouble(entry -> {
					final String character = entry.getKey();
					final Double frequency = entry.getValue();
					// Delta between english frequency and actual frequency
					final Double difference = Math.abs(frequency - letterFrequency.get(character));
					// Add a weight to the return relative to the english frequency of the character.
					return difference / (1 + letterFrequency.get(character));
				}).sum();
	}

}
