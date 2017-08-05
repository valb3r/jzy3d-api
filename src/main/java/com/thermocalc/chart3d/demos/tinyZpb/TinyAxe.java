package com.thermocalc.chart3d.demos.tinyZpb;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.align.Valign;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

public class TinyAxe extends AxeBox{

    public void drawAxisTicks(GL gl, GLU glu, Camera cam, int direction, Color color, Halign hal, Valign val, float tickLength, BoundingBox3d ticksTxtBounds, double xpos, double ypos, double zpos, float xdir, float ydir, float zdir, double[] ticks) {
        double xlab;
        double ylab;
        double zlab;
        String tickLabel = "";

        for (int t = 0; t < ticks.length; t++) {
            // Shift the tick vector along the selected axis
            // and set the tick length
            if (spaceTransformer == null) {
                if (isX(direction)) {
                    xpos = ticks[t];
                    xlab = xpos;
                    ylab = (yrange / tickLength) * ydir + ypos;
                    zlab = (zrange / tickLength) * zdir + zpos;
                    tickLabel = layout.getXTickRenderer().format(xpos);
                } else if (isY(direction)) {
                    ypos = ticks[t];
                    xlab = (xrange / tickLength) * xdir + xpos;
                    ylab = ypos;
                    zlab = (zrange / tickLength) * zdir + zpos;
                    tickLabel = layout.getYTickRenderer().format(ypos);
                } else { // (axis==AXE_Z)
                    zpos = ticks[t];
                    xlab = (xrange / tickLength) * xdir + xpos;
                    ylab = (yrange / tickLength) * ydir + ypos;
                    zlab = zpos;
                    tickLabel = layout.getZTickRenderer().format(zpos);
                }
            } else {
                // use space transform shift if we have a space transformer
                if (isX(direction)) {
                    xpos = spaceTransformer.getX().compute((float) ticks[t]);
                    xlab = xpos;
                    ylab = Math.signum(tickLength * ydir) * (yrange / spaceTransformer.getY().compute(Math.abs(tickLength))) * spaceTransformer.getY().compute(Math.abs(ydir)) + ypos;
                    zlab = Math.signum(tickLength * ydir) * (zrange / spaceTransformer.getZ().compute(Math.abs(tickLength))) * spaceTransformer.getZ().compute(Math.abs(zdir)) + zpos;
                    tickLabel = layout.getXTickRenderer().format(xpos);
                } else if (isY(direction)) {
                    ypos = spaceTransformer.getY().compute((float) ticks[t]);
                    xlab = Math.signum(tickLength * xdir) * (xrange / spaceTransformer.getX().compute(Math.abs(tickLength))) * spaceTransformer.getX().compute(Math.abs(xdir)) + xpos;
                    ylab = ypos;
                    zlab = Math.signum(tickLength * zdir) * (zrange / spaceTransformer.getZ().compute(Math.abs(tickLength))) * spaceTransformer.getZ().compute(Math.abs(zdir)) + zpos;
                    tickLabel = layout.getYTickRenderer().format(ypos);
                } else { // (axis==AXE_Z)
                    zpos = spaceTransformer.getZ().compute((float) ticks[t]);
                    xlab = Math.signum(tickLength * xdir) * (xrange / spaceTransformer.getX().compute(Math.abs(tickLength))) * spaceTransformer.getX().compute(Math.abs(xdir)) + xpos;
                    ylab = Math.signum(tickLength * ydir) * (yrange / spaceTransformer.getY().compute(Math.abs(tickLength))) * spaceTransformer.getY().compute(Math.abs(ydir)) + ypos;
                    zlab = zpos;
                    tickLabel = layout.getZTickRenderer().format(zpos);
                }
            }
            Coord3d tickPosition = new Coord3d(xlab, ylab, zlab);
            //System.out.println(tickPosition); // properly processed
            
            if (layout.isTickLineDisplayed()) {
                if (gl.isGL2()) {
                    drawTickLine(gl, color, xpos, ypos, zpos, xlab, ylab, zlab);
                } else {
                    // FIXME REWRITE ANDROID
                }
            }

            // Select the alignement of the tick label
            Halign hAlign = layoutHorizontal(direction, cam, hal, tickPosition);
            Valign vAlign = layoutVertical(direction, val, zdir);

            // Draw the text label of the current tick
            drawAxisTickNumericLabel(gl, glu, direction, cam, color, hAlign, vAlign, ticksTxtBounds, tickLabel, tickPosition);
        }
    }

    public TinyAxe(BoundingBox3d bbox, IAxeLayout layout) {
        super(bbox, layout);
    }

    public TinyAxe(BoundingBox3d bbox) {
        super(bbox);
    }
}
