package Lab4;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MonteCarlo {

	//плотность вероятности равномерного распределения
	public static double p(double a, double b) {

		return 1 / (b - a);
	}

	public static void main(String[] args) {

		testExample1();
		testExample2();
	}

	public static void testExample1() {

		int n = 10;
		double a = 0;
		double b = 5 * Math.PI / 7;
		Double trueIntegralValue = -0.485736;
		Function<Double, Double> g = x -> Math.cos(x + Math.sin(x));

		List<Pair<Integer, Double>> results = new ArrayList<>();
		for (int i = n; i < n * 100; i += n) {
			double integralResult = integrate(a, b, n, g);
			//double difference = Math.abs(integralResult - trueIntegralValue);
			results.add(new Pair<>(i, integralResult));
		}
		GraphicsUtils.show(results, trueIntegralValue);
	}

	public static double integrate(double a, double b, int n, Function<Double, Double> g) {

		double integralResult = 0;
		//плотность вероятности равномерного распределения
		double p = p(a, b);
		Random random = new Random();
		for (int i = 0; i < n; i++) {

			// генерирование равномерно распределенной случайной величины
			double r = random.nextDouble();
			double x = (b - a) * r + a;
			integralResult += g.apply(x) / p;
		}
		integralResult /= n;
		return integralResult;
	}

	public static void testExample2() {

		int n = 10;
		//1 <= x^2 + y^2 <3
		//то есть интегрирую по области
		double a = -Math.sqrt(3);
		double b = Math.sqrt(3);
		//calculate properly!
		Double trueIntegralValue = -0.485736;
		BiFunction<Double, Double, Double> g = (x, y) -> 1 / (Math.pow(x, 2) + Math.pow(y, 4));

		List<Pair<Integer, Double>> results = new ArrayList<>();
		for (int i = n; i < n * 100; i += n) {
			double integralResult = integrate2(a, b, n, g);
			//double difference = Math.abs(integralResult - trueIntegralValue);
			results.add(new Pair<>(i, integralResult));
		}
		GraphicsUtils.show(results, trueIntegralValue);
	}

	public static double integrate2(double a, double b, int n, BiFunction<Double, Double, Double> g) {

		double integralResult = 0;
		//плотность вероятности равномерного распределения
		double p = p(a, b);
		Random random = new Random();
		for (int i = 0; i < n; i++) {

			// генерирование равномерно распределенной случайно величины
			double r1 = random.nextDouble();
			double x = (b - a) * r1 + a;
			double r2 = random.nextDouble();
			double y = (b - a) * r2 + a;
			if (between(x * x + y * y, 1, 3)) {
				integralResult += g.apply(x, y) / (p * p);
			}
		}
		integralResult /= n;
		return integralResult;
	}

	public static boolean between(double x, double a, double b) {

		return x >= a && x <= b;
	}
}
