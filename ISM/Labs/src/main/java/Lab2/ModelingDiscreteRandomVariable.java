package Lab2;

//Смоделировать дискретную случайную величину (задания на стр. 18-22).
//Исследовать точность моделирования.
//
//1) Осуществить моделирование n = 1000 реализаций СВ из заданных
//дискретных распределений.
//2) Вывести на экран несмещенные оценки математического ожидания
//и дисперсии, сравнить их с истинными значениями.
//3) Для каждой из случайных величин построить свой χ 2 -критерием
//Пирсона с уровнем значимость ε=0.05. Проверить, что вероятность
//ошибки I рода стремится к 0.05.
//4) Осуществить проверку каждой из сгенерированных выборок
//каждым из построенных критериев.
//Варианты:
//1) Бернулли – Bi(1,p), p = 0.7; Биномиальное – Bi(m,p), m = 5, p = 0.25.
//2) Бернулли – Bi(1,p), p = 0.5; Отрицательное биномиальное – Bi (r,p), r = 5,
//p = 0.25.
//3) Бернулли – Bi(1,p), p = 0.6; Пуассона – П(λ), λ = 0.5;
//4) Бернулли – Bi(1,p), p = 0.2; Геометрическое – G(p), p = 0.6;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Random;

public class ModelingDiscreteRandomVariable {

	private static final int N = 1000;
	private static final double E = 0.05;

	public static void main(String[] args) {

		bernulli();

	}

	private static void bernulli() {

		double p = 0.2;
		int[] list = new int[N];
		int[] v = new int[2]; //частота 0 и 1
		double average = 0;
		double disp = 0;

		System.out.println("Бернулли");
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			double a = random.nextDouble();

			if (a <= p) {
				list[i] = 1;
				v[1]++;
			} else {
				list[i] = 0;
				v[0]++;
			}

			average += list[i];
		}
		// несмещенная оценка мат. ожидания
		//https://ru.wikipedia.org/wiki/%D0%9D%D0%B5%D1%81%D0%BC%D0%B5%D1%89%D1%91%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BE%D1%86%D0%B5%D0%BD%D0%BA%D0%B0
		average /= (double) N;
		// https://ru.wikipedia.org/wiki/%D0%92%D1%8B%D0%B1%D0%BE%D1%80%D0%BE%D1%87%D0%BD%D0%B0%D1%8F_%D0%B4%D0%B8%D1%81%D0%BF%D0%B5%D1%80%D1%81%D0%B8%D1%8F
		// несмещённая (исправленная) дисперсия
		for (int i = 0; i < N; i++) {
			disp += Math.pow(list[i] - average, 2);
		}
		disp /= (double) (N - 1);

		double x1 = Math.pow(v[1] - N * p, 2) / (N * p) + Math.pow(v[0] - N * (1 - p), 2) / (N * (1 - p));

		int numberOfObservatedGradations = 2; // потому что ДСВ Бернулли принимает значения 0 или 1
		int numberOfDegreesOfFreedom = numberOfObservatedGradations - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		double tableX2 = chiSquaredDistribution.inverseCumulativeProbability(1 - E);
		System.out.println("Table χ2 = " + tableX2);
		if (x1 < tableX2) {
			System.out.println("x1 = " + x1 + " < tableX2");
		}
		System.out.println("Average = " + average + ", but true value = " + p);
		System.out.println("Dispersia = " + disp + ", but true value = " + p * (1 - p));
	}

	private static void geometric() {

		double p = 0.6;
		double q = 1 - p;
		int[] list = new int[N];
		int[] v = new int[Integer.MAX_VALUE]; //частота 0 и 1
		double average = 0;
		double disp = 0;

		System.out.println("Геометрическое распределение");
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			double a = random.nextDouble();
			int x = (int) Math.round(Math.log(a) / Math.log(q));
			list[i] = x;
			v[x]++;

			average += list[i];
		}

		// несмещенная оценка мат. ожидания
		//https://ru.wikipedia.org/wiki/%D0%9D%D0%B5%D1%81%D0%BC%D0%B5%D1%89%D1%91%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BE%D1%86%D0%B5%D0%BD%D0%BA%D0%B0
		average /= N;
		// https://ru.wikipedia.org/wiki/%D0%92%D1%8B%D0%B1%D0%BE%D1%80%D0%BE%D1%87%D0%BD%D0%B0%D1%8F_%D0%B4%D0%B8%D1%81%D0%BF%D0%B5%D1%80%D1%81%D0%B8%D1%8F
		// несмещённая (исправленная) дисперсия
		for (int i = 0; i < N; i++) {
			disp += Math.pow(list[i] - average, 2);
		}

		disp /= 999.0;

		double x1 = Math.pow(v[1] - N * p, 2) / (N * p) + Math.pow(v[0] - N * (1 - p), 2) / (N * (1 - p));

		int numberOfObservatedGradations = 2; // 0 or 1
		int numberOfDegreesOfFreedom = numberOfObservatedGradations - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		double tableX2 = chiSquaredDistribution.inverseCumulativeProbability(1 - E);
		System.out.println("Table χ2 = " + tableX2);
		if (x1 < tableX2) {
			System.out.println("x1 = " + x1 + " < tableX2");
		}
		System.out.println("Average = " + average + ", but true value = " + 1 / p);
		System.out.println("Dispersia = " + disp + ", but true value = " + q / Math.pow(p, 2));
	}

	private static void negativeBinomial() {

		double r = 5;
		double p = 0.25;
		int[] list = new int[N];
		int[] v = new int[2]; //частота 0 и 1
		double average = 0;
		double disp = 0;

		System.out.println("Бернулли");
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			double a = random.nextDouble();

			if (a <= p) {
				list[i] = 1;
				v[1]++;
			} else {
				list[i] = 0;
				v[0]++;
			}

			average += list[i];
		}
		// несмещенная оценка мат. ожидания
		//https://ru.wikipedia.org/wiki/%D0%9D%D0%B5%D1%81%D0%BC%D0%B5%D1%89%D1%91%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BE%D1%86%D0%B5%D0%BD%D0%BA%D0%B0
		average /= 1000.0;
		// https://ru.wikipedia.org/wiki/%D0%92%D1%8B%D0%B1%D0%BE%D1%80%D0%BE%D1%87%D0%BD%D0%B0%D1%8F_%D0%B4%D0%B8%D1%81%D0%BF%D0%B5%D1%80%D1%81%D0%B8%D1%8F
		// несмещённая (исправленная) дисперсия
		for (int i = 0; i < N; i++) {
			disp += Math.pow(list[i] - average, 2);
		}
		disp /= 999.0;

		double x1 = Math.pow(v[1] - N * p, 2) / (N * p) + Math.pow(v[0] - N * (1 - p), 2) / (N * (1 - p));

		int numberOfObservatedGradations = 2; // 0 or 1
		int numberOfDegreesOfFreedom = numberOfObservatedGradations - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		double tableX2 = chiSquaredDistribution.inverseCumulativeProbability(1 - E);
		System.out.println("Table χ2 = " + tableX2);
		if (x1 < tableX2) {
			System.out.println("x1 = " + x1 + " < tableX2");
		}
		System.out.println("Average = " + average + ", but true value = " + p);
		System.out.println("Dispersia = " + disp + ", but true value = " + p * (1 - p));
	}
}
