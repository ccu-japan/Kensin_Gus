package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

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
    // 使用量金額の計算
    //
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    static void Calc_HYOF_PRICE (float row , SQLiteDatabase db , int COL_BAN , MainActivity main) {
        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";
        Pattern p = Pattern.compile(regex);
        int price = 0;
        int TAX = 0;
        String minutes;
        TextView Row3_Text = main.findViewById(R.id.Row3_Text);
        TextView Row3_Text2 = main.findViewById(R.id.Row3_Text2);


        int usage_integer_minutes_C = (int) Math.floor(row);
        String s = String.valueOf(row);
        Matcher matcher = (Matcher) p.matcher(s);

        if (matcher.matches()) {
            int row1 = (int) (row % 1.0 * 10);
            minutes = String.valueOf(row1);
        } else {
            minutes = "0";
        }

        Cursor cursor = db.rawQuery("SELECT /*i:0*/ usage_integer_minutes ,/*i:1*/c_point_0 ,/*i:2*/c_point_1 ,/*i:3*/c_point_2 ,/*i:4*/c_point_3 ,/*i:5*/c_point_4 , " +
                "/*i:6*/c_point_5 , /*i:7*/c_point_6 , /*i:8*/c_point_7 ,/*i:9*/ c_point_8 ,/*i:10*/ c_point_9  FROM HYOF WHERE usage_integer_minutes = " + usage_integer_minutes_C, null);
        cursor.moveToFirst();

        Cursor cursor1 = db.rawQuery("SELECT/*i:0*/G_T_rate ,/*i:1*/S_price ,/*i:2*/M_C_price ,/*i:3*/A_price ,/*i:4*/G_C_tax ,/*i:5*/B_amount FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor1.moveToFirst();

        if (row <= 50.1 && row >= 0.0) {

            switch (minutes) {
                case "0":
                    price = Integer.parseInt(cursor.getString(1));
                    break;
                case "1":
                    price = Integer.parseInt(cursor.getString(2));
                    break;
                case "2":
                    price = Integer.parseInt(cursor.getString(3));
                    break;
                case "3":
                    price = Integer.parseInt(cursor.getString(4));
                    break;
                case "4":
                    price = Integer.parseInt(cursor.getString(5));
                    break;
                case "5":
                    price = Integer.parseInt(cursor.getString(6));
                    break;
                case "6":
                    price = Integer.parseInt(cursor.getString(7));
                    break;
                case "7":
                    price = Integer.parseInt(cursor.getString(8));
                    break;
                case "8":
                    price = Integer.parseInt(cursor.getString(9));
                    break;
                case "9":
                    price = Integer.parseInt(cursor.getString(10));
                    break;
            }
        }
        price = (int) (price + Float.parseFloat(cursor1.getString(1)) + Float.parseFloat(cursor1.getString(2)) + Float.parseFloat(cursor1.getString(3)));
        TAX = (int) (price * Float.parseFloat(cursor1.getString(0)) / 100);


        Row3_Text.setText(String.valueOf(price + TAX));
        Row3_Text2.setText(String.valueOf(TAX));

    }
}
