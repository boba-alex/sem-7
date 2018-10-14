package Lab3.distribution.impl;

import Lab3.distribution.ContinuousDistribution;
import org.apache.commons.math3.special.Erf;

import java.util.Random;

public class NormalDistribution extends ContinuousDistribution {

	private double mathExpectation;
	private double dispersion;

	public NormalDistribution(double mathExpectation, double dispersion, int n, double e) {

		super(n, e);
		this.mathExpectation = mathExpectation;
		this.dispersion = dispersion;
	}

	@Override
	public String getName() {

		return "Нормальное распределение";
	}

	@Override
	public double distributionFunction(double x) {

		//https://ru.wikipedia.org/wiki/Нормальное_распределение
		return 0.5 * (1 + Erf.erf((x - mathExpectation) / Math.sqrt(2 * dispersion)));
	}

	@Override
	public double getMathExpectation() {

		return mathExpectation;
	}

	@Override
	public double getDispersion() {

		return dispersion;
	}

	@Override
	public double[] generateSequence() {

		Random random = new Random();
		for (int i = 0; i < n; i++) {
			double r = 0.0;
			for (int j = 0; j < 12; j++) {
				r += random.nextDouble();
			}
			r -= 6;
			list[i] = mathExpectation + r * Math.sqrt(dispersion);
		}
		return list;
	}
}
