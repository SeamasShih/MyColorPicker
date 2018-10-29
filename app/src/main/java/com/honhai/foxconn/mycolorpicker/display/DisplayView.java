package com.honhai.foxconn.mycolorpicker.display;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class DisplayView extends View {

    private Paint textPaint = new Paint();
    private float adjY;
    private float x;
    private String text = "";
    private ValueAnimator animator = new ValueAnimator();

    private Resources resources = getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;

    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(screenHeight);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        adjY = (metrics.descent - metrics.ascent) / 2 - metrics.descent;

        float l = textPaint.measureText(text);
        animator.setFloatValues(screenWidth, -l);
        animator.setDuration((long) (l + screenWidth));
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            x = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    public void setText(String text) {
        if (text == null)
            text = "preview";
        this.text = text;
        animator.cancel();
        float l = textPaint.measureText(text);
        animator.setFloatValues(screenWidth, -l);
        animator.setDuration((long) (l + screenWidth));
        animator.start();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, x, getHeight()/2 + adjY, textPaint);
    }
}
