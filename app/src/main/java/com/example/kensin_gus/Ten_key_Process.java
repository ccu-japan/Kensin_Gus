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
    Intent TEN_KEY_INTENT;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kensin_ten_key);

        TEN_KEY_INTENT = getIntent();

        ten_key_edit = findViewById(R.id.ten_key_edit);
        button = findViewById(R.id.button);
        ten_key_OK = findViewById(R.id.ten_key_OK);

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

        ten_key_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ROW1_RESULT = ten_key_edit.getText().toString();
                if (ROW1_RESULT.equals("")) {
                    ROW1_RESULT = "0";
                }
                TEN_KEY_INTENT.putExtra("RESULT_KEY", ROW1_RESULT);
                setResult(RESULT_OK, TEN_KEY_INTENT);
                finish();
            }
        });

    }

    View.OnClickListener buttonNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;

            ten_key_edit.append(button.getText());
        }
    };

}
