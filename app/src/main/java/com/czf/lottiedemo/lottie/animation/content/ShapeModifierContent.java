package com.czf.lottiedemo.lottie.animation.content;

import com.czf.lottiedemo.lottie.model.content.ShapeData;

public interface ShapeModifierContent extends Content {
  ShapeData modifyShape(ShapeData shapeData);
}
