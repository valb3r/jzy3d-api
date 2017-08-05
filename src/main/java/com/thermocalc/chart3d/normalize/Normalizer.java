package com.thermocalc.chart3d.normalize;

import java.util.List;

import org.jzy3d.maths.Coord3d;

public class Normalizer {
    static boolean debug = true;

    public static class Model {
        Coord3d shift;
        Coord3d factor;
    }

    /** Normalize Z components between 0 and 1 */
    public static Model normalize(List<Coord3d> coords) {
        Coord3d min = Coord3d.min(coords);
        Coord3d max = Coord3d.max(coords);
        float zRange = max.z - min.z;

        if (debug) {
            System.out.println(Coord3d.min(coords));
            System.out.println(Coord3d.max(coords));
            System.out.println(zRange);
        }

        // ---------------------------------
        // Z axis normalization model
        min.x = 0;
        min.y = 0;
        max.x = 0;
        max.y = 0;

        Model model = new Model();
        model.shift = min.mul(-1f, -1f, -1f);
        model.factor = new Coord3d(1, 1, zRange);

        // ---------------------------------
        // apply model
        normalize(coords, model);

        return model;
    }

    public static void normalize(List<Coord3d> coords, Model model) {
        Coord3d.add(coords, model.shift); // shift to 0

        if (debug) {
            System.out.println("after shift to 0, min Z is:" + Coord3d.min(coords).z);
            System.out.println("after shift to 0, max Z is:" + Coord3d.max(coords).z);
        }

        Coord3d.div(coords, model.factor); // normalize between 0 and 1

        if (debug) {
            System.out.println("after applying facotr, min is " + Coord3d.min(coords));
            System.out.println("after applying facotr, max is " + Coord3d.max(coords));
        }
    }
    
    public static void unnormalize(List<Coord3d> coords, Model model) {
        Coord3d.mul(coords, model.factor); // normalize between 0 and 1

        if (debug) {
            System.out.println("after applying facotr, min is " + Coord3d.min(coords));
            System.out.println("after applying facotr, max is " + Coord3d.max(coords));
        }
        
        Coord3d.sub(coords, model.shift); // shift back from 0

        if (debug) {
            System.out.println("after unshift from 0, min Z is:" + Coord3d.min(coords).z);
            System.out.println("after unshift from 0, max Z is:" + Coord3d.max(coords).z);
        }
    }
}
