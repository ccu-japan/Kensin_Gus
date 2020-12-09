package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

@SuppressLint("Registered")
public class Button_Processing{

    //――――――――――――――――――――――――――――――――――――――――――
    //  FIXED_UNFIXED_BUTTONの設定メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean Check_task(boolean check , Activity activity){

        //false：未　の場合
        if(!check)
        {
                                                                            //　ボタンテキスト
            ((Button)activity.findViewById(R.id.Update)).setText("済");
                                                                            //　バックグラウンドカラー
            ((Button)activity.findViewById(R.id.Update)).setBackgroundColor(Color.BLUE);
            
                                                                            //　入力テキスト
            ((EditText)activity.findViewById(R.id.Input_number)).setEnabled(false);
            
                                                                            //　点検ボタン
            ((Button)activity.findViewById(R.id.Check_Button)).setEnabled(false);
            
                                                                            //　検針ボタン
            ((Button)activity.findViewById(R.id.Kensin_Button)).setEnabled(false);
            
                                                                            //　印刷ボタン
            ((Button)activity.findViewById(R.id.Printer_Button)).setEnabled(true);
            
                                                                            // 日付ボタン
            ((Button)activity.findViewById(R.id.Calender_Button)).setEnabled(false);
            
            
            check = true;
        }
        //true : 済　の場合
        else
        {
            ((Button)activity.findViewById(R.id.Update)).setText("未");
            
                                                                            // バックグラウンドカラー
            ((Button)activity.findViewById(R.id.Update)).setBackgroundColor(Color.RED);
    
                                                                            // 入力テキスト
            ((EditText)activity.findViewById(R.id.Input_number)).setEnabled(true);
    
                                                                            // 点検ボタン
            ((Button)activity.findViewById(R.id.Check_Button)).setEnabled(true);
    
                                                                            // 検針ボタン
            ((Button)activity.findViewById(R.id.Kensin_Button)).setEnabled(true);
    
                                                                            // 印刷ボタン
            ((Button)activity.findViewById(R.id.Printer_Button)).setEnabled(false);
            
                                                                            // 日付ボタン
            ((Button)activity.findViewById(R.id.Calender_Button)).setEnabled(true);
    
            ((TextView)activity.findViewById(R.id.date_now)).setText(Screen_Layout.Main_Screen.Today());
            ((TextView)activity.findViewById(R.id.Input_number)).setText("0");
            ((TextView)activity.findViewById(R.id.Used_number)).setText("0");
            ((TextView)activity.findViewById(R.id.Using_Amount)).setText("0");
            ((TextView)activity.findViewById(R.id.Tax)).setText("0");
            check = false;
        }

        return check;
    }

    //――――――――――――――――――――――――――――――――――――――――――
    //  UP_DOWNキー押下時のFIXED_UNFIXEDボタンのレコード登録メソッド
    //――――――――――――――――――――――――――――――――――――――――――
    public void Up_Down_Button(KensinMainActivity activity, SQLiteDatabase db , int COL_BAN )
    {
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("SELECT /*i:0*/ P_flag ,/*i:1*/ company ,/*i:2*/ customer , /*i:3*/ place , /*i:4*/T_T_pointer FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();
        
                                                                                        //　メインアクティビティの入力テキストの値が「0」の時
        if(((TextView)activity.findViewById(R.id.Input_number)).getText().toString().equals("0")
            &&  ((TextView)activity.findViewById(R.id.Used_number)).getText().toString().equals("0"))
        {
            ((Button)activity.findViewById(R.id.Update)).setText("未");
            ((Button)activity.findViewById(R.id.Update)).setBackgroundColor(Color.RED);
            ((EditText)activity.findViewById(R.id.Input_number)).setEnabled(true);
            ((Button)activity.findViewById(R.id.Check_Button)).setEnabled(true);
            ((Button)activity.findViewById(R.id.Kensin_Button)).setEnabled(true);
            ((Button)activity.findViewById(R.id.Printer_Button)).setEnabled(false);
            ((Button)activity.findViewById(R.id.Calender_Button)).setEnabled(true);
            contentValues.put("P_flag"," ");
        }                                                                                //「0」以外の時
        else
        {
            ((Button)activity.findViewById(R.id.Update)).setText("済");
            ((Button)activity.findViewById(R.id.Update)).setBackgroundColor(Color.BLUE);
            ((EditText)activity.findViewById(R.id.Input_number)).setEnabled(false);
            ((Button)activity.findViewById(R.id.Check_Button)).setEnabled(false);
            ((Button)activity.findViewById(R.id.Kensin_Button)).setEnabled(false);
            ((Button)activity.findViewById(R.id.Printer_Button)).setEnabled(true);
            ((Button)activity.findViewById(R.id.Calender_Button)).setEnabled(false);
            contentValues.put("P_flag","1");
        }
        
                                                                                        //レコード登録
        db.update("TOKUIF", contentValues, " ban = ? ", new String[]{String.valueOf(COL_BAN)});
    }
}
