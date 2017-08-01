package com.thermocalc.chart3d.fallback;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JFrame;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.ui.LookAndFeel;
import org.jzy3d.ui.views.ImagePanel;

/** A frame to show a list of charts */
public class FallbackChartFrame extends JFrame {
    private static final long serialVersionUID = 7519209038396190502L;

    public FallbackChartFrame(Chart... charts) {
        this(Arrays.asList(charts));
    }

    public FallbackChartFrame(Collection<? extends Chart> charts) {
        LookAndFeel.apply();

        setGridLayout(charts);

        windowExitListener();
        this.pack();
        setVisible(true);
        setBounds(new java.awt.Rectangle(10, 10, 800, 600));
        
        render(charts); // Generate the first image once the frame is loaded with its final dimension
    }

    private void setGridLayout(Collection<? extends Chart> charts) {
        setLayout(new GridLayout(charts.size(), 1));

        for (Chart c : charts) {
            addChartToGridLayout(c);
        }
    }
    
    protected void render(Collection<? extends Chart> charts) {
        for (Chart c : charts) {
            c.render();
        }
    }

    protected void addChartToGridLayout(Chart chart) {
        if(chart instanceof FallbackChart){
            ImagePanel chartPanel = ((FallbackChart)chart).getImagePanel();//new ImagePanel();

            // Register image panel to chart changes
            FallbackChartFactory.bind(chartPanel, (AWTChart)chart);
            FallbackChartFactory.addPanelSizeChangedListener(chartPanel, chart);
            
            add(chartPanel);
            
        }
        else{
            throw new IllegalArgumentException("Expecting a FallbackChart instance");
        }
    }

    public void windowExitListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FallbackChartFrame.this.dispose();
                System.exit(0);
            }
        });
    }
}