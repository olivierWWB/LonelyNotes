package com.homework.wtw.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.homework.wtw.model.Diary;
import com.homework.wtw.model.DiaryMessage;

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
		values.put(DiarySQLiteOpenHelper.COL_DAY, diary.getDay());
		values.put(DiarySQLiteOpenHelper.COL_DATE, diary.getDate());
		values.put(DiarySQLiteOpenHelper.COL_TIME, diary.getCreate_time());
		values.put(DiarySQLiteOpenHelper.COL_USERMESSAGE, diary.getUser_message());
		return mDB.insert(DiarySQLiteOpenHelper.DATABASE_TABLE_DIARY, null,
				values);
	}
	public long publishMessage(DiaryMessage diaryMessage) {
		ContentValues values = new ContentValues();
		values.put(DiarySQLiteOpenHelper.COL_MCONTENT, diaryMessage.getContent());
		values.put(DiarySQLiteOpenHelper.COL_MDATE, diaryMessage.getCreate_time());
		values.put(DiarySQLiteOpenHelper.COL_MDIARY, diaryMessage.getSource_diary_id());
		values.put(DiarySQLiteOpenHelper.COL_MDELETE, diaryMessage.getIs_active());
		return mDB.insert(DiarySQLiteOpenHelper.DATABASE_TABLE_MESSAGE, null,
				values);
	}

//	public long updateMessage(Diary user) {
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

	public Diary findDiaryById(int diaryId) {
		Diary diary = new Diary();

		Cursor cursor = mDB.query(DiarySQLiteOpenHelper.DATABASE_TABLE_DIARY,
				null, DiarySQLiteOpenHelper.COL_ID + " =?",
				new String[] {diaryId+""}, null, null, null);
		if (null != cursor) {
			while (cursor.moveToNext()) {
				diary.setDiary_id(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_ID)));
				diary.setAddress(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_ADDRESS)));
				diary.setDate(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_DATE)));
				diary.setDay(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_DAY)));
				diary.setWhether(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHER)));
				diary.setWhether_image(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHERIMAGE)));
				diary.setUser_message(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_USERMESSAGE)));
				diary.setTag(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_TAG)));
				diary.setContent(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_CONTENT)));
			}
			cursor.close();
		}
		return diary;
	}

	public List<Diary> findAll() {
		List<Diary> diaryList = new ArrayList<Diary>();
		//order by modifytime desc
		Cursor cursor = mDB.query(DiarySQLiteOpenHelper.DATABASE_TABLE_DIARY,
				null, null, null, null, null, DiarySQLiteOpenHelper.COL_TIME
						+ " desc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				Diary diary = new Diary();
				diary.setDiary_id(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_ID)));
				diary.setAddress(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_ADDRESS)));
				diary.setDate(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_DATE)));
				diary.setDay(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_DAY)));
				diary.setWhether(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHER)));
				diary.setWhether_image(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHERIMAGE)));
				diary.setUser_message(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_USERMESSAGE)));
				diary.setTag(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_TAG)));
				diary.setContent(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_CONTENT)));
				diaryList.add(diary);
			}
			cursor.close();
		}
		return diaryList;
	}

	public List<DiaryMessage> findMessageByDiaryId(int diaryId) {
		List<DiaryMessage> diaryMessageList = new ArrayList<>();

		Cursor cursor = mDB.query(DiarySQLiteOpenHelper.DATABASE_TABLE_MESSAGE,
			null, DiarySQLiteOpenHelper.COL_MDIARY + " =?",
			new String[] {diaryId+""}, null, null, null);
		if (null != cursor) {
			while (cursor.moveToNext()) {
				DiaryMessage diaryMessage = new DiaryMessage();
				diaryMessage.setDiary_message_id(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_MID)));
				diaryMessage.setContent(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_MCONTENT)));
				diaryMessage.setCreate_time(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_MDATE)));
				diaryMessage.setSource_diary_id(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_MDIARY)));
				diaryMessage.setIs_active(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_MDELETE)));
				diaryMessageList.add(diaryMessage);
			}
			cursor.close();
		}
		return diaryMessageList;
	}

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
}
