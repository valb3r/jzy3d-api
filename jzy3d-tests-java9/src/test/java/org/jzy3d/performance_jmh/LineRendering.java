package org.jzy3d.performance_jmh;

// Kudos to http://www.edepot.com/linebenchmark.html

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

/**
 LineRendering.bresenhamLine  avgt   20  59.452 ± 2.522  ns/op
 LineRendering.ddaLine        avgt   20  91.317 ± 0.684  ns/op
 LineRendering.eflaLine       avgt   20  54.969 ± 0.408  ns/op
 LineRendering.wuLine         avgt   20  60.197 ± 0.192  ns/op
 */
public class LineRendering {

  private static int width = 1024;
  private static int[] canvas = new int[width * 768];

  private static int[] lineMedium = new int[] {100, 100, 200, 200};

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
        .include(LineRendering.class.getSimpleName())
        .exclude("Triangle.*")
        .mode(Mode.AverageTime)
        .forks(2)
        .warmupIterations(10)
        .measurementIterations(10)
        .timeUnit(TimeUnit.NANOSECONDS)
        .build();

    new Runner(opt).run();
  }

  @Benchmark
  public Object wuLine() {
    wu(lineMedium[0], lineMedium[1], lineMedium[2], lineMedium[3], 99);
    return canvas;
  }

  @Benchmark
  public Object ddaLine() {
    dda(lineMedium[0], lineMedium[1], lineMedium[2], lineMedium[3], 99);
    return canvas;
  }

  @Benchmark
  public Object bresenhamLine() {
    bresenham(lineMedium[0], lineMedium[1], lineMedium[2], lineMedium[3], 99);
    return canvas;
  }

  @Benchmark
  public Object eflaLine() {
    efla_addition_fp_precalc(lineMedium[0], lineMedium[1], lineMedium[2], lineMedium[3], 99);
    return canvas;
  }

  void wu(int x0, int y0, int x1, int y1,int clr) {
    //int pix = color.getRGB();
    int dy = y1 - y0;
    int dx = x1 - x0;
    int stepx, stepy;

    if (dy < 0) { dy = -dy;  stepy = -1; } else { stepy = 1; }
    if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }

    pixel( x0, y0,clr);
    pixel( x1, y1,clr);
    if (dx > dy) {
      int length = (dx - 1) >> 2;
      int extras = (dx - 1) & 3;
      int incr2 = (dy << 2) - (dx << 1);
      if (incr2 < 0) {
        int c = dy << 1;
        int incr1 = c << 1;
        int d =  incr1 - dx;
        for (int i = 0; i < length; i++) {
          x0 += stepx;
          x1 -= stepx;
          if (d < 0) {                                               // Pattern:
            pixel( x0, y0,clr);                          //
            pixel( x0 += stepx, y0,clr);                 //  x o o
            pixel( x1, y1,clr);                          //
            pixel( x1 -= stepx, y1,clr);
            d += incr1;
          } else {
            if (d < c) {                                           // Pattern:
              pixel( x0, y0,clr);                      //      o
              pixel( x0 += stepx, y0 += stepy,clr);    //  x o
              pixel( x1, y1,clr);                      //
              pixel( x1 -= stepx, y1 -= stepy,clr);
            } else {
              pixel( x0, y0 += stepy,clr);             // Pattern:
              pixel( x0 += stepx, y0,clr);             //    o o
              pixel( x1, y1 -= stepy,clr);             //  x
              pixel( x1 -= stepx, y1,clr);             //
            }
            d += incr2;
          }
        }
        if (extras > 0) {
          if (d < 0) {
            pixel( x0 += stepx, y0,clr);
            if (extras > 1) pixel( x0 += stepx, y0,clr);
            if (extras > 2) pixel( x1 -= stepx, y1,clr);
          } else
          if (d < c) {
            pixel( x0 += stepx, y0,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1 -= stepx, y1,clr);
          } else {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0,clr);
            if (extras > 2) pixel( x1 -= stepx, y1 -= stepy,clr);
          }
        }
      } else {
        int c = (dy - dx) << 1;
        int incr1 = c << 1;
        int d =  incr1 + dx;
        for (int i = 0; i < length; i++) {
          x0 += stepx;
          x1 -= stepx;
          if (d > 0) {
            pixel( x0, y0 += stepy,clr);                      // Pattern:
            pixel( x0 += stepx, y0 += stepy,clr);             //      o
            pixel( x1, y1 -= stepy,clr);                      //    o
            pixel( x1 -= stepx, y1 -= stepy,clr);		        //  x
            d += incr1;
          } else {
            if (d < c) {
              pixel( x0, y0,clr);                           // Pattern:
              pixel( x0 += stepx, y0 += stepy,clr);         //      o
              pixel( x1, y1,clr);                           //  x o
              pixel( x1 -= stepx, y1 -= stepy,clr);         //
            } else {
              pixel( x0, y0 += stepy,clr);                  // Pattern:
              pixel( x0 += stepx, y0,clr);                  //    o o
              pixel( x1, y1 -= stepy,clr);                  //  x
              pixel( x1 -= stepx, y1,clr);                  //
            }
            d += incr2;
          }
        }
        if (extras > 0) {
          if (d > 0) {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1 -= stepx, y1 -= stepy,clr);
          } else
          if (d < c) {
            pixel( x0 += stepx, y0,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1 -= stepx, y1,clr);
          } else {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0,clr);
            if (extras > 2) {
              if (d > c)
                pixel( x1 -= stepx, y1 -= stepy,clr);
              else
                pixel( x1 -= stepx, y1,clr);
            }
          }
        }
      }
    } else {
      int length = (dy - 1) >> 2;
      int extras = (dy - 1) & 3;
      int incr2 = (dx << 2) - (dy << 1);
      if (incr2 < 0) {
        int c = dx << 1;
        int incr1 = c << 1;
        int d =  incr1 - dy;
        for (int i = 0; i < length; i++) {
          y0 += stepy;
          y1 -= stepy;
          if (d < 0) {
            pixel( x0, y0,clr);
            pixel( x0, y0 += stepy,clr);
            pixel( x1, y1,clr);
            pixel( x1, y1 -= stepy,clr);
            d += incr1;
          } else {
            if (d < c) {
              pixel( x0, y0,clr);
              pixel( x0 += stepx, y0 += stepy,clr);
              pixel( x1, y1,clr);
              pixel( x1 -= stepx, y1 -= stepy,clr);
            } else {
              pixel( x0 += stepx, y0,clr);
              pixel( x0, y0 += stepy,clr);
              pixel( x1 -= stepx, y1,clr);
              pixel( x1, y1 -= stepy,clr);
            }
            d += incr2;
          }
        }
        if (extras > 0) {
          if (d < 0) {
            pixel( x0, y0 += stepy,clr);
            if (extras > 1) pixel( x0, y0 += stepy,clr);
            if (extras > 2) pixel( x1, y1 -= stepy,clr);
          } else
          if (d < c) {
            pixel( stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1, y1 -= stepy,clr);
          } else {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0, y0 += stepy,clr);
            if (extras > 2) pixel( x1 -= stepx, y1 -= stepy,clr);
          }
        }
      } else {
        int c = (dx - dy) << 1;
        int incr1 = c << 1;
        int d =  incr1 + dy;
        for (int i = 0; i < length; i++) {
          y0 += stepy;
          y1 -= stepy;
          if (d > 0) {
            pixel( x0 += stepx, y0,clr);
            pixel( x0 += stepx, y0 += stepy,clr);
            pixel( x1 -= stepy, y1,clr);
            pixel( x1 -= stepx, y1 -= stepy,clr);
            d += incr1;
          } else {
            if (d < c) {
              pixel( x0, y0,clr);
              pixel( x0 += stepx, y0 += stepy,clr);
              pixel( x1, y1,clr);
              pixel( x1 -= stepx, y1 -= stepy,clr);
            } else {
              pixel( x0 += stepx, y0,clr);
              pixel( x0, y0 += stepy,clr);
              pixel( x1 -= stepx, y1,clr);
              pixel( x1, y1 -= stepy,clr);
            }
            d += incr2;
          }
        }
        if (extras > 0) {
          if (d > 0) {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1 -= stepx, y1 -= stepy,clr);
          } else
          if (d < c) {
            pixel( x0, y0 += stepy,clr);
            if (extras > 1) pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 2) pixel( x1, y1 -= stepy,clr);
          } else {
            pixel( x0 += stepx, y0 += stepy,clr);
            if (extras > 1) pixel( x0, y0 += stepy,clr);
            if (extras > 2) {
              if (d > c)
                pixel( x1 -= stepx, y1 -= stepy,clr);
              else
                pixel( x1, y1 -= stepy,clr);
            }
          }
        }
      }
    }
  }


  void dda(int x1,int y1,int x2, int y2, int clr) {
    int length,i;
    double x,y;
    double xincrement;
    double yincrement;

    length = abs(x2 - x1);
    if (abs(y2 - y1) > length) length = abs(y2 - y1);
    xincrement = (double)(x2 - x1)/(double)length;
    yincrement = (double)(y2 - y1)/(double)length;
    x = x1 + 0.5;
    y = y1 + 0.5;
    for (i = 1; i<= length;++i) {
      pixel((int)x, (int)y, clr);
      x = x + xincrement;
      y = y + yincrement;
    }

  }


  void bresenham(int x1, int y1, int x2, int y2, int clr) {
    int		x, y;
    int		dx, dy;
    int		incx, incy;
    int		balance;


    if (x2 >= x1)
    {
      dx = x2 - x1;
      incx = 1;
    }
    else
    {
      dx = x1 - x2;
      incx = -1;
    }

    if (y2 >= y1)
    {
      dy = y2 - y1;
      incy = 1;
    }
    else
    {
      dy = y1 - y2;
      incy = -1;
    }

    x = x1;
    y = y1;

    if (dx >= dy)
    {
      dy <<= 1;
      balance = dy - dx;
      dx <<= 1;

      while (x != x2)
      {
        pixel(x, y,clr);
        if (balance >= 0)
        {
          y += incy;
          balance -= dx;
        }
        balance += dy;
        x += incx;
      } pixel(x, y,clr);
    }
    else
    {
      dx <<= 1;
      balance = dx - dy;
      dy <<= 1;

      while (y != y2)
      {
        pixel(x, y,clr);
        if (balance >= 0)
        {
          x += incx;
          balance -= dy;
        }
        balance += dx;
        y += incy;
      } pixel(x, y,clr);
    }
  }

  // THE EXTREMELY FAST LINE ALGORITHM Variation E (Addition fixed point precalc)
  void efla_addition_fp_precalc(int x, int y, int x2, int y2, int clr) {
    boolean yLonger=false;
    int shortLen=y2-y;
    int longLen=x2-x;
    if (abs(shortLen)>abs(longLen)) {
      int swap=shortLen;
      shortLen=longLen;
      longLen=swap;
      yLonger=true;
    }
    int decInc;
    if (longLen==0) decInc=0;
    else decInc = (shortLen << 16) / longLen;

    if (yLonger) {
      if (longLen>0) {
        longLen+=y;
        for (int j=0x8000+(x<<16);y<=longLen;++y) {
          pixel(j >> 16,y,clr);
          j+=decInc;
        }
        return;
      }
      longLen+=y;
      for (int j=0x8000+(x<<16);y>=longLen;--y) {
        pixel(j >> 16,y,clr);
        j-=decInc;
      }
      return;
    }

    if (longLen>0) {
      longLen+=x;
      for (int j=0x8000+(y<<16);x<=longLen;++x) {
        pixel(x,j >> 16,clr);
        j+=decInc;
      }
      return;
    }
    longLen+=x;
    for (int j=0x8000+(y<<16);x>=longLen;--x) {
      pixel(x,j >> 16,clr);
      j-=decInc;
    }
  }

  private void pixel(int x, int y, int color) {
    canvas[y * width + x] = color;
  }
}