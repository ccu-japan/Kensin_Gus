package com.example.kensin_gus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.db_library.HYOF;
import com.example.db_library.Kenshin_DB;
import com.example.db_library.TOKUIF;
import com.fujitsufrontech.patioprinter.fhtuprt.SdcardLog;


import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------------------------------------
    //
    // *** インスタンス化　***
    //
    // -------------------------------------------------------------------------------
    int[] MULTIPLE_CHECKBOX = new int[12];
    int CHECKBOX_RESULT     = 1000; //点検クラスの戻り値
    int ROOT_SEARCH_RESULT = 2000;  //道順クラスの戻り値
    int TEN_KEY_RESULT = 3000;  //Ten_key_Processクラスの戻り値
    int CALENDAR_RESULT = 4000; //Calendar_Selectクラスの戻り値
    int PRINTER_RESULT = 5000;  //PrintSearchクラスの戻り値
    int COL_BAN = 0;             //DBの連番
    private final int REQUEST_CODE = 1; //権限許可　


    String RETURN_ADDRESS ="";  //BlueToothアドレスの格納変数
    String TODAY;
    boolean CHECK_FLG = true;   //未/済ボタンフラグ　true:未　false:済
    Cursor cursor;

    TOKUIF tokuif;
    Kenshin_DB kenshin_db;
    Ten_key_Process ten_key_process;
    Button_Processing button_processing;
    Screen_Layout.Main_Screen main_screen;

    LayoutInflater layoutInflater;
    ContentValues VALUES;
    View main_view;

    Button KENSIN_BUTTON;
    Button DOWN_BUTTON;
    Button UP_BUTTON;
    Button FIXED_UNFIXED_BUTTON;
    Button END_BUTTON;
    Button CHECK_BUTTON;
    Button ROOT_SEARCH_BUTTON;
    Button CALENDER_SELECT_BUTTON;
    Button OUT_PUT_BUTTON;
    Button TSV_IMPORT_BUTTON;
    Button PRINTER_BUTTON;

    EditText INPUT_NUMBER_EDIT;
    TextView CURRENCT_USAGE_TEXT;
    TextView CURRENCT_AMOUNT_TEXT;
    TextView DAYS;
    TextView Row3_2;

     Dialog dialog;

    String Usaged_now;
    String Usaged_now2;
    String price;
    String Tax;
    boolean PRINT_FLG = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= 23 ) {
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
            checkPermission(permissions, REQUEST_CODE);
        }

        //-------------------------------------------------------------------------------------------//
        //                                                                                           //
        // *** インスタンス化　***                                                                    //
        //                                                                                           //
        //-------------------------------------------------------------------------------------------//
        VALUES = new ContentValues();

        DOWN_BUTTON = findViewById(R.id.down);
        UP_BUTTON = findViewById(R.id.up);
        END_BUTTON = findViewById(R.id.end_app);
        CHECK_BUTTON = findViewById(R.id.Check_Button);
        ROOT_SEARCH_BUTTON = findViewById(R.id.customer_Button);
        TSV_IMPORT_BUTTON = findViewById(R.id.button6);
        KENSIN_BUTTON = findViewById(R.id.Kensin_Button);
        CALENDER_SELECT_BUTTON = findViewById(R.id.Calender_Button);
        FIXED_UNFIXED_BUTTON = findViewById(R.id.Update);
        OUT_PUT_BUTTON = findViewById(R.id.OUT_PUT_BUTTON);
        PRINTER_BUTTON = findViewById(R.id.Printer_Button);

        kenshin_db = new Kenshin_DB(getApplicationContext());
        tokuif = new TOKUIF();
        ten_key_process = new Ten_key_Process();

        button_processing = new Button_Processing();
        main_screen = new Screen_Layout.Main_Screen();
        layoutInflater = LayoutInflater.from(this);
         dialog = new Dialog();

        INPUT_NUMBER_EDIT = findViewById(R.id.Row1_Text);
        CURRENCT_USAGE_TEXT = findViewById(R.id.Row2_Text);
        CURRENCT_AMOUNT_TEXT = findViewById(R.id.Row3_Text);
        Row3_2 = findViewById(R.id.Row3_Text2);
        DAYS = findViewById(R.id.date_now);

        //―――――――――――――――――――――――――――――――――――――――――//
        //                               今日の日付設定　                              //
        //―――――――――――――――――――――――――――――――――――――――――//

        TODAY = main_screen.Screen_Data(this);

        //―――――――――――――――――――――――――――――――――――――――――//
        //                             データベースの存在確認　                         //
        //―――――――――――――――――――――――――――――――――――――――――//

        if (!this.getDatabasePath(Kenshin_DB.DB_NAME).exists()) {
            new Kenshin_DB(this);         //起動時データベースがない場合に作成
        }

        //----------------------------------------------------------------------------//
        //                      メイン画面、テキスト出力(COL_BAN : 1 )                  //
        //----------------------------------------------------------------------------//

        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);     //TOKUIFテーブルのデータを取得

        if (cursor.getCount() > 0) {     //データがあればレイアウト入力　ない場合はスルー
            cursor.moveToFirst();
            COL_BAN = Integer.parseInt(cursor.getString(0));
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

        } else {
            KENSIN_BUTTON.setEnabled(false);
            CHECK_BUTTON.setEnabled(false);
            PRINTER_BUTTON.setEnabled(false);
        }
    }
    //―――――――――――――――――――――――――――――――――――――――――――//
    //                                  権限許可                                      //
    //―――――――――――――――――――――――――――――――――――――――――――//
    private void checkPermission(String[] permissions, int request_code) {
        ActivityCompat.requestPermissions(this,permissions,request_code);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE){
            for(int i=0;i<permissions.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    //
                    Log.d("Pemisson","許可されています");
                }else{
                    Log.d("Pemisson","許可されていません");
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       outState.putString("COL_BAN", String.valueOf(COL_BAN)) ;
       SdcardLog.addLog("COL_BANの復帰");
       outState.putString("PRINT_FLG", String.valueOf(PRINT_FLG));

    }
    @SuppressLint("NewApi")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        COL_BAN = Integer.valueOf(Objects.requireNonNull(savedInstanceState.getString("COL_BAN")));
        PRINT_FLG = Boolean.parseBoolean(savedInstanceState.getString("PRINT_FLG"));
    }


    @Override
    protected void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent intent) {        //Intent　戻り値
        super.onActivityResult(REQUEST_CODE, resultCode, intent);

        if (resultCode == RESULT_OK && REQUEST_CODE == CHECKBOX_RESULT && null != intent) {           //点検画面戻り値
            MULTIPLE_CHECKBOX = intent.getIntArrayExtra("CHECK_KEY");
            tokuif.Check_Result_return(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX, TODAY);
        }

        if (resultCode == RESULT_OK && REQUEST_CODE == ROOT_SEARCH_RESULT && null != intent) {        //道順検索　戻り値
            COL_BAN = intent.getIntExtra("ROOT_KEY", 0);
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
        }

        if (resultCode == RESULT_OK && REQUEST_CODE == TEN_KEY_RESULT && null != intent) {             //10キー画面　戻り値
            INPUT_NUMBER_EDIT.setText(intent.getStringExtra("RESULT_KEY"));
            CURRENCT_USAGE_TEXT.setText(Calc_class.Calc_Used(Float.parseFloat(INPUT_NUMBER_EDIT.getText().toString()), kenshin_db.db, COL_BAN));
            Calc_class.Calc_HYOF_PRICE(Float.parseFloat(CURRENCT_USAGE_TEXT.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);
        }

        if (resultCode == RESULT_OK && REQUEST_CODE == CALENDAR_RESULT && null != intent) {            //カレンダー画面　戻り値
            TODAY = intent.getStringExtra("CALENDAR_KEY");
            DAYS.setText(TODAY);
        }

        if(resultCode == RESULT_OK && REQUEST_CODE == PRINTER_RESULT && null != intent){               //印刷画面　戻り値
            RETURN_ADDRESS = intent.getStringExtra("PatioPrinter");
            PRINT_FLG = true;
            main_Down(main_view);
            new PrintKensin().Print_Open(COL_BAN,MainActivity.this,RETURN_ADDRESS);
        }
    }

    //-------------------------------------------------------------------------------//
    //                                 使用量　タスク                                 //
    //-------------------------------------------------------------------------------//
    public void Used_text(View view){
        dialog.Dialog_SYOSAI(layoutInflater, MainActivity.this, kenshin_db.db, COL_BAN);
    }

    //-------------------------------------------------------------------------------//
    //                                 未/済ボタンタスク                              //
    //-------------------------------------------------------------------------------//
    public void Fixed_UnFixed(View view ) {
        db_registration();  //データベース登録

        CHECK_FLG = button_processing.Check_task(INPUT_NUMBER_EDIT, CHECK_BUTTON, FIXED_UNFIXED_BUTTON, KENSIN_BUTTON, CHECK_FLG, PRINTER_BUTTON);
        if (CHECK_FLG) {
            main_Printer(view); //印刷タスク出力


        }
    }

    //-------------------------------------------------------------------------------//
    //                                 数値入力タスク                                 //
    //-------------------------------------------------------------------------------//
    public void number_form(View view )
    {
        Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
        TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
        startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
    }

    //-------------------------------------------------------------------------------//
    //                                 検針　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Kensin(View view)
    {
        Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
        TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
        startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
    }

    //-------------------------------------------------------------------------------//
    //                                 道順　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Root_Search(View view)
    {
        try {
            Intent root_search = new Intent(getApplication(), Root_Search.class);
            root_search.setAction(Intent.ACTION_VIEW);
            startActivityForResult(root_search, ROOT_SEARCH_RESULT);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------//
    //                                 印刷　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Printer(final View view) {
        main_view = view;
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("検針票を印刷しますか？")     //ダイアログ（タイトル・メッセージ）
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {   //キャンセルボタン　
                        PRINT_FLG = false;
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {    //出力ボタン
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {                    //印刷処理
                if (RETURN_ADDRESS.equals("")) {
                    Intent intent = new Intent(MainActivity.this, Print_Search.class);
                    startActivityForResult(intent, PRINTER_RESULT);
                } else {
                    PRINT_FLG = true;
                    main_Down(view);
                    new PrintKensin().Print_Open(COL_BAN, getApplication(), RETURN_ADDRESS);

                }
            }
        }).show();
    }

    //-------------------------------------------------------------------------------//
    //                                 日付　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Calendar(View view) {
        Intent calender_intent = new Intent(getApplication(), Calendar_Select.class);
        calender_intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(calender_intent, CALENDAR_RESULT);
    }

    //-------------------------------------------------------------------------------//
    //                                 終了　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Finish(View view) {
        kenshin_db.close();
        COL_BAN = 0;
        finish();
    }

    //-------------------------------------------------------------------------------//
    //                                 入力　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Input(View view) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("TSVを取り込みますか？")     //ダイアログ（タイトル・メッセージ）
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                .setMessage("TSVの取込をｷｬﾝｾﾙしました")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                   }
                                }).show();  //取込キャンセルボタン
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {    //取込ボタン
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                kenshin_db.allDelete("TOKUIF"); //TOKUIFデータ削除
                kenshin_db.allDelete("HYOF");   //HYOFデータ削除

                tokuif.TOKUIF_CSV(kenshin_db.db, MainActivity.this);     //TOKUIF作成タスク
                new HYOF().HYOF_CSV(kenshin_db.db, MainActivity.this);   //HYOF作成タスク

                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                        .setMessage("TSVを取り込みました")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cursor = kenshin_db.db.rawQuery(" SELECT ban FROM TOKUIF ", null);
                                cursor.moveToFirst();
                                COL_BAN = Integer.parseInt(cursor.getString(0));
                                COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
                                button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
                            }
                        }).show();
            }
        }).show();

    }

    //-------------------------------------------------------------------------------//
    //                                 出力　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Output(View view) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("TSVを出力しますか？").setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int idx) {
                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                        .setMessage("TSVの取込をｷｬﾝｾﾙしました")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();  //取込キャンセルボタン
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {                               //取込ボタン
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tokuif.OUT_PUT_TSV(kenshin_db.db, MainActivity.this);   //取込タスク
                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                        .setMessage("TSVを出力しました")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }).show();
    }

    //-------------------------------------------------------------------------------//
    //                                 戻る　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_up(View view) {

        db_registration();

        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
        cursor.moveToFirst();
        while (COL_BAN != Integer.parseInt(cursor.getString(0))) {
            cursor.moveToNext();
        }
        cursor.moveToPrevious();
        try {
            COL_BAN = Integer.parseInt(cursor.getString(0));

        } catch (IndexOutOfBoundsException e) {
            COL_BAN = -1;
        }
        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
    }

    //-------------------------------------------------------------------------------//
    //                                 進む　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Down(View view) {

        if(!PRINT_FLG) {    //未/済ボタン操作後じゃない場合　*登録
            db_registration();
        }
        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);       //TOKUIFテーブルのデータを取得
        cursor.moveToFirst();
        while (COL_BAN != cursor.getInt(0)) {
            cursor.moveToNext();
        }
        cursor.moveToNext();
        try {
            COL_BAN = cursor.getInt(0);
        } catch (IndexOutOfBoundsException e) {
            COL_BAN = COL_BAN + 1;
        }
        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

        PRINT_FLG = false; //PRINT_FLGの初期化
    }

    //-------------------------------------------------------------------------------//
    //                                 点検　タスク                                   //
    //-------------------------------------------------------------------------------//
    public void main_Check(View view) {
        MULTIPLE_CHECKBOX = TOKUIF.Check_Result(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX);

        Intent check_activity = new Intent(getApplication(), CheckActivity.class);

        check_activity.putExtra("CHECK_KEY", MULTIPLE_CHECKBOX);
        check_activity.setAction(Intent.ACTION_VIEW);
        startActivityForResult(check_activity, CHECKBOX_RESULT);

    }

    public void db_registration(){      //データベースに登録
        Usaged_now = INPUT_NUMBER_EDIT.getText().toString();
        Usaged_now2 = CURRENCT_USAGE_TEXT.getText().toString();
        price = CURRENCT_AMOUNT_TEXT.getText().toString();
        Tax = Row3_2.getText().toString();

        tokuif.Usaged(kenshin_db.db, Usaged_now, VALUES, Usaged_now2, COL_BAN, TODAY);
        tokuif.TAX_PRICE(price, Tax, VALUES, kenshin_db.db, COL_BAN, DAYS.getText().toString());
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

    }
}