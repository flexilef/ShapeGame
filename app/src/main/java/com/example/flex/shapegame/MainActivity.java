package com.example.flex.shapegame;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public enum EventProbability {
        DEFAULT(0),
        CIRCLE(80),
        RECTANGLE(20);

        private final int weight;
        EventProbability(int probability) {
            this.weight = probability;
        }

        public int getWeight() {
            return weight;
        }
    }
    private final ShapeFactory shapeFactory = new ShapeFactory();

    //drawing surface info
    private ImageView mImageView;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    //screen info
    private int screenWidth;
    private int screenHeight;

    //Game Objects
    private ArrayList<Shape> shapes;
    private CountDownTimer gameTimer;
    private CountDownTimer shapeTimer;
    private TextView mTextViewTime;
    private TextView mTextViewTotalPoints;
    private TextView mTextViewLevelPoints;
    private TextView mTextViewCircleCount;
    private TextView mTextViewRectCount;

    //game info
    private int totalPoints;
    private int currentLevelPoints;
    private int advanceLevelPoints;
    private int currentLevel;
    private long levelTime;
    private boolean gameOver;

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

        //Start game logic
        totalPoints = 0;
        currentLevel = 1;
        currentLevelPoints = 0;
        advanceLevelPoints = 100;
        levelTime = 30000;
        gameOver = false;

        mTextViewTime = (TextView) findViewById(R.id.textView_time);
        mTextViewTotalPoints = (TextView) findViewById(R.id.textView_totalPoints);
        mTextViewLevelPoints = (TextView) findViewById(R.id.textView_levelPoints);
        mTextViewRectCount = (TextView) findViewById(R.id.textView_rectCount);
        mTextViewCircleCount = (TextView) findViewById(R.id.textView_circCount);

        gameTimer = new CountDownTimer(levelTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextViewTime.setText("00:" + millisUntilFinished/1000);

                Random rand = new Random();
                final int randomTime = rand.nextInt(2000);
                final int randomTick = rand.nextInt(1000);

                shapeTimer = new CountDownTimer(randomTime, randomTick) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTextViewCircleCount.setText("randomTime:" + randomTime);
                        mTextViewRectCount.setText("randomTick:" + randomTick);
                    }

                    @Override
                    public void onFinish() {

                        EventProbability event = selectShapeEvent();

                        if (event == EventProbability.DEFAULT) {

                            Shape circle = shapeFactory.getShape(getApplicationContext(), Shape.ShapeType.CIRCLE);

                            circle.onDraw(mCanvas);
                            mImageView.setImageBitmap(mBitmap);
                        } else if (event == EventProbability.CIRCLE) {
                            Shape circle = shapeFactory.getShape(getApplicationContext(), Shape.ShapeType.CIRCLE);

                            circle.onDraw(mCanvas);
                            mImageView.setImageBitmap(mBitmap);
                        } else if (event == EventProbability.RECTANGLE) {
                            Shape rect = shapeFactory.getShape(getApplicationContext(), Shape.ShapeType.RECTANGLE);

                            rect.onDraw(mCanvas);
                            mImageView.setImageBitmap(mBitmap);
                        }
                    }
                }.start();
            }

            @Override
            public void onFinish() {
                mTextViewTime.setText("00:00");
                gameOver = true;
            }
        };

        gameTimer.start();

        //handle buttons here
        Button rectButton = (Button) findViewById(R.id.button_rect);
        rectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shape rect = shapeFactory.getShape(v.getContext(), Shape.ShapeType.RECTANGLE);

                rect.onDraw(mCanvas);

                mImageView.setImageBitmap(mBitmap);
            }
        });

        Button circButton = (Button) findViewById(R.id.button_circ);
        circButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shape circ = shapeFactory.getShape(v.getContext(), Shape.ShapeType.CIRCLE);

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

    public EventProbability selectShapeEvent() {

        int sum = 100; //sum of weights of events
        int total = 0;

        Random rand = new Random();
        int randNum = rand.nextInt(sum);

        for(EventProbability e : EventProbability.values()) {

            total = total + e.getWeight();
            if(total > randNum) {
                return e;
            }
        }

        return EventProbability.DEFAULT;
    }
}
