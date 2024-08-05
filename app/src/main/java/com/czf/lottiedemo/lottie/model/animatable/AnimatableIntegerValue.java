package com.czf.lottiedemo.lottie.model.animatable;

import com.czf.lottiedemo.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.czf.lottiedemo.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.czf.lottiedemo.lottie.value.Keyframe;

import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public class AnimatableIntegerValue extends BaseAnimatableValue<Integer, Integer> {

  public AnimatableIntegerValue(List<Keyframe<Integer>> keyframes) {
    super(keyframes);
  }

  @Override public BaseKeyframeAnimation<Integer, Integer> createAnimation() {
    return new IntegerKeyframeAnimation(keyframes);
  }
}
