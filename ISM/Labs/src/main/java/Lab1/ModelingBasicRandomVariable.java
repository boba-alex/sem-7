package Lab1;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Arrays;
import java.util.Random;

//Используя метод Маклерена-Марсальи построить датчик БСВ (1 датчик
//должен быть мультипликативно конгруентный, второй – на выбор).
//Исследовать точность построенной БСВ.
//1) Осуществить моделирование n = 1000 реализаций БСВ с помощью
//мультипликативного конгруэнтного метода (МКМ) с параметрами
//a 0 , β, M = 2 31 .
//2) Осуществить моделирование n = 1000 реализаций БСВ с помощью
//метода Макларена-Марсальи (один датчик должен быть
//мультипликативно конгруентный (п. 1), второй – на выбор).
//K – объем вспомогательной таблицы.
//3) Проверить точность моделирования обоих датчиков (п. 1 и п. 2) с
//помощью критерия согласия Колмогорова и χ 2 -критерия Пирсона с
//уровнем значимости ε = 0.05.
//
//Вариант 4) a 0 = β = 78 125, K = 256

//https://statanaliz.info/metody/proverka-gipotez/174-kriterij-soglasiya-pirsona-khi-kvadrat

public class ModelingBasicRandomVariable {

	//Variant 4) a0 = β = 78 125, K = 256
	private static final int ALPHA0 = 78125;
	private static final int BETA = 78125;
	private static final int K = 256;
	private static final int M = Integer.MAX_VALUE;
	private static final int N = 1000; // количество реализаций моделирования
	private static final double E = 0.05; // уровень значимости - только когда будет на графике в этом уровне в самом углу справа - имеет тогда значение

	private static double[] list1 = new double[N]; //b[i]
	private static double[] list2 = new double[N]; //a[i]

	public static void main(String[] args) {

		//multiplicative-congruent method
		double tempAlpha = ALPHA0;
		for (int t = 0; t < N; t++) {

			tempAlpha = (BETA * tempAlpha) % M; // -> '<= M'
			list1[t] = tempAlpha / M;
		}

		//V - helpful for Macklaren-Marsali
		double[] v = new double[K];
		for (int i = 0; i < K; i++) {
			v[i] = list1[i];
		}

		Random random = new Random(); // for c[i], ! seed always returns the same pseudo-random sequence !
		for (int t = 0; t < N; t++) {
			int s = (int) (random.nextInt(100) / 100.0 * K);
			list2[t] = v[s];
			v[s] = list1[(t + K) % N];
		}

		System.out.println("Критерий согласия Пирсона χ2");
		//частоты (Observed), количество наблюдений = 1000 >=50, частота в каждом должна быть больше или равно 5
		int numberOfObservatedGradations = 50;
		//если мы имеем выборку, по которой уже посчитана сумма частот, то одну из частот всегда можно определить, как разность общего количества и суммой всех остальных.
		//На самом деле число независимых слагаемых будет на один меньше, чем количество градаций номинальной переменной n.
		int numberOfDegreesOfFreedom = numberOfObservatedGradations - 1;
		int[] v1 = new int[50];
		int[] v2 = new int[50];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 50; j++) {
				if (j / 50.0 < list1[i] && list1[i] < (j + 1) / 50.0) {
					v1[j]++;
					break;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 50; j++) {
				if (j / 50.0 < list2[i] && list2[i] < (j + 1) / 50.0) {
					v2[j]++;
					break;
				}
			}
		}
		double x1 = 0;
		double x2 = 0;
		double p = 1 / 50.0;
		for (int i = 0; i < 50; i++) {
			x1 += Math.pow(v1[i] - N * p, 2) / (N * p);
			x2 += Math.pow(v2[i] - N * p, 2) / (N * p);
		}

		System.out.println("χ2 для list1[] x1 = " + x1);
		System.out.println("χ2 для list2[] x2 = " + x2);

		// table values
		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(numberOfDegreesOfFreedom, ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		double tableX2 = chiSquaredDistribution.inverseCumulativeProbability(1 - E);
		System.out.println("Table χ2 = " + tableX2);
		if (x1 < tableX2) {
			System.out.println("x1 < tableX2");
		}

		if (x2 < tableX2) {
			System.out.println("x2 < tableX2");
		}

		//Kolmogorov

		//http://helpstat.ru/statisticheskie-tablitsyi/raspredelenie-statistiki-kolmogorova/
		double quantile = 1.3581; //для a = 0.05 квантиль уровня 1-a
		Arrays.sort(list1);
		Arrays.sort(list2);

		double maxDn1 = 0, maxDn2 = 0, func0, empiricalFunc;
		for (int i = 0; i < N; i++) {
			func0 = list1[i]; // ф-ция равномерного закона распределения
			empiricalFunc = (double) i / N; // кол-во попавших до текущего
			maxDn1 = Math.max(Math.abs(empiricalFunc - func0), maxDn1);
		}

		double statistics1 = maxDn1 * Math.sqrt(N);
		System.out.println("Statistics 1 : " + statistics1);
		if (statistics1 < quantile) {
			System.out.println("Statistics 1 : " + statistics1 + " < " + quantile);
		}

		for (int i = 0; i < N; i++) {
			func0 = list2[i];
			empiricalFunc = (double) i / N;
			maxDn2 = Math.max(Math.abs(empiricalFunc - func0), maxDn2);
		}

		double statistics2 = maxDn1 * Math.sqrt(N);
		System.out.println("Statistics 2 : " + statistics2);
		if (statistics2 < quantile) {
			System.out.println("Statistics 2 : " + statistics2 + " < " + quantile);
		}
	}
}