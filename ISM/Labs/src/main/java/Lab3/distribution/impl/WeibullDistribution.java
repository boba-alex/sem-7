package Lab3.distribution.impl;

import Lab3.distribution.ContinuousDistribution;
import org.apache.commons.math3.special.Gamma;

import java.util.Random;

public class WeibullDistribution extends ContinuousDistribution {

	private double a;
	private double b;
	private double lambda;//in literature instead of a

	public WeibullDistribution(double a, double b, int n, double e) {

		super(n, e);
		this.a = a;
		this.b = b;
		this.lambda = Math.pow(a, -b);

	}

	@Override
	public String getName() {

		return "Распределение Вейбулла";
	}

	@Override
	public double distributionFunction(double x) {

		return 1 - Math.exp(-lambda * Math.pow(x, b));
	}

	@Override
	public double getMathExpectation() {

		return Math.pow(lambda, -1 / b) * Gamma.gamma(1 / b + 1);
	}

	@Override
	public double getDispersion() {

		return Math.pow(lambda, -2 / b) * (Gamma.gamma(2 / b + 1) + Math.pow(Gamma.gamma(1 / b + 1), 2));
	}

	@Override
	public double[] generateSequence() {

		Random random = new Random();
		for (int i = 0; i < n; i++) {

			list[i] = a * Math.pow(-Math.log(random.nextDouble()), 1 / b);
		}
		return list;
	}

	//Probability density function (плотность вероятности) стр. 158 Вадинский
	public double pdf(double x) {

		return lambda * b * Math.pow(x, b - 1) * Math.exp(-lambda * Math.pow(x, b)); // x > 0
	}
}
