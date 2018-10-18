package Lab1.kasiski;

import Lab1.kasiski.frequency.FrequencyKeyBreaker;
import Lab1.kasiski.utils.VigenereStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class KasiskiTest {

	public static List<Integer> getKeyLengths(String encoded) {

		String encodedNormalized = VigenereStringUtils.normalizePlainText(encoded);

		out.println("Possibles key lengths are :");
		out.println("    For Babbage and kasiski:");

		final List<KeyLength> kasiskiKeys = new KasiskiKeyLengthEstimator().estimate(encodedNormalized);
		kasiskiKeys.sort(Comparator.comparingInt(KeyLength::getOccurrence).reversed());
		for (final KeyLength keyLength : kasiskiKeys) {
			final String toPrint = String.format("        - Key length: %2s  Occurences: %s", keyLength.getLength(), keyLength.getOccurrence());
			System.out.println(toPrint);
		}
		return kasiskiKeys.stream().map(KeyLength::getLength).collect(Collectors.toList());
	}

	public static List<String> getKeys(List<Integer> keyLengths, String cypherString) {

		final String encoded = VigenereStringUtils.normalizePlainText(cypherString);
		List<String> keys = new ArrayList<>();
		for (final Integer keyLength : keyLengths) {

			final String key = new FrequencyKeyBreaker().breakKey(encoded, keyLength);
			System.out.print("Key is: " + key);
			keys.add(key);
		}
		return keys;
	}

	public static int NOD(int a, int b) {

		while (a > 0 && b > 0) {
			if (a > b) {
				a %= b;
			} else {
				b %= a;
			}
		}
		return a + b;

	}

}
