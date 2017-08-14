/*
 * Copyright (c) Since 2017, Martin Pernollet
 * All rights reserved. 
 *
 * Redistribution in binary form, with or without modification, is permitted.
 * Edition of source files is allowed.
 * Redistribution of original or modified source files is FORBIDDEN.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thermocalc.chart3d.demos.logarithms;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.logarithm.chart.LogChartComponentFactory;
import org.jzy3d.logarithm.transform.SpaceTransformLog10;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Surface;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;
import org.jzy3d.plot3d.transform.space.SpaceTransformer;

/**
 * A surface having z=Math.pow(10, x+y) that is expected to appear
 * as a diagonal plane when using log10 transform.
 * 
 * FIXME : "shaking" camera with high values : Math.pow(10, 4);
 * FIXME : Wrong rendering sphere for high values : Math.pow(10, 10);
 * FIXME : Misplaced Z label
 * 
 * TODO  : Add a LogAxis tick provider based on Axis bounding box 
 * 
 * @author Martin Pernollet
 */
public class DemoLogSurface {

    public static void main(String[] args) {
        SpaceTransformer spaceTransformer = new SpaceTransformer(null, null, new SpaceTransformLog10());

        LogChartComponentFactory f = new LogChartComponentFactory(spaceTransformer);
        
        // -----------------------------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(10, x+y);
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(0, 2); 
        int steps = 80;

        // Create the object to represent the function over the given range.
        Shape surface = Surface.shape(mapper, range, steps, new ColorMapRainbow(), .5f);

        // -----------------------------------------------------
        // Create a chart and add drawables
        Chart chart = f.newChart(Quality.Advanced, "awt");
        chart.add(surface);
        chart.getView().setBoundMode(ViewBoundMode.AUTO_FIT);
        float[] zticks = { 0.0f, 10.0f, 100.0f, 1000.0f, 10000.0f };
        chart.getView().getAxe().getLayout().setZTickProvider(new StaticTickProvider(zticks));
        chart.getView().setSquared(true);
        
        //--------------
        ChartLauncher.openChart(chart);
    }
}
