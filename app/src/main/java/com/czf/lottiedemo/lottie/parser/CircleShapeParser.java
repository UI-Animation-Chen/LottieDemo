package com.czf.lottiedemo.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatablePointValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableValue;
import com.czf.lottiedemo.lottie.model.content.CircleShape;

import java.io.IOException;

class CircleShapeParser {


    private CircleShapeParser() {
    }

    static CircleShape parse(
            JsonReader reader, LottieComposition composition, int d) throws IOException {
        String name = null;
        AnimatableValue<PointF, PointF> position = null;
        AnimatablePointValue size = null;
        boolean reversed = d == 3;
        boolean hidden = false;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "p":
                    position = AnimatablePathValueParser.parseSplitPath(reader, composition);
                    break;
                case "s":
                    size = AnimatableValueParser.parsePoint(reader, composition);
                    break;
                case "hd":
                    hidden = reader.nextBoolean();
                    break;
                case "d":
                    // "d" is 2 for normal and 3 for reversed.
                    reversed = reader.nextInt() == 3;
                    break;
                default:
                    reader.skipValue();
            }
        }

        return new CircleShape(name, position, size, reversed, hidden);
    }
}
