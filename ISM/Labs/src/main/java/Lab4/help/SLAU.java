package main.java.Lab4.help;

import java.util.Random;

public class SLAU {

    public static int n = 2;            //Размерность системы
    public static double x = 0, y = 0;    //Решение системы

    public static double[] h;
    public static double[] pi = new double[n];    //Вектор нач. вероятностей цепи Маркова
    public static double[][] p = new double[n][n];    //Матрица переходных состояний цепи Маркова
    public static int N;        //Длина цепи Маркова
    public static int[] i;        //Цепь Маркова
    public static double[] Q;    //Веса состояний цепи Маркова
    public static double[] ksi;    //СВ
    public static int m;        //Количество реализаций цепи Маркова
    public static double alpha;    //БСВ

    public static double[][] a = new double[n][n];
    public static double[] f = new double[n];

    public static double calculate(double[] h) {
        Random rnd = new Random();

        double result = 0;

        for (int j = 0; j < m; j++) {
            alpha = rnd.nextDouble();
            if (alpha < pi[0])
                i[0] = 0;        //реализуется 1-е состояние
            else i[0] = 1;            //реализуется 2-е состояние
            for (int k = 1; k <= N; k++) {
                alpha = rnd.nextDouble();
                if (alpha < 0.5)
                    i[k] = 0;
                else i[k] = 1;
            }
            //Вычисляем веса цепи Маркова
            if (pi[i[0]] > 0)
                Q[0] = h[i[0]] / pi[i[0]];
            else Q[0] = 0;
            for (int k = 1; k <= N; k++) {
                if (p[i[k - 1]][i[k]] > 0)
                    Q[k] = Q[k - 1] * a[i[k - 1]][i[k]] / p[i[k - 1]][i[k]];
                else Q[k] = 0;
            }
            for (int k = 0; k <= N; k++)
                ksi[j] = ksi[j] + Q[k] * f[i[k]];
        }
        for (int k = 0; k < m; k++)
            result += ksi[k];
        result /= m;
        return  result;
    }

    public static void main(String[] args) {
//        double[][] a = {
//                {0.7, -0.2, 0.3},
//                {0.5, 1.3, 0.1},
//                {-0.1, 0.4, 1.3}};
//        double[] f = {-2, -3, 2};


        f[0] = (double) 0.1;
        f[1] = (double) -0.2;

        a[0][0] = (double) -0.1;
        a[0][1] = (double) 0.8;
        a[1][0] = (double) 0.4;
        a[1][1] = (double) -0.1;


        h = new double[n];
        N = 1000;

        i = new int[N + 1];
        Q = new double[N + 1];

        m = 10000;

        ksi = new double[m];
        for (int j = 0; j < m; j++)
            ksi[j] = 0;

        h[1] = 1;
        h[0] = 0;
        pi[1] = (double) 0.5;
        pi[0] = (double) 0.5;
        p[0][0] = (double) 0.5;
        p[0][1] = (double) 0.5;
        p[1][0] = (double) 0.5;
        p[1][1] = (double) 0.5;
        y = calculate(h);
        System.out.println(y);

    }
}