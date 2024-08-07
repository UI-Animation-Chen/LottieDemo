package com.czf.lottiedemo.lottie.animation.content;

import static com.czf.lottiedemo.lottie.LottieProperty.STROKE_COLOR;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.support.annotation.Nullable;

import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.LottieProperty;
import com.czf.lottiedemo.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.czf.lottiedemo.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.czf.lottiedemo.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.czf.lottiedemo.lottie.model.content.ShapeStroke;
import com.czf.lottiedemo.lottie.model.layer.BaseLayer;
import com.czf.lottiedemo.lottie.value.LottieValueCallback;

public class StrokeContent extends BaseStrokeContent {

  private final BaseLayer layer;
  private final String name;
  private final boolean hidden;
  private final BaseKeyframeAnimation<Integer, Integer> colorAnimation;
  @Nullable
  private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;

  public StrokeContent(final LottieDrawable lottieDrawable, BaseLayer layer, ShapeStroke stroke) {
    super(lottieDrawable, layer, stroke.getCapType().toPaintCap(),
        stroke.getJoinType().toPaintJoin(), stroke.getMiterLimit(), stroke.getOpacity(),
        stroke.getWidth(), stroke.getLineDashPattern(), stroke.getDashOffset());
    this.layer = layer;
    name = stroke.getName();
    hidden = stroke.isHidden();
    colorAnimation = stroke.getColor().createAnimation();
    colorAnimation.addUpdateListener(this);
    layer.addAnimation(colorAnimation);
  }

  @Override public void draw(Canvas canvas, Matrix parentMatrix, int parentAlpha) {
    if (hidden) {
      return;
    }
    paint.setColor(((ColorKeyframeAnimation) colorAnimation).getIntValue());
    if (colorFilterAnimation != null) {
      paint.setColorFilter(colorFilterAnimation.getValue());
    }
    super.draw(canvas, parentMatrix, parentAlpha);
  }

  @Override public String getName() {
    return name;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void addValueCallback(T property, @Nullable LottieValueCallback<T> callback) {
    super.addValueCallback(property, callback);
    if (property == STROKE_COLOR) {
      colorAnimation.setValueCallback((LottieValueCallback<Integer>) callback);
    } else if (property == LottieProperty.COLOR_FILTER) {
      if (colorFilterAnimation != null) {
        layer.removeAnimation(colorFilterAnimation);
      }

      if (callback == null) {
        colorFilterAnimation = null;
      } else {
        colorFilterAnimation =
            new ValueCallbackKeyframeAnimation<>((LottieValueCallback<ColorFilter>) callback);
        colorFilterAnimation.addUpdateListener(this);
        layer.addAnimation(colorAnimation);
      }
    }
  }
}
