package main.Lab1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Form extends JFrame {
	public static boolean WAIT = true;
	Main main = new Main();

	private JComboBox comboBox1;
	private JButton button1;

	public static void main(String[] args) {
		Form form = new Form();
		form.setVisible(true);
		form.initialize();
	}

	public String chooseValue(List<String> values){
		for(String v : values){
			comboBox1.addItem(v);
		}
		WAIT = true;
		while(WAIT);
		return comboBox1.getSelectedItem().toString();
	}

	public void initialize(){
		button1.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				main.analyzeBDOfRules(new Statement("pups", null, null));
				WAIT = false;
			}
		});
	}

}
