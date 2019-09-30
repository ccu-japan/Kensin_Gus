package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fujitsufrontech.patioprinter.fhtuprt.fhtUprt;

public class PrintKensin {
    PatioPrinter patio = new PatioPrinter();

    Meter meter;                                           // プリント出力項目
    int COL_BAN;
    int check_result;                                    //点検チェックボックスのチェック数
    Context context;
    private fhtUprt mPrint = null;						// プリンタ制御ライブラリ
    private byte[] sendbuf = null;						// 印刷データ用バッファ
    private String mOpenAddrs = "";						// オープンするBluetoothアドレス
    private int mComp = fhtUprt.PRT_DISABLE_COMPLETE;
    private int mMode = 0;                               // 印刷=0 自動電源オフ=1
    private Byte mStatus = 0x00;                         // プリンタステータス
    String[] CHECK_BOX = null;

    //――――――――――――――――――――――――――――――――――――
    // プリンタ出力メソッド
    //――――――――――――――――――――――――――――――――――――
    @SuppressLint({"NewApi", "DefaultLocale"})
    public void printKenshin(int COL_BAN, Context con) {
        context = con;
        meter = new Meter().Meter_List(COL_BAN, context);
        check_result = meter.CHECKBOX_COL() * 4;        //チェックボックス行　*4は行幅
        CHECK_BOX = meter.CHECK_BOX(context);

        patio.LengthUnitMode = PatioPrinter.UNIT_MODE.Millimeter;
        patio.rect = new Rect(4,0,70,117 + check_result);   //プリントサイズ
        patio.WriteMode = PatioPrinter.WRITE_MODE.IMMEDIATE_MODE;                    //プリントモード
        patio.Font = PatioPrinter.FONT.GOSIC;                                          //フォント指定
        patio.InitPage();                                                                //ページ作成

        patio.Bold = false;
        patio.WriteString(28, 10, PatioPrinter.FONT_SIZE.size24, "ガス使用量のお知らせ");
        patio.WriteString(20, 18, "検針日");

        String TODAY = meter.TODAY();   //今日の日付
        TODAY = TODAY.replace("/", "");  //　YYYY/MM/DD　入力から "/"を消す

        patio.WriteString(31, 18, PatioPrinter.FONT_SIZE.size32, TODAY.substring(0, 4) + "年");  //substring 0~4 「YYYY」まで入力
        patio.WriteString(43, 18, TODAY.substring(4, 6) + "月");                                  //substring 5~6 「MM」
        patio.WriteString(51, 18, TODAY.substring(6, 8) + "日");                                  //substring 7~8 「DD」
        patio.WriteString(20, 25, meter.CUSTOMER() + " - " + meter.PLACE_CODE());               //顧客名　＋　設置場所コード

        patio.ZenkakuMode = true;

        //顧客名が10文字以上の場合
        if(meter.C_NAME().length() >= 10) {
            patio.FontSize = PatioPrinter.FONT_SIZE.size24;
        }
        //顧客名が7以上9文字までの場合
        else if(meter.C_NAME().length() >=7)
        {
            patio.FontSize = PatioPrinter.FONT_SIZE.size32;
        }
        //それ以外
        else
        {
            patio.FontSize = PatioPrinter.FONT_SIZE.size48;
        }

        patio.WriteString(20, 31, meter.C_NAME());
        patio.ZenkakuMode = false;
        patio.WriteString(63, 31, PatioPrinter.FONT_SIZE.size24, "様");
        patio.WriteString(25, 35, PatioPrinter.FONT_SIZE.size32, meter.P_NAME());
        patio.LineMode = true;
        patio.WriteString(20, 38, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;

        patio.WriteString(22, 40, PatioPrinter.FONT_SIZE.size24, "前回検針日");
        patio.WriteString(45, 40, "前回使用量");

        patio.WriteString(22, 44, PatioPrinter.FONT_SIZE.size32, meter.LAST_TIME_DAY().substring(2));
        patio.WriteString(45, 44, String.format("%7s", meter.LAST_TIME_USE()) + " m");
        patio.WriteString(63, 42, PatioPrinter.FONT_SIZE.size16, "3");
        patio.LineMode = true;
        patio.WriteString(20, 46, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;
        patio.WriteString(22, 48, PatioPrinter.FONT_SIZE.size24, "旧メータ器使用量");
        patio.WriteString(45, 52, PatioPrinter.FONT_SIZE.size32, String.format("%7s", "0.0") + " m"); // 旧メーター使用量
        patio.WriteString(63, 50, PatioPrinter.FONT_SIZE.size16, "3");
        patio.LineMode = true;
        patio.WriteString(20, 54, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;

        patio.WriteString(22, 56, PatioPrinter.FONT_SIZE.size24, "今回検針");
        patio.WriteString(38, 56, "前回検針");
        patio.WriteString(55, 56, "使用量");
        patio.WriteString(22, 60, PatioPrinter.FONT_SIZE.size32, String.format("%6s", meter.TODAY_POINT())); // 今回指針
        patio.WriteString(38, 60, String.format("%6s", meter.LAST_POINT())); // 前回指針
        patio.WriteString(52, 60, String.format("%6s", meter.USE_COMP()));  // 今回使用量
        patio.LineMode = true;
        patio.WriteString(20, 62, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;

        patio.WriteString(22, 64, PatioPrinter.FONT_SIZE.size24, "基本料金");
        patio.WriteString(38, 64, "ガス料金");
        patio.WriteString(53, 64, "内消費税");
        patio.WriteString(20, 68, PatioPrinter.FONT_SIZE.size32, String.format("%7s", String.format("%,d", meter.STANDARD_PRICE())));
        patio.WriteString(36, 68, String.format("%7s", String.format("%,d", meter.GUS_PRICE())));
        patio.WriteString(51, 68, String.format("%7s", String.format("%,d", meter.TAX())));

        patio.LineMode = true;
        patio.WriteString(20, 70, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;
        patio.WriteString(24, 73, PatioPrinter.FONT_SIZE.size24, "合計");
        patio.WriteString(36, 74, PatioPrinter.FONT_SIZE.size32, String.format("%7s", String.format("%,d", meter.GUS_PRICE())));
        patio.WriteString(51, 74, String.format("%7s", String.format("%,d", meter.TAX())));
        patio.LineMode = true;
        patio.WriteString(20, 76, PatioPrinter.FONT_SIZE.size24, "───────────────");
        patio.LineMode = false;

        INFOMF infomf = new INFOMF().Infomf_list(context);

        String infoline1 = infomf.NEWS().substring(0,15);
        String infoline2 = infomf.NEWS().substring(15);
        patio.WriteString(20,79,PatioPrinter.FONT_SIZE.size24,infoline1);
        patio.WriteString(20,82,infoline2);
        patio.WriteString(20,87,"点検項目");

        if(CHECK_BOX.length == 0)
        {
            patio.WriteString(20,91,"異常なし");    //点検項目1~12　項目1につきy+4
        }else{
            for (int i = 0; i < CHECK_BOX.length; i++)
            {
                patio.WriteString(30, 91 + ((i+1)*4), "＊" + CHECK_BOX[i]);
            }
        }

        patio.WriteString(20,97 +check_result,infomf.COMPANY_NAME());
        patio.WriteString(26,105+check_result,PatioPrinter.FONT_SIZE.size32,infomf.PHONE_NUMBER());

        sendbuf = patio.PrintPage();

    }

    //―――――――――――――――――――――――――――――――――――――
    //  プリントオープンメソッド　
    //―――――――――――――――――――――――――――――――――――――
    public void Print_Open(int return_col , Context con, String RETURN_ADDRESS){
        COL_BAN = return_col;
        context = con;
        mOpenAddrs = RETURN_ADDRESS;
        mPrint = new fhtUprt();
        mPrint.fhtPrInit(context,mHandler);

        // プリンタのオープン
        mMode = 0;  // モード：印刷

        if( mPrint.fhtPrOpen(mOpenAddrs) != fhtUprt.PRT_SUCCESS ){
            Log.d("Print_Search","接続先がpatioPrinterではありません");
        }
        Log.d("Print_Search",""+ mPrint.fhtPrGetStatus());
    }

    ///////////////////////////////////////////////////////////////////////////
    // プリンタ制御ライブラリの完了通知を受けるハンドラ
    //
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // fhtPrOpen の完了通知
                case fhtUprt.FHTPROPEN_FINISHED:
                    if (msg.arg1 == fhtUprt.PRT_SUCCESS) {    // 結果判定
                        if (mPrint.fhtPrSense() != fhtUprt.PRT_SUCCESS) {    // プリンタの状態取得
                            mPrint.fhtPrClose();
                            return;        // エラー完了
                        }
                    }
                    break;

                // fhtPrSense の完了通知
                case fhtUprt.FHTPRSENSE_FINISHED:
                    // ステータスの確認
                    //---------------------------------------------------------------------------
                    mStatus = (byte) (msg.arg2);
                    // MCU異常out true RetryCnt
                    if ((mStatus & (byte) fhtUprt.PRT_ERRSTATUS_MCU) == fhtUprt.PRT_ERRSTATUS_MCU) {
                        mPrint.fhtPrClose();
                        return;
                    }
                    // ヘッド温度異常
                    else if ((mStatus & (byte) fhtUprt.PRT_ERRSTATUS_HEADTEMP) == fhtUprt.PRT_ERRSTATUS_HEADTEMP) {
                        mPrint.fhtPrClose();
                        return;
                    }
                    // パワーアラーム
                    else if ((mStatus & (byte) fhtUprt.PRT_ERRSTATUS_POWERALARM) == fhtUprt.PRT_ERRSTATUS_POWERALARM) {
                        mPrint.fhtPrClose();
                        return;
                    }
                    // カバーオープン（カバーオープン中は用紙なしも検出するため、先に確認する）
                    else if ((mStatus & (byte) fhtUprt.PRT_ERRSTATUS_COVEROPEN) == fhtUprt.PRT_ERRSTATUS_COVEROPEN) {
                        mPrint.fhtPrClose();
                        return;
                    }
                    // 用紙なし
                    else if ((mStatus & (byte) fhtUprt.PRT_ERRSTATUS_ENDOFPAPER) == fhtUprt.PRT_ERRSTATUS_ENDOFPAPER) {
                        mPrint.fhtPrClose();
                        return;
                    }
                    // エラーなしのため印刷・自動電源オフ実行（バッテリアラームはエラーとしない）
                    //---------------------------------------------------------------------------
                    // 印刷実行の場合
                    if (mMode == 0) {
                        printKenshin(COL_BAN, context);

                        // 印刷依頼
                        // fhtPrPrintExメソッドは、1024バイト単位に印字データ送信。 fhtPrPrintメソッドは、255バイト単位に印字データ送信。
                        if (mPrint.fhtPrPrintEx(sendbuf, mComp) != fhtUprt.PRT_SUCCESS) {
                            mPrint.fhtPrClose();
                            return;                    // Bluetoothクローズしエラー完了
                        }
                    }
                    break;

                // fhtPrPrintEx の転送状況通知
                case fhtUprt.FHTPRPRINTEX_PROGRESS:
                    Log.d("PrintActivity", "FHTPRPRINTEX_PROGRESS(" + String.valueOf(msg.arg1) + "/" + String.valueOf(msg.arg2) + ")");
                    break;

                // fhtPrPrintEx の完了通知
                case fhtUprt.FHTPRPRINTEX_FINISHED:
                    Log.d("PrintActivity", "FHTPRPRINTEX_FINISHED");
                    Log.d("PrintActivity", "PrintExFinish(" + msg.arg1 + ")");

                    // プリンタのクローズ
                    mPrint.fhtPrClose();
                    break;

                // fhtPrSetAutoPOff の完了通知
                case fhtUprt.FHTPRSETAUTOPOFF_FINISHED:
                    // プリンタのクローズ
                    mPrint.fhtPrClose();
                    break;
            }
        }
    };

}
