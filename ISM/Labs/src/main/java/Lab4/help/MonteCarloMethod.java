package main.java.Lab4.help;

import java.util.Random;


public class MonteCarloMethod {

    public static double g(double x) {
        return Math.pow(x, 5) * Math.exp(-Math.pow(x, 2)/2);
    }

    public static double p(double x) {
        return x * Math.exp(-0.5 * Math.pow(x, 2));
    }

    public static void main(String[] args) {
        int n = 2000000;
        double result = 0;
        double[] ni = new double[n];
        double c = 2;
        double l = 0.5;

        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            double a = rnd.nextDouble();
            ni[i] = Math.pow(- 1 / l * Math.log(a), 1 / c);
            result += g(ni[i]) / p(ni[i]);
        }

        result /= n * 1.0;

        System.out.println("Integral = " + result);
        System.out.println(8 - result);
    }
}