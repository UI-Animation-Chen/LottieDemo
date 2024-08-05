package com.czf.lottiedemo.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableFloatValue;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableValue;
import com.czf.lottiedemo.lottie.model.content.PolystarShape;

import java.io.IOException;

class PolystarShapeParser {


    private PolystarShapeParser() {
    }

    static PolystarShape parse(
            JsonReader reader, LottieComposition composition, int d) throws IOException {
        String name = null;
        PolystarShape.Type type = null;
        AnimatableFloatValue points = null;
        AnimatableValue<PointF, PointF> position = null;
        AnimatableFloatValue rotation = null;
        AnimatableFloatValue outerRadius = null;
        AnimatableFloatValue outerRoundedness = null;
        AnimatableFloatValue innerRadius = null;
        AnimatableFloatValue innerRoundedness = null;
        boolean hidden = false;
        boolean reversed = d == 3;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "sy":
                    type = PolystarShape.Type.forValue(reader.nextInt());
                    break;
                case "pt":
                    points = AnimatableValueParser.parseFloat(reader, composition, false);
                    break;
                case "p":
                    position = AnimatablePathValueParser.parseSplitPath(reader, composition);
                    break;
                case "r":
                    rotation = AnimatableValueParser.parseFloat(reader, composition, false);
                    break;
                case "or":
                    outerRadius = AnimatableValueParser.parseFloat(reader, composition);
                    break;
                case "os":
                    outerRoundedness = AnimatableValueParser.parseFloat(reader, composition, false);
                    break;
                case "ir":
                    innerRadius = AnimatableValueParser.parseFloat(reader, composition);
                    break;
                case "is":
                    innerRoundedness = AnimatableValueParser.parseFloat(reader, composition, false);
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

        return new PolystarShape(
                name, type, points, position, rotation, innerRadius, outerRadius,
                innerRoundedness, outerRoundedness, hidden, reversed);
    }
}
