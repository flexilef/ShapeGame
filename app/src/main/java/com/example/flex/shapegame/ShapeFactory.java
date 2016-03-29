package com.example.flex.shapegame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.Random;

/**
 * Created by Flex on 3/23/2016.
 */
public class ShapeFactory {

    public Shape getShape(Context context, Shape.ShapeType shape) {

        if(shape == null) {
            return null;
        }

        //get max screen size
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point displaySize = new Point();
        display.getSize(displaySize);
        int screenWidth = displaySize.x;
        int screenHeight = displaySize.y;

        Random rand = new Random();

        switch(shape) {
            case CIRCLE: {

                //prepare values to initialize shape
                float radius = rand.nextFloat()*(screenWidth/4-screenWidth/8) + screenWidth/8;

                float xCoord = rand.nextFloat()*screenWidth;
                float yCoord = rand.nextFloat()*screenHeight;
                Coordinates coords = new Coordinates(xCoord, yCoord);

                //colors
                int alpha = 255;//rand.nextInt(256);
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);
                int color = Color.argb(alpha, red, green, blue);

                return new Circle(context, coords, radius, color);
            }
            case RECTANGLE: {

                //prepare values to initialize shape
                float length = rand.nextFloat()*(screenWidth/2 - screenWidth/4) + screenWidth/4;
                float width = rand.nextFloat()*(screenWidth/2 - screenWidth/4) + screenWidth/4;

                float xCoord = rand.nextFloat()*screenWidth;
                float yCoord = rand.nextFloat()*screenHeight;
                Coordinates coords = new Coordinates(xCoord, yCoord);

                int alpha = 255;//rand.nextInt(256);
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);
                int color = Color.argb(alpha, red, green , blue);

                return new Rectangle(context, coords, length, width, color);
            }
        }

        return null;
    }
}
