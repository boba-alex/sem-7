package Lab2.distribution.impl;

import Lab2.MathHelper;
import Lab2.distribution.DiscreteDistribution;

import java.util.Random;

// Распределе́ние Пуассо́на — вероятностное распределение дискретного типа,
// моделирует случайную величину, представляющую собой число событий,
// произошедших за фиксированное время, при условии, что данные события происходят
// с некоторой фиксированной средней интенсивностью и независимо друг от друга.
public class PoissonDistribution extends DiscreteDistribution {

	private double lambda;

	public PoissonDistribution(int n, double lambda, double e) {

		this.n = n;
		this.lambda = lambda;
		this.e = e;
		this.list = new int[n];
		this.gradationsCount = 4; //0,1,2, и >=3
	}

	@Override
	public String getName() {

		return "Распределение Пуассона";
	}

	@Override
	public double probabilityFunction(int x) {

		return Math.pow(lambda, x) * Math.pow(Math.E, -lambda) / MathHelper.factorial(x);
	}

	@Override
	public double getMathExpectation() {

		return lambda;
	}

	@Override
	public double getDispersion() {

		return lambda;
	}

	@Override
	public int[] generateSequence() {

		Random random = new Random();
		for (int i = 0; i < n; i++) {
			double p = Math.pow(Math.E, -lambda);
			int x = 0;
			double r = random.nextDouble();
			r -= p;
			while (r >= 0) {
				x++;
				p = p * lambda / x;
				r -= p;
			}
			list[i] = x;
		}
		return list;
	}

	@Override
	public double calculateX2() {

		int[] v = new int[gradationsCount]; //частота 0,1,2,>=3
		for (int i = 0; i < n; i++) {
			for (int value = 0; value < gradationsCount - 1; value++) {
				if (list[i] == value) {
					v[value]++;
					break;
				} else if (list[i] >= gradationsCount - 1) {
					v[gradationsCount - 1]++;// >=3
					break;
				}
			}
		}

		double[] pk = new double[gradationsCount];
		for (int i = 0; i < gradationsCount - 1; i++) {
			pk[i] = probabilityFunction(i);
		}
		pk[gradationsCount - 1] = 1 - MathHelper.sum(pk, gradationsCount - 1);

		double x2 = 0;
		for (int i = 0; i < gradationsCount; i++) {
			x2 += Math.pow(v[i] - n * pk[i], 2) / (n * pk[i]);
		}
		return x2;
	}
}
