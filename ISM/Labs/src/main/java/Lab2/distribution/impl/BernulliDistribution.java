package Lab2.distribution.impl;

public class BernulliDistribution implements DiscreteDistribution {

	private static final int N = 1000;
	private static final double E = 0.05;
	private static final double P = 0.2;
	private int[] list = new int[N];

	@Override
	public double getMathExpectation() {

		return 0;
	}

	@Override
	public double getDispersion() {

		return 0;
	}

	@Override
	public double getUnbiasedEstimateOfMathExpectation() {

		return 0;
	}

	@Override
	public double getUndiasedEstimateOfDispersion() {

		return 0;
	}

	@Override
	public int[] generateSequence() {

		return new int[0];
	}
}
