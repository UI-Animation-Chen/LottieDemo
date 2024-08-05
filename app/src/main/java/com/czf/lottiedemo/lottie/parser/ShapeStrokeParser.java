package com.czf.lottiedemo.lottie.parser;


import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableColorValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableFloatValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableIntegerValue;
import com.czf.lottiedemo.lottie.model.content.ShapeStroke;
import com.czf.lottiedemo.lottie.value.Keyframe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ShapeStrokeParser {


    private ShapeStrokeParser() {
    }

    static ShapeStroke parse(
            JsonReader reader, LottieComposition composition) throws IOException {
        String name = null;
        AnimatableColorValue color = null;
        AnimatableFloatValue width = null;
        AnimatableIntegerValue opacity = null;
        ShapeStroke.LineCapType capType = null;
        ShapeStroke.LineJoinType joinType = null;
        AnimatableFloatValue offset = null;
        float miterLimit = 0f;
        boolean hidden = false;

        List<AnimatableFloatValue> lineDashPattern = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "c":
                    color = AnimatableValueParser.parseColor(reader, composition);
                    break;
                case "w":
                    width = AnimatableValueParser.parseFloat(reader, composition);
                    break;
                case "o":
                    opacity = AnimatableValueParser.parseInteger(reader, composition);
                    break;
                case "lc":
                    capType = ShapeStroke.LineCapType.values()[reader.nextInt() - 1];
                    break;
                case "lj":
                    joinType = ShapeStroke.LineJoinType.values()[reader.nextInt() - 1];
                    break;
                case "ml":
                    miterLimit = (float) reader.nextDouble();
                    break;
                case "hd":
                    hidden = reader.nextBoolean();
                    break;
                case "d":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String n = null;
                        AnimatableFloatValue val = null;

                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "n":
                                    n = reader.nextString();
                                    break;
                                case "v":
                                    val = AnimatableValueParser.parseFloat(reader, composition);
                                    break;
                                default:
                                    reader.skipValue();
                            }
                        }
                        reader.endObject();

                        switch (n) {
                            case "o":
                                offset = val;
                                break;
                            case "d":
                            case "g":
                                composition.setHasDashPattern(true);
                                lineDashPattern.add(val);
                                break;
                        }
                    }
                    reader.endArray();

                    if (lineDashPattern.size() == 1) {
                        // If there is only 1 value then it is assumed to be equal parts on and off.
                        lineDashPattern.add(lineDashPattern.get(0));
                    }
                    break;
                default:
                    reader.skipValue();
            }
        }

        // Telegram sometimes omits opacity.
        // https://github.com/airbnb/lottie-android/issues/1600
        opacity = opacity == null ? new AnimatableIntegerValue(Collections.singletonList(new Keyframe<>(100))) : opacity;
        return new ShapeStroke(
                name, offset, lineDashPattern, color, opacity, width, capType, joinType, miterLimit, hidden);
    }
}
