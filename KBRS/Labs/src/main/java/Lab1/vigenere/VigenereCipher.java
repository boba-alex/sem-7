package Lab1.vigenere;

import Lab1.kasiski.utils.VigenereStringUtils;

public class VigenereCipher {

	public static byte[] encrypt(String text, String key) {

		System.out.println("\n--------------------");
		System.out.println("\nKey : " + key);
		text = VigenereStringUtils.normalizePlainText(text);
		key = VigenereStringUtils.normalizePlainText(key);
		int keyLength = key.length();
		int[] shifts = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			shifts[i] = (int) key.charAt(i) - (int) 'a';
		}
		byte[] code = new byte[text.length()];
		int codeInt;
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (!(c >= 'a' && c <= 'z')) {
				code[i] = (byte) c;
			} else {
				codeInt = (int) text.charAt(i) + shifts[i % keyLength]; // Выбираю i-ый алфавит, так как периодический ключ
				if (codeInt > (int) 'z') {
					codeInt = codeInt - (int) 'z' + (int) 'a' - 1;
				}
				c = (char) (codeInt);
				code[i] = (byte) c;
			}
		}

		return code;
	}

	public static byte[] decrypt(String code, String key) {

		code = VigenereStringUtils.normalizePlainText(code);
		key = VigenereStringUtils.normalizePlainText(key);
		code = code.toLowerCase();
		int keyLength = key.length();
		int[] shifts = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			shifts[i] = (int) key.charAt(i) - (int) 'a';
		}
		byte[] text = new byte[code.length()];
		int textInt;
		char c;
		for (int i = 0; i < code.length(); i++) {
			c = code.charAt(i);
			if (!(c >= 'a' && c <= 'z')) {
				text[i] = (byte) c;
			} else {
				textInt = (int) code.charAt(i) - shifts[i % keyLength];
				if (textInt < (int) 'a') {
					textInt = (int) 'z' - ((int) 'a' - textInt) + 1;
				}
				c = (char) (textInt);
				text[i] = (byte) c;
			}
		}

		return text;
	}

}
