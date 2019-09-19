package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Screen_Layout {

    @SuppressLint("Registered")
    public static class Main_Screen {

        //------------------------------------------------------------------------------------------------------------
        //
        // *** 今日の日付　***
        //
        //------------------------------------------------------------------------------------------------------------
        public String Screen_Data(MainActivity mainActivity) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date Date_now   = new Date();
            String Date = sdf.format(Date_now);
            TextView date   = mainActivity.findViewById(R.id.date_now);
            date.setText(Date);

            Log.d("Date_now", " 今日:"+ Date);

            return Date;
        }

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //***** 画面表示タスク　******
        //
        //
        //5. G_price  : ガス料金　    6. B_amount  : ガス請求金額  7.T_T_Billing : 今回請求金額  8.G_C_tax : ガス消費税
        //
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        static int SELECT_COM(MainActivity mainActivity, int COL_BAN , SQLiteDatabase db) {

            Cursor cursor_ban   = db.rawQuery("SELECT ban FROM TOKUIF",null);
            Cursor c            = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage ,/*i:9*/ ban ,/*i:10*/T_T_kensin  FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            Cursor C_price      = db.rawQuery("SELECT /*i:0*/T_T_Billing ,/*i:1*/ G_C_tax FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});

            c.moveToFirst();
            C_price.moveToFirst();

            TextView name1 = mainActivity.findViewById(R.id.Name1);
            TextView name2 = mainActivity.findViewById(R.id.Name2);
            TextView code1 = mainActivity.findViewById(R.id.code);

            EditText now        = mainActivity.findViewById(R.id.Row1_Text);
            TextView used       = mainActivity.findViewById(R.id.Row1_Text2);
            TextView usaged     = mainActivity.findViewById(R.id.Row2_Text);
            TextView Row3_Text  = mainActivity.findViewById(R.id.Row3_Text);
            TextView Row3_2Text = mainActivity.findViewById(R.id.Row3_Text2);
            TextView Date_now   = mainActivity.findViewById(R.id.date_now);

            try {
                String Row3_text = String.format("%,d", Integer.parseInt(C_price.getString(0)));
                String Row3_text2 = String.format("%,d", Integer.parseInt(C_price.getString(1)));

                name1.setText(c.getString(0));
                name2.setText(c.getString(1));

                code1.setText(c.getString(2) + " "  + c.getString(3));
                used.setText(String.valueOf(c.getDouble(4)));
                usaged.setText(c.getString(8));
                now.setText(c.getString(5));
                Row3_Text.setText(Row3_text);
                Row3_2Text.setText(Row3_text2);

                if(!c.getString(10).equals("0000/00/00")) {
                    Date_now.setText(c.getString(10));
                }
            }
            catch(IndexOutOfBoundsException e) {
                cursor_ban.moveToFirst();
                int FIRST_BAN = Integer.parseInt(cursor_ban.getString(0));
                cursor_ban.moveToLast();
                int LAST_BAN = Integer.parseInt(cursor_ban.getString(0));

                if(COL_BAN < FIRST_BAN) {
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                                                            .setMessage("このレコードは先頭です。")
                                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int idx) {
                                                                }
                                                            }).show();
                    COL_BAN = FIRST_BAN;
                } else{
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                            .setMessage("このレコードは最終です。")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int idx) {
                                }
                            }).show();
                    COL_BAN = LAST_BAN;
                }
            }catch (NumberFormatException e){
                Log.d("number","数値以外があります");
            }
            return  COL_BAN;
        }



    }
}