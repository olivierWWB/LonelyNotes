package com.homework.wtw.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.homework.wtw.model.Diary;

import java.util.ArrayList;
import java.util.List;

public class DiaryDataBaseOperate {

	private static final String TAG = "DBRemoteLive";
	private static final boolean DEBUG = true;

	protected SQLiteDatabase mDB = null;

	public DiaryDataBaseOperate(SQLiteDatabase db) {
		if (null == db) {
			throw new NullPointerException("The db cannot be null.");
		}
		mDB = db;
	}

	public long insertToDiary(Diary diary) {
		ContentValues values = new ContentValues();
		values.put(DiarySQLiteOpenHelper.COL_TAG, diary.getTag());
		values.put(DiarySQLiteOpenHelper.COL_CONTENT, diary.getContent());
		values.put(DiarySQLiteOpenHelper.COL_ADDRESS, diary.getAddress());
		values.put(DiarySQLiteOpenHelper.COL_WEATHER, diary.getWhether());
		values.put(DiarySQLiteOpenHelper.COL_WEATHERIMAGE, diary.getWhether_image());
		return mDB.insert(DiarySQLiteOpenHelper.DATABASE_TABLE_USER, null,
				values);
	}

//	public long updateUser(Diary user) {
//		ContentValues values = new ContentValues();
//		values.put(UserSQLiteOpenHelper.COL_NAME, user.getName());
//		values.put(UserSQLiteOpenHelper.COL_PWD, user.getPwd());
//		values.put(UserSQLiteOpenHelper.COL_TIME, user.getModifyTime());
//		return mDB.update(UserSQLiteOpenHelper.DATABASE_TABLE_USER, values,
//				"_id=?", new String[] { ""+user.get_id() });
//	}
//
//	// clear databases
//	public long deleteAll() {
//		return mDB.delete(UserSQLiteOpenHelper.DATABASE_TABLE_USER, null, null);
//	}
//
//	public long deleteUserByname(String name){
//
//		return mDB.delete("user_info", "name=?", new String[]{name});
//	}
//
//	public long getCount(String conditions, String[] args) {
//		long count = 0;
//		if (TextUtils.isEmpty(conditions)) {
//			conditions = " 1 = 1 ";
//		}
//		StringBuilder builder = new StringBuilder();
//		builder.append("SELECT COUNT(1) AS count FROM ");
//		builder.append(UserSQLiteOpenHelper.DATABASE_TABLE_USER).append(" ");
//		builder.append("WHERE ");
//		builder.append(conditions);
//		if (DEBUG)
//			Log.d(TAG, "SQL: " + builder.toString());
//		Cursor cursor = mDB.rawQuery(builder.toString(), args);
//		if (null != cursor) {
//			if (cursor.moveToNext()) {
//				count = cursor.getLong(cursor.getColumnIndex("count"));
//			}
//			cursor.close();
//		}
//		return count;
//	}
//	public List<UserBean> findAll() {
//		List<UserBean> userList = new ArrayList<UserBean>();
//		//order by modifytime desc
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, null, null, null, null, UserSQLiteOpenHelper.COL_TIME
//						+ " desc");
//		if (null != cursor) {
//			while (cursor.moveToNext()) {
//				UserBean user = new UserBean();
//				user.set_id(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_ID)));
//				user.setName(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_NAME)));
//				user.setPwd(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_PWD)));
//				user.setModifyTime(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_TIME)));
//				userList.add(user);
//			}
//			cursor.close();
//		}
//		return userList;
//	}
//	public UserBean findUserLatest() {
//		UserBean user = null;
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, null, null, null, null, UserSQLiteOpenHelper.COL_TIME
//						+ " desc");
//		if (null != cursor) {
//			if (cursor.moveToFirst()) {
//				user = new UserBean();
//				user.set_id(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_ID)));
//				user.setName(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_NAME)));
//				user.setPwd(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_PWD)));
//				user.setModifyTime(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_TIME)));
//			}
//			cursor.close();
//		}
//		return user;
//	}
//
//	public List<UserBean> findUserByName(String name) {
//
//		List<UserBean> userList = new ArrayList<UserBean>();
//		//模糊查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_NAME + " like?",
//				new String[] {"%"+name+"%"}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " desc");
//
////		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
////			null, UserSQLiteOpenHelper.COL_NAME + " =?",
////			new String[] {name}, null, null, UserSQLiteOpenHelper.COL_ID
////			+ " desc");
//
//		//多个条件查询
////		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
////				null, UserSQLiteOpenHelper.COL_NAME + " like?"+" and "+UserSQLiteOpenHelper.COL_ID+" >?",
////				new String[] {"%"+name+"%",2+""}, null, null, UserSQLiteOpenHelper.COL_ID
////				+ " desc");
//		if (null != cursor) {
//			while (cursor.moveToNext()) {
//				UserBean user = new UserBean();
//				user.set_id(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_ID)));
//				user.setName(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_NAME)));
//				user.setPwd(cursor.getString(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_PWD)));
//				user.setModifyTime(cursor.getLong(cursor
//						.getColumnIndex(UserSQLiteOpenHelper.COL_TIME)));
//				userList.add(user);
//			}
//			cursor.close();
//		}
//		return userList;
//	}





//	private static SQLiteDatabase mTestDb;
//	public static void InsertTest(){
//		//
//		mTestDb = SQLiteDatabase.openOrCreateDatabase("/data/data/cn.vn.sqlitedatademo/databases/my.db", null);
//		//
//		mTestDb.execSQL("CREATE TABLE IF NOT EXISTS user_info(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,pwd TEXT,modifyTime INTEGER)");
//		UserBean user = new UserBean();
//		user.setName("xiaopihai");
//		user.setPwd("12345678");
//		user.setModifyTime(System.currentTimeMillis());
//		//增加一条数据，因s为有5列，所以需要写5个数据，否则会失败,
//		//第一个数据位null，这是因为它是自动增长的。.
//		mTestDb.execSQL("INSERT INTO user_info VALUES(null,'xiaopihai','12345678',"+user.getModifyTime()+")");
//		//
//		//mTestDb.execSQL("INSERT INTO user_info VALUES(12,'zhangsan','mima1111',"+System.currentTimeMillis()+")");
//		//SQLiteConstraintException: PRIMARY KEY must be unique (code 19)
//		//下面是增加一条数据（只设置某个或某几个内容），默认没添加的数据时空的。
//		mTestDb.execSQL("INSERT INTO user_info(name,pwd) VALUES('lisi','mimajjjj')");
//		mTestDb.execSQL("INSERT INTO user_info VALUES(null,?,?,?)",new Object[]{user.getName(),user.getPwd(),user.getModifyTime()});
//		mTestDb.execSQL("INSERT INTO user_info(name,pwd) VALUES(?,?)",new Object[]{"lisi","mimajjjj"});
//
//		ContentValues values = new ContentValues();
//		values.put(UserSQLiteOpenHelper.COL_NAME,"qwerdf");
//		//values.put(UserSQLiteOpenHelper.COL_PWD, "qwerdflol");
//		values.put(UserSQLiteOpenHelper.COL_TIME, user.getModifyTime());
//		mTestDb.insert("user_info", null,values);
//	}
//
//public static void updateTest(){
//	mTestDb.execSQL("UPDATE user_info SET name = 'update1' WHERE _id = 1;");
//	mTestDb.execSQL("UPDATE user_info SET name =? WHERE _id=?",new Object[]{"update1",1});
//	mTestDb.execSQL("UPDATE user_info SET name = 'update2',modifyTime=111 WHERE _id = 2;");
//	mTestDb.execSQL("UPDATE user_info SET name =? ,modifyTime =? WHERE _id=?",new Object[]{"update1",111,2});
//
//	ContentValues values = new ContentValues();
//	values.put("name","update3");
//	values.put("pwd", "2222222");
//	values.put("modifyTime", 898);
//	mTestDb.update("user_info", values, "_id=?", new String[]{String.valueOf(3)});
//}
//
//public static void deleteTest(){
//	mTestDb.execSQL("delete from user_info where _id=1");
//	//mTestDb.execSQL("delete from user_info where _id=?",new Object[]{1});
//
//	int de = mTestDb.delete("user_info", "_id=?", new String[]{String.valueOf(2)});
//	int de8 = mTestDb.delete("user_info", "_id=?", new String[]{String.valueOf(8)});
//	int deall =mTestDb.delete("user_info", null, null);
//	Log.d(TAG, "de ="+de+" de8="+de8+" deall="+deall);
//}
}
