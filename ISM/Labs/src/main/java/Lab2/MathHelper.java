package Lab2;

public class MathHelper {

	public static double sum(double[] p, int n) {

		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += p[i];
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

	public static long factorial(int n) {

		long result = 1;
		for (int i = 2; i <= n; i++) {
			result *= i;
		}
		return result;
	}
}
