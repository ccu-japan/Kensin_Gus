package com.example.db_library;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

public class TOKUIF {

        private File file;
        private File path;
        private String fileName = "TOKUIF.TSV";
        private String OUT_PUT_FILE_NAME = "RCV_TOKUIF.TSV";



        //---------------------------------------------------------------------------------------------------------------------
        //
        // 　カラム名定義
        //
        //---------------------------------------------------------------------------------------------------------------------
        public final static String DB_TABLE = " TOKUIF ";

        public final String COL_BAN = "ban";
        public final String COL_STX = "stx";
        public final String COL_TYPE = "type";
        public final String COL_COMPANY_CODE = "company";
        public final String COL_CUSTOMER_CODE = "customer";
        public final String COL_PLACE_CODE = "place";
        public final String COL_PLACE_NAME = "P_name";
        public final String COL_KENSIN = "kensin";
        public final String COL_KENSINJUN = "K_jun";
        public final String COL_CUSTOMER_NAME1 = "C_name1";
        public final String COL_CUSTOMER_NAME2 = "C_name2";
        public final String COL_GAS_PRICE_SECTION = "P_section";
        public final String COL_GAS_PRICE_TABLE = "P_table";
        public final String COL_UNIT_PRICE = "U_price";
        public final String COL_STANDARD_USAGE = "S_usage";
        public final String COL_LAST_TIME_KENSIN = "L_T_kensin";
        public final String COL_LAST_TIME_POINTER = " L_T_pointer";
        public final String COL_LAST_TIME_USAGE = "L_T_usage";
        public final String COL_THIS_TIME_KENSIN = "T_T_kensin";
        public final String COL_THIS_TIME_POINTER = "T_T_pointer";
        public final String COL_THIS_TIME_USAGE = " T_T_usage";
        public final String COL_GAS_PRICE = "G_price";
        public final String COL_STANDARD_PRICE = "S_price";
        public final String COL_MICRO_COMPUTER_PRICE = "M_C_price";
        public final String COL_ALARM_PRICE = "A_price";
        public final String COL_BILLING_AMOUNT = " B_amount";
        public final String COL_GAS_CONSUMPTION_TAX = "G_C_tax";
        public final String COL_GAS_TAX_RATE = "G_T_rate";
        public final String COL_THIS_TIME_BILLING = "T_T_Billing";
        public final String COL_MATER_EXCHANGE_FLAG = "M_C_flag";
        public final String COL_MATER_EXCHANGE_DATE = "M_E_date";
        public final String COL_MATER_EXCHANGE_USAGE = "M_E_usage";
        public final String COL_TENKEN_RESULT01 = "result1";
        public final String COL_TENKEN_RESULT02 = "result2";
        public final String COL_TENKEN_RESULT03 = "result3";
        public final String COL_TENKEN_RESULT04 = "result4";
        public final String COL_TENKEN_RESULT05 = "result5";
        public final String COL_TENKEN_RESULT06 = "result6";
        public final String COL_TENKEN_RESULT07 = "result7";
        public final String COL_TENKEN_RESULT08 = "result8";
        public final String COL_TENKEN_RESULT09 = "result9";
        public final String COL_TENKEN_RESULT10 = "result10";
        public final String COL_TENKEN_RESULT11 = "result11";
        public final String COL_TENKEN_RESULT12 = "result12";
        public final String COL_TANTOUSYA = "tantousya";
        public final String COL_GROUP_CODE = "G_code";
        public final String COL_TAX_CALCULATION_TABLE = "T_C_table";
        public final String COL_FILLER = "filler";
        public final String COL_PROBED_FLAG = "P_flag";
        public final String COL_LAST_TIME__BILLING_REMAINDER = "B_remainder";
        public final String COL_LAST_TIME__BILLING_REMAINDER_tax = "B_reminder_tax";
        public final String COL_OTHER_SALES = "O_sales";
        public final String COL_OTHER_SALES_TAX = "O_sales_tax";
        public final String COL_TERMINAL_ID = "T_id";
        public final String COL_ETX = "ETX";
        public final String COL_END_CODE = "E_code";

