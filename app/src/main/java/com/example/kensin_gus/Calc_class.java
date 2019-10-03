package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc_class {

    //――――――――――――――――――――――――――――――――――――――――――
    //  使用量の計算メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    static String Calc_Used(float row, SQLiteDatabase db, int COL_BAN) {
        //i=0.顧客名1 , i=1 顧客名2 , i=2 得意先コード , i=3 設置場所名称 , i=4 前回検針指針
        Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ," +
                //i=5 今回検針指針 , i=6 会社コード , i=7 設置場所コード , i=8 今回使用量
                "/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        c.moveToFirst();

        float num = row - Float.parseFloat(c.getString(4));
        @SuppressLint("DefaultLocale") String amount_to_use = (String.format("%.1f", num));

        return amount_to_use;
    }

    //――――――――――――――――――――――――――――――――――――――――――
    //  使用量金額の計算メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    @SuppressLint("DefaultLocale")
    static void Calc_HYOF_PRICE(float row, SQLiteDatabase db, int COL_BAN, KensinMainActivity main) {
        //KensinMainActivity　オブジェクト生成
        TextView Amount_Text = main.findViewById(R.id.Using_Amount);
        TextView Tax_Text = main.findViewById(R.id.Tax);
        TextView number_Text = main.findViewById(R.id.Used_number);

        //クラス変数
        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";   //整数か少数かを区別する
        Pattern p = Pattern.compile(regex);
        String minutes;                                    //使用量の小数部　(0~9 金額変更)
        String Using_Amount;                               //先月の使用量　(今月分との比較のため)
        String Tax;                                        //使用金額の消費税
        float price = 0;
        double TAX;
        float Row2 = Float.parseFloat(number_Text.getText().toString());
        int usage_integer_minutes_C = (int) Math.floor(row);    //使用量の整数部
        String s = String.valueOf(row);
        Matcher matcher = p.matcher(s);

        if (matcher.matches()) {
            int row1 = (int) (row % 1.0 * 10);
            minutes = String.valueOf(row1);
        } else {
            minutes = "0";
        }

        Cursor cursor_HYOF = db.rawQuery("SELECT /*i:0*/ usage_integer_minutes ,/*i:1*/c_point_0 ,/*i:2*/c_point_1 ,/*i:3*/c_point_2 ,/*i:4*/c_point_3 ,/*i:5*/c_point_4 , " +
                "/*i:6*/c_point_5 , /*i:7*/c_point_6 , /*i:8*/c_point_7 ,/*i:9*/ c_point_8 ,/*i:10*/ c_point_9  FROM HYOF WHERE usage_integer_minutes = " + usage_integer_minutes_C, null);
        Cursor cursor_TOKUIF = db.rawQuery("SELECT G_T_rate , S_price , M_C_price , A_price , G_C_tax , B_amount , P_section , U_price ,S_usage , T_C_table FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor_HYOF.moveToFirst();
        cursor_TOKUIF.moveToFirst();

        //ガス料金区分
        switch (cursor_TOKUIF.getString(6)) {
            //料金区分:A
            case "A":
                //使用量が0.0～50.1の時
                if (row <= 50.1 && row >= 0.0) {
                    //小数点1ケタ
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

            //料金区分:B
            case "B":
                //使用量が0以上の時
                if (Row2 >= 0) {
                    //　使用量1.0 * 単価 /　基準使用量
                    price = (int) (Row2 * Integer.parseInt(cursor_TOKUIF.getString(7)) / Float.parseFloat(cursor_TOKUIF.getString(8)));
                }
                break;
        }

        // ガス請求金額
        price = price + Float.parseFloat(cursor_TOKUIF.getString(1)) + Float.parseFloat(cursor_TOKUIF.getString(2)) + Float.parseFloat(cursor_TOKUIF.getString(3));

        // ガス消費税
        TAX = price * Float.parseFloat(cursor_TOKUIF.getString(0)) / 100;

        //消費税計算区分
        switch (cursor_TOKUIF.getString(9)) {
            //四捨五入
            case "0":
                TAX = (int) Math.round(TAX);
                break;
            //切捨
            case "1":
                TAX = (int) Math.floor(TAX);
                break;
            //切上
            case "2":
                TAX = (int) Math.ceil(TAX);
                break;
        }

        // 今回請求金額(税込)
        Using_Amount = String.format("%,d", (int) (price + TAX));
        Tax = String.format("%,d", (int) TAX);

        Amount_Text.setText(Using_Amount);
        Tax_Text.setText(Tax);

    }
}
