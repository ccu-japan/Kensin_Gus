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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.db_library.Kenshin_DB;

@SuppressLint("Registered")
public class Root_Search extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    InputMethodManager inputMethodManager;

    Kenshin_DB kenshin_db = null;
    Cursor cursor = null;
    Intent root_search;

    static final int SEARCH_FLG1 = 1;       // 未検針データ検索
    static final int SEARCH_FLG2 = 2;       // 得意先コード検索
    static final int SEARCH_FLG3 = 3;       // 群コード検索
    static final int SEARCH_FLG4 = 4;       // 得意先、群コード検索

    int SUBSCRIPT_LENGTH = 0;
    int BUTTON_SELECT_FLG;                   //検索ボタンフラグ
    int Array_line;                           //データ配列　行番号
    int Array_column;                         //データ配列　列番号
    String[][] cursor_data_input = null;
    int TEN_KEY_RESULT = 1;

    EditText customer_code ;
    EditText G_code;
    EditText Group_K_jun_C;
    EditText Group_K_jun_G;

    Button Not_yet_Button;
    Button customer_Button;
    Button Group_Button;
    Button Group_K_jun_Button;
    Button Up_Button;

    Button Down_Button;
    Button OK_Button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_search);

        root_search = getIntent();
        kenshin_db = new Kenshin_DB(this);

        Not_yet_Button = findViewById(R.id.Kensin_Button);
        customer_Button = findViewById(R.id.customer_Button);
        Group_Button = findViewById(R.id.Printer_Button);
        Group_K_jun_Button = findViewById(R.id.Calender_Button);
        Up_Button = findViewById(R.id.Up);

        Down_Button = findViewById(R.id.Down);
        OK_Button = findViewById(R.id.OK);

        cursor = kenshin_db.db.rawQuery("SELECT * FROM TOKUIF ", null);
        cursor_data_input = new String[cursor.getCount()][7];   //検索人数分をカラム数に挿入
        cursor = null;

        customer_code = findViewById(R.id.customercode);
        G_code         = findViewById(R.id.Code01);
        Group_K_jun_C = findViewById(R.id.Code02);
        Group_K_jun_G = findViewById(R.id.Code03);

        findViewById(R.id.customercode).setOnClickListener(Input_text);
        findViewById(R.id.Code01).setOnClickListener(Input_text);
        findViewById(R.id.Code02).setOnClickListener(Input_text);
        findViewById(R.id.Code03).setOnClickListener(Input_text);

        constraintLayout =findViewById(R.id.root_search_layout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        //――――――――――――――――――――――――――――――――――――
        // 未検針データ検索メソッド
        //――――――――――――――――――――――――――――――――――――
        Not_yet_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG1;
                Select_Cursor();
                //データの検索
                if(cursor_data_input.length > 0) {
                    //データを配列に挿入
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
        //――――――――――――――――――――――――――――――――――――
        // 得意先コード検索メソッド
        //――――――――――――――――――――――――――――――――――――
        customer_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG2;
                Select_Cursor();
                //データの検索
                if(cursor_data_input.length > 0) {
                    //データを配列に挿入
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

        //――――――――――――――――――――――――――――――――――――
        // 群コード検索メソッド
        //――――――――――――――――――――――――――――――――――――
        Group_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG3;
                Select_Cursor();
                //データの検索
                if(cursor_data_input.length > 0) {
                    //データを配列に挿入
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

        //――――――――――――――――――――――――――――――――――――
        // 群コード、検針順　検索メソッド
        //――――――――――――――――――――――――――――――――――――
        Group_K_jun_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUTTON_SELECT_FLG = SEARCH_FLG4;
                Select_Cursor();
                //データの検索
                if(cursor_data_input.length > 0) {
                    //データを配列に挿入
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

        //――――――――――――――――――――――――――――――――――――
        // 前の人を検索するメソッド
        //――――――――――――――――――――――――――――――――――――
        Up_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //行番号を増やす
                Array_line++;
                Cursor_VIEW();
            }
        });

        //――――――――――――――――――――――――――――――――――――
        //  次の人を検索するメソッド
        //――――――――――――――――――――――――――――――――――――
        Down_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //行番号を減らす
                Array_line--;
                Cursor_VIEW();
            }
        });

        OK_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    //検索せずに終わる場合
                    if (cursor_data_input.length == 0) {
                        finish();
                    }
                    //検索していた場合
                    else {
                        int cursor_data = Integer.parseInt(cursor_data_input[Array_line][6]);
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

    //――――――――――――――――――――――――――――――――――――
    // 検索ボタンメソッド
    //――――――――――――――――――――――――――――――――――――

    public void Select_Cursor() {

        //検索ボタンの確認
        switch (BUTTON_SELECT_FLG) {

            //未検針データ検索
            case SEARCH_FLG1:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? ", new String[]{" "});
                break;

            //得意先コード検索
            case SEARCH_FLG2:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? AND customer = ? ", new String[]{" ", customer_code.getText().toString()});
                break;

            //群コード検索
            case SEARCH_FLG3:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF WHERE P_flag = ? AND G_code = ? ", new String[]{" ", G_code.getText().toString()});
                break;

            //群コード,検針順　検索
            case SEARCH_FLG4:
                cursor = kenshin_db.db.rawQuery("SELECT G_code ,  K_jun , C_name1 , C_name2 , customer , P_name , ban FROM TOKUIF " +
                                                        "WHERE P_flag = ? AND G_code = ? AND K_jun = ? ", new String[]{" ", Group_K_jun_C.getText().toString(),(Group_K_jun_G.getText().toString())});
                break;
        }
        if ((cursor.moveToFirst())) {
            Array_line = 0;
            Array_column = 0;
            do {
                //検索データ数分
                for (Array_column = 0; Array_column < cursor.getColumnCount(); Array_column++) {
                    cursor_data_input[Array_line][Array_column] = cursor.getString(Array_column);
                }
                Array_line++;
            } while (cursor.moveToNext());
            Array_line = 0;
        }
        SUBSCRIPT_LENGTH = cursor_data_input.length;
    }

    //――――――――――――――――――――――――――――――――――――

    // 検索データ表示メソッド
    //――――――――――――――――――――――――――――――――――――
    @SuppressLint("SetTextI18n")
    public void Cursor_VIEW() {

        final TextView G_code = findViewById(R.id.G_code);
        final TextView K_jun = findViewById(R.id.K_jun);
        final TextView Name1 = findViewById(R.id.Name1);
        final TextView Name2 = findViewById(R.id.Name2);
        final TextView CustomCode = findViewById(R.id.CustomCode);
        final TextView P_name = findViewById(R.id.P_name);

        //データ行が0以下になった時
        if (Array_line < 0) {
            Array_line = 0;
        }
        else
            {
            try {
                if (cursor_data_input[Array_line][0] == null) {
                    Array_line--;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Array_line--;
            }
        }
        Log.d("math", "" + Array_line);

        //データ表示
        try {
            G_code.setText(cursor_data_input[Array_line][0]);       //群コード
            K_jun.setText(cursor_data_input[Array_line][1]);        //検針順
            Name1.setText(cursor_data_input[Array_line][2]);        //顧客名1
            Name2.setText(cursor_data_input[Array_line][3]);        //顧客名2
            CustomCode.setText(cursor_data_input[Array_line][4]);   //得意先コード
            P_name.setText(cursor_data_input[Array_line][5]);       //設置場所名称

        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d("", "配列外にでました");
        }
    }


    View.OnClickListener Input_text = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           int i = v.getId();

           Intent ten_key = new Intent(getApplication(),Ten_key_Process.class);
           ten_key.putExtra("BUTTON_ID",i);
           startActivityForResult(ten_key,TEN_KEY_RESULT);

        }
    };

    @Override
    protected void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(REQUEST_CODE, resultCode, intent);

        //テンキーアクティビティからの結果を受け取る
        if(resultCode == RESULT_OK && REQUEST_CODE == TEN_KEY_RESULT && null != intent){
            int TEN_KEY_RETURN = intent.getIntExtra("BUTTON_ID",-1);
            switch (TEN_KEY_RETURN){
                case R.id.customercode:
                    customer_code.setText(intent.getStringExtra("RESULT_KEY"));
                    break;

                case R.id.Code01:
                    G_code.setText(intent.getStringExtra("RESULT_KEY"));
                    break;

                case R.id. Code02:
                    Group_K_jun_C.setText(intent.getStringExtra("RESULT_KEY"));
                    break;

                case R.id. Code03:
                    Group_K_jun_G.setText(intent.getStringExtra("RESULT_KEY"));
                    break;
            }
        }


    }

    //――――――――――――――――――――――――――――――――――――
    // タッチイベントメソッド
    //――――――――――――――――――――――――――――――――――――
    public boolean onTouchEvent(MotionEvent event){
        // 画面をタッチすることでキーボード等を隠す
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        constraintLayout.requestFocus();

        return false;
    }
}
