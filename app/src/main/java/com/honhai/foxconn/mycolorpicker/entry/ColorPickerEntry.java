package com.honhai.foxconn.mycolorpicker.entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ColorPickerEntry extends View {

    private int color;
    private Paint edge;

    public ColorPickerEntry(Context context, AttributeSet attrs) {
        super(context, attrs);

        edge = new Paint();
        edge.setColor(Color.rgb(0,170,0));
        edge.setStyle(Paint.Style.STROKE);
        edge.setStrokeWidth(8);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth()-1,getHeight()-1,edge);
    }
}
