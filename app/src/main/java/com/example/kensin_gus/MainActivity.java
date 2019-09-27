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
import com.example.db_library.HYOF;
import com.example.db_library.Kenshin_DB;
import com.example.db_library.TOKUIF;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String Number_Input;
    String Usaged;
    String price;
    String Tax;
    boolean DB_REG_FLG = false;              //db_registrationの出力判定
    int COL_BAN = 0;                           //DBの連番
    int[] MULTIPLE_CHECKBOX = new int[12];  //点検クラスのチェックボックス(1:入り 0:外す）

    String RETURN_ADDRESS = "";  //使用するBlueToothアドレスの格納変数
    String TODAY;

    boolean CHECK_FLG = true;   //未/済ボタンフラグ　true:未　false:済

    //Activityの結果を取得ための整数コード(*整数のみ )
    int CHECK_RESULT = 1;                                 //点検アクティビティ結果の取得
    int ROOT_SEARCH_RESULT = 2;                          //道順アクティビティ結果の取得
    int TEN_KEY_RESULT = 3;                               //10キーアクティビティ結果の取得
    int CALENDAR_RESULT = 4;                              //カレンダーアクティビティ結果の取得
    int PRINTER_RESULT = 5;                               //印刷アクティビティ結果の取得

    int REQUEST_CODE = 1; //パーミッション許可

    Button KENSIN_BUTTON;               //検針ボタン
    Button DOWN_BUTTON;                 //DOWNボタン
    Button UP_BUTTON;                   //UPボタン
    Button FIXED_UNFIXED_BUTTON;      //済ボタン
    Button END_BUTTON;                  //終了ボタン
    Button CHECK_BUTTON;                //点検ボタン
    Button ROOT_SEARCH_BUTTON;         //道順ボタン
    Button CALENDER_SELECT_BUTTON;    //カレンダーボタン
    Button OUT_PUT_BUTTON;              //TSV形式出力ボタン
    Button TSV_IMPORT_BUTTON;           //TSV形式入力ボタン
    Button PRINTER_BUTTON;              //印刷ボタン

    EditText INPUT_NUMBER_EDIT;         //数値入力テキスト
    TextView CURRENCT_USAGE_TEXT;       //使用量テキスト
    TextView CURRENCT_AMOUNT_TEXT;      //金額テキスト
    TextView DAYS;                        //日付テキスト
    TextView TAX;                         //消費税分テキスト

    TOKUIF tokuif;                              //TOKUIFクラス
    Kenshin_DB kenshin_db;                      //検針DBクラス
    Ten_key_Process ten_key_process;           //10キークラス
    Button_Processing button_processing;       //済ボタンクラス
    Screen_Layout.Main_Screen main_screen;      //表示値変更クラス
    LayoutInflater layoutInflater;
    ContentValues VALUES;
    View main_view;
    Dialog dialog;
    Cursor cursor;

    //――――――――――――――――――――――――――――――
    //  一番最初に1回だけ呼ばれるメソッド
    //――――――――――――――――――――――――――――――
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //メインアクティビティの生成

        //パーミッションの許可
        if (Build.VERSION.SDK_INT >= 23) {  //***アンドロイド端末のSDKが23以上の場合許可が必要
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,      //内部ストレージ(SDカード）の読取
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,     //内部ストレージ(SDカード)への書込
                    Manifest.permission.ACCESS_FINE_LOCATION,       //GPSでの位置情報を取得
                    Manifest.permission.ACCESS_COARSE_LOCATION      //ネットワークから位置情報を取得
            };
            checkPermission(permissions, REQUEST_CODE);
        }

        //――――――――――――――――――――――――――――――――――――
        //  オブジェクト生成
        //――――――――――――――――――――――――――――――――――――
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

        INPUT_NUMBER_EDIT = findViewById(R.id.Input_number);
        CURRENCT_USAGE_TEXT = findViewById(R.id.Used_number);
        CURRENCT_AMOUNT_TEXT = findViewById(R.id.Using_Amount);
        TAX = findViewById(R.id.Tax);
        DAYS = findViewById(R.id.date_now);

        //――――――――――――――――――――――――――――――――――――
        //  インスンタンス化
        //――――――――――――――――――――――――――――――――――――
        kenshin_db = new Kenshin_DB(getApplicationContext());
        tokuif = new TOKUIF();
        ten_key_process = new Ten_key_Process();
        button_processing = new Button_Processing();
        main_screen = new Screen_Layout.Main_Screen();
        layoutInflater = LayoutInflater.from(this);
        dialog = new Dialog();
        VALUES = new ContentValues();

        //―――――――――――――――――――――――――――――――――――――――――


        //今日の日付を入力　戻り値：String 型
        TODAY = main_screen.Screen_Data(this);

        //―――――――――――――――――――――――――――――――――――――――――
        //  データベースの存在確認
        //―――――――――――――――――――――――――――――――――――――――――

        if (!this.getDatabasePath(Kenshin_DB.DB_NAME).exists()) {
            //アプリ起動時データベースがない場合に作成
            new Kenshin_DB(this);
        }

        //―――――――――――――――――――――――――――――――――――――――――
        //  起動時のデータ出力
        //―――――――――――――――――――――――――――――――――――――――――

        //TOKUIFテーブルのデータを取得
        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);

        //データがあればレイアウト入力
        if (cursor.getCount() > 0) {
            //TOKUIFテーブルの一番最初の人を出力
            cursor.moveToFirst();
            COL_BAN = Integer.parseInt(cursor.getString(0));
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

        }
        else
        {//データがない場合は特定のボタンを選択できなくする
            KENSIN_BUTTON.setEnabled(false);
            CHECK_BUTTON.setEnabled(false);
            PRINTER_BUTTON.setEnabled(false);
        }
    }

    //―――――――――――――――――――――――――――――――――――――
    //  パーミッションの許可申請メソッド
    //―――――――――――――――――――――――――――――――――――――

    private void checkPermission(String[] permissions, int request_code) {
        ActivityCompat.requestPermissions(this, permissions, request_code);
    }
    //―――――――――――――――――――――――――――――――――――――
    //パーミッション許可が下りているか確認メソッド
    //―――――――――――――――――――――――――――――――――――――
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Pemisson", "許可されています");
                } else {
                    Log.d("Pemisson", "許可されていません");
                }
            }
        }
    }

    //――――――――――――――――――――――――――――――――――――
    //　メインアクティビティを破棄するときに起動するメソッド
    //――――――――――――――――――――――――――――――――――――

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //破棄するときに必要な物を保存する
        super.onSaveInstanceState(outState);
        outState.putString("COL_BAN", String.valueOf(COL_BAN));
        outState.putString("DB_REG_FLG", String.valueOf(DB_REG_FLG));
    }

    //――――――――――――――――――――――――――――――――――――
    //　メインアクティビティを再生成する時に起動するメソッド
    //――――――――――――――――――――――――――――――――――――
    @SuppressLint("NewApi")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //保存していたものを復元させる
        super.onRestoreInstanceState(savedInstanceState);
        COL_BAN = Integer.valueOf(Objects.requireNonNull(savedInstanceState.getString("COL_BAN")));
        DB_REG_FLG = Boolean.parseBoolean(savedInstanceState.getString("DB_REG_FLG"));
    }

    //――――――――――――――――――――――――――――――――――――
    //　別のアクティビティの結果を受け取るメソッド
    //――――――――――――――――――――――――――――――――――――
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(REQUEST_CODE, resultCode, intent);

        //点検アクティビティからの結果を受け取る
        if (resultCode == RESULT_OK && REQUEST_CODE == CHECK_RESULT && null != intent) {
            MULTIPLE_CHECKBOX = intent.getIntArrayExtra("CHECK_KEY");
            tokuif.Check_Result_return(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX, TODAY);
        }

        //道順アクティビティからの結果を受け取る
        if (resultCode == RESULT_OK && REQUEST_CODE == ROOT_SEARCH_RESULT && null != intent) {
            COL_BAN = intent.getIntExtra("ROOT_KEY", 0);
            COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
            button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);
        }

        //テンキーアクティビティからの結果を受け取る
        if (resultCode == RESULT_OK && REQUEST_CODE == TEN_KEY_RESULT && null != intent) {
            //テンキーアクティビティの結果
            StringBuilder Result = new StringBuilder(intent.getStringExtra("RESULT_KEY"));

            // 文字列が ２以上の時
            if(Result.length() > 1){
                //小数点がないとき
                if(Result.charAt(1) != '.') {
                    if (Result.charAt(0) == '0') {
                        Result.deleteCharAt(0);
                    }
                }
            }

            INPUT_NUMBER_EDIT.setText(Result);
            CURRENCT_USAGE_TEXT.setText(Calc_class.Calc_Used(Float.parseFloat(INPUT_NUMBER_EDIT.getText().toString()), kenshin_db.db, COL_BAN));
            Calc_class.Calc_HYOF_PRICE(Float.parseFloat(CURRENCT_USAGE_TEXT.getText().toString()), kenshin_db.db, COL_BAN, MainActivity.this);
        }

        //カレンダーアクティビティからの結果を受け取る
        if (resultCode == RESULT_OK && REQUEST_CODE == CALENDAR_RESULT && null != intent) {
            TODAY = intent.getStringExtra("CALENDAR_KEY");
            DAYS.setText(TODAY);
        }

        //印刷アクティビティからの結果を受け取る
        if (resultCode == RESULT_OK && REQUEST_CODE == PRINTER_RESULT && null != intent) {
            RETURN_ADDRESS = intent.getStringExtra("PatioPrinter");
            DB_REG_FLG = true;
            new PrintKensin().Print_Open(COL_BAN, MainActivity.this, RETURN_ADDRESS);
            main_Down(main_view);
        }
    }

    //――――――――――――――――――――――――――――――――――――
    //  使用量メソッド
    //――――――――――――――――――――――――――――――――――――
    public void Used_text(View view)
    {
        //使用量ダイアログの表示
        dialog.Dialog_SYOSAI(layoutInflater, MainActivity.this, kenshin_db.db, COL_BAN);
    }

    //――――――――――――――――――――――――――――――――――――
    //  未/済ボタンメソッド
    //――――――――――――――――――――――――――――――――――――
    public void Fixed_UnFixed(View view) {
        db_registration();  //データベース登録

        //　未済アクティビティに遷移 戻り値：Boolean型
        CHECK_FLG = button_processing.Check_task(INPUT_NUMBER_EDIT, CHECK_BUTTON, FIXED_UNFIXED_BUTTON, KENSIN_BUTTON, CHECK_FLG, PRINTER_BUTTON);
        //CHECK_FLG　TRUE:済　FALSE:未
        if (CHECK_FLG) {
            main_Printer(view); //印刷メソッド
        }
    }

    //――――――――――――――――――――――――――――――――――――
    //  数値入力メソッド
    //――――――――――――――――――――――――――――――――――――
    public void number_form(View view) {
        //テンキーアクティビティに遷移
        Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
        TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
        startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
    }

    //――――――――――――――――――――――――――――――――――――
    //  検針メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Kensin(View view) {
        //検針アクティビティに遷移
        Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
        TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
        startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
    }

    //――――――――――――――――――――――――――――――――――――
    //  道順メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Root_Search(View view) {
        //道順アクティビティに遷移
        Intent root_search = new Intent(getApplication(), Root_Search.class);
        root_search.setAction(Intent.ACTION_VIEW);
        startActivityForResult(root_search, ROOT_SEARCH_RESULT);
    }

    //――――――――――――――――――――――――――――――――――――
    //  印刷メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Printer(View view) {
        main_view = view;
        //印刷アクティビティに遷移
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("検針票を印刷しますか？")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {
                        DB_REG_FLG = false;
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //RETURN_ADDRESSの中身が空かどうか判定
                if (RETURN_ADDRESS.equals(""))
                {// 空：印刷タスクに遷移
                    Intent intent = new Intent(MainActivity.this, Print_Search.class);
                    startActivityForResult(intent, PRINTER_RESULT);
                }
                else
                {// 有：選択したプリンタを継続して使用
                    DB_REG_FLG = true;
                    new PrintKensin().Print_Open(COL_BAN, getApplication(), RETURN_ADDRESS);
                    main_Down(main_view);
                }
            }
        }).show();
    }

    //――――――――――――――――――――――――――――――――――――
    //  カレンダーメソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Calendar(View view) {
        //カレンダーアクティビティに遷移
        Intent calender_intent = new Intent(getApplication(), Calendar_Select.class);
        calender_intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(calender_intent, CALENDAR_RESULT);
    }

    //――――――――――――――――――――――――――――――――――――
    //  終了メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Finish(View view) {
        kenshin_db.close(); //DBのクローズ
        finish();
    }

    //――――――――――――――――――――――――――――――――――――
    //  TSV形式入力メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Input(View view) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("TSVを取り込みますか？")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    //No を選択時
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                                .setMessage("TSVの取込をｷｬﾝｾﾙしました")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            //Yes　を選択時
            public void onClick(DialogInterface dialogInterface, int i) {
                kenshin_db.allDelete("TOKUIF"); //TOKUIFデータ削除
                kenshin_db.allDelete("HYOF");   //HYOFデータ削除

                tokuif.TOKUIF_CSV(kenshin_db.db, MainActivity.this);     //TOKUIFの作成
                new HYOF().HYOF_TSV(kenshin_db.db, MainActivity.this);   //HYOFの作成

                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                        .setMessage("TSVを取り込みました")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            //確認後、一番最初の人を出力する
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

    //――――――――――――――――――――――――――――――――――――
    //  TSV形式出力メソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_Output(View view) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確認ダイアログ").setMessage("TSVを出力しますか？")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            //No を選択時
            public void onClick(DialogInterface dialogInterface, int idx) {
                new AlertDialog.Builder(MainActivity.this).setTitle("確認ダイアログ")
                        .setMessage("TSVの取込をｷｬﾝｾﾙしました")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            //何もしない
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            //Yes を選択
            public void onClick(DialogInterface dialogInterface, int i) {
                tokuif.OUT_PUT_TSV(kenshin_db.db, MainActivity.this);   //出力メソッド

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

    //――――――――――――――――――――――――――――――――――――
    //  戻るメソッド
    //――――――――――――――――――――――――――――――――――――
    public void main_up(View view) {
        db_registration();  //データベースの登録
        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
        cursor.moveToFirst();
        //  連番を検索
        while (COL_BAN != Integer.parseInt(cursor.getString(0))) {
            cursor.moveToNext();
        }
        //  １つ前の人を出力する
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

        //　2重登録ではない場合
        if (!DB_REG_FLG) {
            db_registration();
        }
        cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
        cursor.moveToFirst();
        while (COL_BAN != cursor.getInt(0)) {
            cursor.moveToNext();
        }
        //１つ次の人を出力する
        cursor.moveToNext();
        try {
            COL_BAN = cursor.getInt(0);
        } catch (IndexOutOfBoundsException e) {
            COL_BAN = COL_BAN + 1;
        }
        COL_BAN = Screen_Layout.Main_Screen.SELECT_COM(MainActivity.this, COL_BAN, kenshin_db.db);
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

        DB_REG_FLG = false; //PRINT_FLGの初期化
    }


    //-------------------------------------------------------------------------------//
    //                                 点検　メソッド                                 //
    //-------------------------------------------------------------------------------//
    public void main_Check(View view) {
        //点検アクティビティのチェック入を出力　戻り値：Int[]
        MULTIPLE_CHECKBOX = TOKUIF.Check_Result(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX);

        //点検アクティビティに遷移
        Intent check_activity = new Intent(getApplication(), CheckActivity.class);
        check_activity.putExtra("CHECK_KEY", MULTIPLE_CHECKBOX);
        check_activity.setAction(Intent.ACTION_VIEW);
        startActivityForResult(check_activity, CHECK_RESULT);
    }

    //データべースに登録する
    public void db_registration() {
        Number_Input = INPUT_NUMBER_EDIT.getText().toString();
        Usaged = CURRENCT_USAGE_TEXT.getText().toString();
        price = CURRENCT_AMOUNT_TEXT.getText().toString();
        Tax = TAX.getText().toString();

        tokuif.Usaged(kenshin_db.db, Number_Input, VALUES, Usaged, COL_BAN, TODAY);
        tokuif.TAX_PRICE(price, Tax, VALUES, kenshin_db.db, COL_BAN, DAYS.getText().toString());
        button_processing.Up_Down_Button(MainActivity.this, kenshin_db.db, COL_BAN);

    }

}