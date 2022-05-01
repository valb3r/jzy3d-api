/*
 * @(#)gl_util.java 0.3 06/11/20
 *
 * jGL 3-D graphics library for Java Copyright (c) 1999-2006 Robin Bing-Yu Chen (robin@ntu.edu.tw)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or any later version. the GNU Lesser General Public License should be
 * included with this distribution in the file LICENSE.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package jgl.context;

/**
 * gl_util is the utility class of jGL 2.4.
 *
 * @version 0.3, 20 Nov 2006
 * @author Robin Bing-Yu Chen
 */

public final class gl_util_new {

  private static void showMatrix(float m[], int size) {
    for (int i = 0; i < size; i++) {
      System.out.print(m[i] + " ");
    }
    System.out.println();
  }

  public static void showMatrix44(float m[]) {
    showMatrix(m, 16);
  }

  public static void showMatrix41(float m[]) {
    showMatrix(m, 4);
  }

  public static float[] mulMatrix44(float a[], float b[]) {
    // assume a = 4x4, b = 4x4
    float[] temp = new float[16];
    temp[0] += a[0] * b[0];
    temp[0] += a[4] * b[1];
    temp[0] += a[8] * b[2];
    temp[0] += a[12] * b[3];
    temp[1] += a[1] * b[0];
    temp[1] += a[5] * b[1];
    temp[1] += a[9] * b[2];
    temp[1] += a[13] * b[3];
    temp[2] += a[2] * b[0];
    temp[2] += a[6] * b[1];
    temp[2] += a[10] * b[2];
    temp[2] += a[14] * b[3];
    temp[3] += a[3] * b[0];
    temp[3] += a[7] * b[1];
    temp[3] += a[11] * b[2];
    temp[3] += a[15] * b[3];
    temp[4] += a[0] * b[4];
    temp[4] += a[4] * b[5];
    temp[4] += a[8] * b[6];
    temp[4] += a[12] * b[7];
    temp[5] += a[1] * b[4];
    temp[5] += a[5] * b[5];
    temp[5] += a[9] * b[6];
    temp[5] += a[13] * b[7];
    temp[6] += a[2] * b[4];
    temp[6] += a[6] * b[5];
    temp[6] += a[10] * b[6];
    temp[6] += a[14] * b[7];
    temp[7] += a[3] * b[4];
    temp[7] += a[7] * b[5];
    temp[7] += a[11] * b[6];
    temp[7] += a[15] * b[7];
    temp[8] += a[0] * b[8];
    temp[8] += a[4] * b[9];
    temp[8] += a[8] * b[10];
    temp[8] += a[12] * b[11];
    temp[9] += a[1] * b[8];
    temp[9] += a[5] * b[9];
    temp[9] += a[9] * b[10];
    temp[9] += a[13] * b[11];
    temp[10] += a[2] * b[8];
    temp[10] += a[6] * b[9];
    temp[10] += a[10] * b[10];
    temp[10] += a[14] * b[11];
    temp[11] += a[3] * b[8];
    temp[11] += a[7] * b[9];
    temp[11] += a[11] * b[10];
    temp[11] += a[15] * b[11];
    temp[12] += a[0] * b[12];
    temp[12] += a[4] * b[13];
    temp[12] += a[8] * b[14];
    temp[12] += a[12] * b[15];
    temp[13] += a[1] * b[12];
    temp[13] += a[5] * b[13];
    temp[13] += a[9] * b[14];
    temp[13] += a[13] * b[15];
    temp[14] += a[2] * b[12];
    temp[14] += a[6] * b[13];
    temp[14] += a[10] * b[14];
    temp[14] += a[14] * b[15];
    temp[15] += a[3] * b[12];
    temp[15] += a[7] * b[13];
    temp[15] += a[11] * b[14];
    temp[15] += a[15] * b[15];
    return temp;
  }

  public static float[] mulMatrix41(float a[], float b[]) {
    // assume a = 4x4, b = 4x1
    float[] temp = new float[4];
    temp[0] += a[0] * b[0];
    temp[0] += a[4] * b[1];
    temp[0] += a[8] * b[2];
    temp[0] += a[12] * b[3];
    temp[1] += a[1] * b[0];
    temp[1] += a[5] * b[1];
    temp[1] += a[9] * b[2];
    temp[1] += a[13] * b[3];
    temp[2] += a[2] * b[0];
    temp[2] += a[6] * b[1];
    temp[2] += a[10] * b[2];
    temp[2] += a[14] * b[3];
    temp[3] += a[3] * b[0];
    temp[3] += a[7] * b[1];
    temp[3] += a[11] * b[2];
    temp[3] += a[15] * b[3];
    return temp;
  }

