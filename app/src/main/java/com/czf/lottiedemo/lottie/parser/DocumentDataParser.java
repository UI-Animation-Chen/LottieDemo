package com.czf.lottiedemo.lottie.parser;


import android.graphics.PointF;
import android.util.JsonReader;

import com.czf.lottiedemo.lottie.model.DocumentData;
import com.czf.lottiedemo.lottie.model.DocumentData.Justification;

import java.io.IOException;

public class DocumentDataParser implements ValueParser<DocumentData> {
    public static final DocumentDataParser INSTANCE = new DocumentDataParser();

    private DocumentDataParser() {
    }

    @Override
    public DocumentData parse(JsonReader reader, float scale) throws IOException {
        String text = null;
        String fontName = null;
        float size = 0f;
        Justification justification = Justification.CENTER;
        int tracking = 0;
        float lineHeight = 0f;
        float baselineShift = 0f;
        int fillColor = 0;
        int strokeColor = 0;
        float strokeWidth = 0f;
        boolean strokeOverFill = true;
        PointF boxPosition = null;
        PointF boxSize = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "t":
                    text = reader.nextString();
                    break;
                case "f":
                    fontName = reader.nextString();
                    break;
                case "s":
                    size = (float) reader.nextDouble();
                    break;
                case "j":
                    int justificationInt = reader.nextInt();
                    if (justificationInt > Justification.CENTER.ordinal() || justificationInt < 0) {
                        justification = Justification.CENTER;
                    } else {
                        justification = Justification.values()[justificationInt];
                    }
                    break;
                case "tr":
                    tracking = reader.nextInt();
                    break;
                case "lh":
                    lineHeight = (float) reader.nextDouble();
                    break;
                case "ls":
                    baselineShift = (float) reader.nextDouble();
                    break;
                case "fc":
                    fillColor = JsonUtils.jsonToColor(reader);
                    break;
                case "sc":
                    strokeColor = JsonUtils.jsonToColor(reader);
                    break;
                case "sw":
                    strokeWidth = (float) reader.nextDouble();
                    break;
                case "of":
                    strokeOverFill = reader.nextBoolean();
                    break;
                case "ps":
                    reader.beginArray();
                    boxPosition = new PointF((float) reader.nextDouble() * scale, (float) reader.nextDouble() * scale);
                    reader.endArray();
                    break;
                case "sz":
                    reader.beginArray();
                    boxSize = new PointF((float) reader.nextDouble() * scale, (float) reader.nextDouble() * scale);
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return new DocumentData(text, fontName, size, justification, tracking, lineHeight,
                baselineShift, fillColor, strokeColor, strokeWidth, strokeOverFill, boxPosition, boxSize);
    }
}
