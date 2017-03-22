package com.xdl.ly.act;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import com.xdl.ly.R;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;
import com.xdl.ly.receiver.SMSCodeReceiver;

//import com.xdl.ly.receiver.SMSCodeReceiver.ISMSCodeListener;

/**
 * 注册页面
 * 
 */
public class RegisterActivity extends BaseActivity implements BaseInterface {

	private EditText mEtPhone, mEtSmsCod, mEtPwd, mEtPwd2, mEtNick; // 获取控件对象
	// 電話號碼 驗證碼 密码 昵称
	private String mStrPhoneNum, mStrSmsCode, mStrPwd, mStrPwd2, mStrNick;
	private Button mBtnCode; // 获取验证码按钮

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_register);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// 初始化控件对象
		mEtPhone = (EditText) viewById(R.id.act_reg_phone);
		mEtSmsCod = (EditText) viewById(R.id.act_reg_code);

		mEtPwd = (EditText) viewById(R.id.act_reg_pwd);
		mEtPwd2 = (EditText) viewById(R.id.act_reg_pwd2);
		mEtNick = (EditText) viewById(R.id.act_reg_nick);
		mBtnCode = (Button) viewById(R.id.act_reg_getcode);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOperas() {
		// 注册成为观察者
		SMSCodeReceiver
				.setISMSCodeListener(new SMSCodeReceiver.ISMSCodeListener() {
					/**
					 * 获取到验证码时回调 将验证码放到文本框中
					 */
					@Override
					public void setCode(String code) {
						if (code != null) {
							mEtSmsCod.setText(code);// 验证码回显
							// mStrSmsCode=code;
						}

					}
				});

	}

	/**
	 * 获取验证码
	 * 
	 * @param v
	 */
	public void onGetCodeClick(View v) {
		// 獲取用户的手机号
		mStrPhoneNum = getViewContent(mEtPhone);

		// 判断输入的手机号是否合法
		if (!mStrPhoneNum
				.matches("^(17[0|4|7|8]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[1|2|3|5|6|7|8|9])\\d{8}$")) {
			Toast.makeText(this, "您输入的手机号码有误，请检查", 0).show();
			return;
		}
		// 判断是否是重复注册
//		if (mStrPhoneNum.equals(phoneNum)) {
//			toast3s("此用户已经注册！");
//			return;
//		}

		mBtnCode.setClickable(false);// 设置按钮不可被点击
		// 倒计时的类 异步 在子线程中进行计算然后通知主线程
		CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

			// 一直回调此方法
			@Override
			public void onTick(long millisUntilFinished) {
				mBtnCode.setText(millisUntilFinished / 1000 + "s");

			}

			@Override
			public void onFinish() {
				mBtnCode.setClickable(true); // 按钮是否可点击
				mBtnCode.setText("Get Code"); // 结束后设置文字

			}
		};
		countDownTimer.start();

		// 请求发送短信验证码
		BmobSMS.requestSMSCode(activity, mStrPhoneNum, "【乐游】",
				new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// 验证码发送成功
							Log.i("bmob", "短信id：" + smsId);// 用于查询本次短信发送详情
						} else {
							Log.i("bmob", "接收失败：" + ex.getLocalizedMessage());
						}

					}
				});
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void onRegBackClick(View v) {
		finish();
	}

	/**
	 * 点击注册
	 * 
	 * @param v
	 */
	public void onRegisterClick(View v) {
		// 获取用户的密码 昵称
		mStrPwd = getViewContent(mEtPwd);
		mStrPwd2 = getViewContent(mEtPwd2);
		mStrNick = getViewContent(mEtNick);
		mStrSmsCode = getViewContent(mEtSmsCod);
//	
//		BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
//		
//		query.getObject(mStrPhoneNum, new QueryListener<UserInfo>() {
//             
//			@Override
//			public void done(UserInfo user,
//					cn.bmob.v3.exception.BmobException ex) {
//				Log.i("myTag","++--------"+mStrPhoneNum);
//				if(ex==null){
//					toast3s("此用户已经注册！");
//					mBtnCode.setClickable(false);// 设置按钮不可被点击
//					return;
//		        }
//				
//			}
//		});

		// Log.i("myTag", mStrSmsCode+"-----------");

		// 判断用户输入
		if (mStrNick.equals("") || mStrNick == null || mStrPwd.equals("")
				|| mStrPwd == null || mStrPwd2.equals("") || mStrPwd2 == null) {
			toast3s("注册信息不能为空！");
			return;
		}

		// 确认密码是否正确
		if (!mStrPwd.equals(mStrPwd2)) {
			toast3s("两次密码不同,请重新输入！");
			return;
		}

		// 判断输入的密码是否是高强度
		// if (!mStrPwd.matches("^[a-zA-Z]\\w{5,17}$")) {
		// Toast.makeText(RegisterActivity.this, "您输入的密码应包含字母，请检查", 0).show();
		// return;
		// }

		// 等待注册的进度框
		final ProgressDialog registerDialog = showProgerssDialog(null,
				"正在注册，请稍后...", null, false);

		// 验证验证码
		BmobSMS.verifySmsCode(activity, mStrPhoneNum, mStrSmsCode,
				new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {// 短信验证码已验证成功
							Log.i("bmob", "验证通过");
							// 将用户信息保存到服务器
							final UserInfo userInfo = new UserInfo();
							userInfo.setUsername(mStrPhoneNum);// 将手机号作为用户名
							userInfo.setPassword(mStrPwd);
							userInfo.setPwd2(mStrPwd2);
							userInfo.setNick(mStrNick);
							userInfo.setMobilePhoneNumber(mStrPhoneNum);
							userInfo.setMobilePhoneNumberVerified(true);
							userInfo.signUp(new SaveListener<UserInfo>() {

								@Override
								public void done(UserInfo arg0,
										cn.bmob.v3.exception.BmobException ex) {
									if (ex == null) {
										// 注册成功 缓存当前用户信息
										MyApplication.currUserInfo = userInfo;
										// 结束注册页面
										finish();
										registerDialog.dismiss();
										toast3s("注册成功");
									} else {
										registerDialog.dismiss();
									}
								}

							});

						} else {
							Log.i("bmob", "验证失败：code =" + ex.getErrorCode()
									+ ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
	}

	/**
	 * 注册页面销毁时 释放短信的监听
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSCodeReceiver.setISMSCodeListener(null);
	}
}
