package Lab5;

import java.util.Random;

public class MonteCarloSLAU {

	static int n = 2; //размерность системы
	static double x = 0, y = 0; // решение системы

	//x = Ax + f
	//все собств зн-я А меньше единицы
	static double[][] a;//{{-0.1, 0.8}, {0.4, -0.1}};
	static double[] f;//{0.1, -0.2};

	static double[] h = {0, 1};
	static double[] hx = {1, 0}; // h для вычисления x
	static double[] hy = {0, 1}; // h для вычисления y
	static double[] pi; // вектор нач состояний цепи маркова
	static double[][] p; //матрица переходных вероятностей
	static int N = 1000; // длина Цепи Маркова
	static int[] i = new int[N + 1]; // цепь Маркова
	static double[] Q = new double[N + 1]; //веса состояний цепи Маркова
	static int m = 10000; // кол-во реализаций цепи Маркова
	static double[] ksi = new double[m]; //СВ
	static double alpha; // БСВ

	public static void main(String[] args) {

		test1();
	}

	public static double calculate(double[] h) {

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

				if (p[i[k - 1]][i[k]] > 0) {
					Q[k] = Q[k - 1] * a[i[k - 1]][i[k]] / p[i[k - 1]][i[k]];
				} else {
					Q[k] = 0;
				}
			}

			ksi[j] = 0;// set to 0 before calculation
			for (int k = 0; k <= N; k++) {

				ksi[j] = ksi[j] + Q[k] * f[i[k]]; // строю Кси-N по j-ой цепи Маркова
			}
		}

		// среднее значение
		double x = 0;
		for (int k = 0; k < m; k++) {

			x += ksi[k];
		}
		x /= m;

		return x;
	}

	public static void test1() {

		//double[][] a1 = {{1.1, -0.8}, {-0.7, -0.3}};
		//double[] f1 = {0.1, 0.1};
		//x = Ax + f
		//все собств зн-я А меньше единицы
		a = new double[][] {{-0.1, 0.8}, {0.4, -0.1}};
		f = new double[] {0.1, -0.2};

		pi = new double[] {0.5, 0.5};
		p = new double[][] {{0.5, 0.5}, {0.5, 0.5}};

		x = calculate(hx);
		y = calculate(hy);
		System.out.println(x);
		System.out.println(y);
	}

}
