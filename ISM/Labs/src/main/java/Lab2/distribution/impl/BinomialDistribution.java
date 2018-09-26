package Lab2.distribution.impl;

import Lab2.MathHelper;
import Lab2.distribution.DiscreteDistribution;

import java.util.Random;

// Биномиальная СВ X – это число успехов в m независимых испытаниях Бернулли,
// если вероятность успеха в каждом испытании равна p.
public class BinomialDistribution extends DiscreteDistribution {

	private int m;

	public BinomialDistribution(int n, int m, double p, double e) {

		this.n = n;
		this.m = m;
		this.p = p;
		this.e = e;
		this.gradationsCount = m + 1; // Бином. распредел-е: 0,1,...,m
		this.list = new int[n];
	}

	@Override
	public String getName() {

		return "Биномиальное распределение";
	}

	@Override
	public double probabilityFunction(int x) {

		return MathHelper.c(m, x) * Math.pow(p, x) * Math.pow(1 - p, m - x);
	}

	@Override
	public double getMathExpectation() {

		return m * p;
	}

	@Override
	public double getDispersion() {

		return m * p * (1 - p);
	}

	@Override
	public int[] generateSequence() {

		Random random = new Random();
		for (int i = 0; i < n; i++) {

			int x = 0;
			for (int j = 0; j < m; j++) {
				double a = random.nextDouble();
				if (p - a > 0) {
					x++;
				}
			}
			list[i] = x;
		}
		return list;
	}

	@Override
	public double calculateX2() {

		int[] v = new int[gradationsCount]; //частота 0,1,...m
		for (int i = 0; i < n; i++) {
			for (int value = 0; value < gradationsCount; value++) {
				if (list[i] == value) {
					v[value]++;
					break;
				}
			}
		}

		double[] pk = new double[gradationsCount];
		for (int i = 0; i < gradationsCount; i++) {
			pk[i] = probabilityFunction(i);
		}

		double x2 = 0;
		for (int i = 0; i < gradationsCount; i++) {
			x2 += Math.pow(v[i] - n * pk[i], 2) / (n * pk[i]);
		}
		return x2;
	}
}
