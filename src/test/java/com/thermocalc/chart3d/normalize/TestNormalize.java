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
        
        Coord3d minSource = Coord3d.min(coords).clone();
        Coord3d maxSource = Coord3d.max(coords).clone();
        
        // ------------------------------------------------
        // Verify normalized dataset stand in [0-1]
        Model modelZ = Normalizer.normalizeZ(coords);

        Coord3d minNormalized = Coord3d.min(coords).clone();
        Coord3d maxNormalized = Coord3d.max(coords).clone();
        
        Assert.assertEquals(0f, minNormalized.z);
        Assert.assertEquals(1f, maxNormalized.z);
        Assert.assertFalse(minNormalized.equals(minSource));
        Assert.assertFalse(maxNormalized.equals(maxSource));
        
        // ------------------------------------------------
        // Verify unnormalized dataset are similar to input
        Normalizer.unnormalize(coords, modelZ);

        Coord3d minUnnormed = Coord3d.min(coords).clone();
        Coord3d maxUnnormed = Coord3d.max(coords).clone();
        
        Assert.assertEquals(minSource, minUnnormed);
        Assert.assertEquals(maxSource, maxUnnormed);
        Assert.assertFalse(minNormalized.equals(minUnnormed));
        Assert.assertFalse(maxNormalized.equals(maxUnnormed));
        
        // ------------------------------------------------
        // Verify we can normalize a single coord
        Coord3d minNormalizedSingle = minSource.clone();
        Coord3d maxNormalizedSingle = maxSource.clone();
        
        Normalizer.normalize(minNormalizedSingle, modelZ);
        Normalizer.normalize(maxNormalizedSingle, modelZ);
        
        Assert.assertEquals(minNormalized, minNormalizedSingle);
        Assert.assertEquals(maxNormalized, maxNormalizedSingle);

        // ------------------------------------------------
        // Verify we can unnormalize a single coord
        Coord3d minUnnormalizedSingle = minNormalizedSingle.clone();
        Coord3d maxUnnormalizedSingle = maxNormalizedSingle.clone();

        Normalizer.unnormalize(minUnnormalizedSingle, modelZ);
        Normalizer.unnormalize(maxUnnormalizedSingle, modelZ);
        
        Assert.assertEquals(minSource, minUnnormalizedSingle);
        Assert.assertEquals(maxSource, maxUnnormalizedSingle);
        
    }

}
