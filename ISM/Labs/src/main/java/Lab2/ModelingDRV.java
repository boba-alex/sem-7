package Lab2;

import java.util.Random;

public class ModelingDRV {

	public static double sum(double[] ver, int n) {

		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += ver[i];
		}
		return sum;
	}

	public static long c(int n, int k) {

		long answer = 1;
		if (k > n - k) {
			for (int i = k + 1; i <= n; i++) {
				answer *= i;
			}
			for (int i = 2; i <= n - k; i++) {
				answer /= i;
			}
		} else {
			for (int i = n - k + 1; i <= n; i++) {
				answer *= i;
			}
			for (int i = 2; i <= k; i++) {
				answer /= i;
			}
		}
		return answer;
	}

	public static void main(String[] args) {

		int percent1 = 0;
		int percent2 = 0;
		for (int a = 0; a < 1000; a++) {

			double[] variables1 = new double[1000];
			int[] variables2 = new int[1000];
			Random rnd = new Random();
			double p = 0.5;
			int r = 5;
			double p2 = 0.25;
			int[] v = new int[2];
			v[0] = 0;
			v[1] = 0;
			double average1 = 0;
			double disp1 = 0;

			double average2 = 0;
			double disp2 = 0;

			for (int i = 0; i < 1000; i++) {
				variables1[i] = rnd.nextDouble();
				if (variables1[i] <= p) {
					v[0]++;
					variables1[i] = 1;
				} else {
					v[1]++;
					variables1[i] = 0;
				}

				average1 += variables1[i];
			}
			double x1 = Math.pow(v[0] - 1000.0 * p, 2) / (1000.0 * p) + Math.pow(v[1] - 1000.0 * (1 - p), 2) / (1000.0 * (1 - p));

			average1 /= 1000.0;
			for (int i = 0; i < 1000; i++) {
				disp1 += Math.pow(variables1[i] - average1, 2);
			}
			disp1 /= 999.0;

			System.out.println("\nBernoulli");
			if (x1 < 3.8415) {
				System.out.println("true");
				percent1++;
			} else
				System.out.println("false");

			System.out.println("Average = " + average1 + "  -   " + p);
			System.out.println("Dispersia = " + disp1 + "  -   " + p * (1 - p));

			double[] ver = new double[10];
			double x2 = 0;
			int[] chast = new int[10];
			for (int i = 0; i < 10; i++) {
				chast[i] = 0;
				ver[i] = 0;
			}

			for (int i = 1; i < 10; i++) {
				ver[i - 1] = c(i + r - 1, i) * Math.pow(p2, i) * Math.pow(1 - p2, r);
			}
			ver[9] = 1 - sum(ver, 9);

			for (int k = 1; k < 1000; k++) {
				double temp = rnd.nextDouble();

				for (int i = 0; i < 10; i++) {
					if (temp < sum(ver, i)) {
						chast[i]++;
						variables2[k] = i;
						average2 += i;
						break;
					}
				}
			}

			for (int k = 1; k < 10; k++) {
				x2 += Math.pow(chast[k] - 1000.0 * ver[k], 2) / 1000.0 * ver[k];
			}

			average2 /= 1000.0;
			for (int k = 0; k < 1000; k++) {
				disp2 += Math.pow(variables2[k] - average2, 2);
			}
			disp2 /= 999.0;

			System.out.println("Negative binomial");
			if (x2 < 16.9190) {
				System.out.println("true");
				percent2++;
			} else
				System.out.println("false");

			System.out.println("Average = " + average2 + "  -   " + r * p2 / (1 - p2));
			System.out.println("Dispersia = " + disp2 + "  -   " + r * p2 / Math.pow(1 - p2, 2));

		}
		System.out.println(percent1 / 10.0 + "   " + percent2 / 10.0);
	}
}