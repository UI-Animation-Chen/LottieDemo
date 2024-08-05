package com.czf.lottiedemo.lottie.model.animatable;

import com.czf.lottiedemo.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.czf.lottiedemo.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public class AnimatableColorValue extends BaseAnimatableValue<Integer, Integer> {
  public AnimatableColorValue(List<Keyframe<Integer>> keyframes) {
    super(keyframes);
  }

  @Override public BaseKeyframeAnimation<Integer, Integer> createAnimation() {
    return new ColorKeyframeAnimation(keyframes);
  }
}