        //---------------------------------------------------------------------------------------------------------------------
        //　/カラム名定義
        //---------------------------------------------------------------------------------------------------------------------

        public void onCreate(SQLiteDatabase db) {
                String createTbl = "CREATE TABLE " + TOKUIF.DB_TABLE + " ("
                        + COL_BAN + " TEXT ,"
                        + COL_STX + "  TEXT ,"
                        + COL_TYPE + "  TEXT ,"
                        + COL_COMPANY_CODE + "  TEXT ,"
                        + COL_CUSTOMER_CODE + "  TEXT  ,"
                        + COL_PLACE_CODE + "  REAL ,"
                        + COL_PLACE_NAME + "  TEXT ,"
                        + COL_KENSIN + "  REAL ,"
                        + COL_KENSINJUN + "  REAL ,"
                        + COL_CUSTOMER_NAME1 + "  TEXT ,"
                        + COL_CUSTOMER_NAME2 + "  TEXT ,"
                        + COL_GAS_PRICE_SECTION + "  TEXT ,"
                        + COL_GAS_PRICE_TABLE + "  TEXT ,"
                        + COL_UNIT_PRICE + "  REAL ,"
                        + COL_STANDARD_USAGE + "  REAL ,"
                        + COL_LAST_TIME_KENSIN + " BLOB  ,"
                        + COL_LAST_TIME_POINTER + "  REAL,"
                        + COL_LAST_TIME_USAGE + "  REAL ,"
                        + COL_THIS_TIME_KENSIN + "  BLOB ,"
                        + COL_THIS_TIME_POINTER + "  REAL ,"
                        + COL_THIS_TIME_USAGE + "  REAL ,"
                        + COL_GAS_PRICE + "  REAL ,"
                        + COL_STANDARD_PRICE + "  REAL ,"
                        + COL_MICRO_COMPUTER_PRICE + "  REAL ,"
                        + COL_ALARM_PRICE + "  REAL ,"
                        + COL_BILLING_AMOUNT + "  REAL ,"
                        + COL_GAS_CONSUMPTION_TAX + "  REAL ,"
                        + COL_GAS_TAX_RATE + "  REAL ,"
                        + COL_THIS_TIME_BILLING + "  REAL ,"
                        + COL_MATER_EXCHANGE_FLAG + "  TEXT ,"
                        + COL_MATER_EXCHANGE_DATE + "  BLOB ,"
                        + COL_MATER_EXCHANGE_USAGE + "  REAL ,"
                        + COL_TENKEN_RESULT01 + "  INTEGER ,"
                        + COL_TENKEN_RESULT02 + "  INTEGER ,"
                        + COL_TENKEN_RESULT03 + "  INTEGER ,"
                        + COL_TENKEN_RESULT04 + "  INTEGER ,"
                        + COL_TENKEN_RESULT05 + "  INTEGER ,"
                        + COL_TENKEN_RESULT06 + "  INTEGER ,"
                        + COL_TENKEN_RESULT07 + "  INTEGER ,"
                        + COL_TENKEN_RESULT08 + "  INTEGER ,"
                        + COL_TENKEN_RESULT09 + "  INTEGER ,"
                        + COL_TENKEN_RESULT10 + "  INTEGER ,"
                        + COL_TENKEN_RESULT11 + "  INTEGER ,"
                        + COL_TENKEN_RESULT12 + "  INTEGER ,"
                        + COL_TANTOUSYA + "  TEXT ,"
                        + COL_GROUP_CODE + "  TEXT ,"
                        + COL_TAX_CALCULATION_TABLE + "  TEXT ,"
                        + COL_FILLER + "  TEXT ,"
                        + COL_PROBED_FLAG + "  TEXT ,"
                        + COL_LAST_TIME__BILLING_REMAINDER + " REAL ,"
                        + COL_LAST_TIME__BILLING_REMAINDER_tax + " REAL ,"
                        + COL_OTHER_SALES + "  REAL ,"
                        + COL_OTHER_SALES_TAX + "  REAL ,"
                        + COL_TERMINAL_ID + "  TEXT ,"
                        + COL_ETX + " TEXT ,"
                        + COL_END_CODE + "  TEXT ,"
                        + "PRIMARY KEY(" + COL_COMPANY_CODE + " ," + COL_CUSTOMER_CODE + ",  " + COL_PLACE_CODE + ")"
                        + ");";
                db.execSQL(createTbl);
        }


