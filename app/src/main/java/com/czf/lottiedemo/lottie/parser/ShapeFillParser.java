package com.czf.lottiedemo.lottie.parser;

import android.graphics.Path;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableColorValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableIntegerValue;
import com.czf.lottiedemo.lottie.model.content.ShapeFill;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.io.IOException;
import java.util.Collections;

class ShapeFillParser {

    private ShapeFillParser() {
    }

    static ShapeFill parse(
            JsonReader reader, LottieComposition composition) throws IOException {
        AnimatableColorValue color = null;
        boolean fillEnabled = false;
        AnimatableIntegerValue opacity = null;
        String name = null;
        int fillTypeInt = 1;
        boolean hidden = false;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "c":
                    color = AnimatableValueParser.parseColor(reader, composition);
                    break;
                case "o":
                    opacity = AnimatableValueParser.parseInteger(reader, composition);
                    break;
                case "fillEnabled":
                    fillEnabled = reader.nextBoolean();
                    break;
                case "r":
                    fillTypeInt = reader.nextInt();
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
        Path.FillType fillType = fillTypeInt == 1 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD;
        return new ShapeFill(name, fillEnabled, fillType, color, opacity, hidden);
    }
}
