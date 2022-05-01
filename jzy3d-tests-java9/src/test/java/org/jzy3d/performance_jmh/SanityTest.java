package org.jzy3d.performance_jmh;

import jgl.context.gl_util;
import jgl.context.gl_util_new;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;

public class SanityTest {

  @Test
  public void testMatrixMultiplication() {
    for (int i = 0; i < 100; ++i) {
      MatrixProvider prov = new MatrixProvider();
      float[] oldR = gl_util.mulMatrix44(prov.getFirstMatrix(), prov.getSecondMatrix());
      float[] newR = gl_util_new.mulMatrix44(prov.getFirstMatrix(), prov.getSecondMatrix());
      assertThat(oldR, Matchers.equalTo(newR));
    }
  }

  @Test
  public void testVectorMultiplication() {
    for (int i = 0; i < 100; ++i) {
      MatrixVectorProvider prov = new MatrixVectorProvider();
      float[] oldR = gl_util.mulMatrix41(prov.getFirstMatrix(), prov.getVector());
      float[] newR = gl_util_new.mulMatrix41(prov.getFirstMatrix(), prov.getVector());
      assertThat(oldR, Matchers.equalTo(newR));
    }
  }

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

  public static class MatrixVectorProvider {
    private float[] firstMatrix;
    private float[] vector;

    public MatrixVectorProvider() {
      firstMatrix =
          new float[] {
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat(),
              randFloat(), randFloat(), randFloat(), randFloat()
          };

      vector =
          new float[] {
              randFloat(), randFloat(), randFloat(), randFloat(),
          };
    }

    private static float randFloat() {
      return ThreadLocalRandom.current().nextFloat();
    }

    public float[] getFirstMatrix() {
      return firstMatrix;
    }

    public float[] getVector() {
      return vector;
    }
  }
}
