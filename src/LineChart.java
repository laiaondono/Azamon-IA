import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.ArrayList;

//CLASE USADA PARA LOS EXPERIMENTOS
public class LineChart extends JFrame {
    private static final long serialVersionUID = 1L;

    public LineChart(ArrayList<Long> t1, ArrayList<Long> t2, ArrayList<Long> t3, ArrayList<Long> t4, ArrayList<Long> t5,
                     ArrayList<Long> t6, ArrayList<Long> t7, ArrayList<Long> t8, ArrayList<Long> t9, ArrayList<Long> t10) {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Experimentos para definir el número de iteraciones", // Chart title
                "proporción del peso transportable", // X-Axis Label
                "tiempo de ejecución [ms]", // Y-Axis Label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public LineChart(ArrayList<Long> t) {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset2(t);
        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Experimentos para definir la ponderación de la felicidad", // Chart title
                "ponderación de la felicidad", // X-Axis Label
                "tiempo de ejecución", // Y-Axis Label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public LineChart(ArrayList<Double> c, int a) {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset3(c);
        // Create chart
        JFreeChart chart;
        if (a == 0) {
            chart = ChartFactory.createLineChart(
                    "Experimentos para definir la ponderación de la felicidad", // Chart title
                    "ponderación de la felicidad", // X-Axis Label
                    "coste", // Y-Axis Label
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );
        }
        else {
            chart = ChartFactory.createLineChart(
                    "Experimentos para definir la ponderación de la felicidad", // Chart title
                    "ponderación de la felicidad", // X-Axis Label
                    "felicidad", // Y-Axis Label
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );
        }

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private DefaultCategoryDataset createDataset(ArrayList<Long> t1, ArrayList<Long> t2, ArrayList<Long> t3, ArrayList<Long> t4, ArrayList<Long> t5,
                                                 ArrayList<Long> t6, ArrayList<Long> t7, ArrayList<Long> t8, ArrayList<Long> t9, ArrayList<Long> t10) {
        String series1 = "1";
        String series2 = "2";
        String series3 = "3";
        String series4 = "4";
        String series5 = "5";
        String series6 = "6";
        String series7 = "7";
        String series8 = "8";
        String series9 = "9";
        String series10 = "10";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double p = 1.2;

        for (int i = 0; i < t1.size(); ++i) {
            dataset.addValue((Number) t1.get(i), series1, p);
            dataset.addValue((Number) t2.get(i), series2, p);
            dataset.addValue((Number) t3.get(i), series3, p);
            dataset.addValue((Number) t4.get(i), series4, p);
            dataset.addValue((Number) t5.get(i), series5, p);
            dataset.addValue((Number) t6.get(i), series6, p);
            dataset.addValue((Number) t7.get(i), series7, p);
            dataset.addValue((Number) t8.get(i), series8, p);
            dataset.addValue((Number) t9.get(i), series9, p);
            dataset.addValue((Number) t10.get(i), series10, p);
            p += 0.2;
        }
        System.out.println("AAA" + dataset.toString());
        return dataset;
    }

    private DefaultCategoryDataset createDataset2(ArrayList<Long> t) {
        String series1 = "1";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < t.size(); ++i)
            dataset.addValue((Number) t.get(i), series1, i+1);

        return dataset;
    }

    private DefaultCategoryDataset createDataset3(ArrayList<Double> t) {
        String series1 = "1";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < t.size(); ++i)
            dataset.addValue((Number) t.get(i), series1, i+1);

        return dataset;
    }

}








