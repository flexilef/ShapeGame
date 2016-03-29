package com.example.flex.shapegame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        //offsets account for layout heights
        int offsetTop = screenHeight/10;
        int offsetBot = screenHeight/10;

        Random rand = new Random();
        switch(shape) {
            case CIRCLE: {

                float maxRadius = screenWidth/4;
                float minRadius = screenWidth/8;

                //prepare values to initialize shape
                float radius = rand.nextFloat()*(maxRadius-minRadius) + minRadius;

                //make coordinates stay inbounds
                float xCoord = rand.nextFloat()*(screenWidth-radius) + radius;
                float yCoord = rand.nextFloat()*(screenHeight-radius) + radius;
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

                //make it dependent on the screen width only because width < height
                float maxSize = screenWidth/2;
                float minSize = screenWidth/4;

                //prepare values to initialize shape
                float length = rand.nextFloat()*(maxSize - minSize) + minSize;
                float width = rand.nextFloat()*(maxSize - minSize) + minSize;

                float xCoord = rand.nextFloat()*(screenWidth-width) + width;
                float yCoord = rand.nextFloat()*((screenHeight-offsetBot-length)-(length+offsetTop)) +
                        length+offsetTop;
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
