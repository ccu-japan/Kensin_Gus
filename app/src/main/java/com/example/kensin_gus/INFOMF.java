package com.example.kensin_gus;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class INFOMF {

    public String STX;
    public String TYPE;
    public String COMPANY_CODE;
    public String COMPANY_NAME;
    public String PHONE_NUMBER;
    public String NEWS;
    public String FILLER;
    public String ETX;
    public String END_CODE;

    private String fileName = "INFOMF.TSV";
    private String[] str;
    private File path;
    private File file ;
    private String[] columnData;

    public INFOMF Infomf_list(Context context){

        try
        {
            if (Build.VERSION.SDK_INT >= 29)
            {
                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }
            else
                {
                path = new File(Environment.getExternalStorageDirectory().getPath());
                Log.d("file_path", "API29以下です");
                Log.d("fill_path", String.valueOf(path));
            }
            FileInputStream fileInputStream = null;
            try
            {
                file = new File(path, fileName);
                fileInputStream = new FileInputStream(file);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"Shift-JIS");
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            String line;
            while ((line = buffer.readLine()) != null)
            {
                columnData = line.split("\t", -1);
                str = new String[columnData.length];
                for(int i=0;i<columnData.length;i++)
                {
                    str[i] = columnData[i];
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return INFOMF.this;
    }

    public String STX()            //    STX
    {
        STX = str[0];
        return STX;
    }
    public String TYPE()           //    タイプ
    {
        TYPE = str[1];
        return TYPE;
    }
    public String COMPANY_CODE()   //   会社コード
    {
        COMPANY_CODE = str[2];
        return COMPANY_CODE;
    }
    public String COMPANY_NAME()   //   会社名前
    {
        COMPANY_NAME = str[3];
        return COMPANY_NAME;
    }
    public String PHONE_NUMBER()  //   電話番号
    {
        PHONE_NUMBER = str[4];
        return PHONE_NUMBER;
    }
    public String NEWS()         //    お知らせ
    {
        NEWS = str[5];
        return NEWS;
    }
    public String FILLER()      //   Filler
    {
        FILLER = str[6];
        return FILLER;
    }
    public String ETX()         //   ETX
    {
        ETX = str[7];
        return ETX;
    }
    public String END_CODE()    //   エンドコード
    {
        END_CODE = str[8];
        return END_CODE;
    }
}
