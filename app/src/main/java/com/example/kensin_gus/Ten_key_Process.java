package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class Ten_key_Process extends AppCompatActivity {
    TextView ten_key_edit;  //テキスト最大入力文字数　6ケタ(「.」も含める (例)123456 or 1234.5)
    Button button;
    Button ten_key_OK;
    Button DELETE_KEY;
    Intent TEN_KEY_INTENT;
    StringBuilder stringBuilder; //「.」や桁数の調整
    int Root_Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kensin_ten_key);

        TEN_KEY_INTENT = getIntent();

        Root_Result = TEN_KEY_INTENT.getIntExtra("BUTTON_ID",-1);

        ten_key_edit = findViewById(R.id.ten_key_edit);
        button = findViewById(R.id.button);
        ten_key_OK = findViewById(R.id.ten_key_OK);
        DELETE_KEY = findViewById(R.id.ten_key_delete);

        //オブジェクト生成
        findViewById(R.id.ten_key_one).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_two).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_thr).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_for).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_fiv).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_six).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_sev).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_eig).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_nin).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_zer).setOnClickListener(buttonNumberListener);
        findViewById(R.id.ten_key_dot).setOnClickListener(buttonNumberListener);


        //―――――――――――――――――――――――――――――――――――
        // OKボタン押下時
        //―――――――――――――――――――――――――――――――――――
        ten_key_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ROW1_RESULT = ten_key_edit.getText().toString();

                //テキストが未入力の場合
                if (ROW1_RESULT.equals("")) {
                    ROW1_RESULT = "0";
                }
                //テキストの最後の文字が「.」の場合
                else if(String.valueOf(ROW1_RESULT.charAt(ROW1_RESULT.length() - 1)).equals(".")){
                    stringBuilder = new StringBuilder(ROW1_RESULT);
                    stringBuilder.setLength(stringBuilder.length()-1);  //末尾を削除
                    ROW1_RESULT = String.valueOf(stringBuilder);
                }

                TEN_KEY_INTENT.putExtra("RESULT_KEY", ROW1_RESULT);
                TEN_KEY_INTENT.putExtra("BUTTON_ID",Root_Result);

                setResult(RESULT_OK, TEN_KEY_INTENT);
                finish();
            }
        });

        //―――――――――――――――――――――――――――――――――――
        //  消去ボタンメソッド
        //―――――――――――――――――――――――――――――――――――
        DELETE_KEY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                int result = stringBuilder.indexOf(" ");

                //空白がある場合は result >= 0 ない場合は -1を返す
                if(result != -1){

                    stringBuilder.replace(result,result+2,"");  //空白を詰める
                    ten_key_edit.setText(stringBuilder);
                }

                //テキスト値が「0」以外のとき
                if(!ten_key_edit.getText().toString().equals("0")) {
                    try {
                        stringBuilder.setLength(stringBuilder.length() - 1); //末尾を消去
                        ten_key_edit.setText(stringBuilder);

                        //全消去した場合
                        if(ten_key_edit.getText().toString().equals("")){
                            ten_key_edit.setText("0");
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //―――――――――――――――――――――――――――――――――――――――
    //  数値入力メソッド
    //―――――――――――――――――――――――――――――――――――――――
    View.OnClickListener buttonNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int i = 0;

            //テキストに文字列「0」　かつ　テンキー「.」以外を押下したとき
      //      if(ten_key_edit.getText().toString().equals("0") && button.getId() != R.id.ten_key_dot){
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
            //    stringBuilder.setLength(stringBuilder.length() - 1); //文字列の末尾を削除
                ten_key_edit.setText(stringBuilder);
    ///        }

            //テンキー「.」を押下したとき
            if (button.getId() == R.id.ten_key_dot) {
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                String[] array = ten_key_edit.getText().toString().split(""); //split("")で1文字ずつ配列に入力

                //テキストの文字列内に「.」があるか1個ずつ検索
                for (String s : array) { //拡張for文
                    if (s.equals(".")) {
                        i++;
                    }
                }
                //「.」がない場合は入力
                if (i == 0) {
                    ten_key_edit.append(button.getText());
                }
            }
            //テンキー0~9押下時
            else {
                ten_key_edit.append(button.getText());


/* 19/09/30 * 未使用　削除するか検討中

                //テキスト入力数を６桁にするために６桁目になった時に処理
                if(ten_key_edit.getText().length() == 6)
                {
                    stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                    String[] array = ten_key_edit.getText().toString().split("");

                    //テキストの文字列内に「.」があるか検索
                    for (String s : array) {
                        if (s.equals(".")) {
                            i++;
                        }
                    }
                    //テキストの最大入力数は8桁なので小数点がない場合は残り２桁を空白で埋める
                    if(i == 0){
                        stringBuilder.insert(0," " + " ");
                        ten_key_edit.setText(stringBuilder);
                    }
                }*/
            }
        }
    };
}
