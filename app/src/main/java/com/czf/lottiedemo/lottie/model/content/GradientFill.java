package com.czf.lottiedemo.lottie.model.content;

import android.graphics.Path;
import android.support.annotation.Nullable;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.animation.content.Content;
import com.czf.lottiedemo.lottie.animation.content.GradientFillContent;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableFloatValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableGradientColorValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableIntegerValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatablePointValue;
import com.czf.lottiedemo.lottie.model.layer.BaseLayer;

public class GradientFill implements ContentModel {

  private final GradientType gradientType;
  private final Path.FillType fillType;
  private final AnimatableGradientColorValue gradientColor;
  private final AnimatableIntegerValue opacity;
  private final AnimatablePointValue startPoint;
  private final AnimatablePointValue endPoint;
  private final String name;
  @Nullable
  private final AnimatableFloatValue highlightLength;
  @Nullable private final AnimatableFloatValue highlightAngle;
  private final boolean hidden;

  public GradientFill(String name, GradientType gradientType, Path.FillType fillType,
      AnimatableGradientColorValue gradientColor,
      AnimatableIntegerValue opacity, AnimatablePointValue startPoint,
      AnimatablePointValue endPoint, AnimatableFloatValue highlightLength,
      AnimatableFloatValue highlightAngle, boolean hidden) {
    this.gradientType = gradientType;
    this.fillType = fillType;
    this.gradientColor = gradientColor;
    this.opacity = opacity;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.name = name;
    this.highlightLength = highlightLength;
    this.highlightAngle = highlightAngle;
    this.hidden = hidden;
  }

  public String getName() {
    return name;
  }

  public GradientType getGradientType() {
    return gradientType;
  }

  public Path.FillType getFillType() {
    return fillType;
  }

  public AnimatableGradientColorValue getGradientColor() {
    return gradientColor;
  }

  public AnimatableIntegerValue getOpacity() {
    return opacity;
  }

  public AnimatablePointValue getStartPoint() {
    return startPoint;
  }

  public AnimatablePointValue getEndPoint() {
    return endPoint;
  }

  public boolean isHidden() {
    return hidden;
  }

  @Override public Content toContent(LottieDrawable drawable, LottieComposition composition, BaseLayer layer) {
    return new GradientFillContent(drawable, composition, layer, this);
  }

}
