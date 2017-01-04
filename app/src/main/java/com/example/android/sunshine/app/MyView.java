package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

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

    private float mDirection;

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new mListener());

    public void setDirection(float degrees) {
        this.mDirection = degrees;
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

    static final float ARROW_LENGTH = 60.0f;
    static final float ARROW_HEAD_LENGTH = ARROW_LENGTH / 2f;
    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.getText().add("Wind direction is " + this.mDirection);
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        this.setBackgroundColor(color);*/
        return super.onTouchEvent(event);
    }

    // TODO: Replace with degrees
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        p.setStrokeWidth(3f);
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.GRAY);
        this.setContentDescription("Wind direction is " + this.mDirection);
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getContext().getSystemService(
                        Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }

        double radians = Math.toRadians(mDirection);
        Point origin = new Point(w/2, h/2);
        double endX = origin.x + ARROW_LENGTH * Math.cos(radians - Math.PI/2);
        double endY = origin.y + ARROW_LENGTH * Math.sin(radians - Math.PI/2);
        double startX = origin.x + ARROW_LENGTH * Math.cos(radians+Math.PI/2);
        double startY = origin.y + ARROW_LENGTH * Math.sin(radians+Math.PI/2);
        canvas.drawCircle(origin.x, origin.y, ARROW_LENGTH, circlePaint);
        canvas.drawLine((float)startX, (float)startY, (float)endX, (float)endY, p);
        canvas.drawLine((float)endX, (float)endY,(float) (endX + Math.cos(radians + Math.PI/2 + Math.PI/6) * ARROW_HEAD_LENGTH), (float)(endY + Math.sin(radians + Math.PI/2 + Math.PI/6) * ARROW_HEAD_LENGTH), p);
        canvas.drawLine((float)endX, (float)endY,(float) (endX + Math.cos(radians + Math.PI/2 - Math.PI/6) * ARROW_HEAD_LENGTH), (float)(endY + Math.sin(radians + Math.PI/2 - Math.PI/6) * ARROW_HEAD_LENGTH), p);

    }

}

class mListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("Fling from " + e1 + " to " + e2);
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}