package main;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Application extends JFrame {

	private static String[] indicationNames; //n
	private static String[] classNames;//l
	private static boolean classes[][][];
	private static double[][] bit;
	private static double[][] ait;
	private static double[] bt;
	private static int t;
	private static int l;

	public static void main(String[] args) {

		createGUI();
	}

	public static void createGUI() {

		JFrame frame = new JFrame();
		JButton buttonFindClass = new JButton("Find class");

		JTextField inputTextField = new JTextField();
		JPanel panelAttributes = new JPanel(new BorderLayout());
		JPanel panelAim = new JPanel(new BorderLayout());
		JPanel panelChoose = new JPanel(new BorderLayout());
		JPanel panelSteps = new JPanel(new BorderLayout());

		//		panelAim.add(buttonRestart, BorderLayout.WEST);
		//		panelAim.add(comboBoxAimKey, BorderLayout.CENTER);
		//		panelAim.add(labelAimResult, BorderLayout.EAST);
		//
		//		panelChoose.add(labelChooseAttributeValue, BorderLayout.NORTH);
		//		panelChoose.add(comboBoxChoose, BorderLayout.CENTER);
		//		panelChoose.add(buttonOk, BorderLayout.EAST);
		//
		//		panelAttributes.add(panelAim, BorderLayout.NORTH);
		//		panelAttributes.add(panelChoose, BorderLayout.SOUTH);
		//
		//		panelPicture.add((new JPanel() {
		//
		//			@Override
		//			protected void paintComponent(Graphics g) {
		//
		//				BufferedImage bufferedImage = new BufferedImage(20, 20, 1);
		//				try {
		//					bufferedImage = ImageIO.read(new File("resources1/" + numberOfPicture + ".jpg"));
		//				} catch (IOException e) {
		//					e.printStackTrace();
		//				}
		//				super.paintComponent(g);
		//				g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);
		//			}
		//		}), BorderLayout.CENTER);
		//
		//		panelSteps.add(new JScrollPane(areaAllSteps), BorderLayout.CENTER);
		//
		//		frame.setLayout(new GridLayout(3, 1));
		//		frame.add(panelAttributes);
		//		frame.add(panelPicture);
		frame.add(panelSteps);
		frame.setLocation(300, 30);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem openFileItem = new JMenuItem("Open");
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
					Boolean[] obj = Arrays.stream(inputTextField.getText().split(" ")).map(Boolean::valueOf).toArray(Boolean[]::new);
					double maxMuI = -1;
					int maxI = -1;
					for (int i = 0; i < classes.length; i++) {
						double sumA = 0;
						for (int k = 0; k < t; k++) {
							sumA += ait[i][k];
						}
						double maxMu = 0;
						for (int j = 0; j < classes[i].length; j++) {
							double mu = 0;
							for (int k = 0; k < t; k++) {
								mu += (classes[i][j][k] == obj[k] ? 1 : -1) * ait[i][k];
							}
							mu /= sumA;
							mu = Math.max(0, mu);
							if (mu > maxMu) {
								maxMu = mu;
							}
						}
						//jTextField3.setText(jTextField3.getText() + limit(String.valueOf(maxMu)) + "  ");
						if (maxMu > maxMuI) {
							maxMuI = maxMu;
							maxI = i;
						}
					}
					//jTextField4.setText(names[maxI]);
				} catch (Exception ex) {
				}
			}
		});
	}
}