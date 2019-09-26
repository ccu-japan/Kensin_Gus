package com.example.db_library;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class HYOF {
    private static final String DB_TABLE = " HYOF ";    //テーブル名

    private static final String COL_STX = "stx";        //STX
    private static final String COL_TYPE = "type";      //タイプ
    private static final String COL_COMPANY_CODE = "company_code";                       //会社コード
    private static final String COL_PRICE_LIST_CODE = "price_list_code";                 //料金表コード
    private static final String COL_USAGE_INTEGER_MINUTES = "usage_integer_minutes";    //使用量整数部
    private static final String COL_C_POINT_0 = "c_point_0";    //少数分 0
    private static final String COL_C_POINT_1 = "c_point_1";    //少数分 1
    private static final String COL_C_POINT_2 = "c_point_2";    //少数分 2
    private static final String COL_C_POINT_3 = "c_point_3";    //少数分 3
    private static final String COL_C_POINT_4 = "c_point_4";    //少数分 4
    private static final String COL_C_POINT_5 = "c_point_5";    //少数分 5
    private static final String COL_C_POINT_6 = "c_point_6";    //少数分 6
    private static final String COL_C_POINT_7 = "c_point_7";    //少数分 7
    private static final String COL_C_POINT_8 = "c_point_8";    //少数分 8
    private static final String COL_C_POINT_9 = "c_point_9";    //少数分 9
    private static final String COL_R_VALUE_MAX = "r_value_max";    //基準値MAX
    private static final String COL_ADD_MONEY = "add_money";    //加算金額
    private static final String COL_FILLER = "feller";      //Filler
    private static final String COL_ETX = "etx";             //ETX
    private static final String COL_END_CODE = "end_code";  //エンドコード

    //――――――――――――――――――――――――――――――――――――
    // HYOFテーブル作成メソッド
    //――――――――――――――――――――――――――――――――――――
    public void CreateTBL_HYOF(SQLiteDatabase db) {
        String CreateTbl = "CREATE TABLE " + HYOF.DB_TABLE + "("
                + COL_STX + " TEXT ,"
                + COL_TYPE + " TEXT ,"
                + COL_COMPANY_CODE + " INTEGER ,"
                + COL_PRICE_LIST_CODE + " TEXT ,"
                + COL_USAGE_INTEGER_MINUTES + " INTEGER ,"
                + COL_C_POINT_0 + " INTEGER ,"
                + COL_C_POINT_1 + " INTEGER ,"
                + COL_C_POINT_2 + " INTEGER ,"
                + COL_C_POINT_3 + " INTEGER ,"
                + COL_C_POINT_4 + " INTEGER ,"
                + COL_C_POINT_5 + " INTEGER ,"
                + COL_C_POINT_6 + " INTEGER ,"
                + COL_C_POINT_7 + " INTEGER ,"
                + COL_C_POINT_8 + " INTEGER ,"
                + COL_C_POINT_9 + " INTEGER ,"
                + COL_R_VALUE_MAX + " INTEGR ,"
                + COL_ADD_MONEY + " INTEGER ,"
                + COL_FILLER + " TEXT ,"
                + COL_ETX + " TEXT ,"
                + COL_END_CODE + " TEXT ,"
                + "PRIMARY KEY(" + COL_COMPANY_CODE + " , " + COL_PRICE_LIST_CODE + " , " + COL_USAGE_INTEGER_MINUTES + ")"
                + ");";
        db.execSQL(CreateTbl);
    }

    //――――――――――――――――――――――――――――――――――――
    // HYOFファイル読込メソッド
    //――――――――――――――――――――――――――――――――――――
    public void HYOF_TSV(SQLiteDatabase db, Context context) {
        File path;
        try {
            //SDKバージョンの確認
            if (Build.VERSION.SDK_INT >= 29) {
                //29以降では 「getExternalStorage」は非推奨になるため扱えません
                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                //28以下対応中
            } else {
                //内部ストレージ or SDカード直下
                path = Environment.getExternalStorageDirectory();

                Log.d("file_path", "API29以下です");
            }

            //読み取りができるように変換
            String fileName = "HYOF.TSV";
            File file = new File(path, fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            ContentValues contentValues = new ContentValues();
            String line;

            //TSV形式なのでタブ文字の1つ手前までを１単語として配列に挿入
            while ((line = buffer.readLine()) != null) {
                String[] columnData = line.split("\t", -1);

                //カラムに挿入
                contentValues.put(COL_STX, columnData[0]);
                contentValues.put(COL_TYPE, columnData[1]);
                contentValues.put(COL_COMPANY_CODE, Integer.parseInt(columnData[2]));
                contentValues.put(COL_PRICE_LIST_CODE, columnData[3]);
                contentValues.put(COL_USAGE_INTEGER_MINUTES, Integer.parseInt(columnData[4]));
                contentValues.put(COL_C_POINT_0, Integer.parseInt(columnData[5]));
                contentValues.put(COL_C_POINT_1, Integer.parseInt(columnData[6]));
                contentValues.put(COL_C_POINT_2, Integer.parseInt(columnData[7]));
                contentValues.put(COL_C_POINT_3, Integer.parseInt(columnData[8]));
                contentValues.put(COL_C_POINT_4, Integer.parseInt(columnData[9]));
                contentValues.put(COL_C_POINT_5, Integer.parseInt(columnData[10]));
                contentValues.put(COL_C_POINT_6, Integer.parseInt(columnData[11]));
                contentValues.put(COL_C_POINT_7, Integer.parseInt(columnData[12]));
                contentValues.put(COL_C_POINT_8, Integer.parseInt(columnData[13]));
                contentValues.put(COL_C_POINT_9, Integer.parseInt(columnData[14]));
                contentValues.put(COL_R_VALUE_MAX, Integer.parseInt(columnData[15]));
                contentValues.put(COL_ADD_MONEY, Integer.parseInt(columnData[16]));
                contentValues.put(COL_FILLER, columnData[17]);
                contentValues.put(COL_ETX, columnData[18]);
                contentValues.put(COL_END_CODE, columnData[19]);
                db.insert(DB_TABLE, null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

