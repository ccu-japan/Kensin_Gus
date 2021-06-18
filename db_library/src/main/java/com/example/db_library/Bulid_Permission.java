package com.example.db_library;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class Bulid_Permission
{
																			//パーミッション許可
	 private static int REQUEST_CODE = 1;
	
	public static void Permmsion_Search(Activity activity)
	{
		try
		{
			System.out.println("Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
																			// アンドロイド端末のSDKが23以上の場合許可が必要
			if (Build.VERSION.SDK_INT >= 23) {
				String[] permissions = {
																			// 内部ストレージ(SDカード）の読取
						Manifest.permission.READ_EXTERNAL_STORAGE,
																			// 内部ストレージ(SDカード)への書込
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
																			// GPSでの位置情報を取得
						Manifest.permission.ACCESS_FINE_LOCATION,
																			// ネットワークから位置情報を取得
						Manifest.permission.ACCESS_COARSE_LOCATION
				};
				checkPermission(permissions, REQUEST_CODE,activity);
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	
	//----------------------------------------------------------------------
	//
	//  パーミッションの許可申請メソッド
	//
	//---------------------------------------------------------------------
	private static void checkPermission(String[] permissions, int request_code, Activity activity)
	{
		try
		{
			ActivityCompat.requestPermissions(activity, permissions, request_code);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}
