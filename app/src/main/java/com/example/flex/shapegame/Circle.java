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

    public Circle(Context context, Coordinates coords, float radius, int color) {
        super(context);

        //coords.x and coords.y represent the center of the circle
        this.coords.x = coords.x;
        this.coords.y = coords.y;

        this.radius = radius;
        this.color = color;
    }

    public void setRadius(float radius) {

        this.radius = radius;
    }

    public float getRadius() {

        return radius;
    }

    public void setSize(float params[]) {

        if(params != null && params.length == 1) {
            //this.radius = params[0];
            setRadius(params[0]);
        }
        else {
            throw new RuntimeException("Error: Failed to set Circle's size... Argument list is not 1 element");
        }
    }

    public Shape.ShapeType getShapeType() {

        return Shape.ShapeType.CIRCLE;
    }

    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawCircle(coords.x, coords.y, radius, paint);
    }
}
