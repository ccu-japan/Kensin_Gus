package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("Registered")
public class Button_Processing{

    //----------------------------------------------------------------------------------------------------
    // ***
    //----------------------------------------------------------------------------------------------------
    public  boolean Check_task(EditText Row1 ,Button check_button ,Button update_button ,Button kensin_button ,boolean check){
        if(!check) {
            update_button.setText("済");
            update_button.setBackgroundColor(Color.BLUE);
            Row1.setEnabled(false);
            check_button.setEnabled(true);
            kensin_button.setEnabled(false);
            check = true;
        }
        else
            {
            update_button.setText("未");
            update_button.setBackgroundColor(Color.RED);
            Row1.setEnabled(true);
            check_button.setEnabled(false);
            kensin_button.setEnabled(true);
            check = false;
        }

        return check;
    }

    //-----------------------------------------------------------------------------------------------
    //
    // * Up,Downボタンの操作
    //
    //-----------------------------------------------------------------------------------------------
    public void Up_Down_Button(MainActivity mainActivity, SQLiteDatabase db , int COL_BAN )
    {
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("SELECT /*i:0*/ P_flag ,/*i:1*/ company ,/*i:2*/ customer , /*i:3*/ place , /*i:4*/T_T_pointer FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

        final Button Update_button = mainActivity.findViewById(R.id.Update);
        final Button Check_button = mainActivity.findViewById(R.id.Check_Button);
        Button kensin_button = mainActivity.findViewById(R.id.Kensin_Button);
        EditText Row1 = mainActivity.findViewById(R.id.Row1_Text);
        String TRUE = "1";
        String FALSE = "";
        if(Row1.getText().toString().equals("0"))
        {
            Update_button.setBackgroundColor(Color.RED);
            Update_button.setText("未");
            contentValues.put("P_flag",FALSE);
            Row1.setEnabled(true);
            Check_button.setEnabled(false);
            kensin_button.setEnabled(true);
        }
        else
        {
            Update_button.setBackgroundColor(Color.BLUE);
            Update_button.setText("済");
            contentValues.put("P_flag",TRUE);
            Row1.setEnabled(false);
            Check_button.setEnabled(true);
            kensin_button.setEnabled(false);

        }

        db.update("TOKUIF", contentValues, " ban = ? ", new String[]{String.valueOf(COL_BAN)});  //レコード登録

    }

}
