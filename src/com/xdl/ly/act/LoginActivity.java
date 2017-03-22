package com.xdl.ly.act;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.Utils.FindActionInfoUtils;
import com.xdl.ly.Utils.FindActionInfoUtils.FindActionInfoListener;
import com.xdl.ly.Utils.FindUserInfoUtils;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;

public class LoginActivity extends BaseActivity implements BaseInterface {

	private TextView mReTv; // 点击注册的控件
	private EditText mEtName, mEtPwd; // 获取控件对象
	private String mStrName, mStrPwd; // 用户名 密码
	private SharedPreferences mPreferences; // 保存到内部存储控件
	private String userName; // 用户名
	private String pwd; // 用户的密码
	private ProgressDialog loginDialog; // 等待登录的对话框

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_login);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// 获取控件对象
		mReTv = (TextView) viewById(R.id.act_log_tvreg);
		mEtName = (EditText) viewById(R.id.act_login_username);
		mEtPwd = (EditText) viewById(R.id.act_login_pwd);
	}

	@Override
	public void initDatas() {
		// 获取存储对象
		mPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
		if (mPreferences != null) { // 获取默认的用户名和密码
			userName = mPreferences.getString("userName", "");
			pwd = mPreferences.getString("pwd", "");

		}
		mEtName.setText(userName);
		mEtPwd.setText(pwd);
	}

	@Override
	public void initOperas() {
		// 加载数据的对话框
//		final ProgressDialog dialog = showProgerssDialog(null, "正在加载数据，请稍后...",
//				null, false);
		// 加载服务器数据
		FindActionInfoUtils.findAllActionInfos(1, null, 0, 0,
				new FindActionInfoListener() {
					@Override
					public void getActionInfo(List<ActionInfo> info,
							BmobException ex) {
						if (ex == null) {

							for (ActionInfo actionInfo : info) {
								// toast3s("加载数据成功");
								//Log.i("myTag", info.toString());
							}
							// 缓存数据到全局
							MyApplication.putPoiInfo("actionInfo", info);
							// 加载成功后取消对话框
//							dialog.dismiss();
						} else {
							// 加载失败toast的内容
							toast3s("加载数据失败！" + ex.getErrorCode() + ","
									+ ex.getLocalizedMessage());
//							dialog.dismiss();
						}

					}
				});
	}

	/**
	 * 登录按钮
	 * 
	 * @param v
	 */
	public void onLoginClick(View v) {
		// 获取用户输入的名和密码
		mStrName = getViewContent(mEtName);
		mStrPwd = getViewContent(mEtPwd);

		System.out.println(mStrName + "  " + mStrPwd);
		loginDialog = showProgerssDialog(null, "登录中...", null, false);

		// 登录
		BmobUser.loginByAccount(mStrName, mStrPwd,
				new LogInListener<UserInfo>() {

					@Override
					public void done(UserInfo user, BmobException e) {
						if (user != null) {
							loginDialog.dismiss();
							toast3s("登录成功！");

							// 登录成功 将用户信息缓存
							MyApplication.currUserInfo = user;
							// 保存用户信息本地（针对本手机 内部存储 ）
							// 获取存储对象

							// 获取编辑器
							Editor editor = mPreferences.edit();
							// 存储数据
							editor.putString("userName", mStrName);
							editor.putString("pwd", mStrPwd);
							// 提交数据
							editor.commit();
							// 跳转主页
							JumpActivity(HomeActivity.class);
							finish(); // 将登录页面取消
						} else {
							loginDialog.dismiss();
							toast3s("手机号或密码错误,请检查！");
						}

					}
				});
	}

	/**
	 * 点击注册文字 跳转到注册页面
	 */
	public void onRegisterNowClick(View v) {
		// 颜色改变有问题
		// mReTv.setTextColor(Color.parseColor("#ff0000"));
		startActivity(new Intent(activity, RegisterActivity.class));
	}

	/**
	 * 退出应用按钮
	 * 
	 * @param v
	 */
	public void onLoginBackClick(View v) {
		// 弹出警告对话框
		AlertDialog.Builder builder = new Builder(activity);
		builder.setTitle("是否退出应用")
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setPositiveButton("退出", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						System.exit(0);
					}
				}).show();
	}

	/**
	 * 注册页面 回显手机号和密码 返回回调
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// 缓存用户的注册信息 以回显
		UserInfo currUserInfo = MyApplication.currUserInfo;
		if (currUserInfo != null) {
			mEtName.setText(currUserInfo.getUsername());
			mEtPwd.setText(currUserInfo.getPwd2());
		}
	}
}
