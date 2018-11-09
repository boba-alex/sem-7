package Lab_1.final1;

import static Lab_1.final1.AlphabetUtils.FIRST_SYMBOL;
import static Lab_1.final1.AlphabetUtils.LAST_SYMBOL;

public class VigenereCipher {

	public static byte[] encrypt(String text, String key) {

		text = normalizePlainText(text);
		key = normalizePlainText(key);
		int keyLength = key.length();
		int[] shifts = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			shifts[i] = (int) key.charAt(i) - (int) FIRST_SYMBOL;
		}
		byte[] code = new byte[text.length()];
		int codeInt;
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (!(c >= FIRST_SYMBOL && c <= LAST_SYMBOL)) {
				code[i] = (byte) c;
			} else {
				codeInt = (int) text.charAt(i) + shifts[i % keyLength]; // Выбираю i-ый алфавит, так как периодический ключ
				if (codeInt > (int) LAST_SYMBOL) {
					codeInt = codeInt - (int) LAST_SYMBOL + (int) FIRST_SYMBOL - 1;
				}
				c = (char) (codeInt);
				code[i] = (byte) c;
			}
		}

		return code;
	}

	public static byte[] decrypt(String code, String key) {

		code = normalizePlainText(code);
		key = normalizePlainText(key);
		code = code.toLowerCase();
		int keyLength = key.length();
		int[] shifts = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			shifts[i] = (int) key.charAt(i) - (int) FIRST_SYMBOL;
		}
		byte[] text = new byte[code.length()];
		int textInt;
		char c;
		for (int i = 0; i < code.length(); i++) {
			c = code.charAt(i);
			if (!(c >= FIRST_SYMBOL && c <= LAST_SYMBOL)) {
				text[i] = (byte) c;
			} else {
				textInt = (int) code.charAt(i) - shifts[i % keyLength];
				if (textInt < (int) FIRST_SYMBOL) {
					textInt = (int) LAST_SYMBOL - ((int) FIRST_SYMBOL - textInt) + 1;
				}
				c = (char) (textInt);
				text[i] = (byte) c;
			}
		}

		return text;
	}

	private static String normalizePlainText(final String plaintText) {

		return plaintText.replaceAll("[.,:;!?]", "").toLowerCase();
	}

}
