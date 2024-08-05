package com.czf.lottiedemo.lottie.parser;

import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableColorValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableFloatValue;

import java.io.IOException;

public class DropShadowEffectParser {


    private AnimatableColorValue color;
    private AnimatableFloatValue opacity;
    private AnimatableFloatValue direction;
    private AnimatableFloatValue distance;
    private AnimatableFloatValue radius;

    @Nullable
    DropShadowEffect parse(JsonReader reader, LottieComposition composition) throws IOException {
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "ef":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        maybeParseInnerEffect(reader, composition);
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        if (color != null && opacity != null && direction != null && distance != null && radius != null) {
            return new DropShadowEffect(color, opacity, direction, distance, radius);
        }
        return null;
    }

    private void maybeParseInnerEffect(JsonReader reader, LottieComposition composition) throws IOException {
        String currentEffectName = "";
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    currentEffectName = reader.nextString();
                    break;
                case "v":
                    switch (currentEffectName) {
                        case "Shadow Color":
                            color = AnimatableValueParser.parseColor(reader, composition);
                            break;
                        case "Opacity":
                            opacity = AnimatableValueParser.parseFloat(reader, composition, false);
                            break;
                        case "Direction":
                            direction = AnimatableValueParser.parseFloat(reader, composition, false);
                            break;
                        case "Distance":
                            distance = AnimatableValueParser.parseFloat(reader, composition);
                            break;
                        case "Softness":
                            radius = AnimatableValueParser.parseFloat(reader, composition);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
    }
}
