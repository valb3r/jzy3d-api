package com.thermocalc.chart3d.tinyAxes;

import java.util.List;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Pair;

/**
 * Allows normalizing and unnormalizing datasets.
 * 
 * @author Martin Pernollet
 */
public class Normalizer {
    static boolean debug = false;

    public static class Model {
        Coord3d shift;
        Coord3d factor;
    }

    
    /** Normalize each dimension between 0 and 1*/
    public static Model normalize(List<Coord3d> coords) {
        Pair<Coord3d,Coord3d> minMax = Coord3d.minmax(coords);
        Coord3d min = minMax.a;
        Coord3d max = minMax.b;

        // ---------------------------------
        // Normalization model
        Model model = new Model();
        model.shift = min.mul(-1f, -1f, -1f);
        model.factor = max.sub(min);
        
        // ---------------------------------
        // apply model
        normalize(coords, model);

        return model;
    }
    
    /** Normalize coordinates using an existing scale factor and offset */
    public static void normalize(List<Coord3d> coords, Model model) {
        Coord3d.add(coords, model.shift); // shift to 0
        Coord3d.div(coords, model.factor); // normalize between 0 and 1
    }
    
    public static void unnormalize(List<Coord3d> coords, Model model) {
        Coord3d.mul(coords, model.factor); // normalize between 0 and 1
        Coord3d.sub(coords, model.shift); // shift back from 0
    }

    public static void normalize(Coord3d coords, Model model) {
        coords.addSelf(model.shift); // shift to 0
        coords.divSelf(model.factor); // normalize between 0 and 1
    }

    public static void unnormalize(Coord3d coords, Model model) {
        coords.mulSelf(model.factor); // normalize between 0 and 1
        coords.subSelf(model.shift); // shift back from 0
    }
    
    public static float unnormalizeX(float x, Model model) {
        return x * model.factor.x - model.shift.x;
    }

    public static float unnormalizeY(float y, Model model) {
        return y * model.factor.y - model.shift.y;
    }

    public static float unnormalizeZ(float z, Model model) {
        return z * model.factor.z - model.shift.z;
    }
    
    public static float unnormalize(float xyOrZ, Model model, Axis axis){
        if(Axis.X.equals(axis)){
            return Normalizer.unnormalizeX(xyOrZ, model);
        }
        else if(Axis.Y.equals(axis)){
            return Normalizer.unnormalizeY(xyOrZ, model);            
        }
        else /*if(Axis.Z.equals(axis))*/{
            return Normalizer.unnormalizeZ(xyOrZ, model);            
        }
    }
    
    public enum Axis{
        X,Y,Z;
    }
    
    /* #################################################### */

    /** Normalize Z components between 0 and 1 */
    public static Model normalizeZ(List<Coord3d> coords) {
        Coord3d min = Coord3d.min(coords);
        Coord3d max = Coord3d.max(coords);

        // ---------------------------------
        // Z axis normalization model
        min.x = 0;
        min.y = 0;
        max.x = 0;
        max.y = 0;

        Model model = new Model();
        model.shift = min.mul(-1f, -1f, -1f);
        model.factor = new Coord3d(1, 1, max.sub(min).z);

        // ---------------------------------
        // apply model
        normalize(coords, model);

        return model;
    }
}
