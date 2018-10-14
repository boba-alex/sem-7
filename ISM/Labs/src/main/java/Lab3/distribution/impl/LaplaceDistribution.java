package Lab3.distribution.impl;

import Lab3.distribution.ContinuousDistribution;

import java.util.Random;

public class LaplaceDistribution extends ContinuousDistribution {

	private double a;
	private double b;

	public LaplaceDistribution(double a, double b, int n, double e) {

		super(n, e);
		this.a = a;
		this.b = b;
	}

	@Override
	public String getName() {

		return "Распределение Лапласа";
	}

	@Override
	public double distributionFunction(double x) {

		if (x <= b) {
			return 0.5 * Math.exp(a * (x - b));
		} else {
			return 1 - 0.5 * Math.exp(-a * (x - b));
		}
	}

	@Override
	public double getMathExpectation() {

		return b;
	}

	@Override
	public double getDispersion() {

		return 2 / Math.pow(a, 2);
	}

	@Override
	public double[] generateSequence() {

		Random random = new Random();
		double[] r = new double[n + 1];
		for (int i = 0; i < n + 1; i++) {
			r[i] = random.nextDouble();
		}
		for (int i = 0; i < n; i++) {
			list[i] = b + 1 / a * Math.log(r[i] / r[i + 1]);
		}
		return list;
	}
}
