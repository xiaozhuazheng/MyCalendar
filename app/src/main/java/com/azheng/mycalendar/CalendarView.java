package com.azheng.mycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class CalendarView extends View {
    private int mMonthTextSize;
    private int mMonthColor;
    private int mWeekTextSize;
    private int mWeekColor;
    private int mDayTextSize;
    private int mDayColor;

    private int mUnitith;
    private Paint mPaint;
    private int mUnitHeight;
    private String mMonthString;

    private CalendarTool mTool = null;
    private Context mContext;

    private Calendar mCalendar;
    private int mCurYear;
    private int mCurMonth;
    private int mCurDay;
    private int mWeekOfFirstDay;
    private int mDaysOfMonth;

    public CalendarView(Context context) {
        this(context,null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context,attrs);
        initData();
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CalendarView);
        mMonthColor = array.getColor(R.styleable.CalendarView_monthtextcolor,0);
        mMonthTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_weektextsize,0);
        mWeekColor = array.getColor(R.styleable.CalendarView_weektextcolor,0);
        mWeekTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_weektextsize,0);
        mDayColor = array.getColor(R.styleable.CalendarView_daytextcolor,0);
        mDayTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_daytextsize,0);
    }

    private void initData() {
        mCalendar = Calendar.getInstance();
        mTool = CalendarTool.getInStance(mContext,mCalendar);
        mPaint = new Paint();

        mCurYear = mTool.getCurYear();
        mCurMonth = mTool.getCurMonth();
        mCurDay = mTool.getCurDay();
        mDaysOfMonth = mTool.getDayOfMonth(mCurYear,mCurMonth);
        mWeekOfFirstDay = mTool.getWeekOfFirstDay(mCurYear,mCurMonth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mUnitith = width / 7;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mUnitHeight = height / 10;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),mUnitHeight * 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMonth(canvas);
        drawWeek(canvas);
        drawDay(canvas);
    }

    private void drawMonth(Canvas canvas){
        mMonthString = "<     " + mCurYear + "/" + mCurMonth + "     >";
        mPaint.setTextSize(mMonthTextSize);
        mPaint.setColor(mMonthColor);
        canvas.drawText(mMonthString, (getWidth() - mPaint.measureText(mMonthString))/2, mUnitHeight,mPaint);
    }

    private void drawWeek(Canvas canvas) {
        String[] weeks = {"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};
        mPaint.setTextSize(mWeekTextSize);
        mPaint.setColor(mWeekColor);
        for (int i = 0; i < weeks.length; i++){
            int x = (int) (i * mUnitith + (mUnitith - mPaint.measureText(weeks[i])) / 2);
            canvas.drawText(weeks[i],x,mUnitHeight * 2,mPaint);
        }
    }

    private void drawDay(Canvas canvas){
        mPaint.setColor(mDayColor);
        mPaint.setTextSize(mDayTextSize);
        int firstlineCount = 7 - mWeekOfFirstDay;//第一行的天数
        int day = 1;//当前画的天
        for (int i = 1;i < 7 ; i++){
            int textlenght = (int) (mUnitith - mPaint.measureText(String.valueOf(i))) / 2;
            int textHeight = (int) (mPaint.getFontMetrics().descent- mPaint.getFontMetrics().ascent) / 3;
            if (i == 1){
                for (int j = 1;j < firstlineCount + 1;j++){
                    day = j;
                    if (day == mCurDay){
                        drawCicle(canvas,(mWeekOfFirstDay + j -1) * mUnitith + textlenght +  (int) mPaint.measureText(String.valueOf(day)) / 2,
                                3 * mUnitHeight - textHeight);
                    }
                    canvas.drawText(String.valueOf(j),(mWeekOfFirstDay + j -1) * mUnitith + textlenght,3 * mUnitHeight,mPaint);
                }
            } else {
                for (int k = 1; k < 8; k ++){
                    day = firstlineCount + k + (i - 2) * 7;
                    if (day == mCurDay){
                        drawCicle(canvas,(k - 1) * mUnitith + textlenght + (int) mPaint.measureText(String.valueOf(day)) / 2,(i + 2) * mUnitHeight - textHeight);
                    }
                    if (day < mDaysOfMonth + 1){
                        canvas.drawText(String.valueOf(day), (k - 1) * mUnitith + textlenght,(i + 2) * mUnitHeight, mPaint);
                    }
                }
            }
        }
    }

    private void drawCicle(Canvas canvas,int x ,int y) {
        if (mCurMonth == Calendar.getInstance().get(Calendar.MONTH) + 1  &&
                mCurYear == Calendar.getInstance().get(Calendar.YEAR)){
            mPaint.setColor(Color.RED);
        } else {
            mPaint.setColor(Color.GREEN);
        }
        canvas.drawCircle(x,y,mUnitHeight/3,mPaint);
        mPaint.setColor(mDayColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (y < mUnitHeight && event.getAction() == MotionEvent.ACTION_DOWN){
            if ( x > (getWidth() - mPaint.measureText(mMonthString)) / 2  && x < (getWidth() - mPaint.measureText(mMonthString)) / 2 + mPaint.measureText("<")){
                changeMonth(false);
            } else if ( x > ((getWidth() - mPaint.measureText(mMonthString)) / 2 +  mPaint.measureText(mMonthString) - mPaint.measureText(">")) &&
                    x < (getWidth() - mPaint.measureText(mMonthString)) / 2 +  mPaint.measureText(mMonthString)){
                changeMonth(true);
            }
        }
        return true;
    }

    private void changeMonth(boolean b) {
        if (b){
            //增加月
            mCurMonth +=1;
            if (mCurMonth > 12){
                mCurYear +=1;
                mCurMonth = 1;
            }
        } else {
            mCurMonth -=1;
            if (mCurMonth < 1){
                mCurYear -=1;
                mCurMonth = 12;
            }
        }
        mCalendar.set(mCurYear,mCurMonth,mCurDay);
        mTool.setCalendar(mCalendar);

        mCurDay = mTool.getCurDay();
        mWeekOfFirstDay = mTool.getWeekOfFirstDay(mCurYear,mCurMonth);
        mDaysOfMonth = mTool.getDayOfMonth(mCurYear,mCurMonth);
        invalidate();
    }
}
