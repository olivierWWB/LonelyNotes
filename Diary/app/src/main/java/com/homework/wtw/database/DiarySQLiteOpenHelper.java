package com.homework.wtw.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiarySQLiteOpenHelper extends SQLiteOpenHelper {

	private static String REMOTE_LIVE_DATABASE_NAME = "Diary.db";
	private static int version = 1;
	public static final String DATABASE_TABLE_USER = "diary";
	public static final String COL_ID = "diary_id";
	public static final String COL_CONTENT = "content";
	public static final String COL_DATE = "date";
	public static final String COL_DAY = "day";
	public static final String COL_TAG = "tag";
	public static final String COL_USERMESSAGE = "user_message";
	public static final String COL_PICTURE = "picture";
	public static final String COL_ADDRESS = "address";
	public static final String COL_WEATHER = "weather";
	public static final String COL_WEATHERIMAGE = "weatherImage";
	public static final String COL_TIME = "create_time";// create time
	private final String REMOTE_LIVE_DATABASE_CREATE ="create table IF NOT EXISTS "+DATABASE_TABLE_USER+"("+
			COL_ID +" integer primary key autoincrement,"+
			COL_CONTENT +" text," +COL_DAY+ " text," + COL_DATE +" text,"+COL_TAG +" text,"+COL_USERMESSAGE +" integer,"+COL_ADDRESS +" text,"+
			COL_WEATHER +" text,"+COL_WEATHERIMAGE +" integer,"+COL_PICTURE +" BLOB,"+COL_TIME+" text)";
	private static DiarySQLiteOpenHelper mInstance = null;
	private static Context mContext;

	public static DiarySQLiteOpenHelper getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new DiarySQLiteOpenHelper(context);
			mContext = context;
		}
		return mInstance;
	}

	private DiarySQLiteOpenHelper(Context context) {
		super(context, REMOTE_LIVE_DATABASE_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REMOTE_LIVE_DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	
}
