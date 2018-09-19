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
	private static final double p1 = 0.2;
	private static final double p2 = 0.6;

	public static void main(String[] args) {

		int[] list1 = new int[N];
		int[] v = new int[2]; //частота 0 и 1
		double average1 = 0;
		double disp1 = 0;

		System.out.println("Бернулли");
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			double a = random.nextDouble();

			if (a <= p1) {
				list1[i] = 1;
				v[1]++;
			} else {
				list1[i] = 0;
				v[0]++;
			}

			average1 += list1[i];
		}
		// несмещенная оценка мат. ожидания
		//https://ru.wikipedia.org/wiki/%D0%9D%D0%B5%D1%81%D0%BC%D0%B5%D1%89%D1%91%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BE%D1%86%D0%B5%D0%BD%D0%BA%D0%B0
		average1 /= 1000.0;
		// https://ru.wikipedia.org/wiki/%D0%92%D1%8B%D0%B1%D0%BE%D1%80%D0%BE%D1%87%D0%BD%D0%B0%D1%8F_%D0%B4%D0%B8%D1%81%D0%BF%D0%B5%D1%80%D1%81%D0%B8%D1%8F
		// несмещённая (исправленная) дисперсия
		for (int i = 0; i < N; i++) {
			disp1 += Math.pow(list1[i] - average1, 2);
		}
		disp1 /= 999.0;

		double x1 = Math.pow(v[1] - N * p1, 2) / (N * p1) + Math.pow(v[0] - N * (1 - p1), 2) / (N * (1 - p1));

		int numberOfObservatedGradations = 2; // 0 or 1
		int numberOfDegreesOfFreedom = numberOfObservatedGradations - 1;
		// table value
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		double tableX2 = chiSquaredDistribution.inverseCumulativeProbability(1 - E);
		System.out.println("Table χ2 = " + tableX2);
		if (x1 < tableX2) {
			System.out.println("x1 = " + x1 + " < tableX2");
		}
		System.out.println("Average = " + average1 + ", but true value = " + p1);
		System.out.println("Dispersia = " + disp1 + ", but true value = " + p1 * (1 - p1));
	}

}
