package com.czf.lottiedemo.lottie.parser;

import android.graphics.Path;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableGradientColorValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableIntegerValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatablePointValue;
import com.czf.lottiedemo.lottie.model.content.GradientFill;
import com.czf.lottiedemo.lottie.model.content.GradientType;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.io.IOException;
import java.util.Collections;

class GradientFillParser {

  private GradientFillParser() {
  }

  static GradientFill parse(
      JsonReader reader, LottieComposition composition) throws IOException {
    String name = null;
    AnimatableGradientColorValue color = null;
    AnimatableIntegerValue opacity = null;
    GradientType gradientType = null;
    AnimatablePointValue startPoint = null;
    AnimatablePointValue endPoint = null;
    Path.FillType fillType = Path.FillType.WINDING;
    boolean hidden = false;

    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case "nm":
          name = reader.nextString();
          break;
        case "g":
          int points = -1;
          reader.beginObject();
          while (reader.hasNext()) {
            switch (reader.nextName()) {
              case "p":
                points = reader.nextInt();
                break;
              case "k":
                color = AnimatableValueParser.parseGradientColor(reader, composition, points);
                break;
              default:
                reader.skipValue();
            }
          }
          reader.endObject();
          break;
        case "o":
          opacity = AnimatableValueParser.parseInteger(reader, composition);
          break;
        case "t":
          gradientType = reader.nextInt() == 1 ? GradientType.LINEAR : GradientType.RADIAL;
          break;
        case "s":
          startPoint = AnimatableValueParser.parsePoint(reader, composition);
          break;
        case "e":
          endPoint = AnimatableValueParser.parsePoint(reader, composition);
          break;
        case "r":
          fillType = reader.nextInt() == 1 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD;
          break;
        case "hd":
          hidden = reader.nextBoolean();
          break;
        default:
          reader.skipValue();
      }
    }

    // Telegram sometimes omits opacity.
    // https://github.com/airbnb/lottie-android/issues/1600
    opacity = opacity == null ? new AnimatableIntegerValue(Collections.singletonList(new Keyframe<>(100))) : opacity;
    return new GradientFill(
        name, gradientType, fillType, color, opacity, startPoint, endPoint, null, null, hidden);
  }
}
