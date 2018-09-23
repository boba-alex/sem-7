package Lab2.distribution.impl;

import Lab2.MathHelper;
import Lab2.distribution.DiscreteDistribution;

public class NegativeBinomialDistribution extends DiscreteDistribution {

    private int r;

    public NegativeBinomialDistribution(int n, int r, double p, double e) {

        this.n = n;
        this.r = r;
        this.p = p;
        this.e = e;
        this.gradationsCount = 10; // Бином. распредел-е: 0,1,...8 и >=9
        this.list = new int[n];
    }

    @Override
    public String getName() {
        return "Отрицательное биномиальное распределение";
    }

    @Override
    public double probabilityFunction(int x) {

        return MathHelper.c(x + r - 1, x) * Math.pow(p, r) * Math.pow(1 - p, x);
    }

    @Override
    public double getMathExpectation() {

        return r * (1 - p) / p;
    }

    @Override
    public double getDispersion() {

        return r * (1 - p) / Math.pow(p, 2);
    }

    @Override
    public int[] generateSequence() {
        return new int[0]; //TODO
    }

    @Override
    public double calculateX2() {
        return 0;
    }
}
