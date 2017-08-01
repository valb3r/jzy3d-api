package com.thermocalc.chart3d.fallback;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.ui.views.ImagePanel;

import com.jogamp.opengl.GLCapabilities;

public class FallbackChart extends AWTChart{
    protected ImagePanel imagePanel = new ImagePanel();

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    
    /* */
    
    public FallbackChart() {
        super();
    }
    public FallbackChart(IChartComponentFactory factory, Quality quality, String windowingToolkit, GLCapabilities capabilities) {
        super(factory, quality, windowingToolkit, capabilities);
    }
    public FallbackChart(IChartComponentFactory factory, Quality quality, String windowingToolkit) {
        super(factory, quality, windowingToolkit);
    }
    public FallbackChart(IChartComponentFactory components, Quality quality) {
        super(components, quality);
    }
    public FallbackChart(Quality quality, String windowingToolkit) {
        super(quality, windowingToolkit);
    }
    public FallbackChart(Quality quality) {
        super(quality);
    }
    public FallbackChart(String windowingToolkit) {
        super(windowingToolkit);
    }
    
    
}
