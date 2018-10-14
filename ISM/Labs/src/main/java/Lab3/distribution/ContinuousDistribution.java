package Lab3.distribution;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Arrays;

public abstract class ContinuousDistribution {

	protected int n;
	protected double e;
	protected int gradationsCount = 2; //мат. ожидание - граница двух градаций
	protected double[] list;

	public ContinuousDistribution(int n, double e) {

		this.n = n;
		this.e = e;
		this.list = new double[n];
	}

	public abstract String getName();

	public double probabilityFunction(double xLeft, double xRight) {

		return distributionFunction(xRight) - distributionFunction(xLeft);
	}

	public abstract double distributionFunction(double x);

	public abstract double getMathExpectation();

	public abstract double getDispersion();

	public abstract double[] generateSequence();

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

	public double calculateKolmogorovDistance() {

		double[] copyList = Arrays.copyOf(list, n);
		//sort to calculate indicators
		Arrays.sort(copyList);

		double maxDn = 0, func0, empiricalFunc, empiricalFuncNext;
		for (int i = 0; i < n; i++) {
			empiricalFunc = (double) i / n; // кол-во попавших до текущего
			empiricalFuncNext = (double) (i + 1) / n;
			func0 = distributionFunction(copyList[i]);
			double dn = Math.max(Math.abs(empiricalFunc - func0), Math.abs(empiricalFuncNext - func0));
			maxDn = Math.max(maxDn, dn);
		}

		return maxDn;
	}

	//http://helpstat.ru/statisticheskie-tablitsyi/raspredelenie-statistiki-kolmogorova/
	public double getQuantile(double e) {

		double quantile = 1.3581; //для a = 0.05 квантиль уровня 1-a
		if (e == 0.05) {
			quantile = 1.3581;
		}

		return quantile;
	}

	public void kolmogorovCriterion() {

		double quantile = getQuantile(e);
		System.out.println("Quantile : " + quantile + " of level " + (1 - e) + " of Kolmogorov distribution with significance level = " + e);
		double dn = calculateKolmogorovDistance();
		double statistics = dn * Math.sqrt(n);
		System.out.println("Statistics : " + statistics);
		if (statistics < quantile) {
			System.out.println("Statistics : " + statistics + " < " + quantile);
		} else {
			System.out.println("Rearson criterion is not met.");
		}
	}

	public double getTableX2() {

		int numberOfDegreesOfFreedom = gradationsCount - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		return chiSquaredDistribution.inverseCumulativeProbability(1 - e);
	}

	public double calculateX2() {

		int[] v = new int[gradationsCount];
		double gradationsBoundaryValue = getMathExpectation();
		for (int i = 0; i < n; i++) {
			for (int value = 0; value < gradationsCount; value++) {
				if (list[i] < gradationsBoundaryValue) {
					v[0]++;
				} else {
					v[1]++;
				}
			}
		}

		double[] pk = new double[gradationsCount];
		pk[0] = probabilityFunction(gradationsBoundaryValue - getDispersion(), gradationsBoundaryValue);
		pk[1] = 1 - pk[0];

		double x2 = 0;
		for (int i = 0; i < gradationsCount; i++) {
			x2 += Math.pow(v[i] - n * pk[i], 2) / (n * pk[i]);
		}
		return x2;
	}

	// FIXME!
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
