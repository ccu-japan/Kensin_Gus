package com.example.kensin_gus;

public class BluetoothDevice {
	private String DevName;				// 検索したBluetoothデバイス名
	private String DevAddr;				// 検索したBluetoothアドレス
	private String ItemMsg;				// 表示用データ

	public BluetoothDevice( String DevName, String DevAddr, String ItemMsg ){
		this.DevName = DevName;
		this.DevAddr = DevAddr;
		this.ItemMsg = ItemMsg;
	}

	public String getDevName(){
		return this.DevName;
	}

	public String getDevAddr(){
		return this.DevAddr;
	}

	public String getItemMsg(){
		return this.ItemMsg;
	}
}
