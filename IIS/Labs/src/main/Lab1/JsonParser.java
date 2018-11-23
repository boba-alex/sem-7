package main.Lab1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonParser {

	private static final String RULES_FILE_PATH = "resources1/rules.json";
	private static final String ATTRIBUTES_FILE_PATH = "resources1/attributes.json";

	public static void parseBaseOfKnowledges() {

		parseAttributes();
		parseRules();
	}

	public static void parseAttributes() {

		attributesHashMap = new HashMap<>();
		try {
			FileReader reader = new FileReader(ATTRIBUTES_FILE_PATH);
			JSONParser jsonParser = new JSONParser();
			JSONArray attributesJson = (JSONArray) jsonParser.parse(reader);
			Iterator iterator = attributesJson.iterator();
			while (iterator.hasNext()) {
				JSONObject attributeJson = (JSONObject) iterator.next();
				String keyJson = (String) attributeJson.get("key");
				JSONArray valuesJson = (JSONArray) attributeJson.get("values");
				Iterator iteratorValues = valuesJson.iterator();
				List<String> values = new ArrayList<>();
				while (iteratorValues.hasNext()) {
					String value = (String) iteratorValues.next();
					values.add(value);
				}
				attributesHashMap.put(keyJson, new Attribute(keyJson, values));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static void parseRules() {

		rulesList = new ArrayList<>();
		try {
			FileReader reader = new FileReader(RULES_FILE_PATH);
			JSONParser jsonParser = new JSONParser();
			JSONArray rulesJson = (JSONArray) jsonParser.parse(reader);
			Iterator iterator = rulesJson.iterator();
			int number = 0;
			while (iterator.hasNext()) {
				JSONObject ruleJson = (JSONObject) iterator.next();
				JSONObject outputJson = (JSONObject) ruleJson.get("then");
				Statement output = new Statement(attributesHashMap.get(outputJson.get("attribute")), (String) outputJson.get("value"));
				JSONArray sentencesJson = (JSONArray) ruleJson.get("if");
				Iterator iteratorSentences = sentencesJson.iterator();
				List<Statement> sentences = new ArrayList<>();
				while (iteratorSentences.hasNext()) {
					JSONObject statementJson = (JSONObject) iteratorSentences.next();
					Statement statement = new Statement(attributesHashMap.get(statementJson.get("attribute")), (String) statementJson.get("value"));
					sentences.add(statement);
				}
				rulesList.add(new Rule(++number, sentences, output));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static HashMap<String, Attribute> attributesHashMap;
	public static List<Rule> rulesList;
}
