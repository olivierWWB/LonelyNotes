package com.homework.wtw.diary;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.*;
import com.homework.wtw.activity.DiaryListActivity;
import com.homework.wtw.database.DiaryDataBaseOperate;
import com.homework.wtw.util.KeyboardUtil;

public class SetLockPwdActivity extends Activity {
	private View backView;
	private DiaryDataBaseOperate diaryDataBaseOperate;
	private EditText etPwdOne, etPwdTwo, etPwdThree, etPwdFour, etPwdText;
	private KeyboardUtil kbUtil;
	public String strLockPwdOne;
	public String strLockPwdTwo;
	private Handler mHandler;
	private TextView textView1;
	private String password;
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		diaryDataBaseOperate = new DiaryDataBaseOperate(DiaryApplication.diarySQLiteOpenHelper.getWritableDatabase());
		//password = diaryDataBaseOperate.findPassword();
		sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		password = sharedPreferences.getString("password","");
		Log.i("SetLockPwd","password="+password);
		if(password.equals("")){
			Intent intent = new Intent(SetLockPwdActivity.this, DiaryListActivity.class);
			startActivity(intent);
		}


		setContentView(R.layout.activity_main);
		findView();
		setListener();
		initData();
	}
	void findView() {
		textView1 = (TextView) findViewById(R.id.input);
		textView1.setText("请输入密码");
		etPwdOne = (EditText) findViewById(R.id.etPwdOne_setLockPwd);
		etPwdTwo = (EditText) findViewById(R.id.etPwdTwo_setLockPwd);
		etPwdThree = (EditText) findViewById(R.id.etPwdThree_setLockPwd);
		etPwdFour = (EditText) findViewById(R.id.etPwdFour_setLockPwd);
		etPwdText = (EditText) findViewById(R.id.etPwdText_setLockPwd);
	}

	void setListener() {
		etPwdText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (etPwdFour.getText() != null
						&& etPwdFour.getText().toString().length() >= 1) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(100);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								Message msg = mHandler.obtainMessage();
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}
					}).start();
				}
			}
		});
	}

	void initData() {
		kbUtil = new KeyboardUtil(SetLockPwdActivity.this);
		ArrayList<EditText> list = new ArrayList<EditText>();
		list.add(etPwdOne);
		list.add(etPwdTwo);
		list.add(etPwdThree);
		list.add(etPwdFour);
		list.add(etPwdText);
		kbUtil.setListEditText(list);
		etPwdOne.setInputType(InputType.TYPE_NULL);
		etPwdTwo.setInputType(InputType.TYPE_NULL);
		etPwdThree.setInputType(InputType.TYPE_NULL);
		etPwdFour.setInputType(InputType.TYPE_NULL);

		MyHandle();
	}

//	void backToActivity() {
//		Intent mIntent = new Intent(SetLockPwdActivity.this, MainActivity.class);
//		startActivity(mIntent);
//	}


	public void MyHandle() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
				//	Toast.makeText(getApplicationContext(), etPwdText.getText().toString(), Toast.LENGTH_SHORT).show();
//					if (etPwdFour.getText() != null
//							&& etPwdFour.getText().toString().length() >= 1) {
//						if (strLockPwdOne != null
//								&& strLockPwdOne.length() == 4) {
//							String strReapt = etPwdText.getText().toString();
//							if (strReapt.equals(strLockPwdOne)) {
//								Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
//								Intent intent = new Intent(SetLockPwdActivity.this, DiaryListActivity.class);
//								startActivity(intent);
//								strLockPwdOne = null;
//							} else {
//								Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
//							}
//						} else {
//							strLockPwdOne = etPwdText.getText().toString();
//						}
//						etPwdOne.setText("");
//						etPwdTwo.setText("");
//						etPwdThree.setText("");
//						etPwdFour.setText("");
//					}
					if (etPwdFour.getText() != null
							&& etPwdFour.getText().toString().length() >= 1) {

							String strReapt = etPwdText.getText().toString();

						//Toast.makeText(getApplicationContext(),password,Toast.LENGTH_SHORT).show();
							if (password.equals(strReapt)) {
								//Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(SetLockPwdActivity.this, DiaryListActivity.class);
								startActivity(intent);
								strLockPwdOne = null;
							} else {
								textView1.setText("密码错误！");
								Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
								strLockPwdOne = null;
							}


						etPwdOne.setText("");
						etPwdTwo.setText("");
						etPwdThree.setText("");
						etPwdFour.setText("");
					}
					break;
				default:
					break;
				}
			}
		};
	}

}