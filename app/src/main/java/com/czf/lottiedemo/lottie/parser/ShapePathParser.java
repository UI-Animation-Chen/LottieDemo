package com.czf.lottiedemo.lottie.parser;


import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableShapeValue;
import com.czf.lottiedemo.lottie.model.content.ShapePath;

import java.io.IOException;

class ShapePathParser {


    private ShapePathParser() {
    }

    static ShapePath parse(
            JsonReader reader, LottieComposition composition) throws IOException {
        String name = null;
        int ind = 0;
        AnimatableShapeValue shape = null;
        boolean hidden = false;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm":
                    name = reader.nextString();
                    break;
                case "ind":
                    ind = reader.nextInt();
                    break;
                case "ks":
                    shape = AnimatableValueParser.parseShapeData(reader, composition);
                    break;
                case "hd":
                    hidden = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }

        return new ShapePath(name, ind, shape, hidden);
    }
}
