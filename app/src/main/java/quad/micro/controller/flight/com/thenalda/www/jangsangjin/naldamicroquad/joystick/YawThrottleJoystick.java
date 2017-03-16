package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.joystick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * Created by jangsangjin on 2017. 2. 17..
 */

public class YawThrottleJoystick extends View implements Runnable{

    private final double RAD = 57.2957795;
    public final static long DEFAULT_LOOP_INTERVAL = 100; // 100 ms
    public final static int FRONT = 3;
    public final static int FRONT_RIGHT = 4;
    public final static int RIGHT = 5;
    public final static int RIGHT_BOTTOM = 6;
    public final static int BOTTOM = 7;
    public final static int BOTTOM_LEFT = 8;
    public final static int LEFT = 1;
    public final static int LEFT_FRONT = 2;
    // Variables
    private RollPitchJoystick.OnJoystickMoveListener onJoystickMoveListener; // Listener
    private Thread thread = new Thread(this);
    private long loopInterval = DEFAULT_LOOP_INTERVAL;
    private int xPosition = 0; // Touch x position
    private int yPosition = 0; // Touch y position
    private double centerX = 0; // Center view x position
    private double centerY = 0; // Center view y position
    private Paint mainCircle;
    private Paint secondaryCircle;
    private Paint button;
    private Paint horizontalLine;
    private Paint verticalLine;
    private int joystickRadius;
    private int buttonRadius;
    private int lastAngle = 0;
    private int lastPower = 0;

    private Bitmap controlBitmap;
    private Bitmap leftImage;
    private Bitmap rightImage;
    private Bitmap frontImage;
    private Bitmap backImage;

    public YawThrottleJoystick(Context context) {
        super(context);
    }

    public YawThrottleJoystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public YawThrottleJoystick(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initJoystickView();
    }

    protected void initJoystickView() {

        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircle.setColor(Color.parseColor("#805D5D5D"));
        mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryCircle = new Paint();
        secondaryCircle.setColor(Color.parseColor("#dadbdc"));
        secondaryCircle.setStrokeWidth(5.0f);
        secondaryCircle.setStyle(Paint.Style.STROKE);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.parseColor("#F6F6F6"));
        button.setStyle(Paint.Style.FILL);

        this.controlBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_radio_button_checked_white_24dp);
        this.leftImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rotate_left_white_24dp);
        this.rightImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rotate_right_white_24dp);
        this.frontImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_throttle_up_white_24dp);
        this.backImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_throttle_down_white_24dp);

    }

    @Override
    protected void onFinishInflate() {

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // before measure, get the center of view
        xPosition = (int) getWidth() / 2;
        yPosition = (int) getWidth() / 2;
        int d = Math.min(xNew, yNew);
        buttonRadius = (int) (d / 2 * 0.25);
        joystickRadius = (int) (d / 2 * 0.55);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting the measured values to resize the view to a certain width and
        // height
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

        setMeasuredDimension(d, d);

    }

    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        centerX = (getWidth()) / 2;
        centerY = (getHeight()) / 2;

        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius + buttonRadius, mainCircle);

        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius + buttonRadius, secondaryCircle);

        float controlBitmapWidth = controlBitmap.getWidth();

        float controlBitmapHeight = controlBitmap.getHeight();

        canvas.drawBitmap(controlBitmap, xPosition - controlBitmapWidth / 2, yPosition - controlBitmapHeight / 2, null);
        canvas.drawBitmap(leftImage, (int) centerX - joystickRadius - leftImage.getWidth() / 2, (int) centerY - leftImage.getHeight() / 2, null);
        canvas.drawBitmap(rightImage, (int) centerX + joystickRadius - rightImage.getWidth() / 2, (int) centerY - rightImage.getHeight() / 2, null);
        canvas.drawBitmap(frontImage, (int) centerX - frontImage.getWidth() / 2, (int) centerY - joystickRadius - frontImage.getHeight() / 2, null);
        canvas.drawBitmap(backImage, (int) centerX - backImage.getWidth() / 2, (int) centerY + joystickRadius - backImage.getHeight() / 2, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xPosition = (int) event.getX();
        yPosition = (int) event.getY();
        double abs = Math.sqrt((xPosition - centerX) * (xPosition - centerX)
                + (yPosition - centerY) * (yPosition - centerY));
        if (abs > joystickRadius) {
            xPosition = (int) ((xPosition - centerX) * joystickRadius / abs + centerX);
            yPosition = (int) ((yPosition - centerY) * joystickRadius / abs + centerY);
        }
        invalidate();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            xPosition = (int) centerX;
            yPosition = (int) centerY;
            thread.interrupt();
            if (onJoystickMoveListener != null)
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(),
                        getDirection());
        }
        if (onJoystickMoveListener != null
                && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
            thread = new Thread(this);
            thread.start();
            if (onJoystickMoveListener != null)
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(),
                        getDirection());
        }
        return true;
    }

    private int getAngle() {
        if (xPosition > centerX) {
            if (yPosition < centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX))
                        * RAD + 90);
            } else if (yPosition > centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX)) * RAD) + 90;
            } else {
                return lastAngle = 90;
            }
        } else if (xPosition < centerX) {
            if (yPosition < centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX))
                        * RAD - 90);
            } else if (yPosition > centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX)) * RAD) - 90;
            } else {
                return lastAngle = -90;
            }
        } else {
            if (yPosition <= centerY) {
                return lastAngle = 0;
            } else {
                if (lastAngle < 0) {
                    return lastAngle = -180;
                } else {
                    return lastAngle = 180;
                }
            }
        }
    }

    private int getPower() {
        return (int) (100 * Math.sqrt((xPosition - centerX)
                * (xPosition - centerX) + (yPosition - centerY)
                * (yPosition - centerY)) / joystickRadius);
    }

    private int getDirection() {
        if (lastPower == 0 && lastAngle == 0) {
            return 0;
        }
        int a = 0;
        if (lastAngle <= 0) {
            a = (lastAngle * -1) + 90;
        } else if (lastAngle > 0) {
            if (lastAngle <= 90) {
                a = 90 - lastAngle;
            } else {
                a = 360 - (lastAngle - 90);
            }
        }

        int direction = (int) (((a + 22) / 45) + 1);

        if (direction > 8) {
            direction = 1;
        }
        return direction;
    }

    public void setOnJoystickMoveListener(RollPitchJoystick.OnJoystickMoveListener listener,
                                          long repeatInterval) {
        this.onJoystickMoveListener = listener;
        this.loopInterval = repeatInterval;
    }

    public interface OnJoystickMoveListener {
        public void onValueChanged(int angle, int power, int direction);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                public void run() {
                    if (onJoystickMoveListener != null)
                        onJoystickMoveListener.onValueChanged(getAngle(),
                                getPower(), getDirection());
                }
            });

            try {

                Thread.sleep(loopInterval);

            } catch (InterruptedException e) {

                break;

            }
        }
    }

}