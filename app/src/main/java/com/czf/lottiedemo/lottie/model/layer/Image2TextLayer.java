package com.czf.lottiedemo.lottie.model.layer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czf.lottiedemo.lottie.LottieDrawable;
import com.czf.lottiedemo.lottie.LottieImageAsset;
import com.czf.lottiedemo.lottie.TextDelegate;
import com.czf.lottiedemo.lottie.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Image2TextLayer extends ImageLayer {

    private LinearLayout linearLayout;

    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);// 防止在绘制函数中大量创建对象

    private final List<String> texts = new ArrayList<>(); // 防止在绘制函数中大量创建对象
    private final List<TextView> textViews = new ArrayList<>();// 防止在绘制函数中大量创建对象

    private String updateText; // 外界设置的文本，例如倒计时更新，优先级比el表达式高

    public Image2TextLayer(LottieDrawable lottieDrawable, Layer layerModel, Context context) {
        super(lottieDrawable, layerModel);
        if (lottieImageAsset != null) {
            List<LottieImageAsset.TextConfig> configs = lottieImageAsset.getTextConigList();
            if (configs != null && configs.size() > 0) {
                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                LinearLayout inner = new LinearLayout(context);
                inner.setOrientation(LinearLayout.HORIZONTAL);
                inner.setGravity(Gravity.BOTTOM);

                linearLayout.addView(inner);

                List<String> texts = getTexts();
                for (int i = 0; i < configs.size(); i++) {
                    LottieImageAsset.TextConfig config = configs.get(i);
                    TextView tv = new TextView(context);
                    String text = (texts != null && i < texts.size()) ? texts.get(i) : "";
                    setupTextView(tv, config, text);
                    if (config.bs != 0) {
                        params.bottomMargin = (int)(config.bs * Utils.dpScale());
                        inner.addView(tv, params);
                    } else {
                        inner.addView(tv);
                    }
                }
                float density = Utils.dpScale();
                layoutView(linearLayout, (int)(lottieImageAsset.getWidth() * density),
                        (int)(lottieImageAsset.getHeight() * density));
            }
        }
    }

    private void setupTextView(TextView tv, LottieImageAsset.TextConfig config, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        } else {
            tv.setText("");
        }
        if (!TextUtils.isEmpty(config.textColor)) {
            tv.setTextColor(Color.parseColor(config.textColor));
        }
        if (!TextUtils.isEmpty(config.bgColor)) {
            tv.setBackgroundColor(Color.parseColor(config.bgColor));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        tv.setTextSize(config.textSize);
    }

    @Override
    public void drawLayer(Canvas canvas, Matrix parentMatrix, int parentAlpha) {
        if (linearLayout != null) {
            canvas.save();
            canvas.concat(parentMatrix);

            composeTexts();

            linearLayout.draw(canvas);
            canvas.restore();
        } else {
            super.drawLayer(canvas, parentMatrix, parentAlpha);
        }
    }

    private void composeTexts() {
        if (lottieImageAsset != null) {
            List<LottieImageAsset.TextConfig> configs = lottieImageAsset.getTextConigList();
            if (configs != null && configs.size() > 0) {
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                if (linearLayout.getChildCount() < 1) {
                    return; // 理论上不会发生
                }
                LinearLayout inner = (LinearLayout) linearLayout.getChildAt(0);
                inner.setOrientation(LinearLayout.HORIZONTAL);
                inner.setGravity(Gravity.BOTTOM); // 默认下对齐，可通过下发的bs参数控制
                linearLayout.removeAllViews();

                if (inner.getChildCount() != configs.size()) {
                    return; // 理论上不会发生
                }

                List<String> texts = getTexts();
                textViews.clear();
                for (int i = 0; i < configs.size(); i++) {
                    LottieImageAsset.TextConfig config = configs.get(i);
                    View v = inner.getChildAt(i);
                    TextView tv = (TextView) v;
                    textViews.add(tv);
                    String text = (texts != null && i < texts.size()) ? texts.get(i) : "";
                    setupTextView(tv, config, text);
                }
                inner.removeAllViews();
                for (int i = 0; i < configs.size(); i++) {
                    LottieImageAsset.TextConfig config = configs.get(i);
                    TextView tv = textViews.get(i);
                    if (config.bs != 0) {
                        params.bottomMargin = (int)(config.bs * Utils.dpScale());
                        inner.addView(tv, params);
                    } else {
                        inner.addView(tv);
                    }
                }
                linearLayout.addView(inner);
                float density = Utils.dpScale();
                layoutView(linearLayout, (int)(lottieImageAsset.getWidth() * density),
                        (int)(lottieImageAsset.getHeight() * density));
            }
        }
    }

    // 富文本形式的话，文本是分段的，每一段有不同的配置，比如字体不一样大，或颜色不同
    private List<String> getTexts() {
        if (lottieImageAsset == null || lottieDrawable == null) {
            return null;
        }

        TextDelegate textDelegate = lottieDrawable.getTextDelegate();
        if (textDelegate == null) {
            return null;
        }

        String rel = lottieImageAsset.getRel();
        if (TextUtils.isEmpty(rel) && TextUtils.isEmpty(updateText)) {
            return null;
        }

        List<LottieImageAsset.TextConfig> configs = lottieImageAsset.getTextConigList();
        if (configs != null) {
            String text = updateText; // 优先级更高，为空表示外界没有更新过，则从delegate取
            if (TextUtils.isEmpty(text)) {
                text = textDelegate.getText(rel);
            }
            if (!TextUtils.isEmpty(text)) {
                texts.clear();
                for (int i = 0; i < configs.size(); i++) {
                    LottieImageAsset.TextConfig config = configs.get(i);
                    int start = config.location; // 字符起始位置，负数表示从后开始数，-1倒数第一个，-2倒数第二个
                    int len = config.textLen; // 正数表示字符的个数，负数表示截止位置，-1表示倒数第一个字符位置
                    if (len == 0) { // 长度为0是非法的，理论上不会下发。但不下发也会解析为0，因此这里渲染整个内容
                        texts.add(text);
                    } else {
                        if (start < 0) {
                            start = start + text.length(); // 如abcd，-1，-1+4=3，从3开始(含3)，即d开始
                        }
                        int end;
                        if (len < 0) {
                            end = text.length() + len; // len表示截止位置，如abcd，-1，-1+4=3，表示截止位置是3(不含3)
                        } else {
                            end = start + len; // len表示个数
                        }
                        if (end > text.length()) {
                            end = text.length(); // end最大为length
                        }
                        if (start >= 0 && start < text.length() && end > start) {
                            String selectedText = text.substring(start, end);
                            texts.add(selectedText);
                        } else {
                            texts.add("");
                        }
                    }
                }
                return texts;
            }
        }
        return null;
    }

    private static void layoutView(View view, int width, int height) {
        view.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void updateText(String text) {
        updateText = text;
    }

}
