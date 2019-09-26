package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.example.db_library.Kenshin_DB;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Meter {
    private Kenshin_DB kenshin_db;
    Cursor kensin_cursol;
    Cursor customer_cursol;
    Cursor checkbox_cursol;

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
    public int CHECKBOX_COL;
    public String[] CHECK_BOX;


    //――――――――――――――――――――――――――――――――――――
    //　プリント出力メソッド　(TOKUIF)
    //――――――――――――――――――――――――――――――――――――
    public Meter Meter_List(int COL_BAN, Context context) {

        kenshin_db = new Kenshin_DB(context);

        kensin_cursol = kenshin_db.db.rawQuery("SELECT T_T_kensin, L_T_kensin , L_T_usage , L_T_pointer , T_T_pointer , S_price , G_price , G_C_tax  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        kensin_cursol.moveToFirst();

        customer_cursol = kenshin_db.db.rawQuery("SELECT customer,place,P_name,C_name1,C_name2  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        customer_cursol.moveToFirst();

        checkbox_cursol = kenshin_db.db.rawQuery("SELECT result1 ,result2 ,result3 ,result4 ,result5 ,result6 , result7," +
                "result8 ,result9 , result10 , result11 , result12 FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        checkbox_cursol.moveToFirst();

        return Meter.this;
    }
    //――――――――――――――――――――――――――――――――――――
    // 今回検針日
    //――――――――――――――――――――――――――――――――――――
    public String TODAY() {
        TODAY = kensin_cursol.getString(0);
        return TODAY;
    }
    //――――――――――――――――――――――――――――――――――――
    // 前回検針日
    //――――――――――――――――――――――――――――――――――――
    public String LAST_TIME_DAY() {
        LAST_TIME_DAY = kensin_cursol.getString(1);
        return LAST_TIME_DAY;
    }

    //――――――――――――――――――――――――――――――――――――
    // 前回使用量
    //――――――――――――――――――――――――――――――――――――
    public String LAST_TIME_USE() {
        LAST_TIME_USE = String.valueOf(kensin_cursol.getDouble(2));
        return LAST_TIME_USE;
    }

    //――――――――――――――――――――――――――――――――――――
    // 前回検針指針
    //――――――――――――――――――――――――――――――――――――
    public String LAST_POINT() {
        LAST_POINT = String.valueOf(kensin_cursol.getDouble(3));
        return LAST_POINT;
    }

    //――――――――――――――――――――――――――――――――――――
    // 今回検針指針
    //――――――――――――――――――――――――――――――――――――
    public String TODAY_POINT() {
        TODAY_POINT = String.valueOf(kensin_cursol.getDouble(4));
        return TODAY_POINT;
    }

    //――――――――――――――――――――――――――――――――――――
    // 使用量の比較
    //――――――――――――――――――――――――――――――――――――
    public String USE_COMP() {
        Double val = Double.valueOf(TODAY_POINT) - Double.valueOf(LAST_POINT);
        BigDecimal bd = new BigDecimal(val);

        // 少数第2位　四捨五入
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

        USE_COMP = String.valueOf(bd);
        return USE_COMP;
    }

    //――――――――――――――――――――――――――――――――――――
    // 基本料金
    //――――――――――――――――――――――――――――――――――――
    public int STANDARD_PRICE() {
        STANDARD_PRICE = kensin_cursol.getInt(5);
        return STANDARD_PRICE;
    }
    //――――――――――――――――――――――――――――――――――――
    // ガス料金
    //――――――――――――――――――――――――――――――――――――
    public int GUS_PRICE() {
        GUS_PRICE = kensin_cursol.getInt(6);
        return GUS_PRICE;
    }

    //――――――――――――――――――――――――――――――――――――
    // 消費税
    //――――――――――――――――――――――――――――――――――――
    public int TAX() {
        TAX = kensin_cursol.getInt(7);
        return TAX;
    }

    //――――――――――――――――――――――――――――――――――――
    // 会社コード
    //――――――――――――――――――――――――――――――――――――
    public String CUSTOMER() {
        CUSTOMER = customer_cursol.getString(0);
        return CUSTOMER;
    }

    //――――――――――――――――――――――――――――――――――――
    // 設置場所コード
    //――――――――――――――――――――――――――――――――――――
    public String PLACE_CODE() {
        PLACE_CODE = "0" + customer_cursol.getInt(1);
        return PLACE_CODE;
    }

    //――――――――――――――――――――――――――――――――――――
    // 顧客名
    //――――――――――――――――――――――――――――――――――――
    public String C_NAME() {
        C_NAME = customer_cursol.getString(3) + customer_cursol.getString(4);
        return C_NAME.trim();
    }

    //――――――――――――――――――――――――――――――――――――
    // 設置場所名称
    //――――――――――――――――――――――――――――――――――――
    public String P_NAME() {
        P_NAME = customer_cursol.getString(2);
        return P_NAME;
    }

    //――――――――――――――――――――――――――――――――――――
    // 出力チェックボックス数
    //――――――――――――――――――――――――――――――――――――
    @SuppressLint("NewApi")
    public int CHECKBOX_COL() {
        CHECKBOX_COL = 0;
        for (int i = 0; i < checkbox_cursol.getColumnCount(); i++) {
            //チェック入がある場合カウント
            if (checkbox_cursol.getInt(i) == 1) {
                CHECKBOX_COL++;
            }
        }
        Log.d("Result", String.valueOf(CHECKBOX_COL));
        return CHECKBOX_COL;
    }

    //――――――――――――――――――――――――――――――――――――
    // チェック入出力メソッド
    //――――――――――――――――――――――――――――――――――――
    public String[] CHECK_BOX(Context context) {
        ArrayList<CheckBox> arrayList;
        arrayList = new CheckActivity().Array_Check_Box(context);
        CHECK_BOX = new String[CHECKBOX_COL()];
        int j = 0;
        //チェックボックス数分
        for (int i = 0; i < arrayList.size(); i++) {
            //チェックがある場合
            if (checkbox_cursol.getInt(i) == 1) {
                //チェックボックステキストを出力
                CHECK_BOX[j] = arrayList.get(i).getText().toString();
                Log.d("Result",CHECK_BOX[j]);
                j++;
            }
        }
        return CHECK_BOX;
    }
}