package com.czf.lottiedemo.lottie.model.animatable;

import com.czf.lottiedemo.lottie.animation.keyframe.TextKeyframeAnimation;
import com.czf.lottiedemo.lottie.model.DocumentData;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.util.List;

public class AnimatableTextFrame extends BaseAnimatableValue<DocumentData, DocumentData> {

  public AnimatableTextFrame(List<Keyframe<DocumentData>> keyframes) {
    super(keyframes);
  }

  @Override public TextKeyframeAnimation createAnimation() {
    return new TextKeyframeAnimation(keyframes);
  }
}
