package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    Kenshin_DB kenshin_db = null;
    Cursor cursor = null ;

    static final int TRUE_FLG = 0;
    static final int FALSE_FLG =1;
    static final int EXCEPTION_FLG = 99;
    static final int SEARCH_FLG1 = 1;
    static final int SEARCH_FLG2 = 2;
    static final int SEARCH_FLG3 = 3;
    static final int SEARCH_FLG4 = 4;

    int SUBSCRIPT_LENGTH = 0;

    int Search_Flg = 0;
    int BUTTON_SELECT_FLG = 0;
    int IndexOut_Flg = 99;
    int Array_Subscript_Vertical;
    int Array_Subscript_Size;
    String[][] cursor_data_input = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_search);

        final Intent root_search = getIntent();
        final int root_search_Code = root_search.getIntExtra("ROOT_KEY",0);

        kenshin_db = new Kenshin_DB(this);

        final Button Not_yet_Button     = findViewById(R.id.Not_yet_Button);
        final Button customer_Button    = findViewById(R.id.customer_Button);
        final Button Group_Button       = findViewById(R.id.Group_Button);
        final Button Group_K_jun_Button = findViewById(R.id.Group_K_jun_Button);
        final Button Up_Button          = findViewById(R.id.Up);
        final Button Down_Button        = findViewById(R.id.Down);
        final Button OK_Button          = findViewById(R.id.OK);

        cursor = kenshin_db.db.rawQuery( "SELECT * FROM TOKUIF ",null);
        cursor_data_input = new String[cursor.getCount()][6];
        cursor = null;


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
        // ---------------------------------------------------------------------------------------------
        Not_yet_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG1;
                Select_Cursor();
                Cursor_VIEW();

            }
        });

        Up_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Array_Subscript_Vertical++;
               Cursor_VIEW();
            }
        });

        Down_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Array_Subscript_Vertical--;
                Cursor_VIEW();
            }
        });



        OK_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root_search.putExtra("ROOT_KEY2",root_search_Code);
                root_search.putExtra("ROOT_KEY",cursor.getString(6));
                setResult(RESULT_OK,root_search);
                finish();
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

                break;


            case SEARCH_FLG3:

                break;

            case SEARCH_FLG4:

                break;

            case EXCEPTION_FLG:
                break;
        }

        if ((cursor.moveToFirst())) {
            Log.d("First",cursor.getString(2));

            Array_Subscript_Vertical = 0;
            Array_Subscript_Size = 0;

            do {

                for (Array_Subscript_Size = 0; Array_Subscript_Size < cursor.getColumnCount()-1; Array_Subscript_Size++) {
                    cursor_data_input[Array_Subscript_Vertical][Array_Subscript_Size] = cursor.getString(Array_Subscript_Size);
                }
                Array_Subscript_Vertical++;
            } while (cursor.moveToNext());
            Array_Subscript_Vertical = 0;
        }
        SUBSCRIPT_LENGTH = cursor_data_input.length;
        Log.d("math", String.valueOf(cursor_data_input.length));

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
        final TextView Name = findViewById(R.id.Name);
        final TextView CustomCode = findViewById(R.id.CustomCode);
        final TextView P_name = findViewById(R.id.P_name);


        if (Array_Subscript_Vertical > SUBSCRIPT_LENGTH)
        {
            Array_Subscript_Vertical = SUBSCRIPT_LENGTH;
        } else if (Array_Subscript_Vertical < 0)
        {
            Array_Subscript_Vertical = 0;
        }
        else {
            try {
                G_code.setText(cursor_data_input[Array_Subscript_Vertical][0]);
                K_jun.setText(cursor_data_input[Array_Subscript_Vertical][1]);
                Name.setText(cursor_data_input[Array_Subscript_Vertical][2] + cursor_data_input[Array_Subscript_Vertical][3]);
                CustomCode.setText(cursor_data_input[Array_Subscript_Vertical][4]);
                P_name.setText(cursor_data_input[Array_Subscript_Vertical][5]);

                Log.d("math"," Name :" + cursor_data_input[0][2] + cursor_data_input[0][3]);
                Log.d("math"," Name :" + cursor_data_input[Array_Subscript_Vertical][2] + cursor_data_input[Array_Subscript_Vertical][3]);

            }catch (ArrayIndexOutOfBoundsException e){
                Log.d("","配列外にでました");
            }
        }
    }
}
