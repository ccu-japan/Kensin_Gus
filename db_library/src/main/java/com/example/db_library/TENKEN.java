package com.example.db_library;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TENKEN
{
    public static final String DB_TABLE = "TENKEN_ITEM";
    
    public static final String COL_NUM1 = "num1";
    public static final String COL_NUM2 = "num2";
    public static final String COL_NUM3 = "num3";
    public static final String COL_NUM4 = "num4";
    public static final String COL_NUM5 = "num5";
    public static final String COL_NUM6 = "num6";
    public static final String COL_NUM7 = "num7";
    public static final String COL_NUM8 = "num8";
    public static final String COL_NUM9 = "num9";
    public static final String COL_NUM10 = "num10";
    public static final String COL_NUM11 = "num11";
    public static final String COL_NUM12 = "num12";
    
    //――――――――――――――――――――――――――――――――――
    // テーブル作成メソッド
    //――――――――――――――――――――――――――――――――――
    public void CreateDB(SQLiteDatabase db)
    {
        String createTbl = " CREATE TABLE " + TENKEN.DB_TABLE + " ("
            + COL_NUM1 + " TEXT ,"
            + COL_NUM2 + " TEXT ,"
            + COL_NUM3 + " TEXT ,"
            + COL_NUM4 + " TEXT ,"
            + COL_NUM5 + " TEXT ,"
            + COL_NUM6 + " TEXT ,"
            + COL_NUM7 + " TEXT ,"
            + COL_NUM8 + " TEXT ,"
            + COL_NUM9 + " TEXT ,"
            + COL_NUM10 + " TEXT ,"
            + COL_NUM11 + " TEXT ,"
            + COL_NUM12 + " TEXT ,"
            + "PRIMARY KEY(" + COL_NUM1 + ")"
            + ");";
        db.execSQL(createTbl);
    }
    
    @SuppressLint("UseCheckPermission")
    public void TENKEN_TSV(SQLiteDatabase db, Context context)
    {
        try
        {
            File path;
            File file;
            String fileName = "TENKEN.TSV";
            String[] columnData;
    
            //SDKバージョンの確認
            if (Build.VERSION.SDK_INT >= 29)
            {
                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }
            //SDK28以下のみ対応  *2019/09/25
            else
            {
                //内部ストレージ,SDカード直下
                path = new File(Environment.getExternalStorageDirectory().getPath());
            }
            file = new File(path, fileName);
    
            if (file.exists())
            {
                FileInputStream fileInputStream = new FileInputStream(file);
    
                //Shift-JIS形式で読取
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "Shift-JIS");
    
                ContentValues contentValues = new ContentValues();
                BufferedReader buffer = new BufferedReader(inputStreamReader);
                String line;
    
                while ((line = buffer.readLine()) != null)
                {
                    columnData = line.split("\t", -1);
    
                    contentValues.put(COL_NUM1, columnData[0]);
                    contentValues.put(COL_NUM2, columnData[1]);
                    contentValues.put(COL_NUM3, columnData[2]);
                    contentValues.put(COL_NUM4, columnData[3]);
                    contentValues.put(COL_NUM5, columnData[4]);
                    contentValues.put(COL_NUM6, columnData[5]);
                    contentValues.put(COL_NUM7, columnData[6]);
                    contentValues.put(COL_NUM8, columnData[7]);
                    contentValues.put(COL_NUM9, columnData[8]);
                    contentValues.put(COL_NUM10, columnData[9]);
                    contentValues.put(COL_NUM11, columnData[10]);
                    contentValues.put(COL_NUM12, columnData[11]);
    
                    db.insert(DB_TABLE, null, contentValues);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
