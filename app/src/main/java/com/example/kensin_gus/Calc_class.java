package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
    @SuppressLint("DefaultLocale")
    static void Calc_HYOF_PRICE (float row , SQLiteDatabase db , int COL_BAN , MainActivity main) {
        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";
        Pattern p = Pattern.compile(regex);
        String minutes;
        String Row3_text;
        String Row3_text2;
        TextView Row3_Text = main.findViewById(R.id.Row3_Text);
        TextView Row3_Text2 = main.findViewById(R.id.Row3_Text2);
        TextView Row2_Text = main.findViewById(R.id.Row2_Text);
        float price = 0;
        double TAX = 0;
        float Row2 = Float.parseFloat(Row2_Text.getText().toString());



        int usage_integer_minutes_C = (int) Math.floor(row);
        String s = String.valueOf(row);
        Matcher matcher = (Matcher) p.matcher(s);

        if (matcher.matches()) {
            int row1 = (int) (row % 1.0 * 10);
            minutes = String.valueOf(row1);
        } else {
            minutes = "0";
        }

        Cursor cursor_HYOF = db.rawQuery("SELECT /*i:0*/ usage_integer_minutes ,/*i:1*/c_point_0 ,/*i:2*/c_point_1 ,/*i:3*/c_point_2 ,/*i:4*/c_point_3 ,/*i:5*/c_point_4 , " +
                "/*i:6*/c_point_5 , /*i:7*/c_point_6 , /*i:8*/c_point_7 ,/*i:9*/ c_point_8 ,/*i:10*/ c_point_9  FROM HYOF WHERE usage_integer_minutes = " + usage_integer_minutes_C, null);
        cursor_HYOF.moveToFirst();

        //--------------------------------------------------------------------------------------------
        // ***** Cursor TOKUIF rawQuery  ******
        //
        // i=0. G_T_rate , i=1. S_price ,   i=2. M_C_price , i=3. A_price , i=4. G_C_tax
        // i=5. B_amount , i=6. P_section , i=7. U_price ,   i=8. S_usage , i=9 T_C_table
        //
        //--------------------------------------------------------------------------------------------
        Cursor cursor_TOKUIF = db.rawQuery("SELECT G_T_rate , S_price , M_C_price , A_price , G_C_tax , B_amount , P_section , U_price ,S_usage , T_C_table FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor_TOKUIF.moveToFirst();

        //--------------------------------------------------------------------------------------------
        //
        // *** ガス料金区分ｺｰﾄﾞ(P_section)ごとの使用金額の計算タスク
        //
        //--------------------------------------------------------------------------------------------
        switch (cursor_TOKUIF.getString(6)) {
            case "A":
                if (row <= 50.1 && row >= 0.0) {
                    switch (minutes) {
                        case "0":
                            price = Integer.parseInt(cursor_HYOF.getString(1));
                            break;
                        case "1":
                            price = Integer.parseInt(cursor_HYOF.getString(2));
                            break;
                        case "2":
                            price = Integer.parseInt(cursor_HYOF.getString(3));
                            break;
                        case "3":
                            price = Integer.parseInt(cursor_HYOF.getString(4));
                            break;
                        case "4":
                            price = Integer.parseInt(cursor_HYOF.getString(5));
                            break;
                        case "5":
                            price = Integer.parseInt(cursor_HYOF.getString(6));
                            break;
                        case "6":
                            price = Integer.parseInt(cursor_HYOF.getString(7));
                            break;
                        case "7":
                            price = Integer.parseInt(cursor_HYOF.getString(8));
                            break;
                        case "8":
                            price = Integer.parseInt(cursor_HYOF.getString(9));
                            break;
                        case "9":
                            price = Integer.parseInt(cursor_HYOF.getString(10));
                            break;
                    }
                }
                break;

            case "B":
                if (Row2 >= 0) {
                    price = (int) (Row2 * Integer.parseInt(cursor_TOKUIF.getString(7)) / Float.parseFloat(cursor_TOKUIF.getString(8)));
                    Log.d("Math", Row2 + " * " + cursor_TOKUIF.getString(7) + " / " + cursor_TOKUIF.getString(8) + " = " + price);
                }
                break;
        }

        price = price + Float.parseFloat(cursor_TOKUIF.getString(1)) + Float.parseFloat(cursor_TOKUIF.getString(2)) + Float.parseFloat(cursor_TOKUIF.getString(3));
        TAX = price * Float.parseFloat(cursor_TOKUIF.getString(0)) / 100;


        Log.d("Math", "Price : " + price);
        Log.d("Math", "TAX : " + TAX);

        Log.d("Math","cursor_TOKUIF.getString(9) : " + cursor_TOKUIF.getString(9));

        switch (cursor_TOKUIF.getString(9)){
            case "0" :
                TAX = (int)Math.round(TAX);
                break;

            case "1" :
                TAX = (int)Math.floor(TAX);
                break;

            case "2":
                TAX = (int)Math.ceil(TAX);
                break;
        }


        Row3_text  = String.format("%,d", (int)(price + TAX));
        Row3_text2 = String.format("%,d", (int)TAX);


        Row3_Text.setText(Row3_text);
        Row3_Text2.setText(Row3_text2);

    }
}
