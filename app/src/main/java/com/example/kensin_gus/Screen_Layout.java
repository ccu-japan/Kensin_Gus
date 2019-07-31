package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        public void Screen_Data(MainActivity mainActivity) {
            Date d = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            TextView textView = mainActivity.findViewById(R.id.date_now);
            textView.setText(sdf.format(d));
        }

        //------------------------------------------------------------------------------------------------------------
        //
        // ****** 企業選択ボタン ******
        //
        //------------------------------------------------------------------------------------------------------------
        @SuppressLint("SetTextI18n")
        void Com_Select(MainActivity mainActivity, SQLiteDatabase db , int COL_BAN) {



            //     String col_ban = Integer.toString(COL_BAN);
            Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage ,/*i:9*/ ban FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            c.moveToFirst();



            TextView name = mainActivity.findViewById(R.id.name);
            TextView code = mainActivity.findViewById(R.id.code);
            TextView used = mainActivity.findViewById(R.id.Row1_Text2);
            EditText now  = mainActivity.findViewById(R.id.Row1_Text);
            TextView usaged = mainActivity.findViewById(R.id.Row2_Text);

            try {

                name.setText(c.getString(0) + c.getString(1));
                code.setText(c.getString(2) + c.getString(3));
                used.setText(c.getString(4));
                usaged.setText(c.getString(8));
                now.setText(c.getString(5));

            }
            catch(IndexOutOfBoundsException e) {
                if(c.getPosition() < 0) {
                    Toast.makeText(mainActivity, "このレコードは先頭です", Toast.LENGTH_SHORT).show();
                    COL_BAN =1;
                } else{
                    Toast.makeText(mainActivity, "このレコードは最終です", Toast.LENGTH_SHORT).show();
                    COL_BAN = -1;
                }
            }catch (NumberFormatException e){
                Log.d("number","数値以外があります");
            }

        }
        //------------------------------------------------------------------------------------------------------------
        //
        // ******  ******
        //
        //------------------------------------------------------------------------------------------------------------

        @SuppressLint("SetTextI18n")
        static int SELECT_COM(MainActivity mainActivity, int COL_BAN , SQLiteDatabase db) {

            //     String col_ban = Integer.toString(COL_BAN);
            Cursor c = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage ,/*i:9*/ ban FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            c.moveToFirst();



            TextView name = mainActivity.findViewById(R.id.name);
            TextView code = mainActivity.findViewById(R.id.code);
            TextView used = mainActivity.findViewById(R.id.Row1_Text2);
            EditText now  = mainActivity.findViewById(R.id.Row1_Text);
            TextView usaged = mainActivity.findViewById(R.id.Row2_Text);

            try {

                name.setText(c.getString(0) + c.getString(1));
                code.setText(c.getString(2) + c.getString(3));
                used.setText(c.getString(4));
                usaged.setText(c.getString(8));
                now.setText(c.getString(5));

            }
            catch(IndexOutOfBoundsException e) {
                if(COL_BAN < 1) {
                    Toast.makeText(mainActivity, "このレコードは先頭です", Toast.LENGTH_SHORT).show();
                    COL_BAN =1;
                } else{
                    Toast.makeText(mainActivity, "このレコードは最終です", Toast.LENGTH_SHORT).show();
                    COL_BAN --;
                }
            }catch (NumberFormatException e){
                Log.d("number","数値以外があります");
            }
            return  COL_BAN;
        }



    }
}