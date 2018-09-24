package Lab2;

import Lab2.distribution.DiscreteDistribution;
import Lab2.distribution.impl.*;

public class ModelingDiscreteRandomVariable {

	public static void main(String[] args) {

		DiscreteDistribution[] distributions = new DiscreteDistribution[5];
		distributions[0] = new BernulliDistribution(1000, 0.2, 0.05);
		distributions[1] = new GeometricDistribution(1000, 0.6, 0.05, 4);
		distributions[2] = new BinomialDistribution(1000, 5, 0.25, 0.05);
		distributions[3] = new NegativeBinomialDistribution(1000, 5, 0.25, 0.05);
		distributions[4] = new PoissonDistribution(1000, 0.5, 0.05);
		for (int i = 0; i < 5; i++) {
			System.out.println('\n' + distributions[i].getName());
			int[] list = distributions[i].generateSequence();
			for (int aList : list) {
				System.out.print(aList + " ");
			}
			distributions[i].compareMathExpectationAndDispersion();
			distributions[i].pearsonCriterion();
		}
	}
}