package org.jzy3d.performance_jmh;

import jgl.context.gl_util;
import jgl.context.gl_util_new;
import org.ejml.simple.SimpleMatrix;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MatrixInversion {

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
        .include(MatrixInversion.class.getSimpleName())
        .exclude("(.+MatrixMultiplication)|(.+MatrixVectorMultiplication)")
        .mode(Mode.AverageTime)
        .forks(2)
        .warmupIterations(2)
        .measurementIterations(10)
        .timeUnit(TimeUnit.MICROSECONDS)
        .build();

    new Runner(opt).run();
  }

  @Benchmark
  public Object homemadeOldMatrixInversion(MatrixProvider matrixProvider) {
    return gl_util.inverseMatrix44(matrixProvider.getFirstMatrix());
  }

  @Benchmark
  public Object homemadeNewMatrixInversion(MatrixProvider matrixProvider) {
    return gl_util_new.inverseMatrix44(matrixProvider.getFirstMatrix());
  }

  @State(Scope.Benchmark)
  public static class MatrixProvider {
    private float[] firstMatrix;
    private float[] secondMatrix;

    public MatrixProvider() {
      firstMatrix =
          new float[] {
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat()
          };

      secondMatrix =
          new float[] {
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat()
          };
    }

    private static float randFloat() {
      return ThreadLocalRandom.current().nextFloat();
    }

    public float[] getFirstMatrix() {
      return firstMatrix;
    }

    public float[] getSecondMatrix() {
      return secondMatrix;
    }
  }
}
