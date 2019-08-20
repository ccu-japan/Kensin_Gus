package com.example.kensin_gus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePick extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog((Calendar_Select) Objects.requireNonNull(getActivity()), (Calendar_Select)getActivity(),  year, month, day);
    };

    @Override
    public void onDateSet(android.widget.DatePicker view, int year,
                          int monthOfYear, int dayOfMonth) {
    }

}
