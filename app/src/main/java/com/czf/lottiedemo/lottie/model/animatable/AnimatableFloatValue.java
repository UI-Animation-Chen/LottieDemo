package com.czf.lottiedemo.lottie.model.animatable;

import com.czf.lottiedemo.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.czf.lottiedemo.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public class AnimatableFloatValue extends BaseAnimatableValue<Float, Float> {

  public AnimatableFloatValue(List<Keyframe<Float>> keyframes) {
    super(keyframes);
  }

  @Override public BaseKeyframeAnimation<Float, Float> createAnimation() {
    return new FloatKeyframeAnimation(keyframes);
  }
}
