package com.homework.wtw.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.wtw.diary.R;
import com.homework.wtw.diary.SetLockPwdActivity;
import com.homework.wtw.util.KeyboardUtil;

import java.util.ArrayList;

/**
 * Created by ts on 2017/4/15.
 */

public class ChangePasswordActivity extends Activity{

    private View backView;
    private EditText etPwdOne, etPwdTwo, etPwdThree, etPwdFour, etPwdText;
    private KeyboardUtil kbUtil;
    public String strLockPwdOne;
    public String strLockPwdTwo;
    private Handler mHandler;
    private TextView inputPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findView();
        setListener();
        initData();
        intent = getIntent();
    }


    void findView() {
        inputPassword = (TextView) findViewById(R.id.input);
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
        kbUtil = new KeyboardUtil(ChangePasswordActivity.this);
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
                        	//Toast.makeText(getApplicationContext(), etPwdText.getText().toString(), Toast.LENGTH_SHORT).show();
					if (etPwdFour.getText() != null
							&& etPwdFour.getText().toString().length() >= 1) {
						if (strLockPwdOne != null
								&& strLockPwdOne.length() == 4) {
							String strReapt = etPwdText.getText().toString();
							if (strReapt.equals(strLockPwdOne)) {
								Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                editor.putString("password",strReapt);
                                editor.commit();
//                                Intent intent = new Intent(ChangePasswordActivity.this, DiaryListActivity.class);
//								startActivity(intent);
								strLockPwdOne = null;
                                setResult(RESULT_OK, intent);
                                finish();
							} else {
                                strLockPwdOne = null;
                                inputPassword.setText("两次输入密码不一致！");
								Toast.makeText(getApplicationContext(), "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                            }
						}
                        else {
                            inputPassword.setText("请再次输入密码");
							strLockPwdOne = etPwdText.getText().toString();
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

