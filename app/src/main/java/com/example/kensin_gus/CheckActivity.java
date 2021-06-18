package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.db_library.Kenshin_DB;
import com.example.db_library.TENKEN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
public class CheckActivity extends AppCompatActivity {
	
	Intent main_activity;
	
	Kenshin_DB kenshin_db;
	int[] check;
	CheckBox Result1;
	CheckBox Result2;
	CheckBox Result3;
	CheckBox Result4;
	CheckBox Result5;
	CheckBox Result6;
	CheckBox Result7;
	CheckBox Result8;
	CheckBox Result9;
	CheckBox Result10;
	CheckBox Result11;
	CheckBox Result12;
	
	Button bt_OK;
	Button bt_Chancel;
	
	ArrayList<CheckBox> Result;
	
	//----------------------------------------------------------------------
	//  起動時読込
	//----------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);
		
																			//  値受け取り
		main_activity = getIntent();
		check = main_activity.getIntArrayExtra("CHECK_KEY");
		bt_Chancel = findViewById(R.id.Check_Cancel);
		bt_OK = findViewById(R.id.Check_OK);
		
																			//  checkboxのオブジェクト生成
		Result1 = findViewById(R.id.Result1);
		Result2 = findViewById(R.id.Result2);
		Result3 = findViewById(R.id.Result3);
		Result4 = findViewById(R.id.Result4);
		Result5 = findViewById(R.id.Result5);
		Result6 = findViewById(R.id.Result6);
		Result7 = findViewById(R.id.Result7);
		Result8 = findViewById(R.id.Result8);
		Result9 = findViewById(R.id.Result9);
		Result10 = findViewById(R.id.Result10);
		Result11 = findViewById(R.id.Result11);
		Result12 = findViewById(R.id.Result12);
																			//  for文でくくるためにList化　 *リスト化する必要性はあまりない
		Result = new ArrayList<>();
		Result.add(Result1);
		Result.add(Result2);
		Result.add(Result3);
		Result.add(Result4);
		Result.add(Result5);
		Result.add(Result6);
		Result.add(Result7);
		Result.add(Result8);
		Result.add(Result9);
		Result.add(Result10);
		Result.add(Result11);
		Result.add(Result12);

        /*
        //　チェックボックス名の取得
        File path = null;
        File file;
        String fileName = "TENKEN.TSV";
        String[] columnData;
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;

        //SDKバージョンの確認
        if (Build.VERSION.SDK_INT >= 29) {
            //path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        }
        //SDK28以下のみ対応  *2019/09/25
        else {
            //内部ストレージ,SDカード直下
            path = new File(Environment.getExternalStorageDirectory().getPath());
        }
        try {
            file = new File(path, fileName);
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Shift-JIS形式で読取
        try {
            inputStreamReader = new InputStreamReader(fileInputStream, "Shift-JIS");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                columnData = line.split("\t",-1);
                for (int i=0; i<columnData.length; i++) {
                    Result.get(i).setText(columnData[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
		kenshin_db = new Kenshin_DB(this);
		@SuppressLint("Recycle") Cursor cursor = kenshin_db.db.rawQuery("SELECT num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12 FROM TENKEN_ITEM ",null);
		cursor.moveToFirst();
		for (int i=0; i<cursor.getColumnCount(); i++) {
			Result.get(i).setText(cursor.getString(i));
		}
		
																			//  使っていないチェックボックスがある場合選択できないようにするための処理
		for (int i = 0; i < check.length; i++) {
			
																			//  チェックボックスのテキストが空の場合
			if (Result.get(i).getText().toString().equals(""))
			{
																			//選択不可
				Result.get(i).setEnabled(false);
			} else {
																			//  前回使用時、チェックの有無の確認
				Result.get(i).setChecked(check[i] == 1);
                /*
                if (check[i] == 1) {
                    Result.get(i).setChecked(true);
                } else {
                    Result.get(i).setChecked(false);
                }
                */
			}
		}
		
																			//ＯＫボタン押下時
		bt_OK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				main_activity.putExtra("CHECK_KEY", check);
				setResult(RESULT_OK, main_activity);
				finish();
			}
		});
																			//キャンセルボタン押下時
		bt_Chancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
	}
	
																			//CheckBox押下時
	@SuppressLint("NonConstantResourceId")
	public void onCheckboxClicked(View view) {
		final boolean checked = ((CheckBox) view).isChecked();
																			//CheckBoxのID
		switch (view.getId()) {
			case R.id.Result1:
				if (checked) {
					check[0] = 1;
				} else {
					check[0] = 0;
				}
				break;
			
			case R.id.Result2:
				if (checked) {
					check[1] = 1;
				} else {
					check[1] = 0;
				}
				break;
			
			case R.id.Result3:
				if (checked) {
					check[2] = 1;
				} else {
					check[2] = 0;
				}
				break;
			
			case R.id.Result4:
				if (checked) {
					check[3] = 1;
				} else {
					check[3] = 0;
				}
				break;
			
			case R.id.Result5:
				if (checked) {
					check[4] = 1;
				} else {
					check[4] = 0;
				}
				break;
			
			case R.id.Result6:
				if (checked) {
					check[5] = 1;
				} else {
					check[5] = 0;
				}
				break;
			
			case R.id.Result7:
				if (checked) {
					check[6] = 1;
				} else {
					check[6] = 0;
				}
				break;
			
			case R.id.Result8:
				if (checked) {
					check[7] = 1;
				} else {
					check[7] = 0;
				}
				break;
			
			case R.id.Result9:
				if (checked) {
					check[8] = 1;
				} else {
					check[8] = 0;
				}
				break;
			
			case R.id.Result10:
				if (checked) {
					check[9] = 1;
				} else {
					check[9] = 0;
				}
				break;
			
			case R.id.Result11:
				if (checked) {
					check[10] = 1;
				} else {
					check[10] = 0;
				}
				break;
			
			case R.id.Result12:
				if (checked) {
					check[11] = 1;
				} else {
					check[11] = 0;
				}
				break;
		}
	}
	
	//―――――――――――――――――――――――――――――――――――
	// 他クラスで使用するときのCheckBoxのオブジェクト生成メソッド
	//―――――――――――――――――――――――――――――――――――
	public ArrayList<CheckBox> Array_Check_Box(Context context) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		@SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_check, null);
		
																			//CheckBoxのオブジェクト生成
		Result1 = view.findViewById(R.id.Result1);
		Result2 = view.findViewById(R.id.Result2);
		Result3 = view.findViewById(R.id.Result3);
		Result4 = view.findViewById(R.id.Result4);
		Result5 = view.findViewById(R.id.Result5);
		Result6 = view.findViewById(R.id.Result6);
		Result7 = view.findViewById(R.id.Result7);
		Result8 = view.findViewById(R.id.Result8);
		Result9 = view.findViewById(R.id.Result9);
		Result10 = view.findViewById(R.id.Result10);
		Result11 = view.findViewById(R.id.Result11);
		Result12 = view.findViewById(R.id.Result12);
		
		Result = new ArrayList<>();
		Result.add(Result1);
		Result.add(Result2);
		Result.add(Result3);
		Result.add(Result4);
		Result.add(Result5);
		Result.add(Result6);
		Result.add(Result7);
		Result.add(Result8);
		Result.add(Result9);
		Result.add(Result10);
		Result.add(Result11);
		Result.add(Result12);
		
		return Result;
	}
}
