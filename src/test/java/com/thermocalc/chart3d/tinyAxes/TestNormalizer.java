package com.thermocalc.chart3d.tinyAxes;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.jzy3d.io.FileDataset;
import org.jzy3d.maths.Coord3d;

import com.thermocalc.chart3d.tinyAxes.Normalizer;
import com.thermocalc.chart3d.tinyAxes.Normalizer.Model;

public class TestNormalizer {
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
    
    @Test
    public void testNormUnnorm() throws Exception {
        List<Coord3d> coords = FileDataset.loadList("data/thermocalc-sample.csv");
        
        Coord3d minSource = Coord3d.min(coords).clone();
        Coord3d maxSource = Coord3d.max(coords).clone();
        
        // ------------------------------------------------
        // Verify normalized dataset stand in [0-1]
        Model model = Normalizer.normalize(coords);

        Coord3d minNormalized = Coord3d.min(coords).clone();
        Coord3d maxNormalized = Coord3d.max(coords).clone();
        
        Assert.assertEquals(0f, minNormalized.x);
        Assert.assertEquals(0f, minNormalized.y);
        Assert.assertEquals(0f, minNormalized.z);
        Assert.assertEquals(1f, maxNormalized.x);
        Assert.assertEquals(1f, maxNormalized.y);
        Assert.assertEquals(1f, maxNormalized.z);
        
        Assert.assertFalse(minNormalized.equals(minSource));
        Assert.assertFalse(maxNormalized.equals(maxSource));
        
        // ------------------------------------------------
        // Verify unnormalized dataset are similar to input
        Normalizer.unnormalize(coords, model);

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
        
        Normalizer.normalize(minNormalizedSingle, model);
        Normalizer.normalize(maxNormalizedSingle, model);
        
        Assert.assertEquals(minNormalized, minNormalizedSingle);
        Assert.assertEquals(maxNormalized, maxNormalizedSingle);

        // ------------------------------------------------
        // Verify we can unnormalize a single coord
        Coord3d minUnnormalizedSingle = minNormalizedSingle.clone();
        Coord3d maxUnnormalizedSingle = maxNormalizedSingle.clone();

        Normalizer.unnormalize(minUnnormalizedSingle, model);
        Normalizer.unnormalize(maxUnnormalizedSingle, model);
        
        Assert.assertEquals(minSource, minUnnormalizedSingle);
        Assert.assertEquals(maxSource, maxUnnormalizedSingle);
        
        // ------------------------------------------------
        // Verify we can unnormalize a single value
        
        Assert.assertEquals(minSource.x, Normalizer.unnormalizeX(0, model));
        Assert.assertEquals(minSource.y, Normalizer.unnormalizeY(0, model));
        Assert.assertEquals(minSource.z, Normalizer.unnormalizeZ(0, model));

        Assert.assertEquals(maxSource.x, Normalizer.unnormalizeX(1, model));
        Assert.assertEquals(maxSource.y, Normalizer.unnormalizeY(1, model));
        Assert.assertEquals(maxSource.z, Normalizer.unnormalizeZ(1, model));

    }

}
