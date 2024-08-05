package com.czf.lottiedemo.lottie;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.List;

/**
 * Data class describing an image asset embedded in a Lottie json file.
 */
public class LottieImageAsset {
  private final int width;
  private final int height;
  private final String id;
  private final String fileName;
  private final String dirName;
  private final String rel;
  private final List<TextConfig> textConigList;
  /**
   * Pre-set a bitmap for this asset
   */
  @Nullable
  private Bitmap bitmap;

  @RestrictTo(RestrictTo.Scope.LIBRARY)
  public LottieImageAsset(int width, int height, String id, String fileName, String dirName, String rel, List<TextConfig> txtConfigs) {
    this.width = width;
    this.height = height;
    this.id = id;
    this.fileName = fileName;
    this.dirName = dirName;
    this.rel = rel;
    this.textConigList = txtConfigs;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<TextConfig> getTextConigList() {
    return this.textConigList;
  }

  public String getRel() {
    return this.rel;
  }

  /**
   * The reference id in the json file.
   */
  public String getId() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }

  @SuppressWarnings("unused") public String getDirName() {
    return dirName;
  }

  /**
   * Returns the bitmap that has been stored for this image asset if one was explicitly set.
   */
  @Nullable public Bitmap getBitmap() {
    return bitmap;
  }

  /**
   * Permanently sets the bitmap on this LottieImageAsset. This will:
   * 1) Overwrite any existing Bitmaps.
   * 2) Apply to *all* animations that use this LottieComposition.
   *
   * If you only want to replace the bitmap for this animation, use dynamic properties
   * with {@link LottieProperty#IMAGE}.
   */
  public void setBitmap(@Nullable Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  /**
   * Returns whether this asset has an embedded Bitmap or whether the fileName is a base64 encoded bitmap.
   */
  public boolean hasBitmap() {
    return bitmap != null || (fileName.startsWith("data:") && fileName.indexOf("base64,") > 0);
  }

  public static class TextConfig {
    public int location; // 文本的起始位置，负数表示从后面开始数，比如-1，就是倒数第一个开始，包含，字符截取是左闭右开
    public int textLen; // 正数表示字符的个数；0非法，直接过滤该配置；负数表示截止位置，-1，倒数第一个字符的位置，不包含
    public String textColor;
    public String bgColor;
    public int textSize;
    public int bs; // 本段文本的从下到上的偏移量，可以理解为paddingBottom或者marginBottom
  }
}
