//package com.elyashevich.ism.geometric;
//
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.elyashevich.ism.util.IsmConstant.N;
//import static com.elyashevich.ism.util.IsmConstant.GEOMETRIC_P;
//
//@Data
//public class Geometric {
//
//    public double probabilityFunction(int argument) {
//        if (argument == 0) {
//            return 0;
//        }
//        return GEOMETRIC_P * Math.pow(1 - GEOMETRIC_P, argument - 1);
//    }
//
//    public List<Double> generateGeometric(List<Double> arr1) {
//        double q = 1 - GEOMETRIC_P;
//        List<Double> x = new ArrayList<>();
//
//        for (int i = 0; i < N; i++) {
//            x.add(Math.ceil(Math.log(arr1.get(i)) / Math.log(q)));
//        }
//        return x;
//    }
//
//    public double getGeometricMathExpectation() {
//        return 1 / GEOMETRIC_P;
//    }
//
//    public double getGeometricDispersion() {
//        return (1 - GEOMETRIC_P)/ Math.pow(GEOMETRIC_P, 2);
//    }
//
//    public Double getUnbiasedEstimateMathExpectation(List<Double> x) {
//        double sum = 0;
//        for (int i = 0; i < N; i++) {
//            sum += x.get(i);
//        }
//        return sum / N;
//    }
//
//    public Double getUnbiasedEstimateDispersion(List<Double> x, double mx) {
//        double sum = 0;
//        for (int i = 0; i < N; i++) {
//            sum += Math.pow(x.get(i) - mx, 2);
//        }
//        return sum / (N);
//    }
//}