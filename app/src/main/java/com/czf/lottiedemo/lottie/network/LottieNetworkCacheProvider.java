package com.czf.lottiedemo.lottie.network;


import android.support.annotation.NonNull;

import com.czf.lottiedemo.lottie.Lottie;

import java.io.File;

/**
 * Interface for providing the custom cache directory where animations downloaded via url are saved.
 *
 * @see Lottie#initialize
 */
public interface LottieNetworkCacheProvider {

    /**
     * Called during cache operations
     *
     * @return cache directory
     */
    @NonNull
    File getCacheDir();
}