package com.thermocalc.chart3d.normalize;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.jzy3d.io.FileDataset;
import org.jzy3d.maths.Coord3d;

import com.thermocalc.chart3d.normalize.Normalizer.Model;

public class TestNormalize {
    @Test
    public void testNormUnnormZ() throws Exception {
        List<Coord3d> coords = FileDataset.loadList("data/thermocalc-sample.csv");
        
        Coord3d minBefore = Coord3d.min(coords);
        Coord3d maxBefore = Coord3d.max(coords);
        
        
        // Verify normalized values stand in [0-1]
        Model m = Normalizer.normalize(coords);

        Coord3d minAfter = Coord3d.min(coords);
        Coord3d maxAfter = Coord3d.max(coords);
        Assert.assertEquals(minAfter.z, 0f);
        Assert.assertEquals(maxAfter.z, 1f);
        
        
        // Verify unnormalized values are similar to input
        Normalizer.unnormalize(coords, m);

        minAfter = Coord3d.min(coords);
        maxAfter = Coord3d.max(coords);
        
        Assert.assertEquals(minBefore, minAfter);
        Assert.assertEquals(maxBefore, maxAfter);
    }

}
