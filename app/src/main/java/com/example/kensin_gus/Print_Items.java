package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.example.db_library.Kenshin_DB;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Print_Items
{
    private Kenshin_DB kenshin_db;
    Cursor kensin_cursor;
    Cursor customer_cursor;
    Cursor checkbox_cursor;
    
    public String TODAY;
    public String LAST_TIME_DAY;
    public String LAST_TIME_USE;
    public String TODAY_POINT;
    public String LAST_POINT;
    public String USE_COMP;
    public int STANDARD_PRICE;
    public int GUS_PRICE;
    public int TAX;
    public String METER_CHANGE;
    public String CUSTOMER;
    public String PLACE_CODE;
    public String C_NAME;
    public String P_NAME;
    public int CHECKBOX_COL;
    public String[] CHECK_BOX;    
    
    //----------------------------------------------------------------------
    //
    //　プリント出力メソッド　(TOKUIF)
    //
    //----------------------------------------------------------------------
    public Print_Items Print_ItemList(int COL_BAN, Context context)
    {
    
        kenshin_db = new Kenshin_DB(context);
    
                                                                            //今月検針日、先月検針日、先月使用量、先月検針、今月検針、基本料金、ガス料金(基本料金）、ガス消費税 , ガス交換時消費量 , メータ交換フラグ
        kensin_cursor = kenshin_db.db.rawQuery("SELECT T_T_kensin, L_T_kensin , L_T_usage , L_T_pointer , T_T_pointer , S_price , G_price , G_C_tax ,M_E_usage , M_C_flag FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
    
        kensin_cursor.moveToFirst();
    
                                                                            //得意先コード、設置場所コード、設置場所名称、顧客名1、顧客名2
        customer_cursor = kenshin_db.db.rawQuery("SELECT customer,place,P_name,C_name1,C_name2  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        customer_cursor.moveToFirst();
    
                                                                            //点検チェックボックス１～１２
        checkbox_cursor = kenshin_db.db.rawQuery("SELECT result1 ,result2 ,result3 ,result4 ,result5 ,result6 , result7," +
            "result8 ,result9 , result10 , result11 , result12 FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        checkbox_cursor.moveToFirst();
    
        return Print_Items.this;
    }
    
    
    //----------------------------------------------------------------------
    //
    // 今回検針日
    //
    //----------------------------------------------------------------------
    public String TODAY()
    {
        TODAY = kensin_cursor.getString(0);
        return TODAY;
    }
    
    //----------------------------------------------------------------------
    //
    // 前回検針日
    //
    //----------------------------------------------------------------------
    public String LAST_TIME_DAY()
    {
        LAST_TIME_DAY = kensin_cursor.getString(1);
        return LAST_TIME_DAY;
    }
    
    
    //----------------------------------------------------------------------
    //
    // 前回使用量
    //
    //----------------------------------------------------------------------
    public String LAST_TIME_USE()
    {
        LAST_TIME_USE = String.valueOf(kensin_cursor.getDouble(2));
        return LAST_TIME_USE;
    }
    
    
    //----------------------------------------------------------------------
    //    
    // 前回検針指針
    //
    //----------------------------------------------------------------------
    public String LAST_POINT()
    {
        LAST_POINT = String.valueOf(kensin_cursor.getDouble(3));
        return LAST_POINT;
    }
    
    
    //----------------------------------------------------------------------
    //
    //  今回検針指針
    //
    //----------------------------------------------------------------------
    public String TODAY_POINT()
    {
        Double POINT = kensin_cursor.getDouble(4);
        
        TODAY_POINT = String.valueOf(POINT);
        
        return TODAY_POINT;
    }
    
    
    //---------------------------------------------------------------------
    //
    // 使用量の比較
    //
    //----------------------------------------------------------------------
    public String USE_COMP()
    {
        Double val = Double.valueOf(TODAY_POINT) - Double.valueOf(LAST_POINT);
    
        if(kensin_cursor.getString(9).equals("1"))
            val = val + kensin_cursor.getDouble(8);
        
        BigDecimal bd = new BigDecimal(val);
    
        // 少数第2位　四捨五入
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
    
        USE_COMP = String.valueOf(bd);
        return USE_COMP;
    }
    
    
    //----------------------------------------------------------------------
    //
    // 基本料金
    //
    //----------------------------------------------------------------------
    
    public int STANDARD_PRICE()
    {
        STANDARD_PRICE = kensin_cursor.getInt(5);
        return STANDARD_PRICE;
    }
    //----------------------------------------------------------------------
    //
    // ガス料金
    //
    //----------------------------------------------------------------------
    
    public int GUS_PRICE()
    {
        GUS_PRICE = kensin_cursor.getInt(5) + kensin_cursor.getInt(6) + kensin_cursor.getInt(7);
        return GUS_PRICE;
    }
    
    //----------------------------------------------------------------------
    //
    // 消費税
    //
    //----------------------------------------------------------------------
    public int TAX()
    {
        TAX = kensin_cursor.getInt(7);
        return TAX;
    }
    
    //----------------------------------------------------------------------
    //
    // メータ交換時使用量
    //
    //----------------------------------------------------------------------
    public String METER_CHANGE()
    {
        METER_CHANGE = kensin_cursor.getString(8);
        return METER_CHANGE;
    }
    
    //----------------------------------------------------------------------
    //
    // 会社コード
    //
    //----------------------------------------------------------------------    
    public String CUSTOMER()
    {
        CUSTOMER = customer_cursor.getString(0);
        return CUSTOMER;
    }
    
    //----------------------------------------------------------------------
    //
    // 設置場所コード
    //
    //----------------------------------------------------------------------    
    public String PLACE_CODE()
    {
        PLACE_CODE = "0" + customer_cursor.getInt(1);
        return PLACE_CODE;
    }
    
    //----------------------------------------------------------------------
    //
    // 顧客名
    //
    //----------------------------------------------------------------------    
    public String C_NAME()
    {
        C_NAME = customer_cursor.getString(3) + customer_cursor.getString(4);
        return C_NAME.trim();
    }
    
    //----------------------------------------------------------------------
    //
    // 設置場所名称
    //
    //----------------------------------------------------------------------
    
    public String P_NAME()
    {
        P_NAME = customer_cursor.getString(2);
        return P_NAME;
    }
    
    //----------------------------------------------------------------------
    //
    // 出力チェックボックス数
    //
    //----------------------------------------------------------------------    
    @SuppressLint("NewApi")
    public int CHECKBOX_COL()
    {
        CHECKBOX_COL = 0;
        for (int i = 0; i < checkbox_cursor.getColumnCount(); i++)
        {
            //チェック入がある場合カウント
            if (checkbox_cursor.getInt(i) == 1)
            {
                CHECKBOX_COL++;
            }
        }
        Log.d("Result", String.valueOf(CHECKBOX_COL));
        return CHECKBOX_COL;
    }
    
    //----------------------------------------------------------------------
    //
    // チェック入出力メソッド
    //
    //----------------------------------------------------------------------    
    public String[] CHECK_BOX(Context context)
    {
        ArrayList<CheckBox> arrayList;
        arrayList = new CheckActivity().Array_Check_Box(context);
        CHECK_BOX = new String[CHECKBOX_COL()];
        Cursor cursor = kenshin_db.db.rawQuery("select num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12 from TENKEN_ITEM",null);
        cursor.moveToFirst();
        int j = 0;
        //チェックボックス数分
        for (int i = 0; i < arrayList.size(); i++)
        {
            System.out.println(cursor.getString(i));
            //チェックがある場合
            if (checkbox_cursor.getInt(i) == 1)
            {
                //チェックボックステキストを出力
                CHECK_BOX[j] = cursor.getString(i);//arrayList.get(i).getText().toString();
                j++;
            }
            //cursor.moveToNext();
        }
        return CHECK_BOX;
    }
}