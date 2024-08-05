package com.czf.lottiedemo.lottie.model.content;

import android.support.annotation.Nullable;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.animation.content.Content;
import com.czf.lottiedemo.lottie.animation.content.RoundedCornersContent;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableValue;
import com.czf.lottiedemo.lottie.model.layer.BaseLayer;

public class RoundedCorners implements ContentModel {
  private final String name;
  private final AnimatableValue<Float, Float> cornerRadius;

  public RoundedCorners(String name, AnimatableValue<Float, Float> cornerRadius) {
    this.name = name;
    this.cornerRadius = cornerRadius;
  }

  public String getName() {
    return name;
  }

  public AnimatableValue<Float, Float> getCornerRadius() {
    return cornerRadius;
  }

  @Nullable
  @Override public Content toContent(LottieDrawable drawable, LottieComposition composition, BaseLayer layer) {
    return new RoundedCornersContent(drawable, layer, this);
  }
}
