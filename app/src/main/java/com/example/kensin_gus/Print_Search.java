package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fujitsufrontech.patioprinter.fhtuprt.fhtUprt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressLint("Registered")
public class Print_Search extends AppCompatActivity {
    private final static String BR = System.getProperty("line.separator");    // 改行コード
    private fhtUprt mPrint = new fhtUprt();                                    // プリンタ制御ライブラリ
    private String mDevName = "";                                            // 選択されたBluetoothデバイス名
    private String mDevAddr = "";                                            // 選択されたBluetoothアドレス
    public Context context;
    Intent main_activity_intent;
    private View view;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_search);
        main_activity_intent = new Intent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // プリンタ制御ライブラリの初期化
        mPrint = new fhtUprt();
        mPrint.fhtPrInit(getApplicationContext(), mHandler);
        Log.d("PrtSampleAppActivity", "onResume");
    }

    ///////////////////////////////////////////////////////////////////////////
    // PatioPrinterを検索する
    //
    public void PrintSearch(View v) {
        view = v;
        // プリンタの検索
        if (mPrint.fhtPrFind() == fhtUprt.PRT_SUCCESS) {
            // 検索中メッセージ表示
            TextView view = findViewById(R.id.main_address_text);
            view.setText("検索中");
            Log.d("PrtSampleAppActivity", "PrintSearch SUCCESS");
        } else {
            Log.d("PrtSampleAppActivity", "PrintSearch ERROR");
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            //fhtPrFindの完了通知
            if (msg.what == fhtUprt.FHTPRFIND_FINISHED) {//検索結果を取得
                fhtUprt.BTHPRTINFO[] inf;
                inf = (fhtUprt.BTHPRTINFO[]) msg.obj;
                if (msg.arg2 == 0) {
                    return;

                }
                //検索結果をリストに格納
                for (int i = 0; i < msg.arg2; i++) {
                    if (!(inf[i].szBtName.isEmpty()) && !(inf[i].szBtAddr.isEmpty())) {
                        if (inf[i].szBtName.equals("MBH7BTZ47-100008")) {
                            mDevName = inf[i].szBtName;
                            mDevAddr = inf[i].szBtAddr;
                            TextView view = findViewById(R.id.main_address_text);
                            view.setText(mDevName + "\n" + mDevAddr);
                            break;
                        }
                    }
                }
                //プリンタが見つからなかったとき
                if(mDevName.isEmpty() && mDevAddr.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Print_Search.this);
                    builder.setTitle("注意")
                            .setMessage("プリンターが見つかりませんでした。")
                            .setMessage("もう一度検索しますか？")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                //再検索
                                public void onClick(DialogInterface dialog, int which) {
                                    PrintSearch(view);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
            }
        }
    };


    //――――――――――――――――――――――――――――――――
    // 印刷ボタン押下時
    //――――――――――――――――――――――――――――――――
    public void PrintBack(View view) {
        main_activity_intent.putExtra("PatioPrinter", mDevAddr);
        // メインアクティビティに値を渡す
        setResult(RESULT_OK, main_activity_intent);
        finish();
    }

}

