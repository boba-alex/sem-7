package Lab3;

import Lab3.distribution.ContinuousDistribution;
import Lab3.distribution.impl.*;

public class ModelingContinuousRandomVariable {

	public static void main(String[] args) {

		ContinuousDistribution[] distributions = new ContinuousDistribution[5];
		distributions[0] = new NormalDistribution(0, 1, 1000, 0.05);
		distributions[1] = new ExponentialDistribution(0.5, 1000, 0.05);
		distributions[2] = new WeibullDistribution(4, 0.5, 1000, 0.05);
		distributions[3] = new LogisticDistribution(2, 3, 1000, 0.05);
		distributions[4] = new LaplaceDistribution(2, 2, 1000, 0.05);
		for (int i = 0; i < 5; i++) {
			System.out.println('\n' + distributions[i].getName());
			double[] list = distributions[i].generateSequence();
			for (double aList : list) {
				System.out.print(aList + " ");
			}
			distributions[i].compareMathExpectationAndDispersion();
			distributions[i].kolmogorovCriterion();
		}
	}
}
