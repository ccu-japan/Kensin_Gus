package com.example.kensin_gus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Check2Activity extends AppCompatActivity {
    final Intent main_activity = getIntent();
    final int[] check = main_activity.getIntArrayExtra("CHECK_KEY");
    int ban = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        //--------------------------------------------------------------------------------------------
        //
        // **インスタンス化
        //
        //--------------------------------------------------------------------------------------------
        Button Check_OK = findViewById(R.id.Check_OK);
        CheckBox Result1 = findViewById(R.id.Result1);
        CheckBox Result2 = findViewById(R.id.Result2);
        CheckBox Result3 = findViewById(R.id.Result3);
        CheckBox Result4 = findViewById(R.id.Result4);
        CheckBox Result5 = findViewById(R.id.Result5);
        CheckBox Result6 = findViewById(R.id.Result6);
        CheckBox Result7 = findViewById(R.id.Result7);
        CheckBox Result8 = findViewById(R.id.Result8);
        CheckBox Result9 = findViewById(R.id.Result9);
        CheckBox Result10 = findViewById(R.id.Result10);
        CheckBox Result11 = findViewById(R.id.Result11);
        CheckBox Result12 = findViewById(R.id.Result12);

        ArrayList<CheckBox> Result = new ArrayList<CheckBox>();
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



        while(true){
            try{
                if(check[ban] == 1){
                    Result.get(ban).setChecked(true);
                }else{
                    Result.get(ban).setChecked(false);
                }
                ban++;
            }catch (IndexOutOfBoundsException e){
                break;
            }
        }


        Check_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_activity.putExtra("CHECK_KEY",check);
                setResult(RESULT_OK,main_activity);
                finish();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        final boolean checked = ((CheckBox)view).isChecked();
        switch (view.getId()){
            case R.id.Result1 :
                if(checked){
                    check[0]=1;
                }
                else
                {
                    check[0]=0;
                }
                break;

            case R.id.Result2 :
                if(checked){
                    check[1]=1;
                }
                else
                {
                    check[1]=0;
                }
                break;

            case R.id.Result3 :
                if(checked){
                    check[3]=1;
                }
                else
                {
                    check[3]=0;
                }
                break;

            case R.id.Result4 :
                if(checked){
                    check[4]=1;
                }
                else
                {
                    check[4]=0;
                }
                break;

            case R.id.Result5 :

                break;

            case R.id.Result6 :

                break;

            case R.id.Result7 :

                break;

            case R.id.Result8 :

                break;

            case R.id.Result9 :

                break;

            case R.id.Result10:

                break;

            case R.id.Result11 :

                break;

            case R.id.Result12 :

                break;
        }


    }
}
