package Lab2.distribution.impl;

import Lab2.MathHelper;
import Lab2.distribution.DiscreteDistribution;

import java.util.Random;

// Отрица́тельное биномиа́льное распределе́ние, также называемое распределением Паскаля —
// это распределение дискретной случайной величины равной количеству произошедших неудач
// в последовательности испытаний Бернулли с вероятностью успеха p, проводимой до r-го успеха.
public class NegativeBinomialDistribution extends DiscreteDistribution {

	private int r;

	public NegativeBinomialDistribution(int n, int r, double p, double e) {

		this.n = n;
		this.r = r;
		this.p = p;
		this.e = e;
		this.gradationsCount = 10;// 0-3, 4-7,...по 4 * 10 градаций // Отриц. бином. распредел-е: 0,1,...9, 10,11,...19,20,21,...29, и >=30
		this.list = new int[n];
	}

	@Override
	public String getName() {

		return "Отрицательное биномиальное распределение";
	}

	@Override
	public double probabilityFunction(int x) {

		return MathHelper.c(x + r - 1, x) * Math.pow(p, r) * Math.pow(1 - p, x);
	}

	@Override
	public double getMathExpectation() {

		return r * (1 - p) / p;
	}

	@Override
	public double getDispersion() {

		return r * (1 - p) / Math.pow(p, 2);
	}

	@Override
	public int[] generateSequence() {

		double m = r;
		double q = 1 - p;
		Random random = new Random();
		for (int i = 0; i < n; i++) {

			double p = Math.pow(this.p, m);
			double a = random.nextDouble();
			int z = 0;
			a -= p;
			while (a >= 0) {
				a -= p;
				z += 1;
				p = p * q * (m - 1 + z) / z;
			}
			list[i] = z;
		}
		return list;
	}

	//variant 2
	//	@Override
	//	public int[] generateSequence() {
	//
	//		double m = r;
	//		Random random = new Random();
	//		double c = 1 / Math.log(1 - p);
	//		for(int i = 0; i < n; i++){
	//
	//			int z = 0;
	//			int j = 1;
	//			double a = random.nextDouble();
	//			int x = (int)Math.round(c * Math.log(a));
	//			z += x;
	//			while(j < m){
	//				j += 1;
	//				a = random.nextDouble();
	//				x = (int)Math.round(c * Math.log(a));
	//				z += x;
	//			}
	//			list[i] = z;
	//		}
	//		return list;
	//	}

	@Override
	public double calculateX2() {

		int[] v = new int[gradationsCount]; //частота 0 - 3, 4-7, ...-
		for (int i = 0; i < n; i++) {
			if (list[i] >= 0 && list[i] <= 3) {
				v[0]++;
			} else if (list[i] >= 4 && list[i] <= 7) {
				v[1]++;
			} else if (list[i] >= 8 && list[i] <= 11) {
				v[2]++;
			} else if (list[i] >= 12 && list[i] <= 15) {
				v[3]++;
			} else if (list[i] >= 16 && list[i] <= 19) {
				v[4]++;
			} else if (list[i] >= 20 && list[i] <= 23) {
				v[5]++;
			} else if (list[i] >= 24 && list[i] <= 27) {
				v[6]++;
			} else if (list[i] >= 28 && list[i] <= 31) {
				v[7]++;
			} else if (list[i] >= 32 && list[i] <= 35) {
				v[8]++;
			} else {
				v[9]++;
			}
		}

		double[] pk = new double[gradationsCount];
		int x = 0;
		for (int i = 0; i < gradationsCount - 1; i++) {
			for (int j = 0; j < 4; j++) {
				pk[i] += probabilityFunction(x);
				x++;
			}
		}

		pk[gradationsCount - 1] = 1 - MathHelper.sum(pk, gradationsCount - 1);

		double x2 = 0;
		for (int i = 0; i < gradationsCount; i++) {
			x2 += Math.pow(v[i] - n * pk[i], 2) / (n * pk[i]);
		}
		return x2;
	}
}
