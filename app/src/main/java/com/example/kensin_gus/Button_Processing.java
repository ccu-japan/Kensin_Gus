package com.example.kensin_gus;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class Button_Processing {

    public void Update_button(MainActivity mainActivity, SQLiteDatabase db , int COL_BAN ,ContentValues Values ) {
        Cursor cursor = db.rawQuery("SELECT /*i:0*/ P_flag ,/*i:1*/ company ,/*i:2*/ customer , /*i:3*/ place , /*i:4*/T_T_pointer FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

        final Button Update_button = mainActivity.findViewById(R.id.Update);
        final Button Check_button = mainActivity.findViewById(R.id.Check_Button);
        EditText Row1 = mainActivity.findViewById(R.id.Row1_Text);
        String TRUE = "1";
        String FALSE = " ";

        if (cursor.getString(0).equals(TRUE))
        {
            Update_button.setText("済");
            Values.put("P_flag", FALSE);
            Row1.setEnabled(false);
            Check_button.setEnabled(true);

        }
        else if (cursor.getString(0).equals(FALSE))
        {
            Update_button.setText("未");
            Values.put("P_flag", TRUE);
            Row1.setEnabled(true);
            Check_button.setEnabled(false);
        }

        db.update("TOKUIF", Values, " company = ?  AND  customer = ?  AND place = ? ", new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3)});  //レコード登録
        Log.d("P_flag", "P_flg : " + cursor.getString(0));
    }


    public void Up_Down_Button(MainActivity mainActivity, SQLiteDatabase db , int COL_BAN ,ContentValues Values )
    {
        Cursor cursor = db.rawQuery("SELECT /*i:0*/ P_flag ,/*i:1*/ company ,/*i:2*/ customer , /*i:3*/ place , /*i:4*/T_T_pointer FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

        final Button Update_button = mainActivity.findViewById(R.id.Update);
        final Button Check_button = mainActivity.findViewById(R.id.Check_Button);
        EditText Row1 = mainActivity.findViewById(R.id.Row1_Text);
        String TRUE = "1";
        String FALSE = " ";
        if(Row1.getText().toString().equals("0"))
        {
            Update_button.setText("未");
            Values.put("P_flag", TRUE);
            Row1.setEnabled(true);
            Check_button.setEnabled(false);
        }
        else
        {
            Update_button.setText("済");
            Values.put("P_flag", FALSE);
            Row1.setEnabled(false);
            Check_button.setEnabled(true);
        }

        db.update("TOKUIF", Values, " company = ?  AND  customer = ?  AND place = ? ", new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3)});  //レコード登録

    }
}
