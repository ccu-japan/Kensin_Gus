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
//--------------------------------------------------------------------------------------------------
//
// *** インスタンス化　***
//
// -------------------------------------------------------------------------------------------------
    int[]   MULTIPLE_CHECKBOX   = new int[12];
    int     CHECKBOX_RESULT     = 1000;
    int     ROOT_SEARCH_RESULT  = 2000;
    int     TEN_KEY_RESULT      = 3000;
    int     CALENDAR_RESULT     = 4000;
    int     COL_BAN              = 0;

    String TODAY    ;
    String TABLE_COUNT;
    boolean CHECK_FLG = false   ;

    TOKUIF tokuif;
    Kenshin_DB kenshin_db;
    Ten_key_Process ten_key_process;
    Button_Processing button_processing ;
    Screen_Layout.Main_Screen main_screen;

    LayoutInflater layoutInflater;
    ContentValues VALUES;

    Button KENSIN_BUTTON ;
    Button DOWN_BUTTON;
    Button UP_BUTTON;
    Button FIXED_UNFIXED_BUTTON;
    Button END_BUTTON;
    Button CHECK_BUTTON;
    Button ROOT_SEARCH_BUTTON;
    Button CALENDER_SELECT_BUTTON;
    Button OUT_PUT_BUTTON;
    Button TSV_IMPORT_BUTTON;


    EditText INPUT_NUMBER_EDIT;
    TextView CURRENCT_USAGE_TEXT;
    TextView CURRENCT_AMOUNT_TEXT;
    TextView DAYS ;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------------------------------------------------------
        //
        // *** インスタンス化　***
        //
        //-------------------------------------------------------------------------------------------
        VALUES = new ContentValues();

        DOWN_BUTTON   = findViewById(R.id.down);
        UP_BUTTON      = findViewById(R.id.up);
        END_BUTTON     = findViewById(R.id.end_app);
        CHECK_BUTTON   = findViewById(R.id.Check_Button);
        ROOT_SEARCH_BUTTON = findViewById(R.id.customer_Button);
        TSV_IMPORT_BUTTON = findViewById(R.id.button6);
        KENSIN_BUTTON       = findViewById(R.id.Kensin_Button);
        CALENDER_SELECT_BUTTON     = findViewById(R.id.Calender_Button);
        FIXED_UNFIXED_BUTTON = findViewById(R.id.Update);
        OUT_PUT_BUTTON      = findViewById(R.id.OUT_PUT_BUTTON);

        kenshin_db = new Kenshin_DB(getApplicationContext());
        tokuif = new TOKUIF();
        ten_key_process = new Ten_key_Process();

        button_processing = new Button_Processing();
        main_screen = new Screen_Layout.Main_Screen();
        layoutInflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog();

        INPUT_NUMBER_EDIT = findViewById(R.id.Row1_Text);
        INPUT_NUMBER_EDIT.setSelection(INPUT_NUMBER_EDIT.getText().length());
        CURRENCT_USAGE_TEXT = findViewById(R.id.Row2_Text);
        CURRENCT_AMOUNT_TEXT = findViewById(R.id.Row3_Text);
        final TextView Row3_2 = findViewById(R.id.Row3_Text2);
        DAYS = findViewById(R.id.date_now);

        //-------------------------------------------------------------------------------------------
        //*** 今日の日付設定　
        //-------------------------------------------------------------------------------------------

        TODAY = main_screen.Screen_Data(this, TODAY);

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
        TABLE_COUNT ="SELECT TABLECOUNT(*) FROM TOKUIF";


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
        TSV_IMPORT_BUTTON.setOnClickListener(new View.OnClickListener() {
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
        DOWN_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Usaged_now = INPUT_NUMBER_EDIT.getText().toString();
                String Usaged_now2 = CURRENCT_USAGE_TEXT.getText().toString();
                String price = CURRENCT_AMOUNT_TEXT.getText().toString();
                String Tax = Row3_2.getText().toString();


                tokuif.Usaged(kenshin_db.db, Usaged_now, VALUES, Usaged_now2, COL_BAN, TODAY);
                tokuif.TAX_PRICE(price, Tax, VALUES, kenshin_db.db, COL_BAN,DAYS.getText().toString());
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
        UP_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Usaged_now = INPUT_NUMBER_EDIT.getText().toString();
                String Usaged_now2 = CURRENCT_USAGE_TEXT.getText().toString();
                String price = CURRENCT_AMOUNT_TEXT.getText().toString();
                String Tax = Row3_2.getText().toString();


                tokuif.Usaged(kenshin_db.db, Usaged_now, VALUES, Usaged_now2, COL_BAN, TODAY);
                tokuif.TAX_PRICE(price, Tax, VALUES, kenshin_db.db, COL_BAN , DAYS.getText().toString());
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

        INPUT_NUMBER_EDIT.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    try {
                        CURRENCT_USAGE_TEXT.setText(Calc_class.Calc_Used(Float.parseFloat(INPUT_NUMBER_EDIT.getText().toString()), kenshin_db.db, COL_BAN));
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

                Calc_class.Calc_HYOF_PRICE(Float.parseFloat(CURRENCT_USAGE_TEXT.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);
                return handled;
            }

        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 金額テキスト　詳細ダイアログタスク *****
        //
        //-------------------------------------------------------------------------------------------------------
        CURRENCT_AMOUNT_TEXT.setOnClickListener(new View.OnClickListener() {
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
        KENSIN_BUTTON.setOnClickListener(new View.OnClickListener() {
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
        FIXED_UNFIXED_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHECK_FLG = button_processing.Check_task(INPUT_NUMBER_EDIT, CHECK_BUTTON, FIXED_UNFIXED_BUTTON, KENSIN_BUTTON ,CHECK_FLG);
            }
        });

        END_BUTTON.setOnClickListener(new View.OnClickListener() {
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
        ROOT_SEARCH_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent root_search = new Intent(getApplication(), Root_Search.class);
                    root_search.setAction(Intent.ACTION_VIEW);
                    startActivityForResult(root_search, ROOT_SEARCH_RESULT);
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
        CALENDER_SELECT_BUTTON.setOnClickListener(new View.OnClickListener() {
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
        CHECK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MULTIPLE_CHECKBOX = TOKUIF.Check_Result(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX);

                Intent check_activity = new Intent(getApplication(), CheckActivity.class);

                check_activity.putExtra("CHECK_KEY", MULTIPLE_CHECKBOX);
                check_activity.setAction(Intent.ACTION_VIEW);
                startActivityForResult(check_activity, CHECKBOX_RESULT);
            }
        });

        //--------------------------------------------------------------------------------------------------------
        //
        // ***** 出力ボタンタスク *****
        //
        //--------------------------------------------------------------------------------------------------------
        OUT_PUT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認ダイアログ")
                        .setMessage("CSVを出力しますか？")
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
                        // *** TSV出力タスク
                        //-----------------------------------------------------------------------------------------------
                        tokuif.OUT_PUT_TSV(kenshin_db.db);

                        new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                .setMessage("CSVを出力しました")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }
                }).show();

            }
        });


    }

    @Override
    protected void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(REQUEST_CODE, resultCode, intent);

        if (resultCode == RESULT_OK && REQUEST_CODE ==  CHECKBOX_RESULT  && null != intent) {
            MULTIPLE_CHECKBOX = intent.getIntArrayExtra("CHECK_KEY");
            tokuif.Check_Result_return(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX);

        }

        if (resultCode== RESULT_OK && REQUEST_CODE == ROOT_SEARCH_RESULT && null != intent) {
            COL_BAN = intent.getIntExtra("ROOT_KEY", 0);
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
        }

        if(resultCode == RESULT_OK && REQUEST_CODE == TEN_KEY_RESULT && null != intent){
            INPUT_NUMBER_EDIT.setText(intent.getStringExtra("RESULT_KEY"));
            CURRENCT_USAGE_TEXT.setText(Calc_class.Calc_Used(Float.parseFloat(INPUT_NUMBER_EDIT.getText().toString()), kenshin_db.db, COL_BAN));
            Calc_class.Calc_HYOF_PRICE(Float.parseFloat(CURRENCT_USAGE_TEXT.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);

        }

        if(resultCode == RESULT_OK && REQUEST_CODE == CALENDAR_RESULT && null != intent){
            TODAY = intent.getStringExtra("CALENDAR_KEY");
            DAYS.setText(TODAY);

        }
    }

}