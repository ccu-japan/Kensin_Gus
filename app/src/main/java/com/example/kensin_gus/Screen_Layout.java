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

        //―――――――――――――――――――――――――――――――――――――――
        // 今日の日付を出力するメソッド
        //―――――――――――――――――――――――――――――――――――――――
        public String Screen_Data(MainActivity mainActivity) {
            //〇〇〇〇年〇〇月〇〇日　表記
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date Date_now   = new Date();
            String Date = sdf.format(Date_now);
            TextView date   = mainActivity.findViewById(R.id.date_now);
            date.setText(Date);

            return Date;
        }

        //―――――――――――――――――――――――――――――――――――――――
        // 画面表示メソッド
        //―――――――――――――――――――――――――――――――――――――――
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        static int SELECT_COM(MainActivity mainActivity, int COL_BAN , SQLiteDatabase db) {

            Cursor cursor_ban   = db.rawQuery("SELECT ban FROM TOKUIF",null);
            Cursor c            = db.rawQuery("SELECT /*i:0*/C_name1 , /*i:1*/C_name2 , /*i:2*/customer , /*i:3*/P_name , /*i:4*/L_T_pointer  ,/*i:5*/T_T_pointer ,/*i:6*/company  ,/* i:7 */ place ,/*i:8*/ T_T_usage ,/*i:9*/ ban ,/*i:10*/T_T_kensin FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
            Cursor C_price      = db.rawQuery("SELECT /*i:0*/T_T_Billing ,/*i:1*/ G_C_tax FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});

            c.moveToFirst();
            C_price.moveToFirst();

            //MainActivity出力
            TextView name1 = mainActivity.findViewById(R.id.Name1);
            TextView name2 = mainActivity.findViewById(R.id.Name2);
            TextView code1 = mainActivity.findViewById(R.id.code);

            EditText now        = mainActivity.findViewById(R.id.Input_number);
            TextView used       = mainActivity.findViewById(R.id.zenkai_kensin_number);
            TextView usaged     = mainActivity.findViewById(R.id.Used_number);
            TextView Row3_Text  = mainActivity.findViewById(R.id.Using_Amount);
            TextView Row3_2Text = mainActivity.findViewById(R.id.Tax);
            TextView Date_now   = mainActivity.findViewById(R.id.date_now);

            try {
                //金額をカンマ区切りで表示
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

                //登録データが0000/00/00 or 空白以外の時
                if(!c.getString(10).equals("0000/00/00")) {
                    Date_now.setText(c.getString(10));
                }
            }
            catch(IndexOutOfBoundsException e) {
                cursor_ban.moveToFirst();
                int FIRST_BAN = Integer.parseInt(cursor_ban.getString(0));
                cursor_ban.moveToLast();
                int LAST_BAN = Integer.parseInt(cursor_ban.getString(0));

                //連番が最小値より下回った場合
                if(COL_BAN < FIRST_BAN) {
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                                                            .setMessage("このレコードは先頭です。")
                                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int idx) {
                                                                }
                                                            }).show();
                    COL_BAN = FIRST_BAN;    //最小値を入力
                //最大値を上回った場合
                } else{
                    new AlertDialog.Builder(mainActivity).setTitle("確認ダイアログ")
                            .setMessage("このレコードは最終です。")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int idx) {
                                }
                            }).show();
                    COL_BAN = LAST_BAN; //最大値を入力
                }
            }catch (NumberFormatException e){
                Log.d("number","数値以外があります");
            }
            return  COL_BAN;
        }
    }
}