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
import org.jzy3d.colors.Color;
import org.jzy3d.logarithm.chart.LogChartComponentFactory;
import org.jzy3d.logarithm.transform.SpaceTransformLog10;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.transform.space.SpaceTransformer;

/**
 * 4 points having 
 * <ul>
 * <li>X lying at 0-1-2-3
 * <li>Y lying at 0-10-20-30
 * <li>Z lying at 0-10-100-1000
 * </ul>
 * 
 * Renderered as a line using {@link SpaceTransformLog10()}
 * 
 * 
 * @author Martin Pernollet
 */
public class DemoLogPoints {

    public static void main(String[] args) {
        SpaceTransformer spaceTransformer = new SpaceTransformer(null, null, new SpaceTransformLog10());

        LogChartComponentFactory f = new LogChartComponentFactory(spaceTransformer);

        // -----------------------------------------------------
        Point point1 = new Point();
        point1.setData(new Coord3d(0, 0, 0));
        point1.setColor(new Color(1, 0, 0));
        point1.setWidth(10);

        Point point2 = new Point();
        point2.setData(new Coord3d(1, 10, 10));
        point2.setColor(new Color(0, 1, 0));
        point2.setWidth(10);

        Point point3 = new Point();
        point3.setData(new Coord3d(2, 20, 100));
        point3.setColor(new Color(0, 0, 1));
        point3.setWidth(10);

        Point point4 = new Point();
        point4.setData(new Coord3d(3, 30, 1000));
        point4.setColor(new Color(0, 0, 1));
        point4.setWidth(10);


        // -----------------------------------------------------
        // Create a chart and add drawables
        Chart chart = f.newChart(Quality.Advanced, "awt");
        chart.getView().setSquared(false);

        float[] xticks = { 0.0f, 1.0f, 2.0f, 3.0f };
        float[] yticks = { 0.0f, 10.0f, 20.0f, 30.0f };
        float[] zticks = { 0.0f, 10.0f, 100.0f, 1000.0f };
        
        chart.getAxeLayout().setXTickProvider(new StaticTickProvider(xticks));
        chart.getAxeLayout().setYTickProvider(new StaticTickProvider(yticks));
        chart.getAxeLayout().setZTickProvider(new StaticTickProvider(zticks));

        chart.add(point1);
        chart.add(point2);
        chart.add(point3);
        chart.add(point4);

        ChartLauncher.openChart(chart);

        //chart.getView().setBoundMode(ViewBoundMode.AUTO_FIT);

    }
}
