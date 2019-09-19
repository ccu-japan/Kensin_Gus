package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.db_library.Kenshin_DB;

@SuppressLint("Registered")
public class Root_Search extends AppCompatActivity {

    //---------------------------------------------------------------------------------------------
    //
    // ***  グローバル変数　インスタンス  ***
    //
    // Search_Flg           0 : moveToFirst , 1 : moveToNext , 2: moveToPrevious
    // IndexOut_Flg         0 : 最初のデータを指す　1 : 最後のデータを指す 99 : 初期値
    // BUTTON_SELECT_FLG    0 : 1 :
    //---------------------------------------------------------------------------------------------
    ConstraintLayout constraintLayout;
    InputMethodManager inputMethodManager;

    Kenshin_DB kenshin_db = null;
    Cursor cursor = null;
    Intent root_search;

   static final int EXCEPTION_FLG = 99;
    static final int SEARCH_FLG1 = 1;
    static final int SEARCH_FLG2 = 2;
    static final int SEARCH_FLG3 = 3;
    static final int SEARCH_FLG4 = 4;

    int SUBSCRIPT_LENGTH = 0;

    int BUTTON_SELECT_FLG = 0;
    int Array_Subscript_Vertical;
    int Array_Subscript_Size;
    String[][] cursor_data_input = null;

