package main.Lab1;

import javafx.util.Pair;

import java.util.*;

import static main.Lab1.Attribute.*;
import static main.Lab1.Constants.*;

class Constants {

	static final String Да = "Да";
	static final String Нет = "Нет";
	static final String Есть = "Есть";
	static final String КорольНочи = "Король Ночи";
	static final String Упырь = "Упырь";
	static final String Джейме = "Джейме";
	static final String Серсея = "Серсея";
	static final String Тирион = "Тирион";
	static final String Роберт = "Роберт";
	static final String Ренли = "Ренли";
	static final String Станнис = "Станнис";
	static final String Рейегар = "Рейегар";
	static final String Дейенерис = "Дейенерис";
	static final String Визерис = "Визерис";
	static final String ДжонСноу = "Джон Сноу";
	static final String Ланнистеры = "Ланнистеры";
	static final String Баратеоны = "Баратеоны";
	static final String Таргариены = "Таргариены";
	static final String Старки = "Старки";
	static final String Красивый = "Красивый";
	static final String Некрасивый = "Некрасивый";
	static final String ONE = "1";
	static final String TWO = "2";
	static final String Строгий = "Строгий";
	static final String Мягкий = "Мягкий";
	static final String Вепрь = "Вепрь";
	static final String Бриенна = "Бриенна";
	static final String Кхал = "Кхал";
	static final String Братья = "Братья";
	static final String Дрогон = "Дрогон";
}

enum Attribute {

	IS_IN_KING_OF_NIGHT_ARMY("Состоишь в войске Короля Ночи", List.of(Да, Нет)),
	IS_ALIVE("Живой", List.of(Да, Нет)),
	HAS_CROWN("Корона", List.of(Есть, Нет)),
	HAS_NAME("Имя", Arrays.asList(КорольНочи, Упырь, Джейме, Серсея, Тирион, Роберт, Ренли, Станнис, Рейегар, Дейенерис, Визерис, ДжонСноу)),
	YOUR_HOUSE("Твой дом", List.of(Ланнистеры, Баратеоны, Таргариены, Старки)),
	YOUR_APPEARANCE("Внешность", List.of(Красивый, Некрасивый)),
	NUMBER_OF_HANDS("Количество рук", List.of(ONE, TWO)),
	YOUR_CHARACTER("Твой характер", List.of(Строгий, Мягкий)),
	KILLED_BY("Тебя убил", List.of(Вепрь, Бриенна, Кхал, Братья)),
	YOUR_SON("Твой сын", List.of(ДжонСноу, Дрогон));

	final String key;
	final List<String> values;

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

	public void setAttribute(Attribute attribute) {

		this.attribute = attribute;
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

	static List<Rule> rules = Arrays.asList(new Rule(1, List.of(new Statement(IS_IN_KING_OF_NIGHT_ARMY, Да)),
					new Statement(IS_ALIVE, Нет)),
			new Rule(2, List.of(new Statement(IS_IN_KING_OF_NIGHT_ARMY, Нет)),
					new Statement(IS_ALIVE, Да)),
			new Rule(3, List.of(new Statement(HAS_CROWN, Да)),
					new Statement(HAS_NAME, КорольНочи)),
			new Rule(4, List.of(new Statement(HAS_CROWN, Нет)),
					new Statement(HAS_NAME, Упырь)),
			new Rule(5, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Ланнистеры), new Statement(YOUR_APPEARANCE, Красивый), new Statement(NUMBER_OF_HANDS, ONE)),
					new Statement(HAS_NAME, Джейме)),
			new Rule(6, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Ланнистеры), new Statement(YOUR_APPEARANCE, Красивый), new Statement(NUMBER_OF_HANDS, TWO)),
					new Statement(HAS_NAME, Серсея)),
			new Rule(7, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Ланнистеры), new Statement(YOUR_APPEARANCE, Некрасивый)),
					new Statement(HAS_NAME, Тирион)),
			new Rule(8, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Баратеоны), new Statement(YOUR_CHARACTER, Строгий), new Statement(KILLED_BY, Вепрь)),
					new Statement(HAS_NAME, Роберт)),
			new Rule(9, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Баратеоны), new Statement(YOUR_CHARACTER, Строгий), new Statement(KILLED_BY, Бриенна)),
					new Statement(HAS_NAME, Станнис)),
			new Rule(10, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Баратеоны), new Statement(YOUR_CHARACTER, Мягкий)),
					new Statement(HAS_NAME, Ренли)),
			new Rule(11, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Таргариены), new Statement(YOUR_SON, ДжонСноу)),
					new Statement(HAS_NAME, Рейегар)),
			new Rule(12, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Таргариены), new Statement(YOUR_SON, Дрогон)),
					new Statement(HAS_NAME, Дейенерис)),
			new Rule(13, List.of(new Statement(IS_ALIVE, Да), new Statement(YOUR_HOUSE, Старки), new Statement(KILLED_BY, Братья)),
					new Statement(HAS_NAME, ДжонСноу)));
	static Stack<Pair<Statement, Rule>> stackOfAims = new Stack<>();
	static Stack<Pair<Statement, Rule>> stackOfContext = new Stack<>();

	public static boolean analyzeBDOfRules(Statement aim) {

		stackOfAims.push(new Pair<>(aim, null));
		Statement unknownStatement = new Statement();
		while (true) {
			Optional<Rule> opt = rules.stream().filter(rule -> rule.output.attribute.key.equals(stackOfAims.peek().getKey().attribute.key)).findFirst();
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
				if (currentAim.getKey().attribute.values != null) {
					String chosenValue = chooseValue(currentAim.getKey().attribute.values);
					currentAim.getKey().setChosenValue(chosenValue);
					stackOfContext.push(currentAim);
				} else {
					break;
				}

			}
		}
		if (stackOfContext.peek().getKey().attribute.key.equals(aim.attribute.key)) {
			//ответ
			return true;
		} else {
			return false;
		}
	}

	private static Boolean calculateRule(Rule rule, Statement unknownStatement) {

		for (Statement sentence : rule.sentences) {
			if (stackOfContext.stream().anyMatch(attributeRulePair -> attributeRulePair.getKey().attribute.key.equals(sentence.attribute.key))) {
				if (stackOfContext.stream().anyMatch(attributeRulePair -> attributeRulePair.getKey().chosenValue.equals(sentence.chosenValue))) {
					continue;
				} else {
					return false;
				}
			} else {
				unknownStatement.setAttribute(sentence.attribute);
				return null;
			}
		}
		return true;
	}

	private static String chooseValue(List<String> values) {

		return "chosen";
	}
}

