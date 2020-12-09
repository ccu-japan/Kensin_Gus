package com.example.db_library;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class File_Stream
{
	public static String strearms(String Name, File Path) throws FileNotFoundException
	{
		File file = new File(Path, Name);
		
		if(file.exists())
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader buffer = new BufferedReader(inputStreamReader);
			ContentValues contentValues = new ContentValues();
			String line;
			
		}
			
			
			return "";
	}
}
