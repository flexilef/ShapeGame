package com.example.flex.shapegame;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by Flex on 3/23/2016.
 */
public abstract class Shape extends View {

    protected float xCenter;
    protected float yCenter;
    protected int color;

    public enum ShapeType {
        CIRCLE, RECTANGLE
    }

    public Shape(Context context) {
        super(context);

        xCenter = 0;
        yCenter = 0;
        color = Color.argb(255, 0, 0, 0);
    }

    @TargetApi(11)
    public void setShapeAlpha(float alpha) {
        setAlpha(alpha);
    }

    @TargetApi(11)
    public float getShapeAlpha() {
        return getAlpha();
    }

    public void removeShape() {
        setVisibility(GONE);
    }

    public abstract ShapeType getShapeType();
    public abstract void onDraw(Canvas canvas);
}
