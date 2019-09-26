package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("Registered")
public class Button_Processing{

    //――――――――――――――――――――――――――――――――――――――――――
    //  FIXED_UNFIXED_BUTTONの設定メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    public boolean Check_task(EditText Input_number ,Button check_button ,Button update_button ,Button kensin_button ,boolean check ,Button printer_button){

        //false：未　の場合
        if(!check) {
            update_button.setText("済");                     //ボタンテキスト
            update_button.setBackgroundColor(Color.BLUE);    //バックグラウンドカラー
            Input_number.setEnabled(false);                  //入力テキスト
            check_button.setEnabled(false);     //点検ボタン
            kensin_button.setEnabled(false);    //検針ボタン
            printer_button.setEnabled(true);    //印刷ボタン
            check = true;
        }
        //true : 済　の場合
        else
            {
            update_button.setText("未");
            update_button.setBackgroundColor(Color.RED);
            Input_number.setEnabled(true);
            check_button.setEnabled(true);
            kensin_button.setEnabled(true);
            printer_button.setEnabled(false);
            check = false;
        }

        return check;
    }

    //――――――――――――――――――――――――――――――――――――――――――
    //  UP_DOWNキー押下時のFIXED_UNFIXEDボタンのレコード登録メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    public void Up_Down_Button(MainActivity mainActivity, SQLiteDatabase db , int COL_BAN )
    {
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("SELECT /*i:0*/ P_flag ,/*i:1*/ company ,/*i:2*/ customer , /*i:3*/ place , /*i:4*/T_T_pointer FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

        final Button Update_button = mainActivity.findViewById(R.id.Update);
        final Button Check_button = mainActivity.findViewById(R.id.Check_Button);
        final Button Printer_button = mainActivity.findViewById(R.id.Printer_Button);
        Button kensin_button = mainActivity.findViewById(R.id.Kensin_Button);
        EditText Row1 = mainActivity.findViewById(R.id.Input_number);
        String TRUE = "1";  //ボタンが済の時
        String FALSE = " "; //ボタンが未の時

        //メインアクティビティの入力テキストの値が「0」の時
        if(Row1.getText().toString().equals("0"))
        {
            Update_button.setBackgroundColor(Color.RED);
            Update_button.setText("未");
            contentValues.put("P_flag",FALSE);
            Row1.setEnabled(true);
            Check_button.setEnabled(true);
            kensin_button.setEnabled(true);
            Printer_button.setEnabled(false);
        }
        //「0」以外の時
        else
        {
            Update_button.setBackgroundColor(Color.BLUE);
            Update_button.setText("済");
            contentValues.put("P_flag",TRUE);
            Row1.setEnabled(false);
            Check_button.setEnabled(false);
            kensin_button.setEnabled(false);
            Printer_button.setEnabled(true);

        }
        db.update("TOKUIF", contentValues, " ban = ? ", new String[]{String.valueOf(COL_BAN)});  //レコード登録
    }

}
