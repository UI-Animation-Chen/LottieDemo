package com.czf.lottiedemo.lottie.parser;

import android.graphics.Rect;
import android.util.JsonReader;
import android.util.LongSparseArray;
import android.util.SparseArray;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.LottieImageAsset;
import com.czf.lottiedemo.lottie.model.Font;
import com.czf.lottiedemo.lottie.model.FontCharacter;
import com.czf.lottiedemo.lottie.model.Marker;
import com.czf.lottiedemo.lottie.model.layer.Layer;
import com.czf.lottiedemo.lottie.utils.Logger;
import com.czf.lottiedemo.lottie.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LottieCompositionParser {
/*  private static final JsonReader.Options NAMES = JsonReader.Options.of(
      "w", // 0
      "h", // 1
      "ip", // 2
      "op", // 3
      "fr", // 4
      "v", // 5
      "layers", // 6
      "assets", // 7
      "fonts", // 8
      "chars", // 9
      "markers" // 10
  );*/

    public static LottieComposition parse(JsonReader reader) throws IOException {
        float scale = Utils.dpScale();
        float startFrame = 0f;
        float endFrame = 0f;
        float frameRate = 0f;
        final LongSparseArray<Layer> layerMap = new LongSparseArray<>();
        final List<Layer> layers = new ArrayList<>();
        int width = 0;
        int height = 0;
        Map<String, List<Layer>> precomps = new HashMap<>();
        Map<String, LottieImageAsset> images = new HashMap<>();
        Map<String, Font> fonts = new HashMap<>();
        List<Marker> markers = new ArrayList<>();
        SparseArray<FontCharacter> characters = new SparseArray<>();
        LottieComposition.Timer timer = new LottieComposition.Timer();

        LottieComposition composition = new LottieComposition();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "w":
                    width = reader.nextInt();
                    break;
                case "h":
                    height = reader.nextInt();
                    break;
                case "ip":
                    startFrame = (float) reader.nextDouble();
                    break;
                case "op":
                    endFrame = (float) reader.nextDouble() - 0.01f;
                    break;
                case "fr":
                    frameRate = (float) reader.nextDouble();
                    break;
                case "v":
                    String version = reader.nextString();
                    String[] versions = version.split("\\.");
                    int majorVersion = Integer.parseInt(versions[0]);
                    int minorVersion = Integer.parseInt(versions[1]);
                    int patchVersion = Integer.parseInt(versions[2]);
                    if (!Utils.isAtLeastVersion(majorVersion, minorVersion, patchVersion,
                            4, 4, 0)) {
                        composition.addWarning("Lottie only supports bodymovin >= 4.4.0");
                    }
                    break;
                case "layers":
                    parseLayers(reader, composition, layers, layerMap);
                    break;
                case "assets":
                    parseAssets(reader, composition, precomps, images);
                    break;
                case "fonts":
                    parseFonts(reader, fonts);
                    break;
                case "chars":
                    parseChars(reader, composition, characters);
                    break;
                case "markers":
                    parseMarkers(reader, markers);
                    break;
                case "timer":
                    parseTimer(reader, timer);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);
        Rect bounds = new Rect(0, 0, scaledWidth, scaledHeight);

        composition.init(bounds, startFrame, endFrame, frameRate, layers, layerMap, precomps,
                images, characters, fonts, markers, timer);

        return composition;
    }

    private static void parseTimer(JsonReader reader, LottieComposition.Timer timer) {
        try {
            reader.beginObject();

            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "ke":
                        timer.ke = reader.nextInt();
                        break;
                    case "id":
                        timer.id = reader.nextString();
                        break;
                    case "tl":
                        timer.tl = reader.nextString();
                        break;
                    case "at":
                        timer.at = reader.nextString();
                        break;
                    case "inel":
                        timer.inel = new int[] {-1, -1}; // -1是非法值
                        reader.beginArray();
                        // 只读取2个数字，理论上也是2个
                        for (int i = 0; i < 2; i++) {
                            if (reader.hasNext()) {
                                timer.inel[i] = reader.nextInt();
                            }
                        }
                        reader.endArray();
                        break;
                    case "el":
                        timer.el = reader.nextString();
                        break;
                    default:
                        break;
                }
            }

            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseLayers(JsonReader reader, LottieComposition composition,
                                    List<Layer> layers, LongSparseArray<Layer> layerMap) throws IOException {
        int imageCount = 0;
        reader.beginArray();
        while (reader.hasNext()) {
            Layer layer = LayerParser.parse(reader, composition);
            if (layer.getLayerType() == Layer.LayerType.IMAGE) {
                imageCount++;
            }
            layers.add(layer);
            layerMap.put(layer.getId(), layer);

            if (imageCount > 4) {
                Logger.warning("You have " + imageCount + " images. Lottie should primarily be " +
                        "used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers" +
                        " to shape layers.");
            }
        }
        reader.endArray();
    }


    private static void parseAssets(JsonReader reader, LottieComposition composition,
                                    Map<String, List<Layer>> precomps, Map<String, LottieImageAsset> images) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            String id = null;
            // For precomps
            List<Layer> layers = new ArrayList<>();
            LongSparseArray<Layer> layerMap = new LongSparseArray<>();
            // For images
            int width = 0;
            int height = 0;
            String imageFileName = null;
            String relativeFolder = null;
            String rel = null;
            List<LottieImageAsset.TextConfig> textConfigs = null;
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "layers":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            Layer layer = LayerParser.parse(reader, composition);
                            layerMap.put(layer.getId(), layer);
                            layers.add(layer);
                        }
                        reader.endArray();
                        break;
                    case "w":
                        width = reader.nextInt();
                        break;
                    case "h":
                        height = reader.nextInt();
                        break;
                    case "p":
                        imageFileName = reader.nextString();
                        break;
                    case "u":
                        relativeFolder = reader.nextString();
                        break;
                    case "tc": // 文本配置
                        reader.beginArray();
                        textConfigs = parseTextConfig(reader);
                        reader.endArray();
                        break;
                    case "rel":
                        rel = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();
            if (imageFileName != null) {
                LottieImageAsset image =
                        new LottieImageAsset(width, height, id, imageFileName, relativeFolder, rel, textConfigs);
                images.put(image.getId(), image);
            } else {
                precomps.put(id, layers);
            }
        }
        reader.endArray();
    }

    private static List<LottieImageAsset.TextConfig> parseTextConfig(JsonReader reader) {
        try {
            List<LottieImageAsset.TextConfig> configs = new ArrayList<>();
            while (reader.hasNext()) {
                LottieImageAsset.TextConfig config = new LottieImageAsset.TextConfig();
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "l": // location，字符起始位置；
                            config.location = reader.nextInt();
                            break;
                        case "le": // 字符个数，当<0时表示截止位置。
                            config.textLen = reader.nextInt();
                            break;
                        case "s": // 字体大小
                            config.textSize = reader.nextInt();
                            break;
                        case "c": // 字体颜色
                            config.textColor = reader.nextString();
                            break;
                        case "f": // 背景颜色
                            config.bgColor = reader.nextString();
                            break;
                        case "bs": // 本段文本的从下到上的偏移量
                            config.bs = reader.nextInt();
                            break;
                        default:
                            reader.skipValue();
                    }
                }
                reader.endObject();
                configs.add(config);
            }
            return configs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseFonts(JsonReader reader, Map<String, Font> fonts) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "list":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Font font = FontParser.parse(reader);
                        fonts.put(font.getName(), font);
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
    }

    private static void parseChars(
            JsonReader reader, LottieComposition composition,
            SparseArray<FontCharacter> characters) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            FontCharacter character = FontCharacterParser.parse(reader, composition);
            characters.put(character.hashCode(), character);
        }
        reader.endArray();
    }


    private static void parseMarkers(JsonReader reader, List<Marker> markers) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            String comment = null;
            float frame = 0f;
            float durationFrames = 0f;
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "cm":
                        comment = reader.nextString();
                        break;
                    case "tm":
                        frame = (float) reader.nextDouble();
                        break;
                    case "dr":
                        durationFrames = (float) reader.nextDouble();
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();
            markers.add(new Marker(comment, frame, durationFrames));
        }
        reader.endArray();
    }
}
