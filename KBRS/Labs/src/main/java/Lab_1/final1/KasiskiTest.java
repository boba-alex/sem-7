package Lab_1.final1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

import static Lab_1.final1.AlphabetUtils.FIRST_SYMBOL;
import static Lab_1.final1.AlphabetUtils.LAST_SYMBOL;

public class KasiskiTest {

	private static int NOD(int a, int b) {

		while (a != 0 && b != 0) {
			if (a > b) {
				a %= b;
			} else {
				b %= a;
			}
		}
		return a + b;
	}

	public static int getKeyLength(String text, int l_gramm) {

		char[] txt = text.toCharArray();
		int result = -1;
		for (int i = 0; i < txt.length; i++) {
			for (int j = i + 1; j < txt.length; j++) {
				int equal = 0;
				while ((j + equal < txt.length) && (txt[i + equal] == txt[j + equal]) && (equal < l_gramm + 1) && (i + equal < j)) {
					equal++;
				}
				if (equal == l_gramm) {
					if (result == -1) {
						result = j - i;
					} else {
						result = NOD(result, j - i);
					}
				}
			}
		}
		return result;
	}

	private static String generateRandomString(int length) {

		StringBuilder s = new StringBuilder(length);
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			s.append((char) (r.nextInt(LAST_SYMBOL - FIRST_SYMBOL + 1) + FIRST_SYMBOL));
		}
		return s.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println(new String(VigenereCipher.encrypt("CRYPTOGRAPHY AND DATA SECURITY", "MOUSE")));
		System.out.println(new String(VigenereCipher.decrypt("ofshxaulsttm srp xsxm mwggfclc", "MOUSE")));
		System.out.println(getKeyLength(
				"lfwkimjclpsiswkhjoglkmvguragkmkmxmamjcvxwuylggiiswalxaeycxmfkmkbqbdclaeflfwkimjcguzugskeczgbwymoacfvmqkyfwxtwmlaidoyqbwfgksdiulqgvsyhjavefwblaeflfwkimjcfhsnnggnwpwdavmqfaaxwfzcxbvelkwmlavgkyedemjxhuxdavyxl",
				8));

		String text = new Scanner(new FileReader("C:\\Users\\apoloshchuk\\Desktop\\sem-7\\KBRS\\Labs\\src\\main\\java\\lol\\text.txt")).nextLine();
		System.out.println(kasiskiTest(text));

	}

	public static String getKeyLengthsByTextLength(String text, String key) {

		Random r = new Random();
		StringBuilder builder = new StringBuilder("TextLength : Probability of success \n");;
		for (int i = 50; i <= 1000; i += 50) {
			double probability = 0;
			int n = 1000;
			for (int j = 0; j < n; j++) {
				int start = r.nextInt(text.length() - i);
				if (getKeyLength(new String(VigenereCipher.encrypt(text.substring(start, start + i), key)), 6) == key.length()) {
					probability += 1;
				}
			}
			builder.append(i + ": " + probability / n + "\n");
		}
		return builder.toString();
	}

	;

	public static String getKeyLengthsByKeyLength(String text, int l_gramm) {

		Random r = new Random();
		StringBuilder builder = new StringBuilder("KeyLength : Probability of success\n");
		for (int i = 1; i < 15; i++) {
			double probability = 0;
			int n = 1000;
			for (int j = 0; j < n; j++) {
				int start = r.nextInt(text.length() - 450);
				if (getKeyLength(new String(VigenereCipher.encrypt(text.substring(start, start + 450), generateRandomString(i))), l_gramm) == i) {
					probability += 1;
				}
			}
			builder.append(i + ": " + probability / n + "\n");
		}
		return builder.toString();
	}

	public static String kasiskiTest(String text) throws FileNotFoundException {

		return getKeyLengthsByTextLength(text, "mouse") + getKeyLengthsByKeyLength(text, 6);
	}

	;
}
