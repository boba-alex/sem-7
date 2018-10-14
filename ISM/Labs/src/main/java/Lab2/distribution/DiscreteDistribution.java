package Lab2.distribution;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public abstract class DiscreteDistribution {

	protected double p;
	protected int n;
	protected double e;
	protected int gradationsCount;
	protected int[] list;

	public DiscreteDistribution(int n, double e) {

		this.n = n;
		this.e = e;
		this.list = new int[n];
	}

	public abstract String getName();

	public abstract double probabilityFunction(int x);

	public abstract double getMathExpectation();

	public abstract double getDispersion();

	public abstract int[] generateSequence();

	// несмещенная оценка мат. ожидания
	//https://ru.wikipedia.org/wiki/%D0%9D%D0%B5%D1%81%D0%BC%D0%B5%D1%89%D1%91%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BE%D1%86%D0%B5%D0%BD%D0%BA%D0%B0
	public double getUnbiasedEstimateOfMathExpectation() {

		double average = 0;
		for (int i = 0; i < n; i++) {
			average += list[i];
		}
		return average / (double) n;
	}

	// https://ru.wikipedia.org/wiki/%D0%92%D1%8B%D0%B1%D0%BE%D1%80%D0%BE%D1%87%D0%BD%D0%B0%D1%8F_%D0%B4%D0%B8%D1%81%D0%BF%D0%B5%D1%80%D1%81%D0%B8%D1%8F
	// несмещённая (исправленная) дисперсия
	public double getUnbiasedEstimateOfDispersion() {

		double average = getUnbiasedEstimateOfMathExpectation();
		double disp = 0;
		for (int i = 0; i < n; i++) {
			disp += Math.pow(list[i] - average, 2);
		}
		disp /= (double) (n - 1);
		return disp;
	}

	public void compareMathExpectationAndDispersion() {

		System.out.println();
		System.out.println("Unbiased math expectation = " + getUnbiasedEstimateOfMathExpectation() + ", but true value = " + getMathExpectation());
		System.out.println("Unbiased dispersion = " + getUnbiasedEstimateOfDispersion() + ", but true value = " + getDispersion());
	}

	public double getTableX2() {

		int numberOfDegreesOfFreedom = gradationsCount - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		return chiSquaredDistribution.inverseCumulativeProbability(1 - e);
	}

	public abstract double calculateX2();

	public void pearsonCriterion() {

		double tableX2 = getTableX2();
		System.out.println("Table χ2 = " + tableX2);
		double calculatedX2 = calculateX2();
		System.out.println("Calculated χ2 = " + calculatedX2);
		if (calculatedX2 < tableX2) {
			System.out.println("Calculated χ2 = " + calculatedX2 + " < tableX2");
		} else {
			System.out.println("Rearson criterion is not met.");
		}
	}
}
