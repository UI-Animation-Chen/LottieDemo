package com.czf.lottiedemo.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableFloatValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableValue;
import com.czf.lottiedemo.lottie.model.content.RectangleShape;

import java.io.IOException;

class RectangleShapeParser {


    private RectangleShapeParser() {
    }

    static RectangleShape parse(
            JsonReader reader, LottieComposition composition) throws IOException {
        String name = null;
        AnimatableValue<PointF, PointF> position = null;
        AnimatableValue<PointF, PointF> size = null;
        AnimatableFloatValue roundedness = null;
        boolean hidden = false;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "p":
                    position =
                            AnimatablePathValueParser.parseSplitPath(reader, composition);
                    break;
                case "s":
                    size = AnimatableValueParser.parsePoint(reader, composition);
                    break;
                case "r":
                    roundedness = AnimatableValueParser.parseFloat(reader, composition);
                    break;
                case "hd":
                    hidden = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }

        return new RectangleShape(name, position, size, roundedness, hidden);
    }
}
