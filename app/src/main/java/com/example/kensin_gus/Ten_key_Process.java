package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class Ten_key_Process extends AppCompatActivity {
    EditText ten_key_edit;
    Button button;
    Button ten_key_OK;
    Button DELETE_KEY;
    Intent TEN_KEY_INTENT;
    StringBuilder stringBuilder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kensin_ten_key);

        TEN_KEY_INTENT = getIntent();

        ten_key_edit = findViewById(R.id.ten_key_edit);
        button = findViewById(R.id.button);
        ten_key_OK = findViewById(R.id.ten_key_OK);
        DELETE_KEY = findViewById(R.id.ten_key_delete);

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

        ten_key_edit.setText("0");

        ten_key_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ROW1_RESULT = ten_key_edit.getText().toString();
                if (ROW1_RESULT.equals("")) {   //テキストが未入力の場合
                    ROW1_RESULT = "0";
                }
                else if(String.valueOf(ROW1_RESULT.charAt(ROW1_RESULT.length() - 1)).equals(".")){  //テキストの最後の文字が「.」の場合
                    stringBuilder = new StringBuilder(ROW1_RESULT);
                    stringBuilder.setLength(stringBuilder.length()-1);  //末尾を削除
                    ROW1_RESULT = String.valueOf(stringBuilder);
                }

                TEN_KEY_INTENT.putExtra("RESULT_KEY", ROW1_RESULT);
                setResult(RESULT_OK, TEN_KEY_INTENT);
                finish();
            }
        });

        DELETE_KEY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                int result = stringBuilder.indexOf(" ");    //空白がある場合は result >= 0 ない場合は -1　を返す

                if(result != -1){

                    stringBuilder.replace(result,result+2,"");  //空白を詰める
                    ten_key_edit.setText(stringBuilder);
                }

                if(!ten_key_edit.getText().toString().equals("0")) {     //テキスト値が「0」以外のとき
                    try {
                        stringBuilder.setLength(stringBuilder.length() - 1);    //末尾を消去
                        ten_key_edit.setText(stringBuilder);

                        if(ten_key_edit.getText().toString().equals("")){        //全消去した場合
                            ten_key_edit.setText("0");
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    View.OnClickListener buttonNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int i = 0;


            if(ten_key_edit.getText().toString().equals("0") && button.getId() != R.id.ten_key_dot){    //テキストに文字列「0」　かつ　テンキー「.」以外を押下したとき
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                stringBuilder.setLength(stringBuilder.length() - 1);                                    //文字列の末尾を削除
                ten_key_edit.setText(stringBuilder);
            }

            if (button.getId() == R.id.ten_key_dot) {           //テンキー「.」を押下したとき
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                String[] array = ten_key_edit.getText().toString().split("");
                for (String s : array) {
                    if (s.equals(".")) {    //テキストの文字列内に「.」があるか検索
                        i++;
                    }
                }
                if (i == 0) {
                    ten_key_edit.append(button.getText());
                }
            } else {
                ten_key_edit.append(button.getText());

                if(ten_key_edit.getText().length() == 6){                                           //テキスト入力数を６桁にするために６桁目になった場合に処理
                    stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                    String[] array = ten_key_edit.getText().toString().split("");
                    for (String s : array) {
                        if (s.equals(".")) {                                                         //テキストの文字列内に「.」があるか検索
                            i++;
                        }
                    }
                    if(i == 0){
                        stringBuilder.insert(0," " + " ");                            //テキストの最大入力数は8桁なので小数点がない場合は残り２桁を空白で埋める
                        ten_key_edit.setText(stringBuilder);
                    }
                }
            }

        }
    };


}
