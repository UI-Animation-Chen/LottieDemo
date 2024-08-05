package com.czf.lottiedemo.lottie.model.animatable;

import android.graphics.Path;

import com.czf.lottiedemo.lottie.animation.keyframe.ShapeKeyframeAnimation;
import com.czf.lottiedemo.lottie.model.content.ShapeData;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public class AnimatableShapeValue extends BaseAnimatableValue<ShapeData, Path> {

  public AnimatableShapeValue(List<Keyframe<ShapeData>> keyframes) {
    super(keyframes);
  }

  @Override public ShapeKeyframeAnimation createAnimation() {
    return new ShapeKeyframeAnimation(keyframes);
  }
}
