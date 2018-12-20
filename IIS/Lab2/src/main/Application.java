package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application extends JFrame {

	private String[] indicationNames; //n
	private String[] classNames;//l
	private boolean classes[][][];
	private double[][] bit;
	private double[][] ait;
	private double[] bt;
	private int t;
	private int l;

	public static void main(String[] args) {

		new Application().createGUI();
	}

	public void createGUI() {

		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(5, 1));
		//original data
		JPanel panelOriginalData = new JPanel(new BorderLayout());
		panelOriginalData.add(new JLabel("Исходные данные"), BorderLayout.NORTH);
		JTextArea textAreaOriginalData = new JTextArea();
		textAreaOriginalData.setEditable(false);
		panelOriginalData.add(textAreaOriginalData, BorderLayout.CENTER);
		//b_it and b_t
		JPanel panelBitAndBt = new JPanel(new BorderLayout());
		//b_it
		JPanel panelBit = new JPanel(new BorderLayout());
		panelBit.add(new JLabel("Параметры b_it"), BorderLayout.NORTH);
		JTextArea textAreaBit = new JTextArea();
		textAreaBit.setEditable(false);
		panelBit.add(textAreaBit, BorderLayout.CENTER);
		//b_t
		JPanel panelBt = new JPanel(new BorderLayout());
		panelBt.add(new JLabel("Параметры b_t"), BorderLayout.NORTH);
		JTextArea textAreaBt = new JTextArea();
		textAreaBt.setEditable(false);
		panelBt.add(textAreaBt, BorderLayout.CENTER);
		panelBitAndBt.add(panelBit, BorderLayout.CENTER);
		panelBitAndBt.add(panelBt, BorderLayout.SOUTH);
		//a_it
		JPanel panelAit = new JPanel(new BorderLayout());
		panelAit.add(new JLabel("Параметры a_it"), BorderLayout.NORTH);
		JTextArea textAreaAit = new JTextArea();
		textAreaAit.setEditable(false);
		panelAit.add(textAreaAit, BorderLayout.CENTER);
		//input
		JPanel panelInput = new JPanel(new BorderLayout());
		panelInput.add(new JLabel("Объект для распознавания"), BorderLayout.NORTH);
		JTextField inputTextField = new JTextField();
		panelInput.add(inputTextField, BorderLayout.CENTER);
		JButton buttonFindClass = new JButton("Найти класс");
		panelInput.add(buttonFindClass, BorderLayout.SOUTH);
		//output
		JPanel panelOutput = new JPanel(new GridLayout(4, 1));
		panelOutput.add(new JLabel("maxMuAki - величина принадлежности объекта классу"));
		JTextField muTextField = new JTextField();
		muTextField.setEditable(false);
		panelOutput.add(muTextField);
		panelOutput.add(new JLabel("Класс объекта"));
		JTextField classNameTextField = new JTextField();
		classNameTextField.setEditable(false);
		panelOutput.add(classNameTextField);
		//add panels
		frame.add(panelOriginalData);
		frame.add(panelBitAndBt);
		frame.add(panelAit);
		frame.add(panelInput);
		frame.add(panelOutput);
		//frame.add(panelSteps);
		frame.setLocation(300, 30);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Файл");
		JMenuItem openFileItem = new JMenuItem("Открыть");
		menu.add(openFileItem);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		openFileItem.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("./resources"));
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						Scanner sc = new Scanner(chooser.getSelectedFile());
						t = Integer.valueOf(sc.nextLine());
						indicationNames = new String[t];
						String indications = sc.nextLine();
						StringTokenizer st = new StringTokenizer(indications);
						int i1 = 0;
						while (st.hasMoreTokens()) {
							indicationNames[i1] = st.nextToken();
							i1++;
						}
						l = Integer.parseInt(sc.nextLine());
						classes = new boolean[l][][];
						classNames = new String[l];
						bit = new double[l][t];
						ait = new double[l][t];
						bt = new double[t];

						for (int i = 0; i < l; i++) {
							classNames[i] = sc.nextLine();
							int numberOfItems = Integer.valueOf(sc.nextLine());
							classes[i] = new boolean[numberOfItems][t];
							//jTextArea1.append(names[i] + ":");
							for (int j = 0; j < numberOfItems; j++) {
								sc.nextLine();//miss name of subclass
								StringTokenizer st1 = new StringTokenizer(sc.nextLine());
								int k = 0;
								while (st1.hasMoreTokens()) {
									classes[i][j][k] = st1.nextToken().equals("1");
									bit[i][k] += classes[i][j][k] ? 1. : 0.;
									k++;
								}
							}

							for (int k = 0; k < t; k++) {
								bit[i][k] /= numberOfItems;
							}
							for (int k = 0; k < t; k++) {
								bit[i][k] /= numberOfItems;
								bt[k] += bit[i][k];
							}
						}
						for (int k = 0; k < t; k++) {
							bt[k] /= l;
							for (int i = 0; i < l; i++) {
								ait[i][k] = Math.abs(bit[i][k] - bt[k]);
							}
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		frame.setVisible(true);

		buttonFindClass.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//find class
				try {
					Boolean[] obj = Arrays.stream(inputTextField.getText().split(" ")).map("1"::equals).toArray(Boolean[]::new);
					double maxMuAki = -1;
					int maxI = -1;
					for (int i = 0; i < classes.length; i++) {
						double sumA = 0;
						for (int k = 0; k < t; k++) {
							sumA += ait[i][k];
						}
						double maxMuXi = 0;
						for (int j = 0; j < classes[i].length; j++) {
							double mu = 0;
							for (int k = 0; k < t; k++) {
								mu += (classes[i][j][k] == obj[k] ? 1 : -1) * ait[i][k];
							}
							mu /= sumA;
							mu = Math.max(0, mu);
							if (mu > maxMuXi) {
								maxMuXi = mu;
							}
						}
						//jTextField3.setText(jTextField3.getText() + limit(String.valueOf(maxMu)) + "  ");
						if (maxMuXi > maxMuAki) {
							maxMuAki = maxMuXi;
							maxI = i;
						}
					}
					classNameTextField.setText(classNames[maxI]);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Неверный формат ввода!", null, JOptionPane.ERROR_MESSAGE);
					Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}
}