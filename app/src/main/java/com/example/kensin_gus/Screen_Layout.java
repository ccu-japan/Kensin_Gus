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
        public String Screen_Data(MainActivity mainActivity , String Date) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date Date_now = new Date();
            Date = sdf.format(Date_now);
            TextView date = mainActivity.findViewById(R.id.date_now);
            date.setText(Date);

            Log.d("Date_now", " 今日:"+ Date);

            return Date ;
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

            Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage ,/*i:9*/ ban FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            Cursor C_price  = db.rawQuery("SELECT /*i:0*/T_T_Billing ,/*i:1*/ G_C_tax FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});

            c.moveToFirst();
            C_price.moveToFirst();

            TextView name = mainActivity.findViewById(R.id.name);
            TextView code = mainActivity.findViewById(R.id.code);

            TextView used = mainActivity.findViewById(R.id.Row1_Text2);
            EditText now  = mainActivity.findViewById(R.id.Row1_Text);
            TextView usaged = mainActivity.findViewById(R.id.Row2_Text);
            TextView Row3_Text = mainActivity.findViewById(R.id.Row3_Text);
            TextView Row3_2Text = mainActivity.findViewById(R.id.Row3_Text2);

            try {
                String Row3_text = String.format("%,d", Integer.parseInt(C_price.getString(0)));
                String Row3_text2 = String.format("%,d", Integer.parseInt(C_price.getString(1)));

                name.setText(c.getString(0) + c.getString(1));
                code.setText(c.getString(2) + " "  + c.getString(3));
                used.setText(c.getString(4));
                usaged.setText(c.getString(8));
                now.setText(c.getString(5));
                Row3_Text.setText(Row3_text);
                Row3_2Text.setText(Row3_text2);

            }
            catch(IndexOutOfBoundsException e) {
                if(COL_BAN < 1) {
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                                                            .setMessage("このレコードは先頭です。")
                                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int idx) {
                                                                }
                                                            }).show();
                    COL_BAN = 1;
                } else{
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                            .setMessage("このレコードは最終です。")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int idx) {
                                }
                            }).show();
                    COL_BAN --;
                }
            }catch (NumberFormatException e){
                Log.d("number","数値以外があります");
            }
            return  COL_BAN;
        }



    }
}