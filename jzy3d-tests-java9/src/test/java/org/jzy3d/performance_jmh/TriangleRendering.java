package org.jzy3d.performance_jmh;

import jgl.context.gl_context;
import jgl.context.gl_vertex;
import jgl.context.render.gl_render_triangle_barycentric;
import jgl.context.render.gl_render_triangle_scanline_new;
import jgl.context.render.gl_render_triangle_scanline_orig;
import jgl.context.render.pixel.gl_render_pixel;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TriangleRendering {
  private static final int SPAN = 20;
  private static final gl_vertex[] triangles = createTriangle();
  private static gl_vertex[] createTriangle() {
    gl_vertex v1 = new gl_vertex();
    v1.Vertex[0] = 300.0f;
    v1.Vertex[1] = 500.0f;
    v1.Vertex[2] = 300.0f;

    gl_vertex v2 = new gl_vertex();
    v2.Vertex[0] = 320.f;
    v2.Vertex[1] = 520.0f;
    v2.Vertex[2] = 310.0f;

    gl_vertex v3 = new gl_vertex();
    v3.Vertex[0] = 320.0f;
    v3.Vertex[1] = 480.0f;
    v3.Vertex[2] = 330.0f;

    gl_vertex[] triangles = new gl_vertex[3];
    triangles[0] = v1;
    triangles[1] = v2;
    triangles[2] = v3;
    return triangles;
  }

  private static float randFloat(float min, float max) {
    return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
  }

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
        .include(TriangleRendering.class.getSimpleName())
        .mode(Mode.AverageTime)
        .forks(2)
        .warmupIterations(3)
        .measurementIterations(3)
        .timeUnit(TimeUnit.NANOSECONDS)
        .build();

    new Runner(opt).run();
  }

  @Benchmark
  public Object scanlineOriginalRender(TrianglesProvider provider) {
    provider.render_triangle_scanline_orig.draw_triangle(
        provider.getTriangles()[0],
        provider.getTriangles()[1],
        provider.getTriangles()[2]
    );
    return provider.context.ColorBuffer;
  }

  @Benchmark
  public Object scanlineNewRender(TrianglesProvider provider) {
    provider.render_triangle_scanline_new.draw_triangle(
        provider.getTriangles()[0],
        provider.getTriangles()[1],
        provider.getTriangles()[2]
    );
    return provider.context.ColorBuffer;
  }

  @Benchmark
  public Object barycentricRender(TrianglesProvider provider) {
    provider.render_triangle_barycentric.draw_triangle(
        provider.getTriangles()[0],
        provider.getTriangles()[1],
        provider.getTriangles()[2]
    );
    return provider.context.ColorBuffer;
  }

  @Test
  public void testManual() {
    TrianglesProvider provider = new TrianglesProvider();
    int iterCount = 1000000;
    clearBuff(provider);
    long stOld = System.nanoTime();
    for (int i = 0; i < iterCount; ++i) {
      provider.render_triangle_scanline_orig.draw_triangle(
          provider.getTriangles()[0],
          provider.getTriangles()[1],
          provider.getTriangles()[2]
      );
    }
    long enOld = System.nanoTime();
    System.out.printf("Old sum %d Old dur %d ns %n", sum(provider), (enOld - stOld) / iterCount);

    clearBuff(provider);

    long stNew = System.nanoTime();
    for (int i = 0; i < iterCount; ++i) {
      provider.render_triangle_barycentric.draw_triangle(
          provider.getTriangles()[0],
          provider.getTriangles()[1],
          provider.getTriangles()[2]
      );
    }
    long enNew = System.nanoTime();
    System.out.printf("New sum %d New dur %d ns %n", sum(provider), (enNew - stNew) / iterCount);
  }

  private long sum(TrianglesProvider provider) {
    long sum = 0;
    for (int pos = 0; pos < provider.context.ColorBuffer.Buffer.length; ++pos) {
      sum += provider.context.ColorBuffer.Buffer[pos];
    }

    return sum;
  }

  private void clearBuff(TrianglesProvider provider) {
    Arrays.fill(provider.context.ColorBuffer.Buffer, 0);
  }

  @State(Scope.Benchmark)
  public static class TrianglesProvider {

    private gl_context context = new gl_context();
    private gl_render_triangle_scanline_orig render_triangle_scanline_orig = new gl_render_triangle_scanline_orig(context);
    private gl_render_triangle_scanline_new render_triangle_scanline_new = new gl_render_triangle_scanline_new(context);
    private gl_render_triangle_barycentric render_triangle_barycentric = new gl_render_triangle_barycentric(context);

    public TrianglesProvider() {
      context.gl_viewport(0, 0, 1000, 1000);
      context.gl_front_face(40);
      context.gl_color(0.4f, 0.3f, 0.1f, 0.5f);
      render_triangle_scanline_orig.set_pixel(new gl_render_pixel(context));
      render_triangle_scanline_new.set_pixel(new gl_render_pixel(context));
      render_triangle_barycentric.set_pixel(new gl_render_pixel(context));
      render_triangle_scanline_orig.color = 99;
      render_triangle_scanline_new.color = 99;
      render_triangle_barycentric.color = 99;
    }



    public gl_vertex[] getTriangles() {
      return triangles;
    }

    public gl_context getContext() {
      return context;
    }

    public gl_render_triangle_scanline_orig getRenderOld() {
      return render_triangle_scanline_orig;
    }
  }
}
