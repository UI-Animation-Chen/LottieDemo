package com.czf.lottiedemo.lottie.model.content;


import android.support.annotation.Nullable;

import com.czf.lottiedemo.lottie.LottieComposition;
import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.animation.content.Content;
import com.czf.lottiedemo.lottie.model.layer.BaseLayer;

public interface ContentModel {
  @Nullable
  Content toContent(LottieDrawable drawable, LottieComposition composition, BaseLayer layer);
}
