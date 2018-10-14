package Lab3.distribution.impl;

import Lab3.distribution.ContinuousDistribution;

import java.util.Random;

public class LogisticDistribution extends ContinuousDistribution {

	private double a;
	private double b;

	public LogisticDistribution(double a, double b, int n, double e) {

		super(n, e);
		this.a = a;
		this.b = b;
	}

	@Override
	public String getName() {

		return "Логистическое распределение";
	}

	@Override
	public double distributionFunction(double x) {

		return 1 / (1 + Math.exp(-(x - a) / b));
	}

	@Override
	public double getMathExpectation() {

		return a;
	}

	@Override
	public double getDispersion() {

		return Math.pow(b, 2) * Math.pow(Math.PI, 2) / 3;
	}

	@Override
	public double[] generateSequence() {

		Random random = new Random();
		for (int i = 0; i < n; i++) {

			double r = random.nextDouble();
			list[i] = a - b * Math.log((1 - r) / r);
		}
		return list;
	}
}
