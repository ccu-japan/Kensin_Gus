package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc_class {
    //----------------------------------------------------------------------
    //
    //  使用量の計算メソッド
    //
    //----------------------------------------------------------------------
    static String Calc_Used(float row, SQLiteDatabase db, int COL_BAN) {
                                                                            //i=0.顧客名1 , i=1 顧客名2 , i=2 得意先コード , i=3 設置場所名称 , i=4 前回検針指針
                                                                            //i=5 今回検針指針 , i=6 会社コード , i=7 設置場所コード , i=8 今回使用量
        if(row != 0.0)
        {
            Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ," +
                "/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
    
            c.moveToFirst();
    
            float num = row - Float.parseFloat(c.getString(4));
            @SuppressLint("DefaultLocale")
            String amount_to_use = (String.format("%.1f", num));
            
            return amount_to_use;
        }
        
        return String.valueOf((int)row);
    }

    //----------------------------------------------------------------------
    //
    //  使用量金額の計算メソッド
    //
    //----------------------------------------------------------------------
    @SuppressLint("DefaultLocale")
    static void Calc_HYOF_PRICE(float row, SQLiteDatabase db, int COL_BAN, KensinMainActivity main) {

        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";                       //正規表現で整数と少数を判別する
        Pattern p = Pattern.compile(regex);
        String minutes;                                                     //使用量の小数部　(0~9 金額変更)
        String Using_Amount;                                                //先月の使用量　(今月分との比較のため)
        String Tax;                                                         //使用金額の消費税
        float price = 0;
        double TAX;
        int usage_integer_minutes_C = (int) Math.floor(row) ;               //使用量の整数部
        float Row2 = Float.parseFloat(((TextView)main.findViewById(R.id.Used_number)).getText().toString());
        String s = String.valueOf(row);
        Matcher matcher = p.matcher(s);

        
        if (matcher.matches())
        {
            double d_row = row % 1.0;
            BigDecimal bd_row = new BigDecimal(d_row);
            BigDecimal bd_row_up = new BigDecimal("10");
            bd_row = bd_row.setScale(1,BigDecimal.ROUND_HALF_UP);
            bd_row = bd_row.multiply(bd_row_up);
            minutes = String.valueOf(bd_row.intValue());
        }
        else
        {
            minutes = "0";
        }
       
        Cursor cursor_TOKUIF = db.rawQuery("SELECT G_T_rate , S_price , M_C_price , A_price , G_C_tax , B_amount , P_section , U_price ,S_usage , T_C_table " +
                                           "FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor_TOKUIF.moveToFirst();
        
        Cursor cursor_HYOF = db.rawQuery("SELECT usage_integer_minutes ,c_point_0 ,c_point_1 ,c_point_2 ,c_point_3 ,c_point_4 ,c_point_5 , c_point_6 ,c_point_7 ,c_point_8 ,c_point_9  " +
                                         "FROM HYOF WHERE usage_integer_minutes = ? " , new String[]{ String.valueOf( usage_integer_minutes_C)});
        cursor_HYOF.moveToFirst();
                                                                            //ガス料金区分
        switch (cursor_TOKUIF.getString(6))
        {
            //料金区分:A
            case "A":
                                                                            //使用量が0.0～50.1の時
                if (row <= 50.1 && row >= 0.0)
                {
                                                                            //小数点1ケタ
                    switch (minutes)
                    {
                        case "0":
                            price = cursor_HYOF.getInt(1);
                            break;
                        case "1":
                            price = cursor_HYOF.getInt(2);
                            break;
                        case "2":
                            price = cursor_HYOF.getInt(3);
                            break;
                        case "3":
                            price = cursor_HYOF.getInt(4);
                            break;
                        case "4":
                            price = cursor_HYOF.getInt(5);
                            break;
                        case "5":
                            price = cursor_HYOF.getInt(6);
                            break;
                        case "6":
                            price = cursor_HYOF.getInt(7);
                            break;
                        case "7":
                            price = cursor_HYOF.getInt(8);
                            break;
                        case "8":
                            price = cursor_HYOF.getInt(9);
                            break;
                        case "9":
                            price = cursor_HYOF.getInt(10);//Integer.parseInt(cursor_HYOF.getString(10));
                            break;
                    }
                    System.out.println("price : " + price);
                }
                break;

            //料金区分:B
            case "B":
                                                                            //使用量が0以上の時
                if (Row2 >= 0) {
                                                                            //　使用量1.0 * 単価 /　基準使用量
                    price = (int)(Row2 * Integer.parseInt(cursor_TOKUIF.getString(7)) / Float.parseFloat(cursor_TOKUIF.getString(8)));
                    System.out.println("Price : " + price);
                }
                break;
        }

        // ガス請求金額
        price = price + Float.parseFloat(cursor_TOKUIF.getString(1)) + Float.parseFloat(cursor_TOKUIF.getString(2)) + Float.parseFloat(cursor_TOKUIF.getString(3));
        System.out.println("Price2 : " + price);
        // ガス消費税
        TAX = price * Float.parseFloat(cursor_TOKUIF.getString(0)) / 100;
        System.out.println("TAX : " + TAX);
        //消費税計算区分
        switch (cursor_TOKUIF.getString(9)) {
            //四捨五入
            case "0":
                TAX = (int) Math.round(TAX);
                System.out.println("TAX(四捨五入) : " + TAX);
                break;
            //切捨
            case "1":
                TAX = (int) Math.floor(TAX);
                System.out.println("TAX(切り捨て) : " + TAX);
                break;
            //切上
            case "2":
                TAX = (int) Math.ceil(TAX);
                System.out.println("TAX(切上) : " + TAX);
                break;
        }

        System.out.println("price(全額) : " + price);
        System.out.println("TAX : " + TAX);
        // 今回請求金額(税込)
        Using_Amount = String.format("%,d", (int) (price + TAX));
        Tax = String.format("%,d", (int) TAX);

        ((TextView)main.findViewById(R.id.Using_Amount)).setText(Using_Amount);
        ((TextView)main.findViewById(R.id.Tax)).setText(Tax);

    }
}
