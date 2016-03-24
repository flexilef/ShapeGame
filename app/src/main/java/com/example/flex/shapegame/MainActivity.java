package com.example.flex.shapegame;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        //get max screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        screenWidth = displaySize.x;
        screenHeight = displaySize.y;

        //setup the surface to draw
        mImageView = (ImageView) findViewById(R.id.imageView_main);
        mImageView.setBackgroundColor(Color.BLACK);

        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //Start game logics
        final Random rand = new Random();

        Button rectButton = (Button) findViewById(R.id.button_rect);
        rectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float length = rand.nextFloat()*(screenWidth/2 - screenWidth/4) + screenWidth/4;
                float width = rand.nextFloat()*(screenWidth/2 - screenWidth/4) + screenWidth/4;
                float xCoord = rand.nextFloat()*screenWidth;
                float yCoord = rand.nextFloat()*screenHeight;

                int alpha = rand.nextInt(256);
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);

                Rectangle rect = new Rectangle(v.getContext(), xCoord, yCoord, length, width,
                                                Color.argb(alpha, red, green, blue));
                rect.onDraw(mCanvas);

                mImageView.setImageBitmap(mBitmap);
            }
        });

        Button circButton = (Button) findViewById(R.id.button_circ);
        circButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float radius = rand.nextFloat()*(screenWidth/4-screenWidth/8) + screenWidth/8;
                float xCoord = rand.nextFloat()*screenWidth;
                float yCoord = rand.nextFloat()*screenHeight;
                //colors
                int alpha = rand.nextInt(256);
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);

                Circle circ = new Circle(v.getContext(), xCoord, yCoord, radius,
                                        Color.argb(alpha, red, green, blue));
                circ.onDraw(mCanvas);

                mImageView.setImageBitmap(mBitmap);
            }
        });


        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvas.drawColor(Color.BLACK);

                mImageView.setImageBitmap(mBitmap);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void adjustShapeAlpha() {

    }

    public void updateShapeCount() {

    }
}
