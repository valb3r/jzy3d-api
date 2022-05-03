package jgl.context.render;

import jgl.context.gl_context;
import jgl.context.gl_vertex;
import jgl.context.render.pixel.gl_render_pixel;

public class gl_render_triangle_barycentric {

  protected gl_context CC;
  protected gl_render_pixel pixel;
  public int color; // for flat shading

  private int TriXY[][] = new int[3][2];

  int orient2d(int ax, int ay, int bx, int by, int cx, int cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }
  
  public void draw_triangle(gl_vertex v1, gl_vertex v2, gl_vertex v3) {
    int v0x = TriXY[0][0] = (int) (v1.Vertex[0] + (float) 0.5);
    int v1x = TriXY[1][0] = (int) (v2.Vertex[0] + (float) 0.5);
    int v2x = TriXY[2][0] = (int) (v3.Vertex[0] + (float) 0.5);
    int v0y = TriXY[0][1] = (int) (v1.Vertex[1] + (float) 0.5);
    int v1y = TriXY[1][1] = (int) (v2.Vertex[1] + (float) 0.5);
    int v2y = TriXY[2][1] = (int) (v3.Vertex[1] + (float) 0.5);
    
// Compute triangle bounding box
    int minX = Math.min(TriXY[0][0], Math.min(TriXY[1][0], TriXY[2][0]));
    int minY = Math.min(TriXY[0][1], Math.min(TriXY[1][1], TriXY[2][1]));
    int maxX = Math.max(TriXY[0][0], Math.max(TriXY[1][0], TriXY[2][0]));
    int maxY = Math.max(TriXY[0][1], Math.max(TriXY[1][1], TriXY[2][1]));

    // Triangle setup
    int A01 = v0y - v1y, B01 = v1x - v0x;
    int A12 = v1y - v2y, B12 = v2x - v1x;
    int A20 = v2y - v0y, B20 = v0x - v2x;

    // Barycentric coordinates at minX/minY corner
    int px = minX;
    int py = minY;

    int w0_row = orient2d(v1x, v1y, v2x, v2y, px, py);
    int w1_row = orient2d(v2x, v2y, v0x, v0y, px, py);
    int w2_row = orient2d(v0x, v0y, v1x, v1y, px, py);

    int index = CC.Viewport.Width * minY;
    // Rasterize
    for (py = minY; py <= maxY; py++) {
      // Barycentric coordinates at start of row
      int w0 = w0_row;
      int w1 = w1_row;
      int w2 = w2_row;

      drawHorizontal(minX, maxX, A01, A12, A20, index, w0, w1, w2);

      // One row step
      w0_row += B12;
      w1_row += B20;
      w2_row += B01;

      index += CC.Viewport.Width;
    }
  }

  private void drawHorizontal(int minX, int maxX, int A01, int A12, int A20, int index, int w0, int w1, int w2) {
    int px;
    for (px = minX; px <= maxX; px++) {
      // If p is on or inside all edges, render pixel.
      putPixelIfNeeded(px, index, w0, w1, w2);

      // One step to the right
      w0 += A12;
      w1 += A20;
      w2 += A01;
    }
  }

  private void putPixelIfNeeded(int px, int index, int w0, int w1, int w2) {
    if (w0 <= 0 && w1 <= 0 && w2 <= 0) {
      //renderPixel(p, w0, w1, w2);
      CC.ColorBuffer.Buffer[index + px] = color;
      //pixel.put_pixel_by_index(x, color);
    }
  }

  public void set_pixel(gl_render_pixel p) {
    pixel = p;
  }

  public gl_render_triangle_barycentric(gl_context cc) {
    CC = cc;
  }
}
