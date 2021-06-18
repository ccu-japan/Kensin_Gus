	package com.example.kensin_gus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.os.IResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.db_library.Bulid_Permission;
import com.example.db_library.GlobalFlag;
import com.example.db_library.HYOF;
import com.example.db_library.Kenshin_DB;
import com.example.db_library.TENKEN;
import com.example.db_library.TOKUIF;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class KensinMainActivity  extends AppCompatActivity
{
	
	public static boolean CHECK_FLG = false;                                //未/済ボタンフラグ　true:未　false:済
	public boolean DB_REG_FLG = false;                                      //db_registrationの出力判定
	public int COL_BAN = 0;                                                 //DBの連番
	public int[] MULTIPLE_CHECKBOX = new int[12];                           //点検クラスのチェックボックス(1:入り 0:外す）
	public String RETURN_ADDRESS = "";                                      //使用するBlueToothアドレスの格納変数
	public String TODAY;
	private String Current_time;
																			//Activityの結果を取得ための整数コード(*整数のみ )
	private final int CHECK_RESULT = 1;                                     //点検アクティビティ結果の取得
	private final int ROOT_SEARCH_RESULT = 2;                               //道順アクティビティ結果の取得
	private final int TEN_KEY_RESULT = 3;                                   //10キーアクティビティ結果の取得
	private final int CALENDAR_RESULT = 4;                                  //カレンダーアクティビティ結果の取得
	private final int PRINTER_RESULT = 5;                                   //印刷アクティビティ結果の取得
	
	//int REQUEST_CODE = 1;                                                 //パーミッション許可
	
	Button KENSIN_BUTTON;                                                   //検針ボタン
	Button FIXED_UNFIXED_BUTTON;                                            //済ボタン
	Button CHECK_BUTTON;                                                    //点検ボタン
	Button PRINTER_BUTTON;                                                  //印刷ボタン
	
	EditText INPUT_NUMBER_EDIT;                                             //数値入力テキスト
	TextView CURRENCT_USAGE_TEXT;                                           //使用量テキスト
	TextView CURRENCT_AMOUNT_TEXT;                                          //金額テキスト
	TextView DAYS;                                                          //日付テキスト
	TextView TAX;                                                           //消費税分テキスト
	
	private TOKUIF tokuif;                                                  //TOKUIFクラス
	private HYOF hyof;                                                      //HYOFクラス
	private TENKEN tenken;                                                  //TENKENクラス
	private Kenshin_DB kenshin_db;                                          //検針DBクラス
	private Button_Processing button_processing;                            //済ボタンクラス
	private Screen_Layout.Main_Screen main_screen;                          //表示値変更クラス
	private LayoutInflater layoutInflater;
	private ContentValues VALUES;
	private Dialog dialog;
	private Cursor cursor;
	private View v_Id;                                                      //印刷ボタンのチェックView
	
	public Layout_Size layout_size;
	public GETTER getter;
	
	
	//----------------------------------------------------------------------
	//
	//  メインmethod (画面構築）
	//
	//----------------------------------------------------------------------
	@SuppressLint("SimpleDateFormat")
	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);                             // メインアクティビティの生成
		
		try
		{
			Bulid_Permission.Permmsion_Search(this);                        //アプリ権限許可
			
																			// インスタンス化
			kenshin_db = new Kenshin_DB(this);
			tokuif = new TOKUIF();
			hyof = new HYOF();
			tenken = new TENKEN();
			button_processing = new Button_Processing();
			main_screen = new Screen_Layout.Main_Screen();
			layoutInflater = LayoutInflater.from(this);
			dialog = new Dialog();
			VALUES = new ContentValues();
			layout_size = new Layout_Size();
			getter = new GETTER();
			
																			//ViewにID設定
			CHECK_BUTTON = findViewById(R.id.Check_Button);
			KENSIN_BUTTON = findViewById(R.id.Kensin_Button);
			FIXED_UNFIXED_BUTTON = findViewById(R.id.Update);
			PRINTER_BUTTON = findViewById(R.id.Printer_Button);
			
			INPUT_NUMBER_EDIT = findViewById(R.id.Input_number);

			CURRENCT_USAGE_TEXT = findViewById(R.id.Used_number);
			CURRENCT_AMOUNT_TEXT = findViewById(R.id.Using_Amount);
			
			TAX = findViewById(R.id.Tax);
			DAYS = findViewById(R.id.date_now);
			Current_time = Screen_Layout.Main_Screen.Today();
	
			findViewById(R.id.Kensin_Button).setOnClickListener(buttonListener);
			findViewById(R.id.customer_Button).setOnClickListener(buttonListener);
			findViewById(R.id.Printer_Button).setOnClickListener(buttonListener);
			findViewById(R.id.Calender_Button).setOnClickListener(buttonListener);
			findViewById(R.id.end_app).setOnClickListener(buttonListener);
			findViewById(R.id.Input_tsv).setOnClickListener(buttonListener);
			findViewById(R.id.Output_tsv).setOnClickListener(buttonListener);
			findViewById(R.id.up).setOnClickListener(buttonListener);
			findViewById(R.id.down).setOnClickListener(buttonListener);
			findViewById(R.id.Check_Button).setOnClickListener(buttonListener);
			
			findViewById(R.id.Input_number).setOnClickListener(buttonListener);
			findViewById(R.id.Used_number).setOnClickListener(buttonListener);
			findViewById(R.id.Update).setOnClickListener(buttonListener);
		
			
			DataBase_Create();
			
		} catch (Exception ex)
		{
			System.out.println("Activity_MAIN : " + ex.getMessage());
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		try
		{
			super.onWindowFocusChanged(hasFocus);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	
	//----------------------------------------------------------------------
	//パーミッション許可が下りているか確認メソッド
	//----------------------------------------------------------------------
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		for (int i = 0; i < permissions.length; i++)
		{
			if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
			{
				Log.d("Permission", "許可されています");
			}
			else
			{
				Log.d("Permission", "許可されていません");
			}
		}
	}
	
	//-------------------------------------------------------------------
	//
	//  DB作成メソッド
	//
	//-------------------------------------------------------------------
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void DataBase_Create()
	{
		try
		{
			String query = "select count(*) from sqlite_master where type='table' and name= 'TOKUIF'";
			cursor = kenshin_db.db.rawQuery(query, null);
			cursor.moveToFirst();
			String result = cursor.getString(0);
			
																			// TABLE:TOKUIF がない場合、作成
			if (result.equals("0"))
			{
				tokuif.onCreate(kenshin_db.db);
				hyof.onCreate(kenshin_db.db);
				tenken.CreateDB(kenshin_db.db);
				Log.d("DB_TABLE", "テーブルを作成しました");
				main_Input();
			}
			else
			{
				Log.d("DB_TABLE", "既にテーブルは作られています");
			}
			
																			// TOKUIFテーブルのデータを取得
			cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
			
																			// データがあればレイアウト入力
			if (cursor.getCount() > 0)
			{
																			// TOKUIFテーブルの一番最初の人を出力
				cursor.moveToFirst();
				COL_BAN = Integer.parseInt(cursor.getString(0));
				getter = Screen_Layout.Main_Screen.SELECT_COM(KensinMainActivity.this,  COL_BAN, kenshin_db.db);
				COL_BAN = getter.BAN;
				CHECK_FLG = getter.FLG;
				
				button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
				Log.d("COL_BAN_DB_REG",String.valueOf(COL_BAN));
			}
			else
			{
																			//データがない場合は特定のボタンを選択できなくする
				((Button)findViewById(R.id.Kensin_Button)).setEnabled(false);
				((Button)findViewById(R.id.Check_Button)).setEnabled(false);
				((Button)findViewById(R.id.Printer_Button)).setEnabled(false);
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	
	//----------------------------------------------------------------------
	//
	//　メインアクティビティを再生成する時に起動するメソッド
	//
	//----------------------------------------------------------------------
	@SuppressLint("NewApi")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
																			//保存していたものを復元させる
		super.onRestoreInstanceState(savedInstanceState);
		COL_BAN = Integer.valueOf(Objects.requireNonNull(savedInstanceState.getString("COL_BAN")));
		DB_REG_FLG = Boolean.parseBoolean(savedInstanceState.getString("DB_REG_FLG"));
	}
	
	//――――――――――――――――――――――――――――――――――――
	//　アクティビティの結果を受け取るメソッド
	//――――――――――――――――――――――――――――――――――――
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent intent)
	{
		super.onActivityResult(REQUEST_CODE, resultCode, intent);
		
		if (resultCode == RESULT_OK && intent != null)
		{
			switch (REQUEST_CODE)
			{
																			//点検アクティビティからの結果を受け取る
				case CHECK_RESULT:
					MULTIPLE_CHECKBOX = intent.getIntArrayExtra("CHECK_KEY");
					tokuif.Check_Result_return(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX, TODAY);
					break;
					
																			//道順アクティビティからの結果を受け取る
				case ROOT_SEARCH_RESULT:
					COL_BAN = intent.getIntExtra("ROOT_KEY", 0);
					getter = Screen_Layout.Main_Screen.SELECT_COM(KensinMainActivity.this, COL_BAN, kenshin_db.db);
					COL_BAN = getter.BAN;
					CHECK_FLG = getter.FLG;
					
					button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
					break;
				
				case TEN_KEY_RESULT:
																			//テンキーアクティビティの結果
					StringBuilder Result = new StringBuilder(intent.getStringExtra("RESULT_KEY"));
					
																			// 文字列が ２以上の時
					if (Result.length() > 1)
					{
						if (Result.charAt(1) != '.')
						{
							if (Result.charAt(0) == '0')
							{
								Result.deleteCharAt(0);
							}
						}
					}
					
					//INPUT_NUMBER_EDIT.setText(Result);
					@SuppressLint("Recycle")
					Cursor getMeters = kenshin_db.db.rawQuery("select M_C_flag , M_E_usage FROM TOKUIF WHERE ban = ? ", new String[]{String.valueOf(COL_BAN)});
					getMeters.moveToFirst();
					
					float  row = Float.parseFloat(Result.toString());
					String Used_Amount = String.valueOf((row == 0.0) ? "0" : row );
					
					((EditText)findViewById(R.id.Input_number)).setText( Used_Amount);
					
					if(getMeters.getString(0).equals("1"))
							Used_Amount =String.valueOf(( Float.parseFloat(Used_Amount) + Float.parseFloat(getMeters.getString(1)) ));
					
					((TextView) findViewById(R.id.Used_number)).setText(Calc_class.Calc_Used(Float.parseFloat(Used_Amount), kenshin_db.db, COL_BAN));
					
					if(!((TextView)this.findViewById(R.id.Input_number)).getText().toString().equals("0")
						&&  !((TextView)this.findViewById(R.id.Used_number)).getText().toString().equals("0"))
					{
						Calc_class.Calc_HYOF_PRICE(Float.parseFloat(((TextView) findViewById(R.id.Used_number)).getText().toString()), kenshin_db.db, COL_BAN, KensinMainActivity.this);
					}
					
					break;
																			// プリンターアクティビティの結果
				case PRINTER_RESULT:
					RETURN_ADDRESS = intent.getStringExtra("PatioPrinter");
					DB_REG_FLG = true;
					new PrintKensin().Print_Open(COL_BAN, KensinMainActivity.this, RETURN_ADDRESS);
					main_Down();
					
					break;
					
				case CALENDAR_RESULT:
					TODAY = intent.getStringExtra("CALENDAR_KEY");
					((TextView)findViewById(R.id.date_now)).setText(TODAY);
					
					break;
					
				default:
					break;
			}
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////
	///
	///     ボタン制御フロー
	///
	////////////////////////////////////////////////////////////////////////
	View.OnClickListener buttonListener = new View.OnClickListener()
	{
		@RequiresApi(api = Build.VERSION_CODES.O)
		@SuppressLint("NonConstantResourceId")
		@Override
		public void onClick(View v)
		{
			try
			{
				switch (v.getId())
				{
					case R.id.Kensin_Button :
						main_Kensin();
						break;
					
					case R.id.customer_Button :
						main_Root_Search();
						break;
					
					case R.id.Printer_Button :
						main_Printer(v);
						break;
					
					case R.id.Calender_Button :
						main_Calendar();
						break;
					
					case R.id.end_app :
						main_Finish();
						break;
					
					case R.id.Input_tsv :
						main_Input();
						break;
					
					case R.id.Output_tsv :
						main_Output();
						break;
					
					case R.id.up :
						main_up();
						break;
					
					case R.id.down :
						main_Down();
						break;
					
					case R.id.Check_Button :
						main_Check();
						break;
					
					case R.id.Input_number :
						number_form(v);
						break;
					
					case R.id.Used_number :
						Used_text();
						break;
					
					case R.id.Update:
						Fixed_UnFixed(v);
						break;
					
					default:
						break;
				}
			}
			catch (Exception ex)
			{
				Log.d("Exception",ex.getMessage());
			}
		}
	};
	//----------------------------------------------------------------------
	//  使用量 テキスト
	//----------------------------------------------------------------------
	public void Used_text()
	{
																			//使用量ダイアログの表示
		dialog.Dialog_SYOSAI(layoutInflater, KensinMainActivity.this, kenshin_db.db, COL_BAN);
	}
	
	//----------------------------------------------------------------------
	//  未/済ボタン　
	//----------------------------------------------------------------------
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void Fixed_UnFixed(View view)
	{
		db_registration();                                                  //　データベース登録
																			//　未済アクティビティに遷移 戻り値：Boolean型
		Log.d("CHECK_FLG",String.valueOf(CHECK_FLG));
		CHECK_FLG = button_processing.Check_task(CHECK_FLG,this);
		
																			//CHECK_FLG　TRUE:済　FALSE:未
																			
		if (CHECK_FLG)
		{
																			//印刷メソッド
			main_Printer(view);
		}
	}
	
	//----------------------------------------------------------------------
	//  数値入力テキスト
	//----------------------------------------------------------------------
	public void number_form(View view)
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			
																			//テンキーアクティビティに遷移
			Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
			TEN_KEY_INTENT.putExtra("BUTTON_ID", GlobalFlag.Flag.NUMBER_1);
			TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
			TEN_KEY_INTENT.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	
	}
	
	//----------------------------------------------------------------------
	//  検針メソッド
	//----------------------------------------------------------------------
	public void main_Kensin()
	{
																			//検針アクティビティに遷移
		Intent TEN_KEY_INTENT = new Intent(getApplication(), Ten_key_Process.class);
		TEN_KEY_INTENT.putExtra("BUTTON_ID", GlobalFlag.Flag.NUMBER_1);
		TEN_KEY_INTENT.setAction(Intent.ACTION_VIEW);
		TEN_KEY_INTENT.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(TEN_KEY_INTENT, TEN_KEY_RESULT);
	}
	
	//----------------------------------------------------------------------
	//  道順メソッド
	//----------------------------------------------------------------------
	public void main_Root_Search()
	{
																			//道順アクティビティに遷移
		Intent root_search = new Intent(getApplication(), Root_Search.class);
		root_search.setAction(Intent.ACTION_VIEW);
		root_search.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(root_search, ROOT_SEARCH_RESULT);
	}
	
	//----------------------------------------------------------------------
	//  印刷メソッド
	//----------------------------------------------------------------------
	
	public void main_Printer(final View view)
	{
																			//印刷アクティビティに遷移
		new AlertDialog.Builder(KensinMainActivity.this)
			.setTitle("確認ダイアログ").setMessage("検針票を印刷しますか？")
			.setPositiveButton("No",new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int idx)
				{
					DB_REG_FLG = false;
				}
			}).setNegativeButton("Yes", new DialogInterface.OnClickListener()
		{
			@RequiresApi(api = Build.VERSION_CODES.O)
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
																			//RETURN_ADDRESSの中身が空かどうか判定
				if (RETURN_ADDRESS.equals(""))
				{                                                           // 空：印刷タスクに遷移
					Intent intent = new Intent(KensinMainActivity.this, Print_Search.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(intent, PRINTER_RESULT);
				} else
				{                                                           // 有：選択したプリンタを継続して使用
					DB_REG_FLG = true;
					new PrintKensin().Print_Open(COL_BAN, getApplication(), RETURN_ADDRESS);
					if (view.equals(findViewById(R.id.Update)))
					{
						main_Down();
					}
				}
			}
		}).show();
	}
	
	//----------------------------------------------------------------------
	//  カレンダーメソッド
	//----------------------------------------------------------------------
	public void main_Calendar()
	{
																			//カレンダーアクティビティに遷移
		Intent calender_intent = new Intent(getApplication(), Calendar_Select.class);
		calender_intent.setAction(Intent.ACTION_VIEW);
		calender_intent.putExtra("CALENDAR_KEY",((TextView)findViewById(R.id.date_now)).getText().toString());
		calender_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(calender_intent, CALENDAR_RESULT);
	}
	
	//----------------------------------------------------------------------
	//  終了メソッド
	//----------------------------------------------------------------------
	public void main_Finish()
	{
		kenshin_db.close();                                                 //DBのクローズ
		finish();
	}
	
	//----------------------------------------------------------------------
	//  TSV形式入力メソッド
	//----------------------------------------------------------------------
	public void main_Input()
	{
		new AlertDialog.Builder(KensinMainActivity.this).setTitle("確認ダイアログ")
			.setMessage("TSVを取り込みますか？").setPositiveButton("No", new DialogInterface.OnClickListener()
		{
																			//No を選択時
			@Override
			public void onClick(DialogInterface dialogInterface, int idx)
			{
				new AlertDialog.Builder(KensinMainActivity.this).setTitle("確認ダイアログ")
					.setMessage("TSVの取込をｷｬﾝｾﾙしました").setPositiveButton("確認", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
						}
					}).show();
			}
		}).setNegativeButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			//Yes　を選択時
			public void onClick(DialogInterface dialogInterface, int i)
			{
				kenshin_db.allDelete("TOKUIF");                             //TOKUIFデータ削除
				kenshin_db.allDelete("HYOF");                               //HYOFデータ削除
				kenshin_db.allDelete("TENKEN_ITEM");                        //TENKENデータ削除
				
				tokuif.TOKUIF_TSV(kenshin_db.db, KensinMainActivity.this);  //TOKUIFの作成
				hyof.HYOF_TSV(kenshin_db.db, KensinMainActivity.this);      //HYOFの作成
				tenken.TENKEN_TSV(kenshin_db.db, KensinMainActivity.this);  //TENKENの作成
				
				new AlertDialog.Builder(KensinMainActivity.this).setTitle("確認ダイアログ")
					.setMessage("TSVを取り込みました").setPositiveButton("確認", new DialogInterface.OnClickListener()
					{
						@RequiresApi(api = Build.VERSION_CODES.O)
						@Override
						//確認後、一番最初の人を出力する
						public void onClick(DialogInterface dialogInterface, int i)
						{
							cursor = kenshin_db.db.rawQuery(" SELECT ban FROM TOKUIF ", null);
							cursor.moveToFirst();
							COL_BAN = Integer.parseInt(cursor.getString(0));
							getter = Screen_Layout.Main_Screen.SELECT_COM(KensinMainActivity.this, COL_BAN, kenshin_db.db);
							COL_BAN = getter.BAN;
							CHECK_FLG = getter.FLG;
							
							button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
						}
					}).show();
			}
		}).show();
		
	}
	
	//----------------------------------------------------------------------
	//  TSV形式出力メソッド
	//----------------------------------------------------------------------
	public void main_Output()
	{
		new AlertDialog.Builder(KensinMainActivity.this)
			.setTitle("確認ダイアログ").setMessage("TSVを出力しますか？")
			.setPositiveButton("No", new DialogInterface.OnClickListener()
			{
				@Override
				//No を選択時
				public void onClick(DialogInterface dialogInterface, int idx)
				{
					new AlertDialog.Builder(KensinMainActivity.this).setTitle("確認ダイアログ")
						.setMessage("TSVの取込をｷｬﾝｾﾙしました")
						.setPositiveButton("確認", new DialogInterface.OnClickListener()
						{
							@Override
							//何もしない
							public void onClick(DialogInterface dialogInterface, int i)
							{
							}
						}).show();
				}
			}).setNegativeButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			//Yes を選択
			public void onClick(DialogInterface dialogInterface, int i)
			{
				tokuif.OUT_PUT_TSV(kenshin_db.db, KensinMainActivity.this);   //出力メソッド
				
				new AlertDialog.Builder(KensinMainActivity.this).setTitle("確認ダイアログ")
					.setMessage("TSVを出力しました")
					.setPositiveButton("確認", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
						}
					}).show();
			}
		}).show();
	}
	
	//----------------------------------------------------------------------
	//  戻るメソッド
	//----------------------------------------------------------------------
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void main_up()
	{
																			//データベースの登録
		db_registration();
		cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
		cursor.moveToFirst();
		//  連番を検索
		while (COL_BAN != Integer.parseInt(cursor.getString(0)))
		{
			cursor.moveToNext();
		}
		//  １つ前の人を出力する
		cursor.moveToPrevious();
		try
		{
			COL_BAN = Integer.parseInt(cursor.getString(0));
			
		} catch (IndexOutOfBoundsException e)
		{
			COL_BAN = - 1;
		}
		getter = Screen_Layout.Main_Screen.SELECT_COM(KensinMainActivity.this, COL_BAN, kenshin_db.db);
		CHECK_FLG = getter.FLG;
		COL_BAN = getter.BAN;
	
		button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
	}
	
	//----------------------------------------------------------------------
	//  進むメソッド
	//----------------------------------------------------------------------
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void main_Down()
	{
		db_registration();
		cursor = kenshin_db.db.rawQuery("SELECT ban FROM TOKUIF ", null);
		cursor.moveToFirst();
		while (COL_BAN != cursor.getInt(0))
		{
			cursor.moveToNext();
		}
																			//１つ次の人を出力する
		cursor.moveToNext();
		
		try
		{
			COL_BAN = cursor.getInt(0);
		}
		catch (IndexOutOfBoundsException e)
		{
			COL_BAN = COL_BAN + 1;
		}
		getter = Screen_Layout.Main_Screen.SELECT_COM(KensinMainActivity.this, COL_BAN, kenshin_db.db);
		CHECK_FLG = getter.FLG;
		COL_BAN = getter.BAN;
		
		button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
	}
	
	//----------------------------------------------------------------------
	//  点検メソッド
	//----------------------------------------------------------------------
	
	public void main_Check()
	{
																			//点検アクティビティのチェック入を出力　戻り値：Int[]
		MULTIPLE_CHECKBOX = TOKUIF.Check_Result(kenshin_db.db, COL_BAN, MULTIPLE_CHECKBOX);
		
																			//点検アクティビティに遷移
		Intent check_activity = new Intent(getApplication(), CheckActivity.class);
		check_activity.putExtra("CHECK_KEY", MULTIPLE_CHECKBOX);
		check_activity.setAction(Intent.ACTION_VIEW);
		check_activity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(check_activity, CHECK_RESULT);
	}
	
																			//データべースに登録する
	public void db_registration()
	{
																			// 検針入力数値
		String Number_Input = ((EditText)findViewById(R.id.Input_number)).getText().toString();
																			// 使用量超過分
		String Usaged = ((TextView)findViewById(R.id.Used_number)).getText().toString();
																			// 使用量金額
		String price =  ((TextView)findViewById(R.id.Using_Amount)).getText().toString();
																			// 消費税
		String Tax = ((TextView)findViewById(R.id.Tax)).getText().toString();
																			// 今日の日付
		TODAY = ((TextView)findViewById(R.id.date_now)).getText().toString();
																			// 消費税計算
		tokuif.TAX_PRICE(price, Tax, kenshin_db.db, COL_BAN, ((TextView)findViewById(R.id.date_now)).getText().toString(),Number_Input, Usaged , TODAY);
		
		button_processing.Up_Down_Button(KensinMainActivity.this, kenshin_db.db, COL_BAN);
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
																			// 画面をタッチすることでキーボード等を隠す
																			//inputMethodManager.hideSoftInputFromWindow(INPUT_NUMBER_EDIT.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
																			//INPUT_NUMBER_EDIT.requestFocus();
		Log.d("onTouchEvent","タッチイベント！");
		
		return false;
	}
	
	public static class GETTER
	{
		int BAN;
		boolean FLG;
	}
}