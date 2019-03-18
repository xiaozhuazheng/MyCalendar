package com.azheng.mycalendar;

import android.content.Context;
import java.util.Calendar;

public class CalendarTool {
    private static CalendarTool mInStance = null;
    private Context mContext;
    private Calendar mCalendar;

    public static CalendarTool getInStance(Context context,Calendar calendar){
        if (mInStance == null){
            synchronized (CalendarTool.class){
                if (mInStance == null){
                    mInStance = new CalendarTool(context,calendar);
                }
            }
        }
        return  mInStance;
    }

    private CalendarTool(Context context, Calendar calendar) {
        this.mContext = context;
        this.mCalendar = calendar;
    }

    /*
    * 设置日期
    */
    protected void setCalendar(Calendar calendar){
        this.mCalendar = calendar;
    }

    public int getCurYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    public int getCurMonth(){
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    public int getCurDay(){
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurWeek(){
        return mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断是当月第一天为星期几
     * @param year
     * @return
     */
    public int getWeekOfFirstDay(int year,int month) {
        mCalendar.set(year, month - 1, 1);
        return mCalendar.get(Calendar.DAY_OF_WEEK) - 1;//从星期天开始，因此需要减一
    }

    /**
     * 判断是否为闰年
     * @param year
     * @return
     */
    public boolean isLeap(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前月有多少天
     * @param year,month
     * @return
     */
    public int getDayOfMonth(int year,int month) {
        int days = 30;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (isLeap(year)){
                    days = 29;
                } else {
                    days = 28;
                }
        }
        return days;
    }
}
