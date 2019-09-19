package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Environment;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import com.fujitsufrontech.patioprinter.fhtuprt.fhtUprt;

import static com.example.kensin_gus.PatioPrinter.FONT.GOSIC;

public class PatioPrinter {
    fhtUprt mPrint = new fhtUprt();

    private ArrayList<Byte> _write_buf;

    public enum FONT_SIZE {size16, size24, size32, size48, size64}

    public enum FONT {MINCHOU, GOSIC}

    public enum WRITE_MODE {IMMEDIATE_MODE, ONFILE_MODE}

    public enum UNIT_MODE {Millimeter, Dot}


    public FONT_SIZE FontSize;
    public FONT Font;
    public MeteringRectangle PrintRect; // 印刷領域（位置とサイズ、単位はミリ）
    public int PageLegth;
    public WRITE_MODE WriteMode;
    public UNIT_MODE LengthUnitMode;
    public boolean ZenkakuMode;
    public boolean Bold;
    public boolean LineMode;// 罫線印字用に追加 (2018/01/26)

    //{ get; set; }


    @SuppressLint("NewApi")
    public PatioPrinter() {
        FontSize = FONT_SIZE.size16;
        Font = GOSIC;
        PageLegth = 0;
        WriteMode = WRITE_MODE.IMMEDIATE_MODE;
        PrintRect = new MeteringRectangle(0, 0, 0, 0, 0);
        LengthUnitMode = UNIT_MODE.Millimeter;
        Bold = false;
        _write_buf = new ArrayList<Byte>();

    }

    //文字列を表示する
    public void WriteString(String str) {
        byte[] line_f = {0x0A};
        WriteFont();
        WriteFontSize();
        if (ZenkakuMode) {
            WriteZenkakuMode();
        }

        if (LineMode) {
            WriteLineMode();
        }

        WriteByte(Encode_SJIS(str));

        if (ZenkakuMode) {
            WriteHankakuMode();
        }

        if (LineMode) {
            WriteLineOffMode();
        }
        WriteByte(line_f);
    }

    // 長さの単位をdotに変換
    private int ConvUnitX(int n) {
        if (LengthUnitMode == UNIT_MODE.Millimeter) {
            return n * 80 / 10;
        }
        return n;
    }

    // 長さの単位をdotに変換
    private int ConvUnitY(int n) {
        if (LengthUnitMode == UNIT_MODE.Millimeter) {
            return n * 84 / 10;
        }
        return n;
    }

    //文字列を表示する
    public void WriteString(int x, int y, String str) {
        byte[] _startx = {0x1B, 0x24};
        byte[] _starty = {0x1D, 0x24};
        byte[] bt = new byte[2];
        int sx = ConvUnitX(x);
        int sy = ConvUnitY(y - 6);

        WriteByte(_startx);
        bt[0] = (byte) (sx % 256);
        bt[1] = (byte) (sx / 256);
        WriteByte(bt);

        WriteByte(_starty);
        bt[0] = (byte) (sy % 256);
        bt[1] = (byte) (sy / 256);
        WriteByte(bt);

        WriteString(str);

        if (Bold) {
            WriteByte(_startx);
            bt[0] = (byte) ((sx + 1) % 256);
            bt[1] = (byte) ((sx + 1) / 256);
            WriteByte(bt);

            WriteByte(_starty);
            bt[0] = (byte) (sy % 256);
            bt[1] = (byte) (sy / 256);
            WriteByte(bt);

            WriteString(str);
        }
    }


    //文字列を表示する
    public void WriteString(int x, int y, FONT_SIZE fs, String str) {
        FontSize = fs;
        WriteString(x, y, str);
    }

