package com.thermocalc.chart3d.demos.tinyZ;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.io.FileDataset;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Surface;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import com.thermocalc.chart3d.tinyAxes.DefaultDecimalTickRendererNormalized;
import com.thermocalc.chart3d.tinyAxes.Normalizer;
import com.thermocalc.chart3d.tinyAxes.Normalizer.Axis;
import com.thermocalc.chart3d.tinyAxes.Normalizer.Model;

/**
 * Shows how to create charts where one or more axes have very small values and ranges.
 * 
 * Tiny values cause problem for converting tick labels from 3d world (model) to 2d world (screen), which is due to low level JOGL bugs (see links below).
 * 
 * The idea here is to normalize data before building primitive so that OpenGL model stand in a feasible range (0-1), and unnormalize the model when coordnate values needs to be shown (e.g. tick renderers).
 * 
 * @see https://stackoverflow.com/questions/1732117/jogl-glu-gluunproject-always-returning-0-0#1754176
 * @see https://www.opengl.org/discussion_boards/showthread.php/163590-gluUnProject-returning-GL_FALSE
 * @see https://jogamp.org/bugzilla/show_bug.cgi?id=483
 * 
 * @author Martin Pernollet
 *
 */
public class DemoTinyAxes extends AbstractAnalysis {

    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new DemoTinyAxes());
    }

    @Override
    public void init() {
        try {
            IChartComponentFactory f = new AWTChartComponentFactory();

            List<Coord3d> coords = FileDataset.loadList("data/thermocalc-sample.csv", 0, 1, 2);

            // The trick is here : NORMALIZING to avoid very small Z values
            Model normalizer = Normalizer.normalize(coords);

            // -------------------------
            // Make shape
            Shape surfaces = Surface.shape(coords, new ColorMapRainbow(), .5f);
            surfaces.setWireframeDisplayed(true);
            surfaces.setWireframeColor(Color.BLACK);

            // -------------------------
            // Make chart
            Quality quality = new Quality(true, true, true, true, false, false, true);
            chart = f.newChart(quality, Toolkit.awt);
            chart.add(surfaces);
            chart.addMouseCameraController();

            // -------------------------
            // Use a specific tick renderer able to UNNORMALIZE
            chart.getAxeLayout().setXTickRenderer(new DefaultDecimalTickRendererNormalized(normalizer, Axis.X, 4));
            chart.getAxeLayout().setYTickRenderer(new DefaultDecimalTickRendererNormalized(normalizer, Axis.Y, 4));
            chart.getAxeLayout().setZTickRenderer(new DefaultDecimalTickRendererNormalized(normalizer, Axis.Z, 4));

            // -------------------------
            chart.getView().updateBounds();

        } catch (IOException ex) {
            Logger.getLogger(DemoTinyAxes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
