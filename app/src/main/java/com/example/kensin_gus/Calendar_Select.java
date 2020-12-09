package com.example.kensin_gus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import java.util.Locale;

public class Calendar_Select extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView textView;
    Intent CALENDAR_INTENT;
    String str ;

    //----------------------------------------------------------------------
    // カレンダーレイアウト　呼び出しメソッド
    //----------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        textView = findViewById(R.id.days);
        CALENDAR_INTENT = new Intent();
        textView.setText(CALENDAR_INTENT.getStringExtra("CALENDAR_KEY"));
    }

    //----------------------------------------------------------------------
    // 日付出力メソッド
    //----------------------------------------------------------------------
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        str = String.format(Locale.US, "%d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
        textView.setText(str);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //OKボタン押下時
    public void OK(View v) {
        //入力値がある場合
        if(!textView.getText().toString().equals("")) {
            CALENDAR_INTENT.putExtra("CALENDAR_KEY", textView.getText().toString());
            setResult(RESULT_OK, CALENDAR_INTENT);
            finish();   //Activity破棄
        }
        //ない場合
        else{
            finish();
        }
    }

    //キャンセルボタン押下時
    public void Cancel(View v){
        finish();
    }
}