    EditText customer_code ;
    EditText G_code;
    EditText Group_K_jun_C;
    EditText Group_K_jun_G;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_search);

        root_search = getIntent();

        kenshin_db = new Kenshin_DB(this);

        final Button Not_yet_Button = findViewById(R.id.Kensin_Button);
        final Button customer_Button = findViewById(R.id.customer_Button);
        final Button Group_Button = findViewById(R.id.Printer_Button);
        final Button Group_K_jun_Button = findViewById(R.id.Calender_Button);
        final Button Up_Button = findViewById(R.id.Up);
        final Button Down_Button = findViewById(R.id.Down);
        final Button OK_Button = findViewById(R.id.OK);

        cursor = kenshin_db.db.rawQuery("SELECT * FROM TOKUIF ", null);
        cursor_data_input = new String[cursor.getCount()][7];
        cursor = null;

        customer_code = findViewById(R.id.customercode);
        G_code         = findViewById(R.id.Code01);
        Group_K_jun_C = findViewById(R.id.Code02);
        Group_K_jun_G = findViewById(R.id.Code03);

        constraintLayout =findViewById(R.id.root_search_layout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //---------------------------------------------------------------------------------------------
        // *** Root_Search 道順タスク　***
        //
        //  i = 0 : 得意先ｺｰﾄﾞ　i = 1 : 群ｺｰﾄﾞ　i = 2 :　道順
        //　i = 3 : 道順        i = 4
        //
        //---------------------------------------------------------------------------------------------

        //---------------------------------------------------------------------------------------------
        //
        // ***  Not_yet_Button  クラス  ***
        //
        // BUTTON_SELECT_FLG = 1
        //
        // ---------------------------------------------------------------------------------------------
        Not_yet_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG1;
                Select_Cursor();

                if(cursor_data_input.length > 0) {
                    if (cursor_data_input[0][0] != null) {
                        Cursor_VIEW();
                    }
                }
                else{
                    new AlertDialog.Builder(Root_Search.this)
                            .setTitle("未検針データダイアログ")
                            .setMessage("\nデータがありません")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });
        //---------------------------------------------------------------------------------------------
        //
        // ***  customer_Button クラス  ***
        //
        // BUTTON_SELECT_FLG = 2
        //
        // ---------------------------------------------------------------------------------------------
        customer_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG2;
                Select_Cursor();
                if(cursor_data_input.length > 0) {
                    if (cursor_data_input[0][0] != null) {
                        Cursor_VIEW();
                    }
                }
                else{
                    new AlertDialog.Builder(Root_Search.this)
                            .setTitle("得意先コードダイアログ")
                            .setMessage("\nデータがありません")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });


        //---------------------------------------------------------------------------------------------
        //
        // ***  Group_Button クラス  ***
        //
        // BUTTON_SELECT_FLG = 3
        //
        // ---------------------------------------------------------------------------------------------
        Group_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG3;
                Select_Cursor();
                if(cursor_data_input.length > 0) {
                    if (cursor_data_input[0][0] != null) {
                        Cursor_VIEW();
                    }
                }
                else{
                    new AlertDialog.Builder(Root_Search.this)
                            .setTitle("群コードダイアログ")
                            .setMessage("\nデータがありません")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });

        //---------------------------------------------------------------------------------------------
        //
        // ***  Group_K_jun_Button クラス  ***
        //
        // BUTTON_SELECT_FLG = 4
        //
        // ---------------------------------------------------------------------------------------------
        Group_K_jun_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG4;
                Select_Cursor();
                if(cursor_data_input.length > 0) {
                    if (cursor_data_input[0][0] != null) {
                        Cursor_VIEW();
                    }
                }
                else{
                    new AlertDialog.Builder(Root_Search.this)
                            .setTitle("群コード・得意先コードダイアログ")
                            .setMessage("\nデータがありません")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });

        //---------------------------------------------------------------------------------------------
        //
        // ***  Up_Button クラス  ***
        //
        // ---------------------------------------------------------------------------------------------
        Up_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Array_Subscript_Vertical++;
                Cursor_VIEW();
            }
        });

        //---------------------------------------------------------------------------------------------
        //
        // ***  Down_Button クラス  ***
        //
        // ---------------------------------------------------------------------------------------------
        Down_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Array_Subscript_Vertical--;
                Cursor_VIEW();
            }
        });


        OK_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    if (cursor_data_input.length == 0) {
                        finish();
                    }
                    else {
                        int cursor_data = Integer.parseInt(cursor_data_input[Array_Subscript_Vertical][6]);
                        root_search.putExtra("ROOT_KEY", cursor_data);
                        setResult(RESULT_OK, root_search);
                        finish();
                    }
                } catch (NullPointerException e) {
                    finish();
                } catch (NumberFormatException e) {
                    finish();
                }
            }
        });
    }


    //---------------------------------------------------------------------------------------------
    //
    // *** 検索ボタン　Cursor選択クラス  ***
    //
    //---------------------------------------------------------------------------------------------
    public void Select_Cursor() {

        switch (BUTTON_SELECT_FLG) {
            case SEARCH_FLG1:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? ", new String[]{" "});
                break;

            case SEARCH_FLG2:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? AND customer = ? ", new String[]{" ", customer_code.getText().toString()});
                break;

            case SEARCH_FLG3:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? AND G_code = ? ", new String[]{" ", G_code.getText().toString()});
                break;

            case SEARCH_FLG4:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? AND G_code = ? AND customer = ? ", new String[]{" ", Group_K_jun_C.getText().toString(), Group_K_jun_G.getText().toString()});
                break;

            case EXCEPTION_FLG:
                break;
        }

        if ((cursor.moveToFirst())) {

            Array_Subscript_Vertical = 0;
            Array_Subscript_Size = 0;

            do {

                for (Array_Subscript_Size = 0; Array_Subscript_Size < cursor.getColumnCount(); Array_Subscript_Size++) {
                    cursor_data_input[Array_Subscript_Vertical][Array_Subscript_Size] = cursor.getString(Array_Subscript_Size);
                }
                Array_Subscript_Vertical++;
            } while (cursor.moveToNext());
            Array_Subscript_Vertical = 0;
        }
        SUBSCRIPT_LENGTH = cursor_data_input.length;

    }

    //---------------------------------------------------------------------------------------------
    //
    // *** 下部ボックス内　描画クラス  ***
    //
    //---------------------------------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    public void Cursor_VIEW() {

        final TextView G_code = findViewById(R.id.G_code);
        final TextView K_jun = findViewById(R.id.K_jun);
        final TextView Name1 = findViewById(R.id.Name1);
        final TextView Name2 = findViewById(R.id.Name2);
        final TextView CustomCode = findViewById(R.id.CustomCode);
        final TextView P_name = findViewById(R.id.P_name);

        if (Array_Subscript_Vertical < 0) {
            Array_Subscript_Vertical = 0;
        }
        else
            {
            try {
                if (cursor_data_input[Array_Subscript_Vertical][0] == null) {
                    Array_Subscript_Vertical--;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Array_Subscript_Vertical--;
            }
        }
        Log.d("math", "" + Array_Subscript_Vertical);


        try {
            G_code.setText(cursor_data_input[Array_Subscript_Vertical][0]);
            K_jun.setText(cursor_data_input[Array_Subscript_Vertical][1]);
            Name1.setText(cursor_data_input[Array_Subscript_Vertical][2]);
            Name2.setText(cursor_data_input[Array_Subscript_Vertical][3]);
            CustomCode.setText(cursor_data_input[Array_Subscript_Vertical][4]);
            P_name.setText(cursor_data_input[Array_Subscript_Vertical][5]);

        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d("", "配列外にでました");
        }
    }
    public  boolean onTouchEvent(MotionEvent event){
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        constraintLayout.requestFocus();

        return false;
    }
}
