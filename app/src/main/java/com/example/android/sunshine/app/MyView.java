package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by marcinolek on 19.12.2016.
 */

public class MyView extends View {
    public MyView(Context context) {
        
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int DefaultStyle) {
        super(context, attrs, DefaultStyle);
    }

    private String[] validDirections = {"N", "S", "W", "E", "NW", "NE", "SW", "SE"};

    private String mDirection = "N";

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new mListener());

    public void setDirection(String direction) {
        List<String> list = Arrays.asList(validDirections);
        if(list.contains(direction)) {
            this.mDirection = direction;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int myHeight;
        if (hSpecMode == MeasureSpec.EXACTLY) {
            myHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            myHeight = 100;
        } else {
            myHeight = 100;
        }

        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myWidth;
        if (wSpecMode == MeasureSpec.EXACTLY) {
            myWidth = wSpecSize;
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            myWidth = 100;
        } else {
            myWidth = 100;
        }

        setMeasuredDimension(myWidth, myHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    static final float ARROW_LENGTH = 30.0f;

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.getText().add("Wind direction is " +  this.mDirection);
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        this.setBackgroundColor(color);
        return super.onTouchEvent(event);
    }

    // TODO: Replace with degrees
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(5f);
        this.setContentDescription("Wind direction is " +  this.mDirection);
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getContext().getSystemService(
                        Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }

        switch(this.mDirection) {
            case "N":
                canvas.drawLine(w/2, h, w/2, 0, p);
                canvas.drawLine(w/2, 0, w/2 - ARROW_LENGTH,0 + ARROW_LENGTH, p);
                canvas.drawLine(w/2, 0, w/2 + ARROW_LENGTH,0 + ARROW_LENGTH, p);

                break;
            case "S":
                canvas.drawLine(w/2, 0, w/2, h,  p);
                canvas.drawLine(w/2, h, w/2 - ARROW_LENGTH,h - ARROW_LENGTH, p);
                canvas.drawLine(w/2, h, w/2 + ARROW_LENGTH,h - ARROW_LENGTH, p);
                break;
            case "E":
                canvas.drawLine(0, h/2, w, h/2,  p);
                canvas.drawLine(w, h/2, w - ARROW_LENGTH,h/2 - ARROW_LENGTH, p);
                canvas.drawLine(w, h/2, w - ARROW_LENGTH,h/2 + ARROW_LENGTH, p);
                break;
            case "W":
                canvas.drawLine(w, h/2, 0, h/2,  p);
                canvas.drawLine(0, h/2, 0 + ARROW_LENGTH,h/2 - ARROW_LENGTH, p);
                canvas.drawLine(0, h/2, 0 + ARROW_LENGTH,h/2 + ARROW_LENGTH, p);
                break;
            case "NE":
                canvas.drawLine(0, h, w, 0, p);
                canvas.drawLine(w, 0, w - ARROW_LENGTH, 0, p);
                canvas.drawLine(w, 0, w, ARROW_LENGTH, p);
                break;
            case "NW":
                canvas.drawLine(w, w, 0, 0, p);
                canvas.drawLine(0, 0, 0, ARROW_LENGTH, p);
                canvas.drawLine(0, 0, ARROW_LENGTH, 0, p);
                break;
            case "SW":
                canvas.drawLine(w, 0, 0, h, p);
                canvas.drawLine(0, h, 0, h - ARROW_LENGTH, p);
                canvas.drawLine(0, h, ARROW_LENGTH, h, p);
                break;
            case "SE":
                canvas.drawLine(0, 0, w, w, p);
                canvas.drawLine(w, w, w, h - ARROW_LENGTH, p);
                canvas.drawLine(w, w, w - ARROW_LENGTH, w, p);
                break;
            default:
                break;
        }

    }

}

class mListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("Fling from " + e1 + " to " + e2);
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}