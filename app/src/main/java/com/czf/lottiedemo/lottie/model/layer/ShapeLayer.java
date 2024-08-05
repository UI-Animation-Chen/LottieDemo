package com.czf.lottiedemo.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.animation.content.Content;
import com.czf.lottiedemo.lottie.animation.content.ContentGroup;
import com.czf.lottiedemo.lottie.model.KeyPath;
import com.czf.lottiedemo.lottie.model.content.BlurEffect;
import com.czf.lottiedemo.lottie.model.content.ShapeGroup;
import com.czf.lottiedemo.lottie.parser.DropShadowEffect;

import java.util.Collections;
import java.util.List;

public class ShapeLayer extends BaseLayer {
    private final ContentGroup contentGroup;
    private final CompositionLayer compositionLayer;

    ShapeLayer(LottieDrawable lottieDrawable, Layer layerModel, CompositionLayer compositionLayer, LottieComposition composition) {
        super(lottieDrawable, layerModel);
        this.compositionLayer = compositionLayer;

        // Naming this __container allows it to be ignored in KeyPath matching.
        ShapeGroup shapeGroup = new ShapeGroup("__container", layerModel.getShapes(), false);
        contentGroup = new ContentGroup(lottieDrawable, this, shapeGroup, composition);
        contentGroup.setContents(Collections.<Content>emptyList(), Collections.<Content>emptyList());
    }

    @Override
    void drawLayer(@NonNull Canvas canvas, Matrix parentMatrix, int parentAlpha) {
        contentGroup.draw(canvas, parentMatrix, parentAlpha);
    }

    @Override
    public void getBounds(RectF outBounds, Matrix parentMatrix, boolean applyParents) {
        super.getBounds(outBounds, parentMatrix, applyParents);
        contentGroup.getBounds(outBounds, boundsMatrix, applyParents);
    }

    @Nullable
    @Override
    public BlurEffect getBlurEffect() {
        BlurEffect layerBlur = super.getBlurEffect();
        if (layerBlur != null) {
            return layerBlur;
        }
        return compositionLayer.getBlurEffect();
    }

    @Nullable
    @Override
    public DropShadowEffect getDropShadowEffect() {
        DropShadowEffect layerDropShadow = super.getDropShadowEffect();
        if (layerDropShadow != null) {
            return layerDropShadow;
        }
        return compositionLayer.getDropShadowEffect();
    }

    @Override
    protected void resolveChildKeyPath(KeyPath keyPath, int depth, List<KeyPath> accumulator,
                                       KeyPath currentPartialKeyPath) {
        contentGroup.resolveKeyPath(keyPath, depth, accumulator, currentPartialKeyPath);
    }
}
