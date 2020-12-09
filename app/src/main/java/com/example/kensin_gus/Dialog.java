package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {
    View view;

    @SuppressLint({"InflateParams", "SetTextI18n"})
    //――――――――――――――――――――――――――――――
    //  使用金額詳細ダイアログメソッド
    //――――――――――――――――――――――――――――――
    public void Dialog_SYOSAI(LayoutInflater layoutInflater, Context context , SQLiteDatabase db , int COL_BAN) {
        view = layoutInflater.inflate(R.layout.dailog_layout, null);
        
        Cursor cursor = db.rawQuery("SELECT L_T_usage , S_price , U_price , M_E_usage , P_section , M_C_flag  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

                                                                            //ガス料金区分の判定
        if(cursor.getString(4).equals("B"))
            ((EditText)view.findViewById(R.id.dialog_edit4)).setText(cursor.getString(2));
        
                                                                            //メータ交換フラグの判定
        if(cursor.getString(5).equals("1"))
            ((EditText)view.findViewById(R.id.dialog_edit1)).setText(cursor.getString(3));
        
                                                                            //前回使用量
        ((EditText)view.findViewById(R.id.dialog_edit2)).setText(cursor.getString(0));

                                                                            //基本料金
        ((EditText)view.findViewById(R.id.dialog_edit3)).setText(cursor.getString(1));

                                                                            //MainActivityで出力
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setTitle("詳細")
                .setPositiveButton("確認", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
        builder.show();
    }

}
