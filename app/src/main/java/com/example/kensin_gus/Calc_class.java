package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc_class {

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //
    // ***********  Calc_Used ************
    // 今月使用量の計算
    //
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    static String Calc_Used(float row , SQLiteDatabase db,int COL_BAN){
        Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        c.moveToFirst();

        float num = row - Float.parseFloat(c.getString(4));
        @SuppressLint("DefaultLocale")String amount_to_use = (String.format("%.1f", num));

        return amount_to_use;
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //
    // ***********  Calc_HYOF ************
    // 今月使用量の計算
    //
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    static String Calc_HYOF_PRICE (float row , SQLiteDatabase db){
        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";
        Pattern p = Pattern.compile(regex);
        String price = null;
        String minutes = null;

        if(row <= 50.1) {
            int usage_integer_minutes_C = (int) Math.floor(row);
            String s = String.valueOf(row);
            Matcher matcher = (Matcher) p.matcher(s);

            if(matcher.matches()) {
                minutes = String.format("%.f",(row % 1.0 * 10));
            }
            else{
                minutes = "0";

            }

            Log.d("Calc", "整数部：" + usage_integer_minutes_C);
            Log.d("Calc", "少数部：" + minutes);

            Cursor cursor = db.rawQuery("SELECT /*i:0*/ usage_integer_minutes ,/*i:1*/c_point_0 ,/*i:2*/c_point_1 ,/*i:3*/c_point_2 ,/*i:4*/c_point_3 ,/*i:5*/c_point_4 , " +
                    "/*i:6*/c_point_5 , /*i:7*/c_point_6 , /*i:8*/c_point_7 ,/*i:9*/ c_point_8 ,/*i:10*/ c_point_9 FROM HYOF WHERE usage_integer_minutes = " + usage_integer_minutes_C,null );
            cursor.moveToFirst();
            switch (minutes) {
                case "0":
                    price = cursor.getString(1);
                    break;
                case "1":
                    price = cursor.getString(2);
                    break;
                case "2":
                    price = cursor.getString(3);
                    break;
                case "3":
                    price = cursor.getString(4);
                    break;
                case  "4":
                    price = cursor.getString(5);
                    break;
                case  "5":
                    price = cursor.getString(6);
                    break;
                case  "6":
                    price = cursor.getString(7);
                    break;
                case  "7":
                    price = cursor.getString(8);
                    break;
                case  "8":
                    price = cursor.getString(9);
                    break;
                case  "9":
                    price = cursor.getString(10);
                    break;
            }

        }else{
            price = "0";

        }
        return price ;
    }

}
