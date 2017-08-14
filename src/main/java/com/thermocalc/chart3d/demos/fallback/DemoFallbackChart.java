package com.thermocalc.chart3d.demos.fallback;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Surface;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import com.thermocalc.chart3d.fallback.FallbackChart;
import com.thermocalc.chart3d.fallback.FallbackChartFactory;
import com.thermocalc.chart3d.fallback.FallbackChartFrameAbstract;
import com.thermocalc.chart3d.fallback.FallbackChartFrameMiglayout;

/**
 * Illustrate how to create charts that properly downsize when used in MigLayout and MacOSX.
 * 
 * The idea is to render the chart offscreen in an AWT image, and then display this image in a standard
 * swing component.
 * 
 * @author Martin Pernollet
 */
public class DemoFallbackChart {
    public static void main(String[] args) {
        
        // Jzy3d
        FallbackChartFactory factory = new FallbackChartFactory();
        AWTChart chart  = getDemoChart(factory);
        
        
        //FallbackChartFrameAbstract w = new FallbackChartFrameSwing(chart);
        FallbackChartFrameAbstract w = new FallbackChartFrameMiglayout(chart);
        
        
        chart.addMouseCameraController();

    }

    private static AWTChart getDemoChart(FallbackChartFactory factory) {
        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return x * Math.sin(x * y);
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(-3, 3);
        int steps = 80;

        // Create the object to represent the function over the given range.
        Shape surface = Surface.shape(mapper, range, steps, new ColorMapRainbow(), .5f);

        // -------------------------------
        // Create a chart
        Quality quality = Quality.Advanced;
        //quality.setSmoothPolygon(true);
        //quality.setAnimated(true);
        
        // let factory bind mouse and keyboard controllers to JavaFX node
        FallbackChart chart = (FallbackChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);
        return chart;
    }
}