  public static float[] mulMatrix31(float a[], float b[]) {
    // assume a = 4x4, b = 3x1
    float[] temp = new float[3];
    temp[0] += a[0] * b[0];
    temp[0] += a[4] * b[1];
    temp[0] += a[8] * b[2];
    temp[1] += a[1] * b[0];
    temp[1] += a[5] * b[1];
    temp[1] += a[9] * b[2];
    temp[2] += a[2] * b[0];
    temp[2] += a[6] * b[1];
    temp[2] += a[10] * b[2];
    return temp;
  }

  public static void loadMatrix44(float a[], float b[]) {
    System.arraycopy(b, 0, a, 0, 16);
  }

  public static float det22(float a, float b, float c, float d) {
    return (a * d - b * c);
  }

  public static float det33(float a1, float a2, float a3, float b1, float b2, float b3, float c1,
      float c2, float c3) {
    return (a1 * det22(b2, b3, c2, c3) - b1 * det22(a2, a3, c2, c3) + c1 * det22(a2, a3, b2, b3));
  }

  public static float det44(float a1, float a2, float a3, float a4, float b1, float b2, float b3,
      float b4, float c1, float c2, float c3, float c4, float d1, float d2, float d3, float d4) {
    return (a1 * det33(b2, b3, b4, c2, c3, c4, d2, d3, d4)
        - b1 * det33(a2, a3, a4, c2, c3, c4, d2, d3, d4)
        + c1 * det33(a2, a3, a4, b2, b3, b4, d2, d3, d4)
        - d1 * det33(a2, a3, a4, b2, b3, b4, c2, c3, c4));
  }

  public static float[] adjoint44(float a[]) {
    float m[] = new float[16];
    m[0] = det33(a[5], a[6], a[7], a[9], a[10], a[11], a[13], a[14], a[15]);
    m[1] = -det33(a[4], a[6], a[7], a[8], a[10], a[11], a[12], a[14], a[15]);
    m[2] = det33(a[4], a[5], a[7], a[8], a[9], a[11], a[12], a[13], a[15]);
    m[3] = -det33(a[4], a[5], a[6], a[8], a[9], a[10], a[12], a[13], a[14]);
    m[4] = -det33(a[1], a[2], a[3], a[9], a[10], a[11], a[13], a[14], a[15]);
    m[5] = det33(a[0], a[2], a[3], a[8], a[10], a[11], a[12], a[14], a[15]);
    m[6] = -det33(a[0], a[1], a[3], a[8], a[9], a[10], a[12], a[13], a[15]);
    m[7] = det33(a[0], a[1], a[2], a[8], a[9], a[10], a[12], a[13], a[14]);
    m[9] = -det33(a[0], a[2], a[3], a[4], a[6], a[7], a[12], a[14], a[15]);
    m[10] = det33(a[0], a[1], a[3], a[4], a[5], a[7], a[12], a[13], a[15]);
    m[11] = -det33(a[0], a[1], a[2], a[4], a[5], a[6], a[12], a[13], a[14]);
    m[12] = -det33(a[1], a[2], a[3], a[5], a[6], a[7], a[9], a[10], a[11]);
    m[13] = det33(a[0], a[2], a[3], a[4], a[6], a[7], a[8], a[10], a[11]);
    m[14] = -det33(a[0], a[1], a[3], a[4], a[5], a[7], a[8], a[9], a[11]);
    m[15] = det33(a[0], a[1], a[2], a[4], a[5], a[6], a[8], a[9], a[10]);
    return m;
  }

  public static float[] inverseMatrix44(float a[]) {
    float m[] = adjoint44(a);
    float det = det44(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9], m[10], m[11],
        m[12], m[13], m[14], m[15]);
    if (det == 0) {
      return null;
    }

    m[0] /= det;
    m[1] /= det;
    m[2] /= det;
    m[3] /= det;
    m[4] /= det;
    m[5] /= det;
    m[6] /= det;
    m[7] /= det;
    m[8] /= det;
    m[9] /= det;
    m[10] /= det;
    m[11] /= det;
    m[12] /= det;
    m[13] /= det;
    m[14] /= det;
    m[15] /= det;

