package com.thermocalc.chart3d.tinyaxes;

import org.apache.log4j.Logger;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.view.Camera;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

/**
 * This camera is intended to adress the inability of JOGL to gluUnProject coordinates containing 
 * values in a very small Z range. 
 * 
 * THIS IMPLEMENTATION DOES NOT FIX ANYTHING. THIS IS WHERE A CLEAN FIX SHOULD APPEAR
 * 
 * @see https://stackoverflow.com/questions/1732117/jogl-glu-gluunproject-always-returning-0-0#1754176
 * @see https://www.opengl.org/discussion_boards/showthread.php/163590-gluUnProject-returning-GL_FALSE
 * @see https://jogamp.org/bugzilla/show_bug.cgi?id=483
 * 
 * @author Martin
 *
 */
public class TinyCamera extends Camera{
    static Logger LOGGER = Logger.getLogger(TinyCamera.class);

    public TinyCamera(Coord3d target) {
        super(target);
        // TODO Auto-generated constructor stub
    }
    
    public Coord3d screenToModel(GL gl, GLU glu, Coord3d screen) {
        int viewport[] = getViewPortAsInt(gl);
        float modelView[] = getModelViewAsFloat(gl);
        float projection[] = getProjectionAsFloat(gl);
        float worldcoord[] = new float[3];// wx, wy, wz;// returned xyz coords
        //float screeny = viewport[3] - (int)screen.y - 1;

        //viewport[3] = 600;
        //viewport[4] = 600;

        /*
        LOGGER.info("viewport : " + Arrays.toString(viewport));
        LOGGER.info("modelView : " + Arrays.toString(modelView));
        LOGGER.info("projection : " + Arrays.toString(projection));
        */
        
        boolean s = glu.gluUnProject(screen.x, screen.y, screen.z, modelView, 0, projection, 0, viewport, 0, worldcoord, 0);
        if (!s)
            failedProjection("Could not retrieve screen coordinates in model.");

        
        // LOGGER.info("worldcoord : " + Arrays.toString(worldcoord));

        
        return new Coord3d(worldcoord[0], worldcoord[1], worldcoord[2]);
    }
    
    /*GLint GLAPIENTRY
    gluUnProject(GLdouble winx, GLdouble winy, GLdouble winz,
            const GLdouble modelMatrix[16], 
            const GLdouble projMatrix[16],
                    const GLint viewport[4],
                GLdouble *objx, GLdouble *objy, GLdouble *objz)
    {
        double finalMatrix[16];
        double in[4];
        double out[4];
     
        __gluMultMatricesd(modelMatrix, projMatrix, finalMatrix);
        if (!__gluInvertMatrixd(finalMatrix, finalMatrix)) return(GL_FALSE);
     
        in[0]=winx;
        in[1]=winy;
        in[2]=winz;
        in[3]=1.0;
     
        // Map x and y from window coordinates 
        in[0] = (in[0] - viewport[0]) / viewport[2];
        in[1] = (in[1] - viewport[1]) / viewport[3];
     
        // Map to range -1 to 1 
        in[0] = in[0] * 2 - 1;
        in[1] = in[1] * 2 - 1;
        in[2] = in[2] * 2 - 1;
     
        __gluMultMatrixVecd(finalMatrix, in, out);
        if (out[3] == 0.0) return(GL_FALSE);
        out[0] /= out[3];
        out[1] /= out[3];
        out[2] /= out[3];
        *objx = out[0];
        *objy = out[1];
        *objz = out[2];
        return(GL_TRUE);
    }*/
    
    protected float[] getViewPortAsFloat(GL gl) {
        float viewport[] = new float[4];
        gl.glGetFloatv(GL.GL_VIEWPORT, viewport, 0);
        return viewport;
    }

    protected int[] getViewPortAsInt(GL gl) {
        int viewport[] = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
        return viewport;
    }
}
