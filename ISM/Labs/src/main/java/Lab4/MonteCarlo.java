package Lab4;

import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MonteCarlo {

	//плотность вероятности равномерного распределения
	public static double p(double a, double b) {

		return 1 / (b - a);
	}

	public static void main(String[] args) {

		testExample1();
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
		show(results, trueIntegralValue);
	}

	public static double integrate(double a, double b, int n, Function<Double, Double> g) {

		double integralResult = 0;
		double p = p(a, b);
		Random random = new Random();
		for (int i = 0; i < n; i++) {

			double y = random.nextDouble();
			double x = (b - a) * y + a;
			integralResult += g.apply(x) / p;
		}
		integralResult /= n;
		return integralResult;
	}

	public static void show(List<Pair<Integer, Double>> points, Double trueIntegralValue) {

		XYSeries seriesAppr = new XYSeries("Approximate integral");
		XYSeries seriesTrue = new XYSeries("True integral");

		for (Pair<Integer, Double> p : points) {
			seriesAppr.add(p.getKey(), p.getValue());
			seriesTrue.add(p.getKey(), trueIntegralValue);
		}

		XYSeriesCollection xyDataset = new XYSeriesCollection();
		xyDataset.addSeries(seriesAppr);
		xyDataset.addSeries(seriesTrue);
		JFreeChart chart = ChartFactory.createXYLineChart("Two integrals", "n", "value", xyDataset, PlotOrientation.VERTICAL, true, true, true);
		//JFreeChart chart=ChartFactory.createScatterPlot("Two integrals", "n", "value", xyDataset);
		JFrame frame = new JFrame("Graphics");
		frame.getContentPane().add(new ChartPanel(chart));
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
