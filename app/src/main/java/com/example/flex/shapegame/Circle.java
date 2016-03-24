package com.example.flex.shapegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Flex on 3/23/2016.
 */
public class Circle extends Shape {

    private float radius;

    public Circle(Context context) {
        super(context);

        radius = 100;
    }

    public Circle(Context context, float x, float y, float radius, int color) {
        super(context);

        this.xCenter = x;
        this.yCenter = y;
        this.radius = radius;
        this.color = color;
    }

    public Shape.ShapeType getShapeType() {

        return Shape.ShapeType.CIRCLE;
    }

    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawCircle(xCenter, yCenter, radius, paint);
    }
}
