package com.example.kensin_gus;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

public class Calendar_Select extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        textView = findViewById(R.id.days);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String str = String.format(Locale.US, "%d/%d/%d",year, monthOfYear+1, dayOfMonth);
        textView.setText( str );

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }
}
