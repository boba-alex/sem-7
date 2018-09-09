public class ModelingBasicRandomVariable {

    //Variant 4) a0 = β = 78 125, K = 256
    private static final int ALPHA0 = 78125;
    private static final int BETA = 78125;
    private static final int K = 256;
    private static final int M = Integer.MAX_VALUE;
    private static final int N = 1000;
    private static final double E = 0.05;

    private static double[] list1 = new double[N];
    private double[] list2 = new double[N];

    public static void main(String[] args) {

        double tempAlpha = ALPHA0;
        for (int t = 0; t < N; t++) {

            tempAlpha = (BETA * tempAlpha) % M;
            list1[t] = tempAlpha / M;
        }

        double[] v = new double[K];
        for (int i = 0; i < K; i++) {
            v[i] = list1[i];
        }


    }
}


//import java.util.Arrays;
//import java.util.Random;
//
//public class Modeling {
//    public static void main(String[] args) {
//        double[] list1 = new double[1000];
//        double[] list2 = new double[1000];
//        int startA, b, m, k, n;
//        double e = 0.05;
//        startA = b = 131075;
//        k = 128;
//        n = 1000;
//        double[] vector = new double[k];
//        m = (int) Math.pow(2, 31);
//        double tempA = (b * startA) % m;
//        list1[0] = (tempA / m);
//        vector[0] = list1[0];
//
//        for (int i = 1; i < n; i++) {
//            list1[i] = (((list1[i - 1] * m * b) % m) / m);
//        }
//        for (int i = 0; i < k; i++) {
//            vector[i] = list1[i];
//        }
//        Random r = new Random(0);
//        for (int i = 0; i < n; i++) {
//            int index = (int) ((r.nextInt(100) % 100) / (100 * 1.0) * k);
//            list2[i] = vector[index];
//            vector[index] = list1[(i + k) % n];
//        }
//        System.out.println("Pearson");
//        int[] v1 = new int[50];
//        int[] v2 = new int[50];
//        for (int i = 0; i < 50; i++) {
//            v1[i] = 0;
//            v2[i] = 0;
//        }
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < 50; j++) {
//                if (j / 50.0 < list1[i] && list1[i] < (j + 1) / 50.0) {
//                    v1[j]++;
//                    break;
//                }
//            }
//        }
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < 50; j++) {
//                if (j / 50.0 < list2[i] && list2[i] < (j + 1) / 50.0) {
//                    v2[j]++;
//                    break;
//                }
//            }
//        }
//
//        double x1 = 0;
//        double x2 = 0;
//        for (int i = 0; i < 50; i++) {
//            x1 += Math.pow(v1[i] - n * 1 / 50, 2) / (n * 1 / 50);
//            x2 += Math.pow(v2[i] - n * 1 / 50, 2) / (n * 1 / 50);
//        }
//        System.out.println("X1 = " + x1);
//        if (x1 < 66.3386)
//            System.out.println("true");
//        else System.out.println("false");
//        System.out.println("X2 = " + x2);
//        if (x2 < 66.3386)
//            System.out.println("true");
//        else System.out.println("false");
//
//        double currentFunc1, nextFunc1, currentFunc2, nextFunc2;
//        double[] dn1 = new double[n];
//        double[] dn2 = new double[n];
//
//        Arrays.sort(list1);
//        Arrays.sort(list2);
//
//        for (int i = 0; i < n; i++) {
//            currentFunc1 = i / 1000.0;
//            nextFunc1 = (i + 1) / 1000.0;
//            dn1[i] = Math.max(Math.abs(currentFunc1 - list1[i]), Math.abs(nextFunc1 - list1[i]));
//
//            currentFunc2 = i / 1000.0;
//            nextFunc2 = (i + 1) / 1000.0;
//            dn2[i] = Math.max(Math.abs(currentFunc2 - list2[i]), Math.abs(nextFunc2 - list2[i]));
//        }
//        double maxDn1 = dn1[0];
//        double maxDn2 = dn2[0];
//        for (int i = 0; i < n; i++) {
//            if (maxDn1 < dn1[i])
//                maxDn1 = dn1[i];
//            if (maxDn2 < dn2[i])
//                maxDn2 = dn2[i];
//        }
//        System.out.println(maxDn1 * Math.sqrt(n));
//        if (maxDn1 * Math.sqrt(n) < 1.358)
//            System.out.println("true");
//        else System.out.println("false");
//
//        System.out.println(maxDn2 * Math.sqrt(n));
//        if (maxDn2 * Math.sqrt(n) < 1.358)
//            System.out.println("true");
//        else System.out.println("false");
//
//    }
//}