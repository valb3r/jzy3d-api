package com.thermocalc.chart3d.tinyAxes;

import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ITickRenderer;

import com.thermocalc.chart3d.tinyAxes.Normalizer.Axis;

/** Force number to be represented with a given number of decimals*/
public class DefaultDecimalTickRendererNormalized implements ITickRenderer{
    Normalizer.Model model;
    Axis axis;
    
	public DefaultDecimalTickRendererNormalized(Normalizer.Model model, Axis axis){
		this(model, axis, 6);
		
	}
	
	public DefaultDecimalTickRendererNormalized(Normalizer.Model model, Axis axis, int precision){
	    this.model = model;
		this.axis = axis;
	    this.precision = precision;
	}
	
	@Override
	public String format(double value) {
	    float unnormalized = Normalizer.unnormalize((float)value, model, axis);
		return Utils.num2str('g', unnormalized, precision);
	}
	
	protected int precision;
}
