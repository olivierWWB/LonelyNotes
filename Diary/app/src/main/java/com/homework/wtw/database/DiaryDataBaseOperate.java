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
		values.put(DiarySQLiteOpenHelper.COL_WEATHER, diary.getWeather());
		values.put(DiarySQLiteOpenHelper.COL_WEATHERIMAGE, diary.getWeather_image());
		values.put(DiarySQLiteOpenHelper.COL_DAY, diary.getDay());
		values.put(DiarySQLiteOpenHelper.COL_DATE, diary.getDate());
		values.put(DiarySQLiteOpenHelper.COL_TIME, diary.getCreate_time());
		values.put(DiarySQLiteOpenHelper.COL_USERMESSAGE, diary.getUser_message());
		return mDB.insert(DiarySQLiteOpenHelper.DATABASE_TABLE_DIARY, null,
				values);
	}

	public long insertToDiaryWithPicture(Diary diary) {
		ContentValues values = new ContentValues();
		values.put(DiarySQLiteOpenHelper.COL_TAG, diary.getTag());
		values.put(DiarySQLiteOpenHelper.COL_CONTENT, diary.getContent());
		values.put(DiarySQLiteOpenHelper.COL_ADDRESS, diary.getAddress());
		values.put(DiarySQLiteOpenHelper.COL_WEATHER, diary.getWeather());
		values.put(DiarySQLiteOpenHelper.COL_WEATHERIMAGE, diary.getWeather_image());
		values.put(DiarySQLiteOpenHelper.COL_DAY, diary.getDay());
		values.put(DiarySQLiteOpenHelper.COL_DATE, diary.getDate());
		values.put(DiarySQLiteOpenHelper.COL_TIME, diary.getCreate_time());
		values.put(DiarySQLiteOpenHelper.COL_USERMESSAGE, diary.getUser_message());
		values.put(DiarySQLiteOpenHelper.COL_PICTURE, diary.getPicture());
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

	public void updateMessageCount(int diaryId, int flag) {
		//flag=0为增加评论 flag=1为删除评论
		if(flag == 0) {
			mDB.execSQL("update diary set user_message=user_message+1 where diary_id="+diaryId);
		}else if(flag == 1) {
			mDB.execSQL("update diary set user_message=user_message-1 where diary_id="+diaryId);
		}else {

		}

	}

	public long deleteMessage(int messageId) {
		return mDB.delete("message", "diary_message_id=?", new String[]{messageId+""});
	}

	public long deleteDiary(int diaryId) {
		return mDB.delete("diary", "diary_id=?", new String[]{diaryId+""});
	}

	public long deleteMessageByDiary(int diaryId) {
		return mDB.delete("message", "source_diary_id=?", new String[]{diaryId+""});
	}


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
				diary.setWeather(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHER)));
				diary.setWeather_image(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHERIMAGE)));
				diary.setUser_message(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_USERMESSAGE)));
				diary.setTag(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_TAG)));
				diary.setContent(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_CONTENT)));
				diary.setPicture(cursor.getBlob(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_PICTURE)));
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
				diary.setWeather(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHER)));
				diary.setWeather_image(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_WEATHERIMAGE)));
				diary.setUser_message(cursor.getInt(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_USERMESSAGE)));
				diary.setTag(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_TAG)));
				diary.setContent(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_CONTENT)));
				diary.setPicture(cursor.getBlob(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_PICTURE)));
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

	public String findPassword(){

		List<String> passwordList=new ArrayList<>();
		Cursor cursor = mDB.query(DiarySQLiteOpenHelper.PASSWORD,
				null, null, null, null, null, null);
		if(cursor!=null){
			while (cursor.moveToNext()){
				passwordList.add(cursor.getString(cursor.getColumnIndex(DiarySQLiteOpenHelper.COL_PASSWORD)));
			}
		}
		if(passwordList.size()>0){
			return passwordList.get(0);
		}
		else return "";
	}
}
