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
    private final static String BR = System.getProperty("line.separator");	// 改行コード
    ArrayList<BluetoothDevice> mDevList = new ArrayList<BluetoothDevice>();	// 検索したBluetoothデバイスリスト
    private fhtUprt mPrint = new fhtUprt();									// プリンタ制御ライブラリ
    private String mDevName = "";											// 選択されたBluetoothデバイス名
    private String mDevAddr = "";											// 選択されたBluetoothアドレス
    private String mItemMsg = "";											// 選択されたBluetoothデバイス情報
    private String mImgStr = "";											// PatioPrinter登録済みイメージ一覧
    private String[] mItems = null;											// 表示用文字列
    public Context context ;
    Intent main_activity_intent;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_search);
        main_activity_intent = new Intent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // プリンタ制御ライブラリの初期化
        mPrint = new fhtUprt();
        mPrint.fhtPrInit(getApplicationContext(),mHandler);
        Log.d("PrtSampleAppActivity", "onResume");
    }

    ///////////////////////////////////////////////////////////////////////////
    // PatioPrinterを検索する
    //
    public void PrintSearch(View v){

        // プリンタの検索
        if( mPrint.fhtPrFind() == fhtUprt.PRT_SUCCESS ){
            // 検索中メッセージ表示
            TextView view = findViewById(R.id.main_address_text);
            view.setText("検索中");

            Log.d("PrtSampleAppActivity", "PrintSearch SUCCESS");
        }else{
            Log.d("PrtSampleAppActivity", "PrintSearch ERROR");
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                //fhtPrFindの完了通知
                case fhtUprt.FHTPRFIND_FINISHED:
                    //検索結果を取得
                    fhtUprt.BTHPRTINFO[] inf;
                    inf = (fhtUprt.BTHPRTINFO[]) msg.obj;
                    if (msg.arg2 == 0) {
                          return;
                    }
                    //検索結果をリストに格納
                    mDevList.clear();
                    BluetoothDevice btDev;
                    int	add_count=0;
                    for(int i = 0; i < msg.arg2; i++){
                        if( !(inf[i].szBtName.isEmpty()) && !(inf[i].szBtAddr.isEmpty())) {
                                btDev = new BluetoothDevice(inf[i].szBtName, inf[i].szBtAddr, inf[i].szBtName + BR + "(" + inf[i].szBtAddr + ")");
                                mDevList.add(btDev);
                                add_count++;
                        }
                    }
                    if(add_count == 0){
                        return;
                    }

                    // 格納数が２個以上の場合、リストをソート
                    if( add_count > 1 ){
                        Collections.sort( mDevList, new Comparator<BluetoothDevice>(){
                            //@Override
                            public int compare(BluetoothDevice obj0, BluetoothDevice obj1) {
                                if( obj0.getDevName().equals( obj1.getDevName() ) ){
                                    return obj0.getDevAddr().compareTo(obj1.getDevAddr());
                                }
                                return obj0.getDevName().compareTo(obj1.getDevName());
                            }
                        });
                    }

                    //表示リストを作成
                    mItems = new String[add_count];
                    for(int i=0;i< add_count;i++){
                        if(!mDevList.get(i).getDevName().equals("unknown")){
                            mItems[i] = mDevList.get(i).getItemMsg();
                        }
                        else{
                            mItems[i] = "";
                        }
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Print_Search.this);
                    alertDialogBuilder.setTitle( " 検索結果 " );

                    // Bluetoothデバイス名選択ダイアログ　クリックされたデバイスをオープンする
                    alertDialogBuilder.setItems( mItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {	// クリック処理

                            // 選択されたデバイス情報を表示
                            TextView view = findViewById(R.id.main_address_text );
                            view.setText(mDevList.get(which).getItemMsg());

                            // 選択されたデバイス情報を保存
                            mDevName = mDevList.get(which).getDevName();
                            mDevAddr = mDevList.get(which).getDevAddr();
                            mItemMsg = mDevList.get(which).getItemMsg();

                        }
                    });
                    // ダイアログを表示
                    alertDialogBuilder.create().show();
                    break;
            }
        }
    };

    public void PrintBack(View view) {
        main_activity_intent.putExtra("PatioPrinter", mDevAddr);
        setResult(RESULT_OK, main_activity_intent);
        finish();
    }
}