    //ページ印刷の初期化
    public void InitPage() {

        byte[] _init_code = {0x1B, 0x40, 0x1B, 0x4C, 0x1C, 0x43, 0x01, 0x1D, 0x45, 0x05};// [1B,40]プリンター初期化, [1B,4C]ページモード 、[1C, 43, 01]シフトＪＩＳモード
//            byte[] _init_code = { 0x1B, 0x40, 0x1B, 0x28, 0x45, 0x04, 0x00, 0x05, 0x03, 0x04, 0x00, 0x1B, 0x4C, 0x1C, 0x43, 0x01 }; // [1B,40]プリンター初期化, [1B,4C]ページモード 、[1C, 43, 01]シフトＪＩＳモード
        @SuppressLint({"NewApi", "LocalSuppress"}) int sx = ConvUnitX(PrintRect.getX());
        @SuppressLint({"NewApi", "LocalSuppress"}) int sy = ConvUnitY(PrintRect.getY());
        @SuppressLint({"NewApi", "LocalSuppress"}) int dx = ConvUnitX(PrintRect.getWidth());
        @SuppressLint({"NewApi", "LocalSuppress"}) int dy = ConvUnitY(PrintRect.getHeight());
        WriteByte(_init_code);

        byte[] _set_print_area = {0x1B, 0x57};
        WriteByte(_set_print_area);
        byte[] bt = new byte[8];
        bt[0] = (byte) (sx % 256);
        bt[1] = (byte) (sx / 256);
        bt[2] = (byte) (sy % 256);
        bt[3] = (byte) (sy / 256);
        bt[4] = (byte) (dx % 256);
        bt[5] = (byte) (dx / 256);
        bt[6] = (byte) (dy % 256);
        bt[7] = (byte) (dy / 256);
        WriteByte(bt);
    }
    //-------------------------------------------------------------------------------
    //
    //
    //
    //-------------------------------------------------------------------------------


    public byte[] PrintPage() {
        byte _flush_page = 0x0C;
        _write_buf.add(_flush_page);
        byte[] bt = new byte[_write_buf.size()];
        for (int i = 0; i < _write_buf.size(); i++) {
            bt[i] = _write_buf.get(i);
        }
       return bt;

    }

    //コマンドデータをバッファに出力
    private boolean WriteByte(byte[] bt) {

        for (byte b : bt) {
            _write_buf.add(b);
        }

        return true;
    }

    //フォントの設定
    private void WriteFont() {
        byte[] gosic = {0x1D, 0x43, 0x04};
        byte[] minchou = {0x1D, 0x43, 0x00};

        switch (Font) {
            case GOSIC:
                WriteByte(gosic);
                break;
            case MINCHOU:
                WriteByte(minchou);
                break;
        }
    }

    //フォントサイズの設定
    private void WriteFontSize() {
        byte[] _16dot = {0x1B, 0x4D};
        byte[] _24dot = {0x1B, 0x50};
        byte[] _32dot = {0x1B, 0x67};
        byte[] _toubai = {0x1D, 0x21, 0x00};
        byte[] _baikaku = {0x1D, 0x21, 0x11};

        switch (FontSize) {
            case size16:
                WriteByte(_16dot);
                WriteByte(_toubai);
                break;
            case size24:
                WriteByte(_24dot);
                WriteByte(_toubai);
                break;
            case size32:
                WriteByte(_32dot);
                WriteByte(_toubai);
                break;
            case size48:
                WriteByte(_24dot);
                WriteByte(_baikaku);
                break;
            case size64:
                WriteByte(_32dot);
                WriteByte(_baikaku);
                break;
        }
    }

    //全角モード
    private void WriteZenkakuMode() {
        byte[] _zenkaku = {0x1C, 0x26};
        WriteByte(_zenkaku);
    }

    // 半角モード
    private void WriteHankakuMode() {
        byte[] _hankaku = {0x1C, 0x2E};
        WriteByte(_hankaku);
    }

    // 罫線印字モード (2018/01/26 追加)
    private void WriteLineMode() {
        byte[] _lineon = {0x1B, 0x73, 0x66};
        WriteByte(_lineon);
    }

    // 通常印字速度モード（高速） (2018/01/26 追加)
    private void WriteLineOffMode() {
        byte[] _lineoff = {0x1B, 0x73, 0x60};
        WriteByte(_lineoff);
    }

    // S-JISへのエンコード処理
    private byte[] Encode_SJIS(String str)  {

        byte[] bytestr = new byte[str.length()];
        try {

            bytestr = str.getBytes("Shift-JIS");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bytestr;
    }

}

