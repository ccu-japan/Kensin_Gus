package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Screen_Layout {

    @SuppressLint("Registered")
    public static class Main_Screen {

        //------------------------------------------------------------------
        //
        // 今日の日付を出力するメソッド
        //
        //------------------------------------------------------------------
        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public static String Today()
        {
            try
            {
                //〇〇〇〇年〇〇月〇〇日　表記
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date Date_now = new Date();
                String Date = sdf.format(Date_now);
                System.out.println(String.format("Date : %s", Date));
                //((TextView) kensinMainActivity.findViewById(R.id.date_now)).setText(Date);
                
                return Date;
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
            return "";
        }
    
        //------------------------------------------------------------------
        //
        // 画面表示メソッド
        //
        //------------------------------------------------------------------
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        static KensinMainActivity.GETTER SELECT_COM(KensinMainActivity activity, int COL_BAN , SQLiteDatabase db) {
            KensinMainActivity.GETTER getter = new KensinMainActivity.GETTER();
            
            Cursor cursor_ban   = db.rawQuery("SELECT ban FROM TOKUIF",null);
            Cursor c_name       = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer ,/*i:5*/ T_T_pointer ,/*i:6*/company ,/* i:7 */ place , /*i:8*/ T_T_usage ,/*i:9*/ ban ,/*i:10*/T_T_kensin, P_flag FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            Cursor C_price      = db.rawQuery("SELECT /*i:0*/T_T_Billing ,/*i:1*/ G_C_tax FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            boolean flg = false;
            c_name.moveToFirst();
            C_price.moveToFirst();

            try
            {
                //金額をカンマ区切りで表示
                String Row3_text = String.format("%,d", Integer.parseInt(C_price.getString(0)));
                String Row3_text2 = String.format("%,d", Integer.parseInt(C_price.getString(1)));

                ((TextView)activity.findViewById(R.id.Name1)).setText(String.format("%s%s", c_name.getString(0),c_name.getString(1)));
                ((TextView)activity.findViewById(R.id.code)).setText(String.format("%s %s", c_name.getString(2),c_name.getString(3)));
                ((TextView)activity.findViewById(R.id.zenkai_kensin_number)).setText(String.valueOf(c_name.getDouble(4)));
                ((TextView)activity.findViewById(R.id.Used_number)).setText(c_name.getString(8));
                ((TextView)activity.findViewById(R.id.Input_number)).setText(c_name.getString(5));
                ((TextView)activity.findViewById(R.id.Using_Amount)).setText(Row3_text);
                ((TextView)activity.findViewById(R.id.Tax)).setText(Row3_text2);
                
                if(c_name.getString(11).equals("1"))
                {
                    ((TextView) activity.findViewById(R.id.date_now)).setText(c_name.getString(10));
                    flg = true;
                }
                else
                {
                    ((TextView) activity.findViewById(R.id.date_now)).setText(Today());
                    flg = false;
                }
            }
            catch(IndexOutOfBoundsException e)
            {
                cursor_ban.moveToFirst();
                int FIRST_BAN = Integer.parseInt(cursor_ban.getString(0));
                cursor_ban.moveToLast();
                int LAST_BAN = Integer.parseInt(cursor_ban.getString(0));

                                                                            //連番が最小値より下回った場合
                if(COL_BAN < FIRST_BAN) {
                    new AlertDialog.Builder(activity).setTitle("確認ダイアログ")
                                                            .setMessage("このレコードは先頭です。")
                                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int idx) {
                                                                }
                                                            }).show();
                    COL_BAN = FIRST_BAN;                                    //最小値を入力
                                                                            //最大値を上回った場合
                }
                else
                {
                    new AlertDialog.Builder(activity).setTitle("確認ダイアログ")
                            .setMessage("このレコードは最終です。")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int idx)
                                {
                                }
                            }).show();
                    COL_BAN = LAST_BAN;                                     //最大値を入力
                }
            }
            catch (NumberFormatException e)
            {
                Log.d("number","数値以外があります");
            }
            finally
            {
                getter.BAN = COL_BAN;
                getter.FLG = flg;
            }
            return getter;
        }
    }
}