        @SuppressLint("UseCheckPermission")
        public void TOKUIF_CSV(SQLiteDatabase db, Context context) {
                //-----------------------------------------------------------------------------------------------------------------------
                //
                // *** CSV読込 ***
                //
                //-----------------------------------------------------------------------------------------------------------------------

                try {
                        if (Build.VERSION.SDK_INT >= 29) {
                                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        } else {
                                path = new File(Environment.getExternalStorageDirectory().getPath());
                                Log.d("file_path", "API29以下です");
                                Log.d("fill_path", String.valueOf(path));
                        }
                        file = new File(path, fileName);

                        FileInputStream fileInputStream = new FileInputStream(file);


                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader buffer = new BufferedReader(inputStreamReader);
                        ContentValues contentValues = new ContentValues();
                        String line;
                        while ((line = buffer.readLine()) != null) {
                                String[] columnData = line.split("\t", -1);

                                contentValues.put(COL_BAN, columnData[0]);
                                contentValues.put(COL_STX, columnData[1]);
                                contentValues.put(COL_TYPE, columnData[2]);
                                contentValues.put(COL_COMPANY_CODE, columnData[3]);
                                contentValues.put(COL_CUSTOMER_CODE, columnData[4]);
                                contentValues.put(COL_PLACE_CODE, Integer.parseInt(columnData[5]));
                                contentValues.put(COL_PLACE_NAME, columnData[6]);
                                contentValues.put(COL_KENSIN, Integer.parseInt(columnData[7]));
                                contentValues.put(COL_KENSINJUN, Double.parseDouble(columnData[8]));
                                contentValues.put(COL_CUSTOMER_NAME1, columnData[9]);
                                contentValues.put(COL_CUSTOMER_NAME2, columnData[10]);
                                contentValues.put(COL_GAS_PRICE_SECTION, columnData[11]);
                                contentValues.put(COL_GAS_PRICE_TABLE, columnData[12]);
                                contentValues.put(COL_UNIT_PRICE, Double.parseDouble(columnData[13]));
                                contentValues.put(COL_STANDARD_USAGE, Double.parseDouble(columnData[14]));
                                contentValues.put(COL_LAST_TIME_KENSIN, columnData[15]);
                                contentValues.put(COL_LAST_TIME_POINTER, Double.parseDouble(columnData[16]));
                                contentValues.put(COL_LAST_TIME_USAGE, Double.parseDouble(columnData[17]));
                                contentValues.put(COL_THIS_TIME_KENSIN, columnData[18]);
                                contentValues.put(COL_THIS_TIME_POINTER, Double.parseDouble(columnData[19]));
                                contentValues.put(COL_THIS_TIME_USAGE, Double.parseDouble(columnData[20]));
                                contentValues.put(COL_GAS_PRICE, Integer.parseInt(columnData[21]));
                                contentValues.put(COL_STANDARD_PRICE, Integer.parseInt(columnData[22]));
                                contentValues.put(COL_MICRO_COMPUTER_PRICE, Integer.parseInt(columnData[23]));
                                contentValues.put(COL_ALARM_PRICE, Integer.parseInt(columnData[24]));
                                contentValues.put(COL_BILLING_AMOUNT, Integer.parseInt(columnData[25]));
                                contentValues.put(COL_GAS_CONSUMPTION_TAX, Double.parseDouble(columnData[26]));
                                contentValues.put(COL_GAS_TAX_RATE, Double.parseDouble(columnData[27]));
                                contentValues.put(COL_THIS_TIME_BILLING, Integer.parseInt(columnData[28]));
                                contentValues.put(COL_MATER_EXCHANGE_FLAG, columnData[29]);
                                contentValues.put(COL_MATER_EXCHANGE_DATE, columnData[30]);
                                contentValues.put(COL_MATER_EXCHANGE_USAGE, columnData[31]);
                                contentValues.put(COL_TENKEN_RESULT01, Integer.parseInt(columnData[32]));
                                contentValues.put(COL_TENKEN_RESULT02, Integer.parseInt(columnData[33]));
                                contentValues.put(COL_TENKEN_RESULT03, Integer.parseInt(columnData[34]));
                                contentValues.put(COL_TENKEN_RESULT04, Integer.parseInt(columnData[35]));
                                contentValues.put(COL_TENKEN_RESULT05, Integer.parseInt(columnData[36]));
                                contentValues.put(COL_TENKEN_RESULT06, Integer.parseInt(columnData[37]));
                                contentValues.put(COL_TENKEN_RESULT07, Integer.parseInt(columnData[38]));
                                contentValues.put(COL_TENKEN_RESULT08, Integer.parseInt(columnData[39]));
                                contentValues.put(COL_TENKEN_RESULT09, Integer.parseInt(columnData[40]));
                                contentValues.put(COL_TENKEN_RESULT10, Integer.parseInt(columnData[41]));
                                contentValues.put(COL_TENKEN_RESULT11, Integer.parseInt(columnData[42]));
                                contentValues.put(COL_TENKEN_RESULT12, Integer.parseInt(columnData[43]));
                                contentValues.put(COL_TANTOUSYA, columnData[44]);
                                contentValues.put(COL_GROUP_CODE, columnData[45]);
                                contentValues.put(COL_TAX_CALCULATION_TABLE, columnData[46]);
                                contentValues.put(COL_FILLER, columnData[47]);
                                contentValues.put(COL_PROBED_FLAG, columnData[48]);
                                contentValues.put(COL_LAST_TIME__BILLING_REMAINDER, Integer.parseInt(columnData[49]));
                                contentValues.put(COL_LAST_TIME__BILLING_REMAINDER_tax, Integer.parseInt(columnData[50]));
                                contentValues.put(COL_OTHER_SALES, Integer.parseInt(columnData[51]));
                                contentValues.put(COL_OTHER_SALES_TAX, Integer.parseInt(columnData[52]));
                                contentValues.put(COL_TERMINAL_ID, columnData[53]);
                                contentValues.put(COL_ETX, columnData[54]);
                                contentValues.put(COL_END_CODE, columnData[55]);
                                db.insert(DB_TABLE, null, contentValues);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public void Usaged(SQLiteDatabase db, String Usaged_now, ContentValues values, String Usaged_now2, int COL_BAN, String now) {
                try {
                        values.put("T_T_kensin", now);
                        values.put("T_T_pointer", Usaged_now);
                        values.put("T_T_usage", Usaged_now2);
                        db.update(DB_TABLE, values, "  ban = ? ", new String[]{String.valueOf(COL_BAN)});  //レコード登録
                } catch (NumberFormatException e) {
                        Log.d("Number", "NumberFormatException");
                }
        }

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //***** TAX_PRICE (料金データ登録)******
        //
        //1. G_T_rate : ガス消費税率  2. S_price   : 基本料金　    3.M_price     : マイコン料金　4.A_price : 警報機料金　
        //5. G_price  : ガス料金　    6. B_amount  : ガス請求金額  7.T_T_Billing : 今回請求金額  8.G_C_tax : ガス消費税
        //
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        @SuppressLint("NewApi")
        public void TAX_PRICE(String Price, String Tax, ContentValues values, SQLiteDatabase db, int COL_BAN, String T_kensin) {
                Cursor cursor = db.rawQuery("SELECT/*i:0*/ G_T_rate ,/*i:1*/ S_price  , /*i:2*/M_C_price   ,/*i:3*/ A_price ," +
                        "/*i:4*/  G_price ,/*i:5*/  B_amount ,/*i:6*/ T_T_Billing ,/*i:7*/ G_C_tax ," +
                        "/*i:8*/  company,/*i:9*/ customer , /*i:10*/  place  ,/*i:11*/P_flag " +
                        "FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
                cursor.moveToFirst();

                //-----------------------------------------------------------------------------------
                // * 「,」３桁区切りを数値に変換
                //-----------------------------------------------------------------------------------
                int i_Price;
                int i_Tax;

                try {
                        Number number = NumberFormat.getInstance().parse(Price);
                        Number number2 = NumberFormat.getInstance().parse(Tax);

                        i_Price = Objects.requireNonNull(number).intValue();
                        i_Tax = Objects.requireNonNull(number2).intValue();

                        int price_only_GUS = (int) (i_Price - Integer.parseInt(cursor.getString(1)) - Float.parseFloat(cursor.getString(2)) - Float.parseFloat(cursor.getString(3)));
                        int price_No_Tax = i_Price - i_Tax;

                        String P_flag = "1";

                        values.put("G_price", price_only_GUS);
                        values.put("B_amount", price_No_Tax);
                        values.put("G_C_tax", i_Tax);
                        values.put("T_T_Billing", i_Price);
                        values.put("P_flag", P_flag);
                        values.put("T_T_kensin", T_kensin);
                        db.update(DB_TABLE, values, "  ban = ? ", new String[]{String.valueOf(COL_BAN)});  //レコード登録

                } catch (NumberFormatException e) {
                        Log.d("Number", "NumberFormatException");
                } catch (ParseException e) {
                        e.printStackTrace();
                }
        }

        public static int[] Check_Result(SQLiteDatabase db, int COL_BAN, int[] check) {
                Cursor check_result = db.rawQuery("SELECT result1 , result2 ,result3 , result4 , result5 , result6 ," +
                        "result7 , result8 ,result9 ,result10 ,result11 , result12 FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
                check_result.moveToLast();

                for (int i = 0; i < check.length; i++) {
                        check[i] = check_result.getInt(i);
                }
                return check;
        }

        public void Check_Result_return(SQLiteDatabase db, int COL_BAN, int[] check , String TODAY) {
                ContentValues values = new ContentValues();

                for (int i = 1; i <= check.length; i++) {
                        values.put("result" + i, check[i - 1]);
                }
                values.put("M_E_date",TODAY);
                db.update(DB_TABLE, values, "  ban = ? ", new String[]{String.valueOf(COL_BAN)});  //レコード登録
        }

        //--------------------------------------------------------------------------------------------------------//
        //                                                                                                        //
        //                                      TSVファイル出力タスク                                              //
        //                                                                                                        //
        //--------------------------------------------------------------------------------------------------------//

        public void OUT_PUT_TSV(SQLiteDatabase db, Context context) {

                Cursor cursor = db.rawQuery("SELECT * FROM TOKUIF WHERE  P_flag = ? ", new String[]{"1"});
                Log.d("cursor", " : " + cursor.getCount());
                Log.d("cursor", " : " + cursor.getColumnCount());

                String[][] RCV = new String[cursor.getCount()][cursor.getColumnCount()];
                int com_count;
                int hum_count2=0;
                try {
                        if (Build.VERSION.SDK_INT >= 29) {
                                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        } else {
                                path = Environment.getExternalStorageDirectory();
                                Log.d("file_path", "API29以下です");
                        }
                        file = new File(path, OUT_PUT_FILE_NAME);

                        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                        BufferedWriter RCV_TOKUIF = new BufferedWriter(outputStreamWriter);

                        if (cursor.moveToFirst()) {
                                do {
                                         for (com_count = 0; com_count < cursor.getColumnCount(); com_count++) {
                                                 RCV[hum_count2][com_count] = cursor.getString(com_count);

                                                 if (com_count == cursor.getColumnCount()-1) {
                                                         RCV_TOKUIF.write(RCV[hum_count2][com_count]);
                                                 } else {
                                                         RCV_TOKUIF.write(RCV[hum_count2][com_count] + "\t");
                                                 }
                                         }

                                        RCV_TOKUIF.newLine();
                                        hum_count2++;

                                } while (cursor.moveToNext());
                                RCV_TOKUIF.flush();

                                MediaScannerConnection.scanFile(context, new String[]{path + "/" + OUT_PUT_FILE_NAME}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String s, Uri uri) {

                                        }
                                });



                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
