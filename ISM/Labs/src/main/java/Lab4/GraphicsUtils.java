package Lab4;

import org.apache.commons.math3.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

public class GraphicsUtils {

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
