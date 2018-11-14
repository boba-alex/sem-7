package Lab5;

import Lab4.GraphicsUtils;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
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
	static int N; // длина Цепи Маркова
	static int[] i; // цепь Маркова
	static double[] Q; //веса состояний цепи Маркова
	static int m; // кол-во реализаций цепи Маркова
	static double[] ksi; //СВ
	static double alpha; // БСВ

	public static void main(String[] args) {

		//testExampleFromBook();
		//testExample2FromPhoto();
		testExample1();
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

	//ИСМ, стр. 61
	public static void testExampleFromBook() {

		//double[][] a1 = {{1.1, -0.8},
		// 				   {-0.7, -0.3}};
		//double[] f1 = {0.1, 0.1};
		//x = Ax + f
		//все собств зн-я А меньше единицы
		a = new double[][] {{-0.1, 0.8}, {0.4, -0.1}};
		f = new double[] {0.1, -0.2};

		pi = new double[] {0.5, 0.5};
		p = new double[][] {{0.5, 0.5}, {0.5, 0.5}};
		setMarkovParameters(1000, 10000);

		x = calculate(hx);
		y = calculate(hy);
		System.out.println(x);
		System.out.println(y);
	}

	public static void testExample1() {

		//A = {1.1 -0.1 0.2} f = {-3}
		//    {0.1 0.5 0.3}		{1}
		//    {-0.3 -0.1 1.3}   {4}
		//x = Ax + f
		//все собств зн-я А меньше единицы
		//fpm.lobach@gmail.com
		a = new double[][] {{-0.1, 0.1, -0.2}, {-0.1, 0.5, -0.3}, {0.3, 0.1, -0.3}};
		f = new double[] {-3, 1, 4};

		pi = new double[] {0.33, 0.33, 0.33};
		p = new double[][] {{0.33, 0.33, 0.33}, {0.33, 0.33, 0.33}, {0.33, 0.33, 0.33}};
		setMarkovParameters(1000, 10000);

		x = calculate(new double[] {1, 0, 0});
		y = calculate(new double[] {0, 1, 0});
		double z = calculate(new double[] {0, 0, 1});
		System.out.println("Test example 2 from photo (variant 4)");
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("z: " + z);

		double xTrue = -175.0 / 57;
		double yTrue = 65.0 / 57;
		double zTrue = 140.0 / 57;
		System.out.println("xTrue: " + xTrue);
		System.out.println("yTrue: " + yTrue);
		System.out.println("zTrue: " + zTrue);

	}

	public static void testExample2FromPhoto() {

		//x = Ax + f
		//system
		//x = -0.3x + 0.2y + 2
		//y = -0.2x + 0.3y - 1
		//все собств зн-я А меньше единицы
		a = new double[][] {{-0.3, 0.2}, {-0.2, 0.3}};
		f = new double[] {2, -1};

		pi = new double[] {0.5, 0.5};
		p = new double[][] {{0.5, 0.5}, {0.5, 0.5}};
		setMarkovParameters(1000, 10000);

		x = calculate(hx);
		y = calculate(hy);
		System.out.println("Test example 2 from photo (variant 4)");
		System.out.println("x: " + x);
		System.out.println("y: " + y);

		double xTrue = 24.0 / 19;
		double yTrue = -34.0 / 19;
		System.out.println("xTrue: " + xTrue);
		System.out.println("yTrue: " + yTrue);

		double perfectDifference = 0;
		List<Pair<Integer, Double>> results = new ArrayList<>();
		// тест по длине цепи Маркова
		for (int j = 1000; j < 2000; j += 50) {
			setMarkovParameters(j, 10000);
			x = calculate(hx);
			y = calculate(hy);
			double difference = Math.abs(x - xTrue) + Math.abs(y - yTrue);
			results.add(new Pair<>(j, difference));
		}
		GraphicsUtils.show(results, perfectDifference);

		results.clear();
		// по кол-ву реализаций цепи Маркова
		for (int j = 10000; j < 15000; j += 1000) {
			setMarkovParameters(1000, j);
			x = calculate(hx);
			y = calculate(hy);
			double difference = Math.abs(x - xTrue) + Math.abs(y - yTrue);
			results.add(new Pair<>(j, difference));
		}
		GraphicsUtils.show(results, perfectDifference);
	}

	private static void setMarkovParameters(int N_, int m_) {

		N = N_;
		m = m_;
		i = new int[N + 1];
		Q = new double[N + 1];
		ksi = new double[m];
	}
}
