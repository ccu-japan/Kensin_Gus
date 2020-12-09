package com.example.kensin_gus;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class Layout_Size extends KensinMainActivity {

	public float dp;
	public int realScreenWidth, realScreenHeight,screenWidth, screenHeight, rswDp, rshDp,swDp, shDp;
	private KensinMainActivity kensin = null;

	public int layoutWidth ,layoutHeight,lwDp,lhDp;

	@SuppressLint("NewApi")
	public void Layout_GETTER(KensinMainActivity p_kensin) {

		kensin = p_kensin;

		dp = kensin.getResources().getDisplayMetrics().density;

		// true: AppCompatActivity
		// false: AppCompatActivity -> Activity 変更
		boolean flg = true;

		if (flg) {
			WindowManager wm = (WindowManager) kensin.getSystemService(WINDOW_SERVICE);
			Display disp;
			if (wm != null) {
				disp = wm.getDefaultDisplay();
			} else {
				return;
			}

			// real diplay area
			Point realSize = new Point();
			disp.getRealSize(realSize);
			realScreenWidth = realSize.x;
			realScreenHeight = realSize.y;

			rswDp = (int) (realScreenWidth / dp);
			rshDp = (int) (realScreenHeight / dp);

			// applicaton display area
			Point size = new Point();
			disp.getSize(size);
			screenWidth = size.x;
			screenHeight = size.y;

			swDp = (int) (screenWidth / dp);
			shDp = (int) (screenHeight / dp);
		} else {
			DisplayMetrics dMetrics = new DisplayMetrics();
			kensin.getWindowManager().getDefaultDisplay().getRealMetrics(dMetrics);

			realScreenWidth = dMetrics.widthPixels;
			realScreenHeight = dMetrics.heightPixels;

			rswDp = (int) (realScreenWidth / dp);
			rshDp = (int) (realScreenHeight / dp);

			kensin.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
			screenWidth = dMetrics.widthPixels;
			screenHeight = dMetrics.heightPixels;

			swDp = (int) (screenWidth / dp);
			shDp = (int) (screenHeight / dp);
		}
	}

	public void Layout_Area_Calc()
	{
		ConstraintLayout layout = kensin.findViewById(R.id.activity_main);
		//LinearLayout layout = findViewById(R.id.activity_main);
		layoutWidth = layout.getWidth();
		layoutHeight = layout.getHeight();

		lwDp = (int) (layout.getWidth() / kensin.layout_size.dp);
		lhDp = (int) (layout.getHeight() / kensin.layout_size.dp);

		String[] title = {
				"--- Real diplay area ---\n",
				"--- Application diplay area ---\n",
				"--- Layout area ---"};

		String[] keypara = {"width = ", "height = ", "width dp= ",
				"height dp= "};
		int[] wh1 = {kensin.layout_size.realScreenWidth, kensin.layout_size.realScreenHeight, kensin.layout_size.rswDp, kensin.layout_size.rshDp};
		int[] wh2 = {kensin.layout_size.screenWidth, kensin.layout_size.screenHeight, kensin.layout_size.swDp, kensin.layout_size.shDp};
		int[] wh3 = {layoutWidth, layoutHeight, lwDp, lhDp};
		String[] unit = {" pix\n", " pix\n", " dp\n", " dp\n"};
		String str1 = "dp = " + String.valueOf(kensin.layout_size.dp) + "\n\n";
		StringBuilder sb1 = new StringBuilder(str1);
		sb1.append(title[0]);
		for (int i = 0; i < 4; i++) {
			sb1.append(keypara[i]);
			String str = String.valueOf(wh1[i]);
			sb1.append(str);
			sb1.append(unit[i]);
		}
		sb1.append("\n");
		System.out.println(sb1);

		String str2 = "--- Application diplay area ---\n";
		StringBuilder sb2 = new StringBuilder(str2);
		sb1.append(title[1]);
		for (int i = 0; i < 4; i++) {
			sb2.append(keypara[i]);
			String str = String.valueOf(wh2[i]);
			sb2.append(str);
			sb2.append(unit[i]);
		}
		sb2.append("\n");
		System.out.println(sb2);

		String str3 = "--- Layout area ---\n";
		StringBuilder sb3 = new StringBuilder(str3);
		sb1.append(title[2]);
		for (int i = 0; i < 4; i++) {
			sb3.append(keypara[i]);
			String str = String.valueOf(wh3[i]);
			sb3.append(str);
			sb3.append(unit[i]);
		}
		sb3.append("\n");
		System.out.println(sb3);
	}

	public void Layout_Depiction()
	{
		//ConstraintLayout layout = kensin.findViewById(R.id.activity_main);
		ConstraintLayout layout1 = kensin.findViewById(R.id.constraintLayout1);
		ConstraintLayout layout2 = kensin.findViewById(R.id.constraintLayout2);
		ConstraintLayout layout3 = kensin.findViewById(R.id.constraintLayout3);
		ConstraintLayout layout4 = kensin.findViewById(R.id.constraintLayout4);

		layout1.setMaxWidth(layoutWidth);
		layout2.setMaxWidth(layoutWidth);
		layout3.setMaxWidth(layoutWidth);
		layout4.setMaxWidth(layoutWidth);

		layout1.setMinHeight(0);
		layout1.setMaxHeight((int) Math.floor(layoutHeight / 4));

		layout2.setMinHeight(layout1.getMaxHeight());
		layout2.setMaxHeight((int) Math.floor(layoutHeight / 4));

		layout3.setMinHeight(layout2.getMaxHeight());
		layout3.setMaxHeight((int) Math.floor(layoutHeight / 4));

		layout4.setMinHeight(layout3.getMaxHeight());
		layout4.setMaxHeight((int) Math.floor(layoutHeight / 4));
	}
}

