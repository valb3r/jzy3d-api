package com.thermocalc.chart3d.demos;

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
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.io.FileDataset;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ScientificNotationTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.transform.space.SpaceTransformLog;
import org.jzy3d.plot3d.transform.space.SpaceTransformer;

import com.thermocalc.chart3d.normalize.Normalizer;

public class DemoLogChart_NOTREADY extends AbstractAnalysis {

    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new DemoLogChart_NOTREADY());
    }

    @Override
    public void init() {
        try {
            IChartComponentFactory f = new AWTChartComponentFactory();
            
            List<Coord3d> coords = FileDataset.loadList("data/thermocalc-sample.csv");
            
            // The trick is here : normalizing to avoid very small Z values
            Normalizer.Model m = Normalizer.normalizeZ(coords);
            
            
            // Make shape
            final Shape surfaces = (Shape) Builder.buildDelaunay(coords);
            surfaces.setColorMapper(new ColorMapper(new ColorMapRainbow(), surfaces.getBounds().getZmin(), surfaces.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
            surfaces.setFaceDisplayed(true);
            surfaces.setWireframeDisplayed(true);
            surfaces.setWireframeColor(Color.BLACK);
            

            // Make chart
            Quality quality = new Quality(true, true, true, true, false, false, true);
            chart = f.newChart(quality, Toolkit.awt);
            chart.getScene().getGraph().add(surfaces);
            chart.getView().getCamera().setViewportMode(ViewportMode.RECTANGLE_NO_STRETCH);
            chart.addMouseCameraController();
            float[] s = {1.0f, 1.0f};
            chart.getCanvas().setPixelScale(s);

            // Use a specific tick renderer able to unnormalize
            chart.getAxeLayout().setZTickRenderer( new ScientificNotationTickRendererNormalized(m, 2));   
            chart.getView().updateBounds();
            
            // ------------------------
            // Apply log - See TrialLogChart
            
            SpaceTransformer logTrfrm = new SpaceTransformer(null, null, new SpaceTransformLog());
            surfaces.setSpaceTransformer(logTrfrm);
            ((AxeBox)chart.getView().getAxe()).setSpaceTransformer(logTrfrm);
            chart.getView().setSpaceTransformer(logTrfrm);
            
        } catch (IOException ex) {
            Logger.getLogger(DemoLogChart_NOTREADY.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class ScientificNotationTickRendererNormalized extends ScientificNotationTickRenderer{
        Normalizer.Model model;
        
        public ScientificNotationTickRendererNormalized(Normalizer.Model model) {
            super();
            this.model = model;
        }

        public ScientificNotationTickRendererNormalized(Normalizer.Model model, int precision) {
            super(precision);
            this.model = model;
        }
        
        @Override
        public String format(double value) {
            Coord3d c = new Coord3d(0,0,value); // TODO : optimize me, use direct value
            Normalizer.unnormalize(c, model);
            return Utils.num2str('e', c.z, precision);
        }
    }
}
