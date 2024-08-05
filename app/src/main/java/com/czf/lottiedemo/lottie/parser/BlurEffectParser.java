package com.czf.lottiedemo.lottie.parser;

import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.model.content.BlurEffect;

import java.io.IOException;

class BlurEffectParser {


    @Nullable
    static BlurEffect parse(JsonReader reader, LottieComposition composition) throws IOException {
        BlurEffect blurEffect = null;
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "ef":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        BlurEffect be = maybeParseInnerEffect(reader, composition);
                        if (be != null) {
                            blurEffect = be;
                        }
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        return blurEffect;
    }

    @Nullable
    private static BlurEffect maybeParseInnerEffect(JsonReader reader, LottieComposition composition) throws IOException {
        BlurEffect blurEffect = null;
        boolean isCorrectType = false;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "ty":
                    isCorrectType = reader.nextInt() == 0;
                    break;
                case "v":
                    if (isCorrectType) {
                        blurEffect = new BlurEffect(AnimatableValueParser.parseFloat(reader, composition));
                    } else {
                        reader.skipValue();
                    }
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return blurEffect;
    }
}
