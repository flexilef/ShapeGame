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

    public Rectangle(Context context, Coordinates coords,
                     float length, float width, int color) {
        super(context);

        //coords.x and coords.y represent the center of the rectangle
        this.coords.x = coords.x;
        this.coords.y = coords.y;

        this.length = length;
        this.width = width;
        this.color = color;
    }

    public void setLength(float length) {

        this.length = length;
    }

    public float getRectLength() {

        return length;
    }

    public void setWidth(float width) {

        this.width = width;
    }

    public float getRectWidth() {

        return width;
    }

    //params is expected to be two elements: the first is the length, and
    //the second is the width
    public void setSize(float[] params) {

        if(params != null && params.length == 2) {
            //this.length = params[0];
            //this.width = params[1];
            setLength(params[0]);
            setWidth(params[1]);
        }
        else {
            throw new RuntimeException("Error: Failed to set Rectangle's size... Argument list is not 2 elements");
        }
    }

    public ShapeType getShapeType() {

        return ShapeType.RECTANGLE;
    }

    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(color);

        float topLeftX = coords.x - length/2;
        float topLeftY = coords.y - width/2;
        float botRightX = coords.x + length/2;
        float botRightY = coords.y + width/2;

        canvas.drawRect(topLeftX, topLeftY, botRightX, botRightY, paint);
    }
}
