package com.ynu.healthyfriendv05.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.ynu.healthyfriendv05.R;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final int BUFFER_SIZE = 400000;

	private static final String DB_NAME = "healthyfriend.db";
	private static final int DB_VERSION = 1;

	public static final String PACKAGE_NAME = "com.ynu.healthyfriendv05";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // ���ֻ��������ݿ��λ��

	private SQLiteDatabase database;
	private Context context;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, null, version);
		this.context = context;
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, DB_VERSION);
	}

	public DatabaseHelper(Context context) {
		this(context, DB_PATH + "/" + DB_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void openDatabase() {
		this.database = importDatabase(DB_PATH + "/" + DB_NAME);
	}

	public SQLiteDatabase importDatabase(String dbfile) {
		SQLiteDatabase temp_database = null;
		try {
			File dir = new File(DB_PATH);
			if (!dir.exists()) // ����ļ��в����ڴ����ļ���
				dir.mkdir();
			if (!(new File(dbfile).exists())) {// �ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
				InputStream is = this.context.getResources().openRawResource(
						R.raw.healthyfriend); // ����������ݿ�
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			temp_database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return temp_database;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		Log.e("���ݿ���Ϣ", "�ر����ݿ�");
		this.close();
		this.database.close();
	}
}