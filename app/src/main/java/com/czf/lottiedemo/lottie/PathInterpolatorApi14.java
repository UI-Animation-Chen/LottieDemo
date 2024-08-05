package com.czf.lottiedemo.lottie;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

class PathInterpolatorApi14 implements Interpolator {
    private static final float PRECISION = 0.002F;
    private final float[] mX;
    private final float[] mY;

    PathInterpolatorApi14(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float pathLength = pathMeasure.getLength();
        int numPoints = (int)(pathLength / 0.002F) + 1;
        this.mX = new float[numPoints];
        this.mY = new float[numPoints];
        float[] position = new float[2];

        for(int i = 0; i < numPoints; ++i) {
            float distance = (float)i * pathLength / (float)(numPoints - 1);
            pathMeasure.getPosTan(distance, position, (float[])null);
            this.mX[i] = position[0];
            this.mY[i] = position[1];
        }

    }

    PathInterpolatorApi14(float controlX, float controlY) {
        this(createQuad(controlX, controlY));
    }

    PathInterpolatorApi14(float controlX1, float controlY1, float controlX2, float controlY2) {
        this(createCubic(controlX1, controlY1, controlX2, controlY2));
    }

    public float getInterpolation(float t) {
        if (t <= 0.0F) {
            return 0.0F;
        } else if (t >= 1.0F) {
            return 1.0F;
        } else {
            int startIndex = 0;
            int endIndex = this.mX.length - 1;

            while(endIndex - startIndex > 1) {
                int midIndex = (startIndex + endIndex) / 2;
                if (t < this.mX[midIndex]) {
                    endIndex = midIndex;
                } else {
                    startIndex = midIndex;
                }
            }

            float xRange = this.mX[endIndex] - this.mX[startIndex];
            if (xRange == 0.0F) {
                return this.mY[startIndex];
            } else {
                float tInRange = t - this.mX[startIndex];
                float fraction = tInRange / xRange;
                float startY = this.mY[startIndex];
                float endY = this.mY[endIndex];
                return startY + fraction * (endY - startY);
            }
        }
    }

    private static Path createQuad(float controlX, float controlY) {
        Path path = new Path();
        path.moveTo(0.0F, 0.0F);
        path.quadTo(controlX, controlY, 1.0F, 1.0F);
        return path;
    }

    private static Path createCubic(float controlX1, float controlY1, float controlX2, float controlY2) {
        Path path = new Path();
        path.moveTo(0.0F, 0.0F);
        path.cubicTo(controlX1, controlY1, controlX2, controlY2, 1.0F, 1.0F);
        return path;
    }
}