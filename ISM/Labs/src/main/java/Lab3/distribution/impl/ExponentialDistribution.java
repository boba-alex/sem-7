package Lab3.distribution.impl;

import Lab3.distribution.ContinuousDistribution;

import java.util.Random;

public class ExponentialDistribution extends ContinuousDistribution {

	private double a;

	public ExponentialDistribution(double a, int n, double e) {

		super(n, e);
		this.a = a;
	}

	@Override
	public String getName() {

		return "Экспоненциальное распределение";
	}

	@Override
	public double distributionFunction(double x) {

		return 1 - Math.exp(-a * x);
	}

	@Override
	public double getMathExpectation() {

		return 1 / a;
	}

	@Override
	public double getDispersion() {

		return 1 / Math.pow(a, 2);
	}

	@Override
	public double[] generateSequence() {

		Random random = new Random();
		for(int  i = 0; i < n; i++){

			list[i] = -1 / a * Math.log(random.nextDouble());
		}
		return list;
	}
}
