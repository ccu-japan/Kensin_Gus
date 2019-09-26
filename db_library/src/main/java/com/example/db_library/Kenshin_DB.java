package com.example.db_library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Kenshin_DB extends SQLiteOpenHelper {
    public final static String DB_NAME = "Kensin.db";  // データベース名
    public final static int DB_VER = 1;  			     // データベースのバージョン
    public SQLiteDatabase db;

    public Kenshin_DB(Context context) {
        super(context, DB_NAME, null, DB_VER);
        db = this.getWritableDatabase();
    }

    //――――――――――――――――――――――――――――――――――――
    // テーブルを削除するメソッド
    //――――――――――――――――――――――――――――――――――――
    public void allDelete(String DB_TABLE){
        try{
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            String sql = " DELETE FROM " + DB_TABLE ;
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new TOKUIF().onCreate(db);
        new HYOF().CreateTBL_HYOF(db);
    }

    //――――――――――――――――――――――――――――――――――――
    //　バージョンアップメソッド
    //――――――――――――――――――――――――――――――――――――
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS TOKUIF");
            Log.d("Delete","テーブルを削除しました");
        }

    }

}

