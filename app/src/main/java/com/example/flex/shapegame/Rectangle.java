package com.example.flex.shapegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by Flex on 3/23/2016.
 */
public class Rectangle extends Shape {

    private float length;
    private float width;

    public Rectangle(Context context) {
        super(context);

        length = 200;
        width = 200;
    }

    public Rectangle(Context context, float x, float y,
                     float length, float width, int color) {
        super(context);

        this.xCenter = x;
        this.yCenter = y;
        this.length = length;
        this.width = width;
        this.color = color;
    }

    public ShapeType getShapeType() {

        return ShapeType.RECTANGLE;
    }

    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(color);

        float topLeftX = xCenter - length/2;
        float topLeftY = yCenter - width/2;
        float botRightX = xCenter + length/2;
        float botRightY = yCenter + width/2;

        canvas.drawRect(topLeftX, topLeftY, botRightX, botRightY, paint);
    }
}
