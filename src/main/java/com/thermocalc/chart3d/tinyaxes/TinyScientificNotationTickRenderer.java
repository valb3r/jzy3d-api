package com.thermocalc.chart3d.tinyaxes;

import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ScientificNotationTickRenderer;

public class TinyScientificNotationTickRenderer extends ScientificNotationTickRenderer {
    protected double transform;
    
    public TinyScientificNotationTickRenderer(int precision, double transform) {
        super(precision);
        this.transform = transform;
    }

    @Override
    public String format(double value) {
        return Utils.num2str('e', value * transform, precision);
    }
}
