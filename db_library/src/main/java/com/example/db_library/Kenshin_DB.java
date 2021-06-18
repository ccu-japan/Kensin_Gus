package com.example.db_library;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Kenshin_DB extends SQLiteOpenHelper {
																			//レコード登録
	public final static String DB_NAME = "Kensin.db";
																			// データベースのバージョン
	public final static int DB_VER = 1;
	public SQLiteDatabase db;
	
	public Kenshin_DB(Context context) {
		super(context, DB_NAME, null, DB_VER);
		db = this.getWritableDatabase();
	}
	
	//----------------------------------------------------------------------
	// テーブルを削除するメソッド
	//----------------------------------------------------------------------
	public void allDelete(String DB_TABLE){
		try{
																			// deleteメソッド DBのレコードを削除
			String sql = " DELETE FROM " + DB_TABLE ;
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
	
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

