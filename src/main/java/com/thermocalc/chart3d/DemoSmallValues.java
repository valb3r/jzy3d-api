package com.thermocalc.chart3d;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.ViewportMode;

import com.thermocalc.chart3d.tinyaxes.TinyAxe;
import com.thermocalc.chart3d.tinyaxes.TinyCamera;
import com.thermocalc.chart3d.tinyaxes.TinyScientificNotationTickRenderer;

public class DemoSmallValues extends AbstractAnalysis {

    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new DemoSmallValues());
    }

    @Override
    public void init() {
        try {
            IChartComponentFactory f = new AWTChartComponentFactory(){
                @Override
                public IAxe newAxe(BoundingBox3d box, View view) {
                    AxeBox axe = new TinyAxe(box);
                    axe.setView(view);
                    return axe;
                }
                
                @Override
                public Camera newCamera(Coord3d center) {
                    return new TinyCamera(center);
                }
            };
            
            List<Coord3d> coords = FileDataset.loadList("data/thermocalc-sample.csv");
            
            // The trick is here : normalizing 
            BigDecimal zMulUp = new BigDecimal(1E+29);
            BigDecimal zMulDown = new BigDecimal(1E-29);
            Coord3d.mul(coords, 1, 1, zMulUp.floatValue());
            
            
            final Shape surfaces = (Shape) Builder.buildDelaunay(coords);
            surfaces.setColorMapper(new ColorMapper(new ColorMapRainbow(), surfaces.getBounds().getZmin(), surfaces.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
            surfaces.setFaceDisplayed(true);
            surfaces.setWireframeDisplayed(true);
            surfaces.setWireframeColor(Color.BLACK);

            Quality advancedQualitySmoothPointTrue = Quality.Intermediate;//new Quality(true, true, true, true, false, false, true);
            chart = f.newChart(advancedQualitySmoothPointTrue, Toolkit.awt);
            chart.getScene().getGraph().add(surfaces);
            chart.getView().getCamera().setViewportMode(ViewportMode.RECTANGLE_NO_STRETCH);
            chart.addMouseCameraController();
            float[] s = {1.0f, 1.0f};
            chart.getCanvas().setPixelScale(s);
            
            chart.getAxeLayout().setZTickRenderer( new TinyScientificNotationTickRenderer(2, zMulDown.floatValue()) );   
            //chart.getAxeLayout().setZTickLabelDisplayed(true);

            chart.getView().updateBounds();
            chart.render();
        } catch (IOException ex) {
            Logger.getLogger(DemoSmallValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
