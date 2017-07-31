package com.thermocalc.chart3d;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.colormaps.ColorMapWhiteGreen;
import org.jzy3d.colors.colormaps.ColorMapWhiteRed;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.dualaxe.chart.DualAxeChart;
import org.jzy3d.dualaxe.chart.DualAxeChartFactory;
import org.jzy3d.dualaxe.chart.DualAxeView;
import org.jzy3d.dualaxe.utils.ShapeVisitor;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Surface;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.drawable.DrawableTextBitmap;

// BUGS
// -UNDER RARE VIEWPOINTS, BOTH Z AXIS RENDER ON SAME SIDE
// -BLUE MAIN COLOR FOR CBAR = YELLOW BACKGROUND IN CBAR
// -LE HALIGN TOURNE PARFOIS LES LABEL VERS L INTERIEUR
public class DemoThermocalcChart extends AbstractAnalysis {
    float zoffset = 10;
    float alpha1 = 0.75f;
    float alpha2 = 0.45f;
    Color color1 = Color.BLACK;
    Color color2 = Color.BLUE;
    IColorMap colorMap1 = new ColorMapWhiteGreen();/* ColorMapRainbow()*/;
    IColorMap colorMap2 = new ColorMapWhiteRed();/* ColorMapRainbow()*/;
    ShapeVisitor finder = new ShapeVisitor();
    
    int steps = 80;


    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new DemoThermocalcChart(), new Rectangle(200, 200, 800, 600));
    }

    public DemoThermocalcChart() {
    }

    @Override
    public void init() {
        factory = new DualAxeChartFactory();

        // Define a function to plot
        Mapper func1 = func3d(0, 0, 0);
        Mapper func2 = func3d(1, 2, zoffset = 100);
        Range range1 = new Range(-3, 3);
        Range range2 = new Range(-4, -2);
        

        // Create the object to represent the functions over the given range.
        Shape surface1 = Surface.shape(func1, range1, steps, colorMap1, alpha1);
        surface1.setWireframeColor(color1.alpha(alpha1));
        surface1.setWireframeDisplayed(true);
        
        Shape surface2 = Surface.shape(func2, range2, steps, colorMap2, alpha2);
        surface2.setWireframeColor(color2.alpha(alpha2));
        surface2.setWireframeDisplayed(true);
        
        Coord3d c1 = finder.zAt(surface1, range1.getMin(), range1.getMin());
        Coord3d c2 = finder.zAt(surface2, range2.getMin(), range2.getMin());
        
        DrawableTextBitmap surface1Name = new DrawableTextBitmap("Surface 1", c1, color1);
        DrawableTextBitmap surface2Name = new DrawableTextBitmap("Surface 2", c2, color1);
        
        surface1Name.setHalign(Halign.RIGHT);
        // Create a chart with two scenes
        chart = initializeChart(quality());
        
        chart.addKeyboardCameraController();

        DualAxeChart dChart = (DualAxeChart) chart;
        dChart.getScene().getGraph().add(surface1);
        dChart.getScene().getGraph().add(surface1Name);
        dChart.getScene2().getGraph().add(surface2);
        //dChart.getScene2().getGraph().add(surface2Name);
        chart.setAnimated(true);
        //dChart.getView().getAnnotations().add(surface1Name);

        /*AxeAnnotation aa = new AxeAnnotation() {
            
            @Override
            public void draw(GL gl, AxeBox axe) {
                surface1Name.draw(gl, glu, cam);
            }
        };*/
        
        DualAxeView dView = (DualAxeView) dChart.getView();
        dView.getAxe().getLayout().setZAxeLabel("Z1");
        dView.getAxe().getLayout().setMainColor(color1);
        //dView.getAxe().getAnnotations().add(aa);
        
        dView.getAxe2().getLayout().setZAxeLabel("Z2");
        dView.getAxe2().getLayout().setMainColor(color2);
        dChart.getView().updateBounds();

        dChart.colorbar(surface1, dView.getAxe().getLayout()).setBackground(Color.WHITE);
        dChart.colorbar(surface2, dView.getAxe2().getLayout()).setBackground(Color.WHITE);
    }
    
    
    
    public static Quality quality() {
        // QUALITY
        Quality q = Quality.Advanced;
        q.setDepthActivated(true);
        q.setDisableDepthBufferWhenAlpha(false); 
        return q;
    }

    public static Mapper func3d(double xoffset, double yoffset, double zoffset) {
        Mapper f1 = new Mapper() {
            @Override
            public double f(double x, double y) {
                return zoffset + (x + xoffset) * Math.sin((x + xoffset) * (y + yoffset));
            }
        };
        return f1;
    }
}
