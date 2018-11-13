package Lab5;

import java.util.Random;

public class MonteCarloSLAU {

	public static void main(String[] args) {

		int n = 2; //размерность системы
		double x = 0, y = 0; // решение системы
		double[][] a1 = {{1.1, -0.8}, {-0.7, -0.3}};

		double[] f1 = {0.1, 0.1};

		//x = Ax + f
		//все собств зн-я А меньше единицы
		double[][] a = {{-0.1, 0.8}, {0.4, -0.1}};
		double[] f = {0.1, -0.2};

		double[] h = {0, 1};
		double[] hx = {1, 0}; // h для вычисления x
		double[] hy = {0, 1}; // h для вычисления y
		double[] pi = {0.5, 0.5}; // вектор нач состояний цепи маркова
		double[][] P = {{0.5, 0.5}, {0.5, 0.5}}; //матрица переходных вероятностей
		int N = 1000; // длина Цепи Маркова
		int[] i = new int[N + 1]; // цепь Маркова
		double[] Q = new double[N + 1]; //веса состояний цепи Маркова
		int m = 10000; // кол-во реализаций цепи Маркова
		double[] ksi = new double[m]; //СВ
		double alpha; // БСВ
		Random random = new Random();

		//Моделирую m цепей Маркова длины N

		for (int j = 0; j < m; j++) {

			alpha = random.nextDouble();
			if (alpha < pi[0]) {
				i[0] = 0; // реализуется первое состояние
			} else {
				i[0] = 1; // реализуется второе состояние
			}
			for (int k = 1; k <= N; k++) {
				alpha = random.nextDouble();
				if (alpha < 0.5) {
					i[k] = 0;
				} else {
					i[k] = 1;
				}
			}

			// Вычисляю веса цепи Маркова
			if (pi[i[0]] > 0) {
				Q[0] = h[i[0]] / pi[i[0]];
			} else {
				Q[0] = 0;
			}

			for (int k = 1; k <= N; k++) {

				if (P[i[k - 1]][i[k]] > 0) {
					Q[k] = Q[k - 1] * a[i[k - 1]][i[k]] / P[i[k - 1]][i[k]];
				} else {
					Q[k] = 0;
				}
			}

			for (int k = 0; k <= N; k++) {

				ksi[j] = ksi[j] + Q[k] * f[i[k]]; // строю Кси-N по j-ой цепи Маркова
			}
		}

		// среднее значение

		for (int k = 0; k < m; k++) {

			x = x + ksi[k];
		}
		x /= m;

		System.out.println(x);
	}

}
