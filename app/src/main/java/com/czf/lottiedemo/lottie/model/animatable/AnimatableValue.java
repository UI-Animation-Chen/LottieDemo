package com.czf.lottiedemo.lottie.model.animatable;

import com.czf.lottiedemo.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public interface AnimatableValue<K, A> {
  List<Keyframe<K>> getKeyframes();

  boolean isStatic();

  BaseKeyframeAnimation<K, A> createAnimation();
}
