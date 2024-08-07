package com.czf.lottiedemo.lottie.animation.content;

import android.graphics.Path;

import com.czf.lottiedemo.lottie.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CompoundTrimPathContent {
  private final List<TrimPathContent> contents = new ArrayList<>();

  void addTrimPath(TrimPathContent trimPath) {
    contents.add(trimPath);
  }

  public void apply(Path path) {
    for (int i = contents.size() - 1; i >= 0; i--) {
      Utils.applyTrimPathIfNeeded(path, contents.get(i));
    }
  }
}
