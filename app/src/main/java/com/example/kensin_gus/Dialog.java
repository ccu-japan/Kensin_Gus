package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {
    View view;
   AlertDialog alertDialog;


    @SuppressLint({"InflateParams", "SetTextI18n"})
    public void Dialog_SYOSAI(LayoutInflater layoutInflater, Context context , SQLiteDatabase db , int COL_BAN) {
        view = layoutInflater.inflate(R.layout.dailog_layout, null);
        EditText dialog_edit1 = view.findViewById(R.id.dialog_edit1);
        EditText dialog_edit2 = view.findViewById(R.id.dialog_edit2);
        EditText dialog_edit3 = view.findViewById(R.id.dialog_edit3);
        EditText dialog_edit4 = view.findViewById(R.id.dialog_edit4);

        //------------------------------------------------------------------------------------------
        // ***** Cursor TOKUIF rawQuery  ******
        //
        //*i=0　前回使用量 　 i=1 基本料金　　　　　 i=2　単価　i=3　メータ交換時使用量　
        // i=4　ガス料金区分　i=5　メータ交換フラグ
        //
        //------------------------------------------------------------------------------------------
        Cursor cursor = db.rawQuery("SELECT L_T_usage , S_price , U_price , M_E_usage , P_section , M_C_flag  FROM TOKUIF WHERE ban = ?", new String[]{String.valueOf(COL_BAN)});
        cursor.moveToFirst();

        if("1".equals(cursor.getString(5))){
            dialog_edit1.setText(cursor.getString(3));
        }
        if("B".equals(cursor.getString(4))){
            dialog_edit4.setText(cursor.getString(2));
        }
        dialog_edit2.setText(cursor.getString(0));
        dialog_edit3.setText(cursor.getString(1));


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view)
                .setTitle("詳細")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
        builder.show();
    }

}
