package main.Lab1;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Form extends JFrame {

	public static boolean WAIT = true;

	private JComboBox comboBox1;

	public static void main(String[] args) {

		//Form form = new Form();
		//form.setVisible(true);
//		new Runnable() {
//
//			@Override
//			public void run() {
//
//				Form form = new Form();
//				form.setSize(new Dimension(200, 200));
//				form.setMinimumSize(new Dimension(200, 200));
//				form.setVisible(true);
//				form.setDefaultCloseOperation(EXIT_ON_CLOSE);
//			}
//		};
		new Runnable() {

			@Override
			public void run() {

				new Main().analyzeBDOfRules(new Statement(Attribute.HAS_NAME, null));
			}
		};
		//form.initialize();
	}

	public String chooseValue(List<String> values) {

		for (String v : values) {
			comboBox1.addItem(v);
		}
		WAIT = true;
		while (WAIT)
			;
		return comboBox1.getSelectedItem().toString();
	}
}
