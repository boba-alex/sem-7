package Lab1;

import Lab1.kasiski.KasiskiTest;
import Lab1.vigenere.VigenereCipher;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;

public class Application {

	private static boolean changed = false;
	private static final int ENGLISH_CHAR_AMOUNT = 26;
	private static String PATH_TO_FILES = System.getProperty("user.dir") + "/resources/Lab1";
	private static final int MIN_FREQ = 3;
	private static final int AMOUNT_BEST = 3;
	private static final String EMPTY = "";
	private static final double[] ENGLISH_CHAR_FREQ_ARRAY = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.0228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406,
			0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.0236, 0.0015, 0.01974, 0.00074};
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
				if (text.isEmpty()) {
					throw new Exception("There is no text!");
				}
				StringBuilder builder = new StringBuilder();
				List<String> keys = hack(text);
				int i = 0;
				for (String key : keys) {
					builder.append("encrypted text chance: ").append(i++).append('\n');
					builder.append(new String(VigenereCipher.decrypt(text, key)));
					builder.append('\n');
				}
				resulTextArea.setText(builder.toString());
				keyArea.setText(keys.toString());
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

	private static List<String> hack(String text) {

		List<Integer> keyLengths = KasiskiTest.getKeyLengths(text);
		System.out.println("Text length to hack : " + text.length());

		return KasiskiTest.getKeys(keyLengths, text);
	}

}