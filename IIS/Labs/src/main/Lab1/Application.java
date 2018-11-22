package main.Lab1;

import javafx.util.Pair;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class Application {

	static Statement aimStatement = new Statement(Attribute.HAS_NAME, null);
	static HashMap<String, Statement> aimStatements = new HashMap<>();
	static JComboBox comboBoxAimKey = new JComboBox();
	static JLabel labelAimResult = new JLabel("?");
	static JLabel labelChooseAttributeValue = new JLabel("Выберите значение атрибута");
	static JComboBox comboBoxChoose = new JComboBox();
	static ThreadEvent threadEvent = new ThreadEvent();
	static JPanel panelPicture = new JPanel(new BorderLayout());
	static int numberOfPicture = 1;
	static StringBuilder allStepsBuilder = new StringBuilder();
	static JTextArea areaAllSteps = new JTextArea("Пошаговая развертка\n");
	static Thread playMusic = new Thread(() -> {
		try {
			InputStream fileInputStream = new FileInputStream("resources1/music.mp3");
			Player player = new Player(fileInputStream);
			player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	});
	static Thread threadOfMicroexpert = new Thread(() -> {

		try {
			analyzeBDOfRules(aimStatement);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	});

	public static void main(String[] args) {

		fillAimStatements();
		playMusic.start();
		createGUI();
		threadOfMicroexpert.start();
	}

	public static String chooseValue(Statement statement) throws InterruptedException {

		labelChooseAttributeValue.setText(statement.attribute.key);
		comboBoxChoose.removeAllItems(); // чистка
		for (String v : statement.attribute.values) {
			comboBoxChoose.addItem(v);
		}
		threadEvent.await();

		return comboBoxChoose.getSelectedItem().toString();
	}

	public static void createGUI() {

		JFrame frame = new JFrame();
		JButton buttonRestart = new JButton("Начать заново");
		JButton buttonOk = new JButton("Ok");

		JPanel panelAttributes = new JPanel(new BorderLayout());
		JPanel panelAim = new JPanel(new BorderLayout());
		JPanel panelChoose = new JPanel(new BorderLayout());
		JPanel panelSteps = new JPanel(new BorderLayout());

		panelAim.add(buttonRestart, BorderLayout.WEST);
		panelAim.add(comboBoxAimKey, BorderLayout.CENTER);
		panelAim.add(labelAimResult, BorderLayout.EAST);

		panelChoose.add(labelChooseAttributeValue, BorderLayout.NORTH);
		panelChoose.add(comboBoxChoose, BorderLayout.CENTER);
		panelChoose.add(buttonOk, BorderLayout.EAST);

		panelAttributes.add(panelAim, BorderLayout.NORTH);
		panelAttributes.add(panelChoose, BorderLayout.SOUTH);

		panelPicture.add((new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {

				BufferedImage bufferedImage = new BufferedImage(20, 20, 1);
				try {
					bufferedImage = ImageIO.read(new File("resources1/" + numberOfPicture + ".jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				super.paintComponent(g);
				g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);
			}
		}), BorderLayout.CENTER);

		panelSteps.add(new JScrollPane(areaAllSteps), BorderLayout.CENTER);

		frame.setLayout(new GridLayout(3, 1));
		frame.add(panelAttributes);
		frame.add(panelPicture);
		frame.add(panelSteps);
		frame.setLocation(300, 30);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		buttonOk.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				threadEvent.signal();
			}
		});

		buttonRestart.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				restart();
				//threadEvent.signal(); //works without it
			}
		});

	}

	static List<Rule> rules = new ArrayList<>(Rule.rules);
	static Stack<Pair<Statement, Rule>> stackOfAims = new Stack<>();
	static Stack<Pair<Statement, Rule>> stackOfContext = new Stack<>();
	static int step = 0;

	public static boolean analyzeBDOfRules(Statement aim) throws InterruptedException {

		stackOfAims.push(new Pair<>(aim, null));
		while (true) {
			Optional<Rule> opt = rules.stream().filter(rule -> rule.output.attribute.key.equals(stackOfAims.peek().getKey().attribute.key)).findFirst();
			if (opt.isPresent()) {
				//logStep(opt.get().output.attribute.key);
				Statement unknownStatement = new Statement();
				var valueOfRule = calculateRule(opt.get(), unknownStatement);
				//logStep(valueOfRule + " | " + );
				if (valueOfRule == null) {//неизвестно
					stackOfAims.push(new Pair<>(unknownStatement, opt.get()));
					logStep(opt.get().number, String.valueOf(valueOfRule), "", unknownStatement.attribute.key, opt.get().number, "", "", -1, -1);
				} else if (valueOfRule == true) {
					stackOfAims.pop();
					stackOfContext.push(new Pair<>(unknownStatement, opt.get()));
					logStep(opt.get().number, String.valueOf(valueOfRule), "", "", -1, unknownStatement.attribute.key, unknownStatement.chosenValue, opt.get().number, -1);

					if (stackOfAims.isEmpty()) {
						break;
					} else {
						continue;
					}
				} else { //valueOfRule == false
					rules.remove(opt.get());
					logStep(opt.get().number, String.valueOf(valueOfRule), "", "", -1, "", "", -1, opt.get().number);
				}
			} else//нельзя найти правило для анализа
			{
				Pair<Statement, Rule> currentAim = stackOfAims.pop();
				if (currentAim.getKey().attribute.values != null && !stackOfAims.empty()) {//если имеется вопрос, связанный с текущей целью,  и эта цель не главная
					String chosenValue = chooseValue(currentAim.getKey());
					currentAim.getKey().setChosenValue(chosenValue);
					stackOfContext.push(currentAim);
					logStep(-1, "", chosenValue, "", -1, currentAim.getKey().attribute.key, chosenValue, -1, -1);
				} else {
					break;
				}

			}
		}
		if (stackOfContext.peek().getKey().attribute.key.equals(aim.attribute.key)) {
			//ответ
			labelAimResult.setText(stackOfContext.peek().getKey().chosenValue);
			areaAllSteps.append(allStepsBuilder.toString());
			paintPicture(stackOfContext.peek().getValue().number);
			return true;
		} else {
			labelAimResult.setText("Нет персонажа с такими характеристиками!");
			paintPicture(2);
			return false;
		}
	}

	private static Boolean calculateRule(Rule rule, Statement unknownStatement) {

		for (Statement sentence : rule.sentences) {
			if (stackOfContext.stream().anyMatch(statementRulePair -> statementRulePair.getKey().attribute.key.equals(sentence.attribute.key))) {
				if (stackOfContext.stream().anyMatch(
						statementRulePair -> statementRulePair.getKey().attribute.key.equals(sentence.attribute.key) && statementRulePair.getKey().chosenValue
								.equals(sentence.chosenValue))) {
					continue;
				} else {
					return false;
				}
			} else {
				unknownStatement.setAttribute(sentence.attribute);
				return null;
			}
		}
		unknownStatement.setAttribute(rule.output.attribute);
		unknownStatement.setChosenValue(rule.output.chosenValue);
		return true;
	}

	private static void restart() {

		aimStatement = aimStatements.get(comboBoxAimKey.getSelectedItem().toString());
		cleanGUI();
		rules = new ArrayList<>(Rule.rules);
		stackOfAims = new Stack<>();
		stackOfContext = new Stack<>();
		threadOfMicroexpert.stop();
		threadOfMicroexpert = new Thread(() -> {

			try {
				analyzeBDOfRules(aimStatement);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		threadOfMicroexpert.start();

	}

	private static void fillAimStatements(){
		comboBoxAimKey.removeAllItems(); // чистка
		aimStatements.put(Attribute.HAS_NAME.key, new Statement(Attribute.HAS_NAME, null));
		aimStatements.put(Attribute.IS_ALIVE.key, new Statement(Attribute.IS_ALIVE, null));
		for (String v : aimStatements.keySet()) {
			comboBoxAimKey.addItem(v);
		}
	}

	private static void logStep(int ruleNumber, String ruleValue, String chosenValue, String attributeName, int ruleNumber2, String contextAttributeName, String chosenValue2,
			int numberAcceptedRule, int numberRemovedRule) {

		allStepsBuilder.append(++step).append("|").append(ruleNumber).append("|").append(ruleValue).append("|").append(chosenValue).append("|").append(attributeName).append("|")
				.append(ruleNumber2).append("|").append(contextAttributeName).append("=").append(chosenValue2).append("|").append(numberAcceptedRule).append("|")
				.append(numberRemovedRule).append("\n");
	}

	private static void cleanGUI() {

		areaAllSteps.setText("Пошаговая развертка\n");
		allStepsBuilder = new StringBuilder();
		labelAimResult.setText("?");
		paintPicture(1);
	}

	private static void paintPicture(int i) {

		numberOfPicture = i;
		panelPicture.repaint();
	}
}

class ThreadEvent {

	private final Object lock = new Object();

	public void signal() {

		synchronized (lock) {
			lock.notify();
		}
	}

	public void await() throws InterruptedException {

		synchronized (lock) {
			lock.wait();
		}
	}
}
