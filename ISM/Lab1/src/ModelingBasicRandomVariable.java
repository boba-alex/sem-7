import java.util.Random;

public class ModelingBasicRandomVariable {

    //Variant 4) a0 = β = 78 125, K = 256
    private static final int ALPHA0 = 78125;
    private static final int BETA = 78125;
    private static final int K = 256;
    private static final int M = Integer.MAX_VALUE;
    private static final int N = 1000;
    private static final double E = 0.05;

    private static double[] list1 = new double[N]; //b[i]
    private static double[] list2 = new double[N]; //a[i]

    public static void main(String[] args) {

        //multiplicative-congruent method
        double tempAlpha = ALPHA0;
        for (int t = 0; t < N; t++) {

            tempAlpha = (BETA * tempAlpha) % M;
            list1[t] = tempAlpha / M;
        }

        //V - helpful for Macklaren-Marsali
        double[] v = new double[K];
        for (int i = 0; i < K; i++) {
            v[i] = list1[i];
        }

        Random random = new Random(0); // for c[i]
        for (int t = 0; t < N; t++) {
            int s = (int) (random.nextInt(100) / 100.0 * K);
            list2[t] = v[s];
            v[s] = list1[(t + K) % N];
        }


    }
}


//    }
//}