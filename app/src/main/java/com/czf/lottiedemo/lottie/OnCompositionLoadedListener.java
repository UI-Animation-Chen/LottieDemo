package com.czf.lottiedemo.lottie;

import android.support.annotation.Nullable;

/**
 * @see LottieCompositionFactory
 * @see LottieResult
 */
@Deprecated
public interface OnCompositionLoadedListener {
  /**
   * Composition will be null if there was an error loading it. Check logcat for more details.
   */
  void onCompositionLoaded(@Nullable LottieComposition composition);
}
