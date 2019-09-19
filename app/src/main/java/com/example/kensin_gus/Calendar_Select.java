package com.example.kensin_gus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

public class Calendar_Select extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    private TextView textView;
    Intent CALENDAR_INTENT;
    String str ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);



        textView = findViewById(R.id.days);
        CALENDAR_INTENT = new Intent();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        str = String.format(Locale.US, "%d/%02d/%d", year, monthOfYear + 1, dayOfMonth);
        textView.setText(str);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void OK(View v) {
        if(!textView.getText().toString().equals("")) {
            CALENDAR_INTENT.putExtra("CALENDAR_KEY", String.valueOf(textView));
            setResult(RESULT_OK, CALENDAR_INTENT);
            finish();
        }
        else{
            finish();
        }
    }
    public void Cancel(View v){
        finish();
    }

}