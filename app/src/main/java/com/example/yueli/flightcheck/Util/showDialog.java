package com.example.yueli.flightcheck.Util;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yueli on 2018/7/7.
 */

public  class showDialog {
    public static void showDateDiaglog(Context context,DatePickerDialog.OnDateSetListener dateSetListener){
        // 获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        // 获取当前的年
        int year = calendar.get(calendar.YEAR);
        // 获取当前的月
        int month = calendar.get(calendar.MONTH);
        // 获取当前月的第几天
        int day = calendar.get(calendar.DAY_OF_MONTH);
        // 获取当前周的第几天
        //    int day = calendar.get(calendar.DAY_OF_WEEK);
        // 获取当前年的第几天
        //    int day = calendar.get(calendar.DAY_OF_YEAR);
        // 参数1：上下文    参数2：年    参数3：月    参数4：：日(ps:参数2、3、4是默认时间,月是从0开始的)
        DatePickerDialog dialog = new DatePickerDialog(context,dateSetListener,year, month, day);
        dialog.show();
    }
}
