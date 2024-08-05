package com.czf.lottiedemo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.czf.lottiedemo.lottie.LottieAnimationView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addLottieView();
    }

    private void addLottieView() {
        FrameLayout flContainer = findViewById(R.id.fl_container);

        LottieAnimationView lottieAnimationView = new LottieAnimationView(this);
        lottieAnimationView.setClickable(true);
        lottieAnimationView.setBackgroundColor(Color.GRAY);
        lottieAnimationView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        lottieAnimationView.setAnimation("lottiefile.zip");
        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (lottieAnimationView.getFrame() == 200) {
                    lottieAnimationView.pauseAnimation();
                }
            }
        });
        lottieAnimationView.setLottieClicklistener(new LottieAnimationView.LottieClickListener() {
            @Override
            public void onLottieClicked(String elName) {
                Log.d("MainActivity", "lottie clicked, elName: " + elName);
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1080, 1500);
        params.gravity = Gravity.CENTER;
        flContainer.addView(lottieAnimationView, params);
    }
}