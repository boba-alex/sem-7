package Lab2.distribution.impl;

import Lab2.MathHelper;
import Lab2.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Random;

public class GeometricDistribution extends DiscreteDistribution {

    private double q;

    public GeometricDistribution(int n, double p, double e, int graduationsCount) {

        this.n = n;
        this.p = p;
        this.q = 1 - p;
        this.e = e;
        this.gradationsCount = graduationsCount; // Геом. распредел-е: 1,2,3,4 и >=5,...
        this.list = new int[n];
    }

    @Override
    public String getName() {
        return "Геометрическое распределение";
    }

    @Override
    public double probabilityFunction(int x) {

        return p * Math.pow(q, x - 1);
    }

    @Override
    public double getMathExpectation() {
        return 1 / p;
    }

    @Override
    public double getDispersion() {
        return q / Math.pow(p, 2);
    }

    @Override
    public int[] generateSequence() {

        Random random = new Random();
        for (int i = 0; i < n; i++) {

            double a = random.nextDouble();
            int x = (int) Math.ceil(Math.log(a) / Math.log(q)); // ceil -> 1 - минимальное число в последовательности
            list[i] = x;
        }
        return list;
    }

    @Override
    public double calculateX2() {

        int[] v = new int[gradationsCount]; //частота 1,2,3,4 и >=5
        for (int i = 0; i < n; i++) {
            for (int value = 1; value < gradationsCount; value++) {
                if (list[i] == value) {
                    v[value - 1]++;
                    break;
                } else if (list[i] >= gradationsCount) {
                    v[gradationsCount - 1]++;// >=5
                    break;
                }
            }
        }

        double[] pk = new double[gradationsCount];
        for (int i = 0; i < gradationsCount - 1; i++) {
            pk[i] = probabilityFunction(i + 1);
        }
        pk[gradationsCount - 1] = 1 - MathHelper.sum(pk, gradationsCount - 1);

        double x2 = 0;
        for (int i = 0; i < gradationsCount; i++) {
            x2 += Math.pow(v[i] - n * pk[i], 2) / (n * pk[i]);
        }
        return x2;
    }
}
