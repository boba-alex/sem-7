package main.Lab1;

import java.util.List;
import java.util.Objects;

class Attribute {

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

	public void setAttribute(Attribute attribute) {

		this.attribute = attribute;
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


