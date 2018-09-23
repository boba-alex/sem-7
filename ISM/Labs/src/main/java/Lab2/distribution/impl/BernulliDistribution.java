package Lab2.distribution.impl;

import Lab2.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Random;

public class BernulliDistribution extends DiscreteDistribution {

    public BernulliDistribution(int n, double p, double e) {

        this.n = n;
        this.p = p;
        this.e = e;
        this.gradationsCount = 2; // потому что ДСВ Бернулли принимает значения 0 или 1
        this.list = new int[n];
    }

    @Override
    public String getName() {
        return "Распределение Бернулли";
    }

    @Override
    public double probabilityFunction(int x) {
        return Math.pow(p, x) * Math.pow(1 - p, 1 - x);
    }

    @Override
    public double getMathExpectation() {

        return p;
    }

    @Override
    public double getDispersion() {

        return p * (1 - p);
    }

    @Override
    public int[] generateSequence() {

        Random random = new Random();
        for (int i = 0; i < n; i++) {

            double a = random.nextDouble();
            if (a <= p) {
                list[i] = 1;
            } else {
                list[i] = 0;
            }
        }
        return list;
    }

    @Override
    public double calculateX2() {

        int[] v = new int[gradationsCount]; //частота 0 и 1
        for (int i = 0; i < n; i++) {
            if (list[i] == 1) {
                v[1]++;
            } else {
                v[0]++;
            }
        }
        // тут гипотетические вероятности Pk это p и 1-p согласно probabilityFunction
        return Math.pow(v[1] - n * p, 2) / (n * p) + Math.pow(v[0] - n * (1 - p), 2) / (n * (1 - p));
    }
}
