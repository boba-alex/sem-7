package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
	private JCheckBox[] checkBoxes;
	private JPanel panelWithIndications = new JPanel(new FlowLayout());
	;

	public static void main(String[] args) {

		new Application().createGUI();
	}

	public void createGUI() {

		setLayout(new GridLayout(5, 1));
		//original data
		JPanel panelOriginalData = new JPanel(new BorderLayout());
		panelOriginalData.add(new JLabel("Исходные данные"), BorderLayout.NORTH);
		JTextArea textAreaOriginalData = new JTextArea();
		textAreaOriginalData.setEditable(false);
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(textAreaOriginalData);
		panelOriginalData.add(jScrollPane1, BorderLayout.CENTER);
		//b_it and b_t
		JPanel panelBitAndBt = new JPanel(new BorderLayout());
		//b_it
		JPanel panelBit = new JPanel(new BorderLayout());
		panelBit.add(new JLabel("Параметры b_it"), BorderLayout.NORTH);
		JTextArea textAreaBit = new JTextArea();
		textAreaBit.setEditable(false);
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setViewportView(textAreaBit);
		panelBit.add(jScrollPane2, BorderLayout.CENTER);
		//b_t
		JPanel panelBt = new JPanel(new BorderLayout());
		panelBt.add(new JLabel("Параметры b_t"), BorderLayout.NORTH);
		JTextArea textAreaBt = new JTextArea();
		textAreaBt.setEditable(false);
		JScrollPane jScrollPane3 = new JScrollPane();
		jScrollPane3.setViewportView(textAreaBt);
		panelBt.add(jScrollPane3, BorderLayout.CENTER);
		panelBitAndBt.add(panelBit, BorderLayout.CENTER);
		panelBitAndBt.add(panelBt, BorderLayout.SOUTH);
		//a_it
		JPanel panelAit = new JPanel(new BorderLayout());
		panelAit.add(new JLabel("Параметры a_it"), BorderLayout.NORTH);
		JTextArea textAreaAit = new JTextArea();
		textAreaAit.setEditable(false);
		JScrollPane jScrollPane4 = new JScrollPane();
		jScrollPane4.setViewportView(textAreaAit);
		panelAit.add(jScrollPane4, BorderLayout.CENTER);
		//input
		JPanel panelInput = new JPanel(new BorderLayout());
		panelInput.add(new JLabel("Объект для распознавания"), BorderLayout.NORTH);
		//JTextField inputTextField = new JTextField();
		//panelInput.add(inputTextField, BorderLayout.CENTER);
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
		add(panelOriginalData);
		add(panelBitAndBt);
		add(panelAit);
		add(panelInput);
		add(panelOutput);
		//frame.add(panelSteps);
		setLocation(300, 30);
		setSize(700, 700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Файл");
		JMenuItem openFileItem = new JMenuItem("Открыть");
		menu.add(openFileItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		openFileItem.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("./resources"));
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						textAreaOriginalData.setText("");
						textAreaBit.setText("");
						textAreaBt.setText("");
						textAreaAit.setText("");
						muTextField.setText("");
						classNameTextField.setText("");
						List<String> strings = Files.readAllLines(Paths.get(chooser.getSelectedFile().getPath()));
						for (String s : strings) {
							textAreaOriginalData.append(s + "\n");
						}

						Scanner sc = new Scanner(chooser.getSelectedFile());
						t = Integer.valueOf(sc.nextLine());
						indicationNames = new String[t];
						String indications = sc.nextLine();
						StringTokenizer st = new StringTokenizer(indications);
						int k1 = 0;
						while (st.hasMoreTokens()) {
							indicationNames[k1] = st.nextToken();
							k1++;
						}
						//
						panelInput.remove(panelWithIndications);
						panelWithIndications = new JPanel(new FlowLayout());
						checkBoxes = new JCheckBox[t];
						for (int k = 0; k < t; k++) {
							checkBoxes[k] = new JCheckBox(indicationNames[k]);
							panelWithIndications.add(checkBoxes[k]);
						}
						panelInput.add(panelWithIndications, BorderLayout.CENTER);
						revalidate();
						//
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

							textAreaBit.append(classNames[i] + " : ");
							for (int k = 0; k < t; k++) {
								bit[i][k] /= numberOfItems;
								textAreaBit.append("  " + bit[i][k]);
								bt[k] += bit[i][k];
							}
							textAreaBit.append("\n");
						}
						for (int k = 0; k < t; k++) {
							bt[k] /= l;
							textAreaBt.append(bt[k] + "  ");
							for (int i = 0; i < l; i++) {
								ait[i][k] = Math.abs(bit[i][k] - bt[k]);
								textAreaAit.append(ait[i][k] + "  ");
							}
							textAreaAit.append("\n");
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		setVisible(true);

		buttonFindClass.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//find class
				try {
					//Boolean[] obj = Arrays.stream(inputTextField.getText().split(" ")).map("1"::equals).toArray(Boolean[]::new);
					//признаки
					Boolean[] indications = new Boolean[t];
					for (int k1 = 0; k1 < t; k1++) {
						indications[k1] = checkBoxes[k1].isSelected();
					}

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
								mu += (classes[i][j][k] == indications[k] ? 1 : -1) * ait[i][k];
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
					muTextField.setText(maxMuAki + "");
					classNameTextField.setText(classNames[maxI]);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Неверный формат ввода!", null, JOptionPane.ERROR_MESSAGE);
					Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}
}