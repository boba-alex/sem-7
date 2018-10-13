package Lab1;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class CodingViniger {

	private static boolean changed = false;
	private static final int CHAR_AMOUNT = 26;
	private static final int MIN_FREQ = 3;
	private static final int AMOUNT_BEST = 3;
	private static final double[] CHAR_FREQ_ARRAY = {0.8167, 0.01492, 0.02782, 0.04253, 0.12702, 0.0228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749,
			0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.0236, 0.0015, 0.01974, 0.00074};
	private static final char[] CHAR_ARRAY = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	public static void main(String[] args) {

		createGUI();
	}

	private static void createGUI() {

		JFrame frame = new JFrame();
		Font font = new Font("Papyrus", Font.PLAIN, 17);
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open file");
		JMenuItem saveItem = new JMenuItem("Save file");
		JMenuItem encryptItem = new JMenuItem("Encrypt file");
		JMenuItem decryptItem = new JMenuItem("Decrypt file");
		JMenuItem hackItem = new JMenuItem("Hack file");

		JTextArea resultArea = new JTextArea(), textArea = new JTextArea(), keyArea = new JTextArea();

		resultArea.setEditable(false);
		textArea.setEditable(false);
		keyArea.setEditable(false);
		resultArea.setLineWrap(true);
		textArea.setLineWrap(true);
		keyArea.setLineWrap(true);

		CaretListener caretListener = e -> changed = true;

		resultArea.addCaretListener(caretListener);

		fileMenu.setFont(font);
		openItem.setFont(font);
		saveItem.setFont(font);
		encryptItem.setFont(font);
		decryptItem.setFont(font);
		hackItem.setFont(font);

		openItem.addActionListener(e -> {

			JFileChooser fileopen = new JFileChooser(new File("d:\\University\\7 semester\\КБРС\\src\\"));
			int res = fileopen.showOpenDialog(null);
			if (res == JFileChooser.APPROVE_OPTION) {
				if (changed) {
					int option = JOptionPane.showConfirmDialog(frame, "The file has been changed. Do you want to save changes?", "Warning!", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						int ret = saveFile(textArea.getText(), resultArea.getText(), keyArea.getText());
						if (ret == JFileChooser.CANCEL_OPTION) {
							return;
						}
					}
				}

				File file = fileopen.getSelectedFile();
				try {
					FileReader fr = new FileReader(file);
					int c;
					String str = "";

					while (((c = fr.read()) != -1)) {
						str += (char) c;
					}

					textArea.setText(str);
					resultArea.setText("");
					keyArea.setText("");

					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				changed = false;

			}

		});

		saveItem.addActionListener(e -> {

			int res = saveFile(textArea.getText(), resultArea.getText(), keyArea.getText());
			if (res == JFileChooser.APPROVE_OPTION) {
				changed = false;
			}

		});

		encryptItem.addActionListener(e -> {

			byte[] str;
			try {
				String text = textArea.getText();
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				String key = JOptionPane.showInputDialog(null, "Enter a key:", "Key", JOptionPane.PLAIN_MESSAGE);
				if (key == null)
					return;
				else if (key.equals("")) {
					throw new Exception("You didn't enter a key!");
				}
				str = encrypt(textArea.getText(), key);
				resultArea.setText(new String(str));
				keyArea.setText(new String(key));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		});

		decryptItem.addActionListener(e -> {

			byte[] str;
			try {
				String text = textArea.getText();
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				String key = JOptionPane.showInputDialog(null, "Enter a key:", "Key", JOptionPane.PLAIN_MESSAGE);
				if (key == null)
					return;
				else if (key.equals("")) {
					throw new Exception("You didn't enter a key!");
				}
				str = decrypt(textArea.getText(), key);
				resultArea.setText(new String(str));
				keyArea.setText(new String(key));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		});

		hackItem.addActionListener(e -> {

			byte[] str;
			try {
				String l_gramm = JOptionPane.showInputDialog(null, "Enter value of l-gramm:", "L-GRAMM", JOptionPane.PLAIN_MESSAGE);
				String text = textArea.getText();
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				StringBuffer key = new StringBuffer();
				str = hack(text, Integer.parseInt(l_gramm), key);
				resultArea.setText(new String(str));
				keyArea.setText(new String(key));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		});

		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(encryptItem);
		fileMenu.add(decryptItem);
		fileMenu.add(hackItem);

		JLabel label1 = new JLabel("Result");
		JLabel label2 = new JLabel("Original text");
		JLabel botLabel = new JLabel("Key");

		label1.setFont(font);
		label2.setFont(font);
		botLabel.setFont(font);

		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel(new BorderLayout());
		JPanel botPanel = new JPanel(new BorderLayout());

		panel1.add(label2, BorderLayout.NORTH);
		panel1.add(new JScrollPane(textArea), BorderLayout.CENTER);

		panel2.add(label1, BorderLayout.NORTH);
		panel2.add(new JScrollPane(resultArea), BorderLayout.CENTER);
		panel2.add(botPanel, BorderLayout.SOUTH);

		botPanel.add(botLabel, BorderLayout.WEST);
		botPanel.add(new JScrollPane(keyArea), BorderLayout.CENTER);

		frame.setLayout(new GridLayout(2, 1));
		frame.setJMenuBar(menuBar);
		frame.add(panel1);
		frame.add(panel2);
		frame.setLocation(300, 30);
		frame.setSize(777, 700);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				if (changed) {
					int option = JOptionPane.showConfirmDialog(frame, "The file has been changed. Do you want to save changes?", "Warning!", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						int ret = saveFile(textArea.getText(), resultArea.getText(), keyArea.getText());
						if (ret == JFileChooser.APPROVE_OPTION) {
							System.exit(0);
						}
					} else {
						System.exit(0);
					}
				} else {
					System.exit(0);
				}

			}

		});

	}

	private static int saveFile(String decryptedText, String encryptedText, String keyText) {

		JFileChooser savefile = new JFileChooser(new File("d:\\University\\7 semester\\КБРС\\src\\"));
		int res = savefile.showSaveDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fw = new FileWriter(savefile.getSelectedFile());
				fw.write(encryptedText);
				fw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return res;

	}

	private static byte[] encrypt(String text, String key) throws UnsupportedEncodingException {

		System.out.println(text.length());
		text = new String(text.toLowerCase());
		int keyLeng = key.length();
		int[] shifts = new int[keyLeng];
		for (int i = 0; i < keyLeng; i++) {
			shifts[i] = (int) key.charAt(i) - (int) 'a';
		}
		byte[] code = new byte[text.length()];
		int codeInt = 0;
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (!(c >= 'a' && c <= 'z')) {
				code[i] = (byte) c;
			} else {
				codeInt = (int) text.charAt(i) + shifts[i % keyLeng];
				if (codeInt > (int) 'z') {
					codeInt = codeInt - (int) 'z' + (int) 'a' - 1;
				}
				c = (char) (codeInt);
				code[i] = (byte) c;
			}
		}

		return code;

	}

	private static byte[] decrypt(String code, String key) {

		code = new String(code.toLowerCase());
		int keyLeng = key.length();
		int[] shifts = new int[keyLeng];
		for (int i = 0; i < keyLeng; i++) {
			shifts[i] = (int) key.charAt(i) - (int) 'a';
		}
		byte[] text = new byte[code.length()];
		int textInt = 0;
		char c;
		for (int i = 0; i < code.length(); i++) {
			c = code.charAt(i);
			if (!(c >= 'a' && c <= 'z')) {
				text[i] = (byte) c;
			} else {
				textInt = (int) code.charAt(i) - shifts[i % keyLeng];
				if (textInt < (int) 'a') {
					textInt = (int) 'z' - ((int) 'a' - textInt) + 1;
				}
				text[i] = (byte) (char) (textInt);
			}
		}

		return text;
	}

	private static byte[] hack(String text, int l_gramm, StringBuffer key) {

		text = new String(text.toLowerCase());
		int keyLength = getKeyLength(text, l_gramm);

		key.append(getKey(keyLength, text));

		return decrypt(text, key.toString());
	}

	private static int getKeyLength(String text, int l_gramm) {

		int length = text.length();
		boolean flag;
		int del = -1, minDel = -1;
		int dist = -1;

		for (int i = 0; i < length - 2 * l_gramm; i++) {
			for (int j = i + l_gramm; j < length - l_gramm; j++) {
				flag = true;
				for (int k = 0; k < l_gramm; k++) {
					if (text.charAt(i + k) != text.charAt(j + k)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					dist = j - i;
					if (del == -1) {
						del = dist;
					} else {
						del = NOD(del, dist);
					}
				}
			}
			if (minDel == -1 || minDel > del) {
				minDel = del;
			}
		}
		return minDel;

	}

	private static String getKey(int keyLength, String text) {

		double freq[] = new double[CHAR_AMOUNT];
		int length = text.length();
		int amount, indexC, intC;
		double max;
		char curC;
		StringBuffer key = new StringBuffer();
		for (int k = 0; k < keyLength; k++) {
			for (int i = 0; i < CHAR_AMOUNT; i++) {
				freq[i] = 0;
			}
			max = -1;
			indexC = -1;
			amount = (length / keyLength) + (((length % keyLength) > k) ? 1 : 0);
			for (int i = k; i < length; i += keyLength) {
				curC = text.charAt(i);
				for (int j = 0; j < CHAR_AMOUNT; j++) {
					if (curC == (char) ((int) 'a' + j)) {
						freq[j]++;
						if (max < freq[j]) {
							max = freq[j];
							indexC = j;
						}
						break;
					}
				}
			}
			intC = indexC + 2 * (int) 'a' - (int) 'e';
			if (intC > (int) 'z') {
				intC = intC - (int) 'z' + (int) 'a' - 1;
			}
			if (intC < (int) 'a') {
				intC = (int) 'z' - ((int) 'a' - intC) + 1;
			}
			key.append((char) intC);
		}
		return key.toString();

	}

	private static int NOD(int a, int b) {

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