package Lab2.distribution.impl;

public interface DiscreteDistribution {

	double getMathExpectation();
	double getDispersion();
	double getUnbiasedEstimateOfMathExpectation();
	double getUndiasedEstimateOfDispersion();
	int[] generateSequence();

}
