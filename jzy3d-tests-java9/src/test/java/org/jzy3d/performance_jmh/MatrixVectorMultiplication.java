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

public class MatrixVectorMultiplication {

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
        .include(MatrixVectorMultiplication.class.getSimpleName())
        .mode(Mode.AverageTime)
        .forks(2)
        .warmupIterations(10)
        .measurementIterations(10)
        .timeUnit(TimeUnit.MICROSECONDS)
        .build();

    new Runner(opt).run();
  }

  @Benchmark
  public Object homemadeOldMatrixMultiplication(MatrixVectorProvider matrixProvider) {
    return gl_util.mulMatrix41(matrixProvider.getFirstMatrix(), matrixProvider.getSecondMatrix());
  }

  @Benchmark
  public Object homemadeNewMatrixMultiplication(MatrixVectorProvider matrixProvider) {
    return gl_util_new.mulMatrix41(matrixProvider.getFirstMatrix(), matrixProvider.getSecondMatrix());
  }

  @Benchmark
  public Object ejmlMatrixMultiplication(MatrixVectorProvider matrixProvider) {
    SimpleMatrix firstMatrix = new SimpleMatrix(4, 4, true, matrixProvider.getFirstMatrix());
    SimpleMatrix secondMatrix = new SimpleMatrix(4, 1, true, matrixProvider.getSecondMatrix());

    return firstMatrix.mult(secondMatrix);
  }

  @State(Scope.Benchmark)
  public static class MatrixVectorProvider {
    private float[] firstMatrix;
    private float[] secondMatrix;

    public MatrixVectorProvider() {
      firstMatrix =
          new float[] {
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat()
          };

      secondMatrix =
          new float[] {
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
