package com.example.flex.shapegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public enum EventProbability {
        NONE(95),
        CIRCLE(4),
        RECTANGLE(1);

        private int weight;
        EventProbability(int probability) {
            this.weight = probability;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
    private final ShapeFactory shapeFactory = new ShapeFactory();

    //drawing surface info
    private ImageView mImageView;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    boolean isOnDeathAnimation = false;
    private int deathAnimationAlpha = 127;

    //Screen info
    private int screenWidth;
    private int screenHeight;

    //Game objects
    private ArrayList<Shape> shapes;
    private CountDownTimer gameTimer;
    private TextView mTextViewTime;
    private TextView mTextViewTotalPoints;
    private TextView mTextViewLevelPoints;
    private TextView mTextViewCircleCount;
    private TextView mTextViewRectCount;
    private Button rectButton;
    private Button circButton;
    private Button clearButton;

    //Game info
    private boolean gameOver;
    private int totalPoints;
    private int currentLevelPoints;
    private int advanceLevelPoints; //not used yet
    private int currentLevel; //not used yet
    private long levelTime;
    private long levelTimeLeft;
    private int currentCircleCount;
    private int currentRectCount;
    private int totalCircleCount;
    private int totalRectCount;
    private int circlesPopped;
    private int rectsPopped;
    private int circlePoints;
    private int rectPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        //setup screen info
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        screenWidth = displaySize.x;
        screenHeight = displaySize.y;

        //setup the drawing surface
        mImageView = (ImageView) findViewById(R.id.imageView_main);
        mImageView.setBackgroundColor(Color.BLACK);

        View.OnTouchListener imageViewOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if(v == mImageView) {
                    if(action == MotionEvent.ACTION_DOWN) {

                        float touchedX = event.getX();
                        float touchedY = event.getY();

                        for(Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext();) {

                            Shape s = iterator.next();

                            float x = s.getCoords().x;
                            float y = s.getCoords().y;

                            if(s.getShapeType() == Shape.ShapeType.CIRCLE) {
                                    Circle circle = (Circle) s;

                                    if (Math.sqrt((touchedX - x) * (touchedX - x) + (touchedY - y) * (touchedY - y))
                                            <= circle.getRadius())
                                    {
                                        s.removeShape();
                                        iterator.remove();

                                        circlesPopped++;
                                        totalPoints += circlePoints;
                                        currentLevelPoints += circlePoints;

                                        //set this to run the animation in render game
                                        isOnDeathAnimation = true;
                                    }
                            }
                            else if(s.getShapeType() == Shape.ShapeType.RECTANGLE) {

                                Rectangle rect = (Rectangle) s;

                                if (Math.abs(touchedX - x) <= rect.getRectLength() / 2 &&
                                        Math.abs(touchedY - y) <= rect.getRectWidth() / 2) {
                                    s.removeShape();
                                    iterator.remove();

                                    rectsPopped++;
                                    totalPoints += rectPoints;
                                    currentLevelPoints += rectPoints;

                                    isOnDeathAnimation = true;
                                }
                            }
                        }
                    }
                }

                return false;
            }
        };
        mImageView.setOnTouchListener(imageViewOnTouchListener);
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //initialize game info
        gameOver = false;
        totalPoints = 0;
        currentLevelPoints = 0;
        advanceLevelPoints = 100; //not used yet
        currentLevel = 1; //not used yet
        levelTime = 30000;
        levelTimeLeft = levelTime;
        currentCircleCount = 0;
        totalCircleCount = 0;
        currentRectCount = 0;
        totalRectCount = 0;
        circlesPopped = 0;
        rectsPopped = 0;
        circlePoints = 1;
        rectPoints = 4;

        //initialize game ui objects
        shapes = new ArrayList<Shape>();

        mTextViewTime = (TextView) findViewById(R.id.textView_time);
        mTextViewTime.setTextColor(Color.argb(255, 164, 192, 39));

        mTextViewTotalPoints = (TextView) findViewById(R.id.textView_totalPoints);
        mTextViewTotalPoints.setTextColor(Color.argb(255, 164, 192, 39));

        mTextViewLevelPoints = (TextView) findViewById(R.id.textView_levelPoints);
        mTextViewLevelPoints.setTextColor(Color.argb(255, 164, 192, 39));

        mTextViewRectCount = (TextView) findViewById(R.id.textView_rectCount);
        mTextViewRectCount.setTextColor(Color.argb(255, 164, 192, 39));

        mTextViewCircleCount = (TextView) findViewById(R.id.textView_circCount);
        mTextViewCircleCount.setTextColor(Color.argb(255, 164, 192, 39));

        clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.getBackground().setColorFilter(0xffffC639, PorterDuff.Mode.MULTIPLY);

        rectButton = (Button) findViewById(R.id.button_rect);
        rectButton.getBackground().setColorFilter(0xffA4C639, PorterDuff.Mode.MULTIPLY);

        circButton = (Button) findViewById(R.id.button_circ);
        circButton.getBackground().setColorFilter(0xffA4C639, PorterDuff.Mode.MULTIPLY);

        if(!gameOver) {
            resetTextViews();
            startGame();
            handleButtons();
        }
    }

    public void startGame() {

        gameTimer = new CountDownTimer(levelTime, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

                //update Timer
                levelTimeLeft = millisUntilFinished;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(levelTimeLeft);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(levelTimeLeft) -
                                TimeUnit.MINUTES.toSeconds(minutes);
                long milliSeconds = levelTimeLeft - TimeUnit.MINUTES.toMillis(minutes) -
                                    TimeUnit.SECONDS.toMillis(seconds);

                if(levelTimeLeft < 3000) {
                    mTextViewTime.setTextColor(Color.argb(255, 255, 0, 39));
                }
                else if(levelTimeLeft < levelTime/2) {
                    mTextViewTime.setTextColor(Color.argb(255, 255, 192, 39));
                }

                mTextViewTime.setText(String.format("%02d:%02d", seconds, milliSeconds / 10));

                updateGame();
                renderGame();
            }

            @Override
            public void onFinish() {

                gameOver = true;
                resetTextViews();

                Paint paint = new Paint();
                paint.setColor(Color.argb( 255, 164, 198, 57 ));
                paint.setTextSize(48.0f);

                float lineSize = paint.getTextSize();
                int lineMultiplier = 0;

                mCanvas.drawColor(Color.BLACK);

                paint.setColor(Color.argb(255, 255, 198, 57));
                mCanvas.drawText("GAME OVER!", screenWidth / 4, screenHeight / 4 + (lineSize * lineMultiplier), paint);
                lineMultiplier++;
                lineMultiplier++;

                paint.setColor(Color.argb( 255, 164, 198, 57 ));
                mCanvas.drawText("Circles Popped- " + circlesPopped,
                        screenWidth / 12, screenHeight / 4 + (lineSize * lineMultiplier), paint);
                lineMultiplier++;

                mCanvas.drawText("Rectangles Popped- " + rectsPopped,
                        screenWidth/12, screenHeight/4+(lineSize*lineMultiplier), paint);
                lineMultiplier++;

                mCanvas.drawText("Total Circles - " + totalCircleCount,
                        screenWidth/12, screenHeight/4+(lineSize*lineMultiplier), paint);
                lineMultiplier++;

                mCanvas.drawText("Total Rectangles - " + totalRectCount,
                        screenWidth/12, screenHeight/4+(lineSize*lineMultiplier), paint);
                lineMultiplier++;

                paint.setColor(Color.argb(255, 255, 198, 57));
                mCanvas.drawText("Pop % - " + (float)(circlesPopped+rectsPopped)/(totalCircleCount+totalRectCount)*100 + "%",
                        screenWidth/12, screenHeight/4+(lineSize*lineMultiplier), paint);
                lineMultiplier++;

                mCanvas.drawText("Total Points - " + totalPoints,
                        screenWidth/12, screenHeight/4+(lineSize*lineMultiplier), paint);
                lineMultiplier++;
            }
        };

        gameTimer.start();
    }

    public void handleButtons() {

        //Spawns several rectangles
        rectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < 5; i++ ) {

                    Shape rect = shapeFactory.getShape(v.getContext(), Shape.ShapeType.RECTANGLE);
                    totalRectCount++;
                    shapes.add(rect);
                }
            }
        });

        //Spawns several circles
        circButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < 5; i++ ) {

                    Shape circ = shapeFactory.getShape(v.getContext(), Shape.ShapeType.CIRCLE);
                    totalCircleCount++;
                    shapes.add(circ);
                }
            }
        });

        //clear button pops all shapes
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext(); ) {

                    Shape s = iterator.next();

                    if (s.getShapeType() == Shape.ShapeType.CIRCLE) {

                        currentLevelPoints += circlePoints;
                        totalPoints += circlePoints;
                        circlesPopped++;
                    } else if (s.getShapeType() == Shape.ShapeType.RECTANGLE) {

                        currentLevelPoints += rectPoints;
                        totalPoints += rectPoints;
                        rectsPopped++;
                    }

                    s.removeShape();
                    iterator.remove();
                }

                isOnDeathAnimation = true;
            }
        });
    }

    public void renderGame() {

        if(isOnDeathAnimation) {
            runDeathAnimation();
        }
        else
        //clear canvas
        {
            mCanvas.drawColor(Color.BLACK);
        }

        //draw game screen
        for(Shape s : shapes) {

            s.onDraw(mCanvas);
        }
        mImageView.setImageBitmap(mBitmap);
    }

    public void updateGame() {

        //update textviews
        mTextViewTotalPoints.setText("Tot Pts: " + totalPoints);
        mTextViewLevelPoints.setText("Lvl Pts: " + currentLevelPoints);
        mTextViewCircleCount.setText("Circ: " + currentCircleCount);
        mTextViewRectCount.setText("Rec: " + currentRectCount);

        //spawn shapes
        EventProbability event = selectShapeEvent();
        if (event == EventProbability.CIRCLE) {

            Shape circle = shapeFactory.getShape(getApplicationContext(), Shape.ShapeType.CIRCLE);
            shapes.add(circle);

            totalCircleCount++;
        }
        else if (event == EventProbability.RECTANGLE) {

            Shape rect = shapeFactory.getShape(getApplicationContext(), Shape.ShapeType.RECTANGLE);
            shapes.add(rect);

            totalRectCount++;
        }

        updateShapeCount();
        adjustShapeAlpha();
    }

    public void adjustShapeAlpha() {

        for(Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext();) {

            Shape shape = iterator.next();

            int alpha = shape.getColorAlpha();
            float alphaDecrease = .90f;
            int threshold = 50;

            alpha = (int) (alpha * alphaDecrease);
            if (alpha < threshold) {

                shape.removeShape();
                iterator.remove();
            }
            else {
                shape.setColorAlpha(alpha);
            }
        }
    }

    public void updateShapeCount() {

        currentCircleCount = 0;
        currentRectCount = 0;

        for(Shape shape : shapes) {

            if(shape.getShapeType() == Shape.ShapeType.CIRCLE) {
                currentCircleCount++;
            }
            else if(shape.getShapeType() == Shape.ShapeType.RECTANGLE) {
                currentRectCount++;
            }
        }
    }

    public void runDeathAnimation() {

        float alphaDecrease = .75f;
        int threshold = 50;

        deathAnimationAlpha = (int) (deathAnimationAlpha * alphaDecrease);
        if(deathAnimationAlpha < threshold) {

            isOnDeathAnimation = false;
            deathAnimationAlpha = 127; //reset alpha
        }

        mCanvas.drawColor(Color.argb(deathAnimationAlpha, 164, 192, 39));
        mImageView.setImageBitmap(mBitmap);
    }

    public void resetTextViews() {

        mTextViewTime.setText("00:00");
        mTextViewRectCount.setText("Rect: 0");
        mTextViewCircleCount.setText("Circ: 0");
        mTextViewTotalPoints.setText("Tot Pts: 0");
        mTextViewLevelPoints.setText("Lvl Pts: 0");
    }

    public EventProbability selectShapeEvent() {

        Random rand = new Random();

        int sumOfWeights = 0;
        int randWeight = 0;
        int total = 0;

        //Get sum of weights of events
        for(EventProbability e : EventProbability.values()) {

            sumOfWeights += e.getWeight();
        }

        randWeight = rand.nextInt(sumOfWeights);
        //Get a random event (based on its weight)
        for(EventProbability e : EventProbability.values()) {

            total = total + e.getWeight();
            if(total > randWeight) {
                return e;
            }
        }

        return EventProbability.NONE;
    }
}
