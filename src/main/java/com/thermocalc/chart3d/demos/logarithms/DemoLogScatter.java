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

import java.util.ArrayList;
import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.logarithm.chart.LogChartComponentFactory;
import org.jzy3d.logarithm.transform.SpaceTransformLog10;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Pair;
import org.jzy3d.plot3d.primitives.ConcurrentScatterMultiColorList;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.transform.space.SpaceTransformer;

/**
 * Draw a scatter using log10 transform.
 * 
 * FIXME : Scale with {0, 0.1, 1} lead to misplaced tick label for 0.1 (overlap 1.0 label)
 * FIXME : if offset is 0.001, scale is OK. If offset is 0.0, multiple weird things (minimum Z value inappropriate)
 * FIXME : Z is not properly places
 * 
 * @author Martin Pernollet
 *
 */
public class DemoLogScatter extends AbstractAnalysis {
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new DemoLogScatter());
    }
    
    SpaceTransformer spaceTransformer = new SpaceTransformer(null, null, new SpaceTransformLog10());

    @Override
    public void init() {
        LogChartComponentFactory f = new LogChartComponentFactory(spaceTransformer);
        
        int size = 500000;
        chart = f.newChart(Quality.Advanced, "awt");
        chart.add(makeDemoScatter(size));

        float[] zticks = {0.0000f, 0.1f, 1f};
        chart.getView().getAxe().getLayout().setZTickProvider(new StaticTickProvider(zticks));
    }

    public ConcurrentScatterMultiColorList makeDemoScatter(int size) {
        float x;
        float y;
        float z;
        float a;

        ArrayList<Coord3d> coords = new ArrayList<Coord3d>();
        Color[] colors = new Color[size];
        Random r = new Random();
        r.setSeed(0);

        float offset = 0.01f;
        
        for (int i = 0; i < size; i++) {
            x = r.nextFloat() + offset;
            y = r.nextFloat() + offset;
            z = r.nextFloat() + offset;
            coords.add(new Coord3d(x, y, z));
            a = 0.25f;
            colors[i] = new Color(x, y, z, a);
        }
        Pair<Coord3d,Coord3d> minMax = Coord3d.minMax(coords);
        
        
        // Make transformable scatter
        ConcurrentScatterMultiColorList scatter = new ConcurrentScatterMultiColorList(coords, new ColorMapper(new ColorMapRainbow(), minMax.a.z, minMax.b.z, new Color(1, 1, 1, .5f)));
        return scatter;
    }
}
