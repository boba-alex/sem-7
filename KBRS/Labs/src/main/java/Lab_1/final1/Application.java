package Lab_1.final1;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import static Lab_1.final1.KasiskiTest.kasiskiTest;

public class Application {

	private static final int ENGLISH_CHAR_AMOUNT = 26;
	private static final int MIN_FREQ = 3;
	private static final int AMOUNT_BEST = 3;
	private static final String EMPTY = "";
	private static final double[] ENGLISH_CHAR_FREQ_ARRAY = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.0228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406,
			0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.0236, 0.0015, 0.01974, 0.00074};
	private static final char[] CHAR_ARRAY = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private static boolean changed = false;
	private static String PATH_TO_FILES = System.getProperty("user.dir") + "/resources/Lab1";

	public static void main(String[] args) {

		createGUI();
	}

	private static void createGUI() {

		JFrame frame = new JFrame();
		Font font = new Font("Default", Font.PLAIN, 17);
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open file");
		JMenuItem saveItem = new JMenuItem("Save file");
		JMenuItem encryptItem = new JMenuItem("Encrypt file");
		JMenuItem decryptItem = new JMenuItem("Decrypt file");
		JMenuItem hackItem = new JMenuItem("Hack file");
		JMenuItem kasiskiTestItem = new JMenuItem("Kasiski test");

		JTextArea originalTextArea = new JTextArea(), resulTextArea = new JTextArea(), keyArea = new JTextArea();

		originalTextArea.setEditable(true);
		resulTextArea.setEditable(false);
		keyArea.setEditable(false);
		originalTextArea.setLineWrap(true);
		resulTextArea.setLineWrap(true);
		keyArea.setLineWrap(true);

		CaretListener caretListener = e -> changed = true;

		resulTextArea.addCaretListener(caretListener);

		fileMenu.setFont(font);
		openItem.setFont(font);
		saveItem.setFont(font);
		encryptItem.setFont(font);
		decryptItem.setFont(font);
		hackItem.setFont(font);
		kasiskiTestItem.setFont(font);

		openItem.addActionListener(e -> {

			JFileChooser fileopen = new JFileChooser(PATH_TO_FILES);
			int res = fileopen.showOpenDialog(null);
			if (res == JFileChooser.APPROVE_OPTION) {
				if (changed) {
					int option = JOptionPane.showConfirmDialog(frame, "The file has been changed. Do you want to save changes?", "Warning!", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						int ret = saveFile(originalTextArea.getText(), resulTextArea.getText(), keyArea.getText());
						if (ret == JFileChooser.CANCEL_OPTION) {
							return;
						}
					}
				}

				File file = fileopen.getSelectedFile();
				try {
					FileReader fr = new FileReader(file);
					int c;
					String str = EMPTY;

					while (((c = fr.read()) != -1)) {
						str += (char) c;
					}

					originalTextArea.setText(str);
					resulTextArea.setText(EMPTY);
					keyArea.setText(EMPTY);

					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				changed = false;
			}

		});

		saveItem.addActionListener(e -> {

			int res = saveFile(originalTextArea.getText(), resulTextArea.getText(), keyArea.getText());
			if (res == JFileChooser.APPROVE_OPTION) {
				changed = false;
			}

		});

		encryptItem.addActionListener(e -> {

			byte[] str;
			try {
				String text = originalTextArea.getText();
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				String key = JOptionPane.showInputDialog(null, "Enter a key:", "Key", JOptionPane.PLAIN_MESSAGE);
				if (key == null)
					return;
				else if (key.equals(EMPTY)) {
					throw new Exception("You didn't enter a key!");
				}
				str = VigenereCipher.encrypt(originalTextArea.getText(), key);
				resulTextArea.setText(new String(str));
				keyArea.setText(key);
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
				String text = originalTextArea.getText();
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				String key = JOptionPane.showInputDialog(null, "Enter a key:", "Key", JOptionPane.PLAIN_MESSAGE);
				if (key == null)
					return;
				else if (key.equals(EMPTY)) {
					throw new Exception("You didn't enter a key!");
				}
				str = VigenereCipher.decrypt(originalTextArea.getText(), key);
				resulTextArea.setText(new String(str));
				keyArea.setText(key);
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
				String text = originalTextArea.getText();
				String l_gramm = JOptionPane.showInputDialog(null, "Enter value of l-gramm:", "L-GRAMM", JOptionPane.PLAIN_MESSAGE);
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}

				int length = hack(text, Integer.parseInt(l_gramm));

				resulTextArea.setText("Key length : " + length);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		});

		kasiskiTestItem.addActionListener(e -> {

			byte[] str;
			try {
				String text = originalTextArea.getText();
				//String l_gramm = JOptionPane.showInputDialog(null, "Enter value of l-gramm:", "L-GRAMM", JOptionPane.PLAIN_MESSAGE);
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}

				String result = kasiskiTest(text);

				resulTextArea.setText("Result : \n" + result);
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
		fileMenu.add(kasiskiTestItem);

		JLabel labelOriginalText = new JLabel("Original text");
		JLabel labelResultText = new JLabel("Result text");
		JLabel labelKey = new JLabel("Key");

		labelOriginalText.setFont(font);
		labelResultText.setFont(font);
		labelKey.setFont(font);

		JPanel panelOriginalText = new JPanel(new BorderLayout());
		JPanel panelResultText = new JPanel(new BorderLayout());
		JPanel panelKey = new JPanel(new BorderLayout());

		panelOriginalText.add(labelOriginalText, BorderLayout.NORTH);
		panelOriginalText.add(new JScrollPane(originalTextArea), BorderLayout.CENTER);

		panelResultText.add(labelResultText, BorderLayout.NORTH);
		panelResultText.add(new JScrollPane(resulTextArea), BorderLayout.CENTER);
		panelResultText.add(panelKey, BorderLayout.SOUTH);

		panelKey.add(labelKey, BorderLayout.WEST);
		panelKey.add(new JScrollPane(keyArea), BorderLayout.CENTER);

		frame.setLayout(new GridLayout(2, 1));
		frame.setJMenuBar(menuBar);
		frame.add(panelOriginalText);
		frame.add(panelResultText);
		frame.setLocation(300, 30);
		frame.setSize(777, 700);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				if (changed) {
					int option = JOptionPane.showConfirmDialog(frame, "The file has been changed. Do you want to save changes?", "Warning!", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						int ret = saveFile(originalTextArea.getText(), resulTextArea.getText(), keyArea.getText());
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

		JFileChooser savefile = new JFileChooser(PATH_TO_FILES);
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

	private static int hack(String text, int l_gramm) {

		int length = KasiskiTest.getKeyLength(text, l_gramm);
		System.out.println("Text length to hack : " + text.length());

		return length;
	}

	//	private static byte[] hack(String text, int l_gramm, StringBuffer key) {
	//
	//		text = new String(text.toLowerCase());
	//		int keyLength = getKeyLength(text, l_gramm);
	//
	//		key.append(getKey(keyLength, text));
	//
	//		try {
	//			return decrypt(text, key.toString());
	//		} catch (UnsupportedEncodingException e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//
	//	}

	//	private static int getKeyLength(String text, int l_gramm) {
	//
	//		int length = text.length();
	//		boolean flag;
	//		int del = -1, minDel = -1;
	//		int dist = -1;
	//
	//		for (int i = 0; i < length - 2 * l_gramm; i++) {
	//			for (int j = i + l_gramm; j < length - l_gramm; j++) {
	//				flag = true;
	//				for (int k = 0; k < l_gramm; k++) {
	//					if (text.charAt(i + k) != text.charAt(j + k)) {
	//						flag = false;
	//						break;
	//					}
	//				}
	//				if (flag) {
	//					dist = j - i;
	//					if (del == -1) {
	//						del = dist;
	//					} else {
	//						del = NOD(del, dist);
	//					}
	//				}
	//			}
	//			if (minDel == -1 || minDel > del) {
	//				minDel = del;
	//			}
	//		}
	//		return minDel;
	//
	//	}

	//	private static String getKey(int keyLength, String text) {
	//
	//		double freq[] = new double[CHAR_AMOUNT];
	//		int length = text.length();
	//		int amount, indexC, intC;
	//		double max;
	//		char curC;
	//		StringBuffer key = new StringBuffer();
	//		for (int k = 0; k < keyLength; k++) {
	//			for (int i = 0; i < CHAR_AMOUNT; i++) {
	//				freq[i] = 0;
	//			}
	//			max = -1;
	//			indexC = -1;
	//			amount = (length / keyLength) + (((length % keyLength) > k) ? 1 : 0);
	//			for (int i = k; i < length; i += keyLength) {
	//				curC = text.charAt(i);
	//				for (int j = 0; j < CHAR_AMOUNT; j++) {
	//					if (curC == (char) ((int) 'a' + j)) {
	//						freq[j]++;
	//						if (max < freq[j]) {
	//							max = freq[j];
	//							indexC = j;
	//						}
	//						break;
	//					}
	//				}
	//			}
	//			intC = indexC + 2 * (int) 'a' - (int) 'e';
	//			if (intC > (int) 'z') {
	//				intC = intC - (int) 'z' + (int) 'a' - 1;
	//			}
	//			if (intC < (int) 'a') {
	//				intC = (int) 'z' - ((int) 'a' - intC) + 1;
	//			}
	//			key.append((char) intC);
	//		}
	//		return key.toString();
	//
	//	}
	//
	//	private static int NOD(int a, int b) {
	//
	//		while (a > 0 && b > 0) {
	//			if (a > b) {
	//				a %= b;
	//			} else {
	//				b %= a;
	//			}
	//		}
	//		return a + b;
	//
	//	}

}