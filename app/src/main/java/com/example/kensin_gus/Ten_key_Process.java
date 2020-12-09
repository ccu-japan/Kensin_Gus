package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.db_library.GlobalFlag;

@SuppressLint("Registered")
public class Ten_key_Process extends AppCompatActivity {
    TextView ten_key_edit;  //テキスト最大入力文字数　6ケタ(「.」も含める (例)123456 or 1234.5)
    Button button;
    Button ten_key_OK;
    Button DELETE_KEY;
    Intent TEN_KEY_INTENT;
    StringBuilder stringBuilder; //「.」や桁数の調整
    int Root_Result;
    int SELECT_FLG;
    
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
    
        
        if(Root_Result == GlobalFlag.Flag.NUMBER_1)
        {                                                                            //オブジェクト生成
            ten_key_edit.setText("0");
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
        }
        else
        {
            findViewById(R.id.ten_key_one).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_two).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_thr).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_for).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_fiv).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_six).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_sev).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_eig).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_nin).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_zer).setOnClickListener(buttonNumberListener2);
            findViewById(R.id.ten_key_dot).setVisibility(View.INVISIBLE);
        }
        

        //―――――――――――――――――――――――――――――――――――
        // OKボタン押下時
        //―――――――――――――――――――――――――――――――――――
        ten_key_OK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
    
                String ROW1_RESULT = ten_key_edit.getText().toString();
    
                try
                {
                    do
                    {
                        //テキストが未入力の場合
                        if (ROW1_RESULT.equals(""))
                        {
                            ROW1_RESULT = "0";
                            break;
                        }
    
                        if (ROW1_RESULT.substring(ROW1_RESULT.length() - 1, ROW1_RESULT.length()).equals("."))
                        {
                            stringBuilder = new StringBuilder(ROW1_RESULT);
                            stringBuilder.setLength(stringBuilder.length() - 1);  //末尾を削除
                            ROW1_RESULT = String.valueOf(stringBuilder);
                            break;
                        }
                    }
                    while (false);
    
                    TEN_KEY_INTENT.putExtra("RESULT_KEY", ROW1_RESULT);
                    TEN_KEY_INTENT.putExtra("BUTTON_ID", Root_Result);
    
                    setResult(RESULT_OK, TEN_KEY_INTENT);
                    finish();
                }
                catch (Exception ex)
                {
                    System.out.println("Ten_Key_Process  : " + ex.getMessage());
                }
            }
        });

        //―――――――――――――――――――――――――――――――――――
        //  消去ボタンメソッド
        //―――――――――――――――――――――――――――――――――――
        DELETE_KEY.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                int result = stringBuilder.indexOf(" ");

                //空白がある場合は result >= 0 ない場合は -1を返す
                if(result != -1)
                {

                    stringBuilder.replace(result,result+2,"");              //空白を詰める
                    ten_key_edit.setText(stringBuilder);
                }
    
                if(Root_Result == GlobalFlag.Flag.NUMBER_1)
                {
                                                                            //テキスト値が「0」以外のとき
                    if (!ten_key_edit.getText().toString().equals("0"))
                    {
                        try
                        {
                                                                            //末尾を消去
                            stringBuilder.setLength(stringBuilder.length() - 1);
                            ten_key_edit.setText(stringBuilder);
            
                                                                            //全消去した場合
                            if (ten_key_edit.getText().toString().equals(""))
                                ten_key_edit.setText("0");
                        }
                        catch (StringIndexOutOfBoundsException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    {
                        if(((int)stringBuilder.length()-1) >= 0)
                        {
                                                                            //末尾を消去
                            stringBuilder.setLength(stringBuilder.length() - 1);
                            ten_key_edit.setText(stringBuilder);
                        }
                    }
                }
            }
        });
    }

    //―――――――――――――――――――――――――――――――――――――――
    //  数値入力メソッド (メインアクティビティ　数値計算用)
    //―――――――――――――――――――――――――――――――――――――――
    View.OnClickListener buttonNumberListener = new View.OnClickListener()
    {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int i = 0;
    
            try {
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                ten_key_edit.setText(stringBuilder);
        
                switch (button.getId()) {
                    case R.id.ten_key_dot:
                        if (!ten_key_edit.getText().toString().contains("."))
                            ten_key_edit.append(button.getText());
                        break;
            
                    case R.id.ten_key_zer:
                        if (ten_key_edit.getText().toString().length() >= 1 && !ten_key_edit.getText().toString().equals("0"))
                            ten_key_edit.append(button.getText());
                            
                        break;
            
                    default:
                        if (ten_key_edit.getText().toString().length() == 1 && ten_key_edit.getText().toString().equals("0"))
                            ten_key_edit.setText(button.getText());
                        else
                            ten_key_edit.append(button.getText());
                
                        break;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    };
    
    
    //―――――――――――――――――――――――――――――――――――――――
    //  数値入力メソッド　（検索用）
    //―――――――――――――――――――――――――――――――――――――――
    View.OnClickListener buttonNumberListener2 = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int i = 0;
            
            try {
                stringBuilder = new StringBuilder(ten_key_edit.getText().toString());
                ten_key_edit.setText(stringBuilder);
                
                ten_key_edit.append(button.getText());
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    };
}
