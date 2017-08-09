package com.thermocalc.chart3d.tinyAxes;

import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ScientificNotationTickRenderer;

import com.thermocalc.chart3d.tinyAxes.Normalizer.Axis;

public class ScientificNotationTickRendererNormalized extends ScientificNotationTickRenderer {
    Normalizer.Model model;
    
    
    Axis axis;

    public ScientificNotationTickRendererNormalized(Normalizer.Model model, Axis axis) {
        super();
        this.model = model;
        this.axis = axis;
    }

    public ScientificNotationTickRendererNormalized(Normalizer.Model model, Axis axis, int precision) {
        super(precision);
        this.model = model;
        this.axis = axis;
    }

    @Override
    public String format(double value) {
        float unnormalized = Normalizer.unnormalize((float)value, model, axis);
        return Utils.num2str('e', unnormalized, precision);
    }
}