    return m;
  }

  public static float dot33(float a[], float b[]) {
    return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
  }

  public static float dot44(float a[], float b[]) {
    return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3]);
  }

  public static float[] diff33(float u[], float v[]) {
    float m[] = new float[3];
    m[0] = v[0] - u[0];
    m[1] = v[1] - u[1];
    m[2] = v[2] - u[2];
    return m;
  }

  public static float[] cross33(float u[], float v[]) {
    float m[] = new float[3];
    m[0] = u[1] * v[2] - u[2] * v[1];
    m[1] = u[2] * v[0] - u[0] * v[2];
    m[2] = u[0] * v[1] - u[1] * v[0];
    return m;
  }

  /* if n isn't an exact power of two return -1 */
  public static int logbase2(int n) {
    int m = n;
    int i = 1;
    int log2 = 0;

    if (n < 0) {
      return -1;
    }
    while (m > i) {
      i = 2 * i;
      log2++;
    }
    if (m != n) {
      return -1;
    } else {
      return log2;
    }
  }

  public static float frac(float x) {
    return (x - (float) Math.floor(x));
  }

  public static float normalize(float p[]) {
    float w = (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
    if (w > 0.000001f) {
      p[0] /= w;
      p[1] /= w;
      p[2] /= w;
    }
    return w;
  }

  public static float interpolate(int i, int n, float u1, float u2, float du) {
    if (i == 0) {
      return u1;
    }
    if (i == n) {
      return u2;
    }
    return (u1 + i * du); // where du = (u2 - u1) / n
  }

  public static float interpolate(int i, int n, float u1, float u2) {
    float du = (u2 - u1) / (float) n;
    return interpolate(i, n, u1, u2, du);
  }

  public static byte CLAMP(byte x, byte min, byte max) {
    if (x < min)
      return min;
    if (x > max)
      return max;
    return x;
  }

  public static short CLAMP(short x, short min, short max) {
    if (x < min)
      return min;
    if (x > max)
      return max;
    return x;
  }

  public static int CLAMP(int x, int min, int max) {
    if (x < min)
      return min;
    if (x > max)
      return max;
    return x;
  }

  public static float CLAMP(float x, float min, float max) {
    if (x < min)
      return min;
    if (x > max)
      return max;
    return x;
  }

  public static float CLAMP(float x, double min, double max) {
    if (x < min)
      return (float) min;
    if (x > max)
      return (float) max;
    return x;
  }

  public static float CLAMP(double x, double min, double max) {
    // the CLAMP for double will return by float
    if (x < min)
      return (float) min;
    if (x > max)
      return (float) max;
    return (float) x;
  }

  public static int BtoI(byte x) {
    return (x & 0xff);
  }

  public static int StoI(short x) {
    return (x & 0xffff);
  }

  public static int FtoI(float x) {
    return (int) (x * 255.0f);
  }

  public static float BtoF(byte x) {
    return (float) (x & 0xff) / 255.0f;
  }

  public static float ItoF(int x) {
    return (float) x / 255.0f;
  }

  public static int ItoR(int x) {
    return ((x & 0x00ff0000) >> 16);
  }

  public static int ItoG(int x) {
    return ((x & 0x0000ff00) >> 8);
  }

  public static int ItoB(int x) {
    return (x & 0x000000ff);
  }

  public static int ItoA(int x) {
    return (((x & 0xff000000) >> 24) & 0x000000ff);
  }

  public static int RGBAtoI(byte r, byte g, byte b, byte a) {
    return (((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | b);
  }

  public static int RGBAtoI(int r, int g, int b, int a) {
    return ((a << 24) | (r << 16) | (g << 8) | b);
  }

  public static int RGBAtoI(float r, float g, float b, float a) {
    return RGBAtoI(FtoI(r), FtoI(g), FtoI(b), FtoI(a));
  }

  public static int RGBtoI(byte r, byte g, byte b) {
    return (0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | b);
  }

  public static int RGBtoI(int r, int g, int b) {
    return (0xff000000 | (r << 16) | (g << 8) | b);
  }

  public static int RGBtoI(float r, float g, float b) {
    return RGBtoI(FtoI(r), FtoI(g), FtoI(b));
  }

  public static int[] ItoRGBA(int i) {
    int rgb[] = new int[4];
    rgb[0] = (i & 0x00ff0000) >> 16;
    rgb[1] = (i & 0x0000ff00) >> 8;
    rgb[2] = (i & 0x000000ff);
    rgb[3] = ((i & 0xff000000) >> 24) & 0x000000ff;
    return rgb;
  }

  public static float[] ItoRGBAf(int i) {
    float rgb[] = new float[4];
    rgb[0] = ItoF((i & 0x00ff0000) >> 16);
    rgb[1] = ItoF((i & 0x0000ff00) >> 8);
    rgb[2] = ItoF((i & 0x000000ff));
    rgb[3] = ItoF(((i & 0xff000000) >> 24) & 0x000000ff);
    return rgb;
  }

  public static int[] ItoRGB(int i) {
    int rgb[] = new int[3];
    rgb[0] = (i & 0x00ff0000) >> 16;
    rgb[1] = (i & 0x0000ff00) >> 8;
    rgb[2] = (i & 0x000000ff);
    return rgb;
  }

  public static float[] ItoRGBf(int i) {
    float rgb[] = new float[3];
    rgb[0] = ItoF((i & 0x00ff0000) >> 16);
    rgb[1] = ItoF((i & 0x0000ff00) >> 8);
    rgb[2] = ItoF((i & 0x000000ff));
    return rgb;
  }

}
