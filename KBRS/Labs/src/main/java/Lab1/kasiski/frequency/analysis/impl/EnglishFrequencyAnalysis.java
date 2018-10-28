package Lab1.kasiski.frequency.analysis.impl;

import Lab1.kasiski.frequency.analysis.FrequencyAnalysis;

import java.util.HashMap;

public class EnglishFrequencyAnalysis extends FrequencyAnalysis {

	public EnglishFrequencyAnalysis() {

		super(new HashMap<>() {

			{
				put("a", 0.082D);
				put("b", 0.015D);
				put("c", 0.028D);
				put("d", 0.043D);
				put("e", 0.127D);
				put("f", 0.022D);
				put("g", 0.020D);
				put("h", 0.061D);
				put("i", 0.070D);
				put("j", 0.002D);
				put("k", 0.008D);
				put("l", 0.040D);
				put("m", 0.024D);
				put("n", 0.067D);
				put("o", 0.075D);
				put("p", 0.019D);
				put("q", 0.001D);
				put("r", 0.060D);
				put("s", 0.063D);
				put("t", 0.091D);
				put("u", 0.028D);
				put("v", 0.010D);
				put("w", 0.023D);
				put("x", 0.001D);
				put("y", 0.020D);
				put("z", 0.001D);
			}
		});
	}
}
