package com.czf.lottiedemo.lottie.parser;

import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.animatable.AnimatableValue;
import com.czf.lottiedemo.lottie.model.content.RoundedCorners;

import java.io.IOException;

public class RoundedCornersParser {


    private RoundedCornersParser() {
    }

    @Nullable
    static RoundedCorners parse(
            JsonReader reader, LottieComposition composition) throws IOException {
        String name = null;
        AnimatableValue<Float, Float> cornerRadius = null;
        boolean hidden = false;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "nm": //nm
                    name = reader.nextString();
                    break;
                case "r": // r
                    cornerRadius = AnimatableValueParser.parseFloat(reader, composition, true);
                    break;
                case "hd": // hd
                    hidden = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }

        return hidden ? null : new RoundedCorners(name, cornerRadius);
    }
}
