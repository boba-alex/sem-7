package lol;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Laba1_2 {

    private static final char firstSymbol = 'a';
    private static final char lastSymbol = 'z';

    private static String Vigenere(String text, String keyword) {
        keyword = keyword.toLowerCase();
        char[] txt = text.replaceAll("[.,:;!?]", "").toLowerCase().toCharArray();
        for (int i = 0; i < txt.length; i++) {
            if ((txt[i] <= lastSymbol) && (txt[i] >= firstSymbol)) {
                txt[i] = (char) ((keyword.charAt(i % keyword.length())
                        + txt[i] - 2 * firstSymbol)
                        % (lastSymbol - firstSymbol + 1) + firstSymbol);
            }
        }
        return new String(txt, 0, txt.length);
    }

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

    private static int Kasiska(String text, int l) {
        char[] txt = text.toCharArray();
        int result = -1;
        for (int i = 0; i < txt.length; i++) {
            for (int j = i + 1; j < txt.length; j++) {
                int equal = 0;
                while ((j + equal < txt.length) && (txt[i + equal] == txt[j + equal]) && (equal < l + 1) && (i + equal < j)) {
                    equal++;
                }
                if (equal == l) {
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

    private static String randomString(int length) {
        StringBuilder s = new StringBuilder(length);
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            s.append((char) (r.nextInt(lastSymbol - firstSymbol + 1) + firstSymbol));
        }
        return s.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(Vigenere("CRYPTOGRAPHY AND DATA SECURITY", "MOUSE"));
        System.out.println(randomString(5) + randomString(5));
        System.out.println(NOD(15, 25));
        String FullText = new Scanner(new FileReader("D:\\7 семестр\\sem-7\\KBRS\\Labs\\src\\main\\java\\lol\\text.txt")).nextLine();
        Random r = new Random();
        for (int i = 50; i <= 1000; i += 50) {
            double prob = 0;
            int n = 1000;
            for (int j = 0; j < n; j++) {
                int start = r.nextInt(FullText.length() - i);
                if (Kasiska(Vigenere(FullText.substring(start, start + i), "mouse"), 6) == 5) {
                    prob += 1;
                }
            }
            System.out.println(i + ": " + prob / n);
        }
        System.out.println();
        for (int i = 1; i < 15; i++) {
            double prob = 0;
            int n = 1000;
            for (int j = 0; j < n; j++) {
                int start = r.nextInt(FullText.length() - 450);
                if (Kasiska(Vigenere(FullText.substring(start, start + 450), randomString(i)), 6) == i) {
                    prob += 1;
                }
            }
            System.out.println(i + ": " + prob / n);
        }
    }

}
