package com.czf.lottiedemo.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.animation.keyframe.PathKeyframe;
import com.czf.lottiedemo.lottie.utils.Utils;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.io.IOException;

class PathKeyframeParser {

  private PathKeyframeParser() {
  }

  static PathKeyframe parse(
          JsonReader reader, LottieComposition composition) throws IOException {
    boolean animated = reader.peek() == JsonToken.BEGIN_OBJECT;
    Keyframe<PointF> keyframe = KeyframeParser.parse(
        reader, composition, Utils.dpScale(), PathParser.INSTANCE, animated, false);

    return new PathKeyframe(composition, keyframe);
  }
}
