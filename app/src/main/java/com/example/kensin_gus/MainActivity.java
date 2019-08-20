package com.example.kensin_gus;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    boolean check = false;
    String Date = "";
    int[] Check_Box = new int[12];
    int RequestCode = 1000;
    int root_search_Code = 2000;
    int TEN_KEY_RESULT = 3000;
    int CALENDAR_RESULT = 4000;

    TOKUIF tokuif = null;
    Kenshin_DB kenshin_db = null;
    Button_Processing button_processing ;
    Ten_key_Process ten_key_process;

    Button kensin_button ;
    Button Down;
    Button Up;
    Button Update;
    Button End;
    Button Check;
    Button root_search_button;
    Button Calender_select;


    EditText Row1 ;
    TextView Row2 ;
    TextView Row3 ;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------------------------------------------------------
        //
        // *** インスタンス化　***
        //
        //-------------------------------------------------------------------------------------------
        final ContentValues values = new ContentValues();
        Button tsv_Import = findViewById(R.id.button6);
        Down = findViewById(R.id.down);
        Up = findViewById(R.id.up);
        Update = findViewById(R.id.Update);
        End = findViewById(R.id.end_app);
        Check = findViewById(R.id.Check_Button);
        root_search_button = findViewById(R.id.customer_Button);
        kensin_button = findViewById(R.id.Kensin_Button);
        Calender_select = findViewById(R.id.Calender_Button);

        kenshin_db = new Kenshin_DB(getApplicationContext());
        tokuif = new TOKUIF();
        ten_key_process = new Ten_key_Process();

        final HYOF hyof = new HYOF();
        button_processing = new Button_Processing();
        final Screen_Layout.Main_Screen main_screen = new Screen_Layout.Main_Screen();
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog();

        Row1 = findViewById(R.id.Row1_Text);
        Row1.setSelection(Row1.getText().length());
        Row2 = findViewById(R.id.Row2_Text);
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
        if (!this.getDatabasePath(Kenshin_DB.DB_NAME).exists()) {
            kenshin_db.db.execSQL("DROP TABLE IF EXISTS TOKUIF");
            kenshin_db.db.execSQL("DROP TABLE IF EXISTS HYOF");
            kenshin_db.onCreate(kenshin_db.db);
        }

        //-------------------------------------------------------------------------------------------
        //
        //*** メイン画面、テキスト出力(COL_BAN : 1 )
        //
        //-------------------------------------------------------------------------------------------

        COL_BAN++;
        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

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

                        tokuif.TOKUIF_CSV(kenshin_db.db, MainActivity.this);
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
                                        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

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
                        Row2.setText(Calc_class.Calc_Used(Float.parseFloat(Row1.getText().toString()), kenshin_db.db, COL_BAN));
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

                Calc_class.Calc_HYOF_PRICE(Float.parseFloat(Row2.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);
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
                dialog.Dialog_SYOSAI(layoutInflater, MainActivity.this, kenshin_db.db, COL_BAN);
            }
        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 検針ボタン　タスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        kensin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent TEN_KEY_INTENT = new Intent(getApplication(),Ten_key_Process.class);
               TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
               startActivityForResult(TEN_KEY_INTENT,TEN_KEY_RESULT);
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
                check = button_processing.Check_task(Row1, Check, Update, kensin_button ,check);
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
        // ***** 道順ボタンタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        root_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent root_search = new Intent(getApplication(), Root_Search.class);
                    root_search.setAction(Intent.ACTION_VIEW);
                    startActivityForResult(root_search, root_search_Code);
                } catch (RuntimeException e) {
                    Log.d("", "error");
                }

            }

        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 日付ボタンタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        Calender_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calender_intent = new Intent(getApplication(),Calendar_Select.class);
                calender_intent.setAction(Intent.ACTION_VIEW);
                startActivityForResult(calender_intent,CALENDAR_RESULT);
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
                Check_Box = TOKUIF.Check_Result(kenshin_db.db, COL_BAN, Check_Box);

                Intent check_activity = new Intent(getApplication(), CheckActivity.class);

                check_activity.putExtra("CHECK_KEY", Check_Box);
                check_activity.setAction(Intent.ACTION_VIEW);
                startActivityForResult(check_activity, RequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && requestCode == RequestCode && null != intent) {
            Check_Box = intent.getIntArrayExtra("CHECK_KEY");
            tokuif.Check_Result_return(kenshin_db.db, COL_BAN, Check_Box);

        }

        if (resultCode== RESULT_OK && requestCode == root_search_Code && null != intent) {
            COL_BAN = intent.getIntExtra("ROOT_KEY", 0);
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
        }

        if(resultCode == RESULT_OK && requestCode == TEN_KEY_RESULT && null != intent){
            Row1.setText(intent.getStringExtra("RESULT_KEY"));
            Row2.setText(Calc_class.Calc_Used(Float.parseFloat(Row1.getText().toString()), kenshin_db.db, COL_BAN));
            Calc_class.Calc_HYOF_PRICE(Float.parseFloat(Row2.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);

        }
    }

}