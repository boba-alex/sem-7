package main.Lab1;

import javafx.util.Pair;

import java.util.*;

import static main.Lab1.Attribute.IS_ALIVE;
import static main.Lab1.Attribute.IS_IN_KING_OF_NIGHT_ARMY;

enum Attribute {

	IS_IN_KING_OF_NIGHT_ARMY("Состоишь в войске Короля Ночи", List.of("Да", "Нет")),
	IS_ALIVE("Живой", List.of("Да", "Нет")),
	HAS_CROWN("Корона", List.of("Есть", "Нет")),
	HAS_NAME("Имя", Arrays.asList("Король Ночи", "Упырь", "Джейме", "Серсея", "Тирион", "Роберт", "Ренли", "Станнис", "Рейегар", "Дейенерис", "Визерис", "Джон Сноу")),
	YOUR_HOUSE("Твой дом", List.of("Ланнистеры", "Баратеоны", "Таргариены", "Старки")),
	YOUR_APPEARANCE("Внешность", List.of("Красивый", "Некрасивый")),
	NUMBER_OF_HANDS("Количество рук", List.of("1", "2")),
	YOUR_CHARACTER("Твой характер", List.of("Строгий", "Мягкий")),
	KILLED_BY("Тебя убил", List.of("Вепрь", "Бриенна", "Кхал", "Братья")),
	YOUR_SON("Твой сын", List.of("Джон Сноу", "Дрогон"));

	private final String key;
	private final List<String> values;

	Attribute(String key, List<String> values) {

		this.key = key;
		this.values = values;
	}

	public String getKey() {

		return key;
	}

	public List<String> getValues() {

		return values;
	}
}

class Statement {

	Attribute attribute;
	String chosenValue;

	public Statement() {

	}

	public Statement(Attribute attribute) {

		this.attribute = attribute;
	}

	public Statement(Attribute attribute, String chosenValue) {

		this.attribute = attribute;
		this.chosenValue = chosenValue;
	}

	public Attribute getAttribute() {

		return attribute;
	}

	public String getChosenValue() {

		return chosenValue;
	}

	public void setChosenValue(String chosenValue) {

		this.chosenValue = chosenValue;
	}

	public boolean isEmptyChosenValue() {

		return chosenValue == null;
	}
}

class Rule {

	int number;
	List<Statement> sentences;
	Statement output;

	public Rule(int number, List<Statement> statements, Statement output) {

		this.number = number;
		this.sentences = statements;
		this.output = output;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Rule))
			return false;
		Rule rule = (Rule) o;
		return number == rule.number;
	}

	@Override
	public int hashCode() {

		return Objects.hash(number);
	}
}

public class Main {

	static List<Rule> rules = Arrays.asList(
			new Rule(1, List.of(new Statement(IS_IN_KING_OF_NIGHT_ARMY,  "Да")), new Statement(IS_ALIVE, ""))
			, new Statement("pups", List.of("pip")))));
	static Stack<Pair<Statement, Rule>> stackOfAims = new Stack<>();
	static Stack<Pair<Statement, Rule>> stackOfContext = new Stack<>();

	public static boolean analyzeBDOfRules(Statement aim) {

		stackOfAims.push(new Pair<>(aim, null));
		Statement unknownStatement = new Statement();
		while (true) {
			Optional<Rule> opt = rules.stream().filter(rule -> rule.output.key.equals(stackOfAims.peek().getKey().key)).findFirst();
			if (opt.isPresent()) {
				var valueOfRule = calculateRule(opt.get(), unknownStatement);
				if (valueOfRule == true) {
					stackOfContext.push(new Pair<>(stackOfAims.pop().getKey(), opt.get()));

					if (stackOfAims.isEmpty()) {
						break;
					} else {
						continue;
					}
				} else if (valueOfRule == false) {
					rules.remove(opt.get());
				} else {
					stackOfAims.push(new Pair<>(unknownStatement, opt.get()));
				}
			} else//нельзя найти правило для анализа
			{
				Pair<Statement, Rule> currentAim = stackOfAims.pop();
				if (currentAim.getKey().values != null) {
					String chosenValue = chooseValue(currentAim.getKey().values);
					currentAim.getKey().setChosenValue(chosenValue);
					stackOfContext.push(currentAim);
				} else {
					break;
				}

			}
		}
		if (stackOfContext.peek().getKey().key.equals(aim.key)) {
			//ответ
			return true;
		} else {
			return false;
		}
	}

	private static Boolean calculateRule(Rule rule, Statement unknownStatement) {

		for (Statement sentence : rule.sentences) {
			if (stackOfContext.stream().anyMatch(attributeRulePair -> attributeRulePair.getKey().key.equals(sentence.key))) {
				if (stackOfContext.stream().anyMatch(attributeRulePair -> attributeRulePair.getKey().chosenValue.equals(sentence.chosenValue))) {
					continue;
				} else {
					return false;
				}
			} else {
				unknownStatement.setKey(sentence.key);
				return null;
			}
		}
		return true;
	}

	private static String chooseValue(List<String> values) {

		return "chosen";
	}
}

