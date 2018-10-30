package com.honhai.foxconn.mycolorpicker.display;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;

public class DisplayView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Paint textPaint = new Paint();
    private int backgroundColor;
    private float adjY;
    private float x;
    private String text = "";
    private ValueAnimator animator = new ValueAnimator();

    private Resources resources = getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;

    private boolean mIsDrawing;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;

    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);

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
        });
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    public void setText(String text) {
        if (text == null)
            text = "preview";
        this.text = text;
        animator.cancel();
        float l = textPaint.measureText(text);
        animator.setFloatValues(screenWidth+20, -l-20);
        animator.setDuration((long) (l + screenWidth)*2/3);
        animator.start();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing){
            drawSomething();
        }
    }

    private void drawSomething() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas == null) return;
            mCanvas.drawColor(backgroundColor);
            mCanvas.drawText(text, x, getHeight()/2 + adjY, textPaint);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCanvas != null){
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}
