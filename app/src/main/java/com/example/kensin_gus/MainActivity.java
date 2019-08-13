package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.db_library.HYOF;
import com.example.db_library.Kenshin_DB;
import com.example.db_library.TOKUIF;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
//---------------------------------------------------------------------------------------------------
//
// *** インスタンス化　***
//
// -------------------------------------------------------------------------------------------------
    int COL_BAN = 0;
    boolean check  = false;
    String Date = "";
    int[] Check_Box = new int[12];
    int requestCode = 1000;

    TOKUIF tokuif = null;
    Kenshin_DB kenshin_db = null;

    EditText Row1 = null;
    TextView Row3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------------------------------------------------------
        //
        // *** インスタンス化　***
        //
        //-------------------------------------------------------------------------------------------
        final ContentValues values = new ContentValues();
        Button tsv_Import = findViewById(R.id.button6);
        final Button Down = findViewById(R.id.down);
        final Button Up = findViewById(R.id.up);
        final Button Update = findViewById(R.id.Update);
        final Button End = findViewById(R.id.end_app);
        final Button Check = findViewById(R.id.Check_Button);

        kenshin_db = new Kenshin_DB(getApplicationContext());
        tokuif = new TOKUIF();
        final HYOF hyof = new HYOF();
        final Button_Processing button_processing = new Button_Processing();
        final Screen_Layout.Main_Screen main_screen = new Screen_Layout.Main_Screen();
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog();

        Row1 = findViewById(R.id.Row1_Text);
        Row1.setSelection(Row1.getText().length());
        final TextView Row2 = findViewById(R.id.Row2_Text);
        Row3 = findViewById(R.id.Row3_Text);
        final TextView Row3_2 = findViewById(R.id.Row3_Text2);


        //-------------------------------------------------------------------------------------------
        //*** 今日の日付設定　
        //-------------------------------------------------------------------------------------------

        Date = main_screen.Screen_Data(this, Date);

        //-------------------------------------------------------------------------------------------
        //*** 全件削除を行いたい時に使う専用タスク
        //-------------------------------------------------------------------------------------------

        //kenshin_db.db.execSQL("DROP TABLE IF EXISTS TOKUIF");

        //-------------------------------------------------------------------------------------------
        //
        //*** データベースの存在確認　getDatabasePath
        //
        //-------------------------------------------------------------------------------------------
        if (!this.getDatabasePath(Kenshin_DB.DB_NAME).exists()) {
            new Kenshin_DB(this);
            Log.d("Database", "データベースを作成しました。");
        } else {
            Log.d("Database", "既に存在しています");
        }

        //-------------------------------------------------------------------------------------------
        //
        //*** 起動後、データがない場合、ファイル読込
        //
        //-------------------------------------------------------------------------------------------
        try {
            @SuppressLint("Recycle") Cursor cursor = kenshin_db.db.rawQuery("SELECT * FROM " + TOKUIF.DB_TABLE, null);
            Log.d("data_low", "データ有。新規読込をｷｬﾝｾﾙ");
        } catch (SQLiteException e) {
            tokuif.onCreate(kenshin_db.db);
            new TOKUIF().TOKUIF_CSV(kenshin_db.db, this);
            Log.d("data_low", "ファイルを読み込みました。");
        }

        try {
            @SuppressLint("Recycle") Cursor cursor1 = kenshin_db.db.rawQuery("SELECT * FROM " + HYOF.DB_TABLE, null);
            Log.d("data_low", "データ有。新規読込をｷｬﾝｾﾙ");
        } catch (SQLiteException e) {
            hyof.CreateTBL_HYOF(kenshin_db.db);
            new HYOF().HYOF_CSV(kenshin_db.db, this);
            Log.d("data_low", "ファイルを読み込みました。");
        }

        //-------------------------------------------------------------------------------------------
        //
        //*** メイン画面、テキスト出力(COL_BAN : 1 )
        //
        //-------------------------------------------------------------------------------------------

        COL_BAN++;
        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
        Log.d("COL_BAN", String.valueOf(COL_BAN));

        //-------------------------------------------------------------------------------------------
        //
        //CSV入力(button6)
        //テーブル作成　及び　削除
        //
        //テーブルが存在しない場合 作成
        //テーブルが存在する場合　削除後　作成　（重複を防ぐため）
        //
        //-------------------------------------------------------------------------------------------
        tsv_Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認ダイアログ")
                        .setMessage("CSVを取り込みますか？")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int idx) {
                                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                        .setMessage("CSVの取込をｷｬﾝｾﾙしました")
                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //---------------------------------------------------------------------
                                                // *取込キャンセル後の判定
                                                //---------------------------------------------------------------------
                                            }
                                        }).show();
                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //-----------------------------------------------------------------------------------------------
                        //テーブル削除。
                        //-----------------------------------------------------------------------------------------------
                        kenshin_db.allDelete("TOKUIF");
                        kenshin_db.allDelete("HYOF");
                        Log.d("Cursor", "データを消去します");

                        new TOKUIF().TOKUIF_CSV(kenshin_db.db, MainActivity.this);
                        new HYOF().HYOF_CSV(kenshin_db.db, MainActivity.this);

                        Log.d("Cursor", "CSVを読み込みました");

                        new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                .setMessage("CSVを取り込みました")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //--------------------------------------------------------------------------------
                                        //
                                        //*** 取り込んだ後のテキスト処理 ***
                                        //
                                        //--------------------------------------------------------------------------------
                                        COL_BAN = 1;
                                        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
                                        Update.setBackgroundColor(Color.RED);
                                        Update.setText("未");
                                        Check.setEnabled(true);
                                    }
                                }).show();
                    }
                }).show();
            }
        });
        //-----------------------------------------------------------------------------------------------------------------
        // *** DOWN　: 選択ボタン判定 ***
        //
        //COL_BAN = TOKUIFの連番と連動　COLBAN = 1 の時　TOKUIF.ban = 1 の人を指す
        //
        //-----------------------------------------------------------------------------------------------------------------
        Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Usaged_now = Row1.getText().toString();
                String Usaged_now2 = Row2.getText().toString();
                String price = Row3.getText().toString();
                String Tax = Row3_2.getText().toString();


                tokuif.Usaged(kenshin_db.db, Usaged_now, values, Usaged_now2, COL_BAN, Date);
                tokuif.TAX_PRICE(price, Tax, values, kenshin_db.db, COL_BAN);
                button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);


                COL_BAN++;
                COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
                button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

                Log.d("COL_BAN", String.valueOf(COL_BAN));

            }
        });
        //-----------------------------------------------------------------------------------------------------------------
        //
        // *** UP : 選択ボタン判定 ***
        //
        //-----------------------------------------------------------------------------------------------------------------
        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Usaged_now = Row1.getText().toString();
                String Usaged_now2 = Row2.getText().toString();
                String price = Row3.getText().toString();
                String Tax = Row3_2.getText().toString();


                tokuif.Usaged(kenshin_db.db, Usaged_now, values, Usaged_now2, COL_BAN, Date);
                tokuif.TAX_PRICE(price, Tax, values, kenshin_db.db, COL_BAN);
                button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);


                COL_BAN--;
                COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);

                button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

                Log.d("COL_BAN", String.valueOf(COL_BAN));

            }
        });

        //------------------------------------------------------------------------------------------------------------------
        //
        // ***** 使用量計算 *****
        //
        //------------------------------------------------------------------------------------------------------------------

        Row1.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    try {
                        final String Row1_text = Row1.getText().toString();
                        Row2.setText(Calc_class.Calc_Used(Float.parseFloat(Row1_text), kenshin_db.db, COL_BAN));
                        handled = true;

                    } catch (NumberFormatException e) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                .setMessage("数字以外が入力されました")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //--------------------------------------------------------------------------
                                        //
                                        //
                                        //
                                        //--------------------------------------------------------------------------
                                    }
                                }).show();
                    }
                }
                //--------------------------------------------------------------------------------------------------------
                //
                // ***** 金額、内消費税　入力タスク *****
                //
                //-------------------------------------------------------------------------------------------------------
                final String Row2_text = Row2.getText().toString();
                Calc_class.Calc_HYOF_PRICE(Float.parseFloat(Row2_text), kenshin_db.db, COL_BAN, MainActivity.this);
                return handled;
            }

        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 金額テキスト　詳細ダイアログタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        Row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.Dialog_SYOSAI(layoutInflater , MainActivity.this , kenshin_db.db , COL_BAN);
            }
        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 確定・未入力変更ボタンタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = button_processing.Check_task(Row1, Check, Update, check);
            }
        });

        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kenshin_db.close();
                COL_BAN = 0;
                finish();
            }
        });
        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 点検ボタンタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_Box = TOKUIF.Check_Result(kenshin_db.db , COL_BAN , Check_Box);

                Intent check_activity = new Intent(getApplication(), Check2Activity.class);

                check_activity.putExtra("CHECK_KEY",Check_Box);
                check_activity.setAction(Intent.ACTION_VIEW);
                startActivityForResult(check_activity,requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK && null != intent) {
            Check_Box = intent.getIntArrayExtra("CHECK_KEY");
            tokuif.Check_Result_return(kenshin_db.db , COL_BAN , Check_Box);


        }
    }
}