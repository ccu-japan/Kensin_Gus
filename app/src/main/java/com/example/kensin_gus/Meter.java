package com.example.kensin_gus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.example.db_library.Kenshin_DB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class Meter {
    private Kenshin_DB kenshin_db;
    Cursor c1;
    Cursor c2;
    Cursor c3;


    //-------------------------------------------------//
    //                                                 //
    //                      メータ変数                  //
    //                                                 //
    //-------------------------------------------------//
    public String TODAY;
    public String LAST_TIME_DAY;
    public String LAST_TIME_USE;
    public String TODAY_POINT;
    public String LAST_POINT;
    public String USE_COMP;
    public int STANDARD_PRICE;
    public int GUS_PRICE;
    public int TAX;
    public String CUSTOMER;
    public String PLACE_CODE;
    public String C_NAME;
    public String P_NAME;
    public int RECTANGLE;
    public String[] CHECK_BOX;


    public Meter Meter_List(int COL_BAN, Context context) {

        kenshin_db = new Kenshin_DB(context);
        //        i*0        i*1         i*2         i*3         i*4         i*5     i*6
        c1 = kenshin_db.db.rawQuery("SELECT T_T_kensin, L_T_kensin , L_T_usage , L_T_pointer , T_T_pointer , S_price , G_price , G_C_tax  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        c1.moveToFirst();

        c2 = kenshin_db.db.rawQuery("SELECT customer,place,P_name,C_name1,C_name2  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        c2.moveToFirst();

        c3 = kenshin_db.db.rawQuery("SELECT result1 ,result2 ,result3 ,result4 ,result5 ,result6 , result7," +
                "result8 ,result9 , result10 , result11 , result12 FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        c3.moveToFirst();

        return Meter.this;
    }


    public String TODAY() {                  // 今回検針日
        TODAY = c1.getString(0);
        return TODAY;
    }

    public String LAST_TIME_DAY() {         // 前回検針日
        LAST_TIME_DAY = c1.getString(1);
        return LAST_TIME_DAY;
    }

    public String LAST_TIME_USE() {         // 前回使用量  　
        LAST_TIME_USE = String.valueOf(c1.getDouble(2));
        return LAST_TIME_USE;
    }

    public String LAST_POINT() {              //
        LAST_POINT = String.valueOf(c1.getDouble(3));
        return LAST_POINT;
    }

    public String TODAY_POINT() {            //
        TODAY_POINT = String.valueOf(c1.getDouble(4));
        return TODAY_POINT;
    }

    public String USE_COMP() {               // 使用量の比較

        Double val = Double.valueOf(TODAY_POINT) - Double.valueOf(LAST_POINT);

        BigDecimal bd = new BigDecimal(val);

        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);   //少数第2位　四捨五入

        USE_COMP = String.valueOf(bd);

        return USE_COMP;
    }

    public int STANDARD_PRICE() {        // 基本料金
        STANDARD_PRICE = c1.getInt(5);
        return STANDARD_PRICE;
    }

    public int GUS_PRICE() {            // ガス料金
        GUS_PRICE = c1.getInt(6);
        return GUS_PRICE;
    }

    public int TAX() {                  // 消費税
        TAX = c1.getInt(7);
        return TAX;
    }

    public String CUSTOMER() {
        CUSTOMER = c2.getString(0);
        return CUSTOMER;
    }

    public String PLACE_CODE() {
        PLACE_CODE = "0" + c2.getInt(1);
        return PLACE_CODE;
    }

    public String C_NAME() {
        C_NAME = c2.getString(3) + c2.getString(4);
        return C_NAME.trim();
    }

    public String P_NAME() {
        P_NAME = c2.getString(2);
        return P_NAME;
    }


    @SuppressLint("NewApi")
    public int RECTANGLE() {
        RECTANGLE = 0;
        for (int i = 0; i < c3.getColumnCount(); i++) {
            if (c3.getInt(i) == 1) {
                RECTANGLE++;           //項目チェック数
            }
        }                                                               // 「4」は文字の縦幅
        Log.d("Result", String.valueOf(RECTANGLE));
        return RECTANGLE;
    }

    public String[] CHECK_BOX(Context context) {
        ArrayList<CheckBox> arrayList;
        arrayList = new CheckActivity().Array_Check_Box(context);
        CHECK_BOX = new String[RECTANGLE()];
        int j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (c3.getInt(i) == 1) {
                CHECK_BOX[j] = arrayList.get(i).getText().toString();
                Log.d("Result",CHECK_BOX[j]);
                j++;
            }
        }
        return CHECK_BOX;
    }
}