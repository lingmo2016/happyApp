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

	private TextView mReTv; // ���ע��Ŀؼ�
	private EditText mEtName, mEtPwd; // ��ȡ�ؼ�����
	private String mStrName, mStrPwd; // �û��� ����
	private SharedPreferences mPreferences; // ���浽�ڲ��洢�ؼ�
	private String userName; // �û���
	private String pwd; // �û�������
	private ProgressDialog loginDialog; // �ȴ���¼�ĶԻ���

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
		// ��ȡ�ؼ�����
		mReTv = (TextView) viewById(R.id.act_log_tvreg);
		mEtName = (EditText) viewById(R.id.act_login_username);
		mEtPwd = (EditText) viewById(R.id.act_login_pwd);
	}

	@Override
	public void initDatas() {
		// ��ȡ�洢����
		mPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
		if (mPreferences != null) { // ��ȡĬ�ϵ��û���������
			userName = mPreferences.getString("userName", "");
			pwd = mPreferences.getString("pwd", "");

		}
		mEtName.setText(userName);
		mEtPwd.setText(pwd);
	}

	@Override
	public void initOperas() {
		// �������ݵĶԻ���
//		final ProgressDialog dialog = showProgerssDialog(null, "���ڼ������ݣ����Ժ�...",
//				null, false);
		// ���ط���������
		FindActionInfoUtils.findAllActionInfos(1, null, 0, 0,
				new FindActionInfoListener() {
					@Override
					public void getActionInfo(List<ActionInfo> info,
							BmobException ex) {
						if (ex == null) {

							for (ActionInfo actionInfo : info) {
								// toast3s("�������ݳɹ�");
								//Log.i("myTag", info.toString());
							}
							// �������ݵ�ȫ��
							MyApplication.putPoiInfo("actionInfo", info);
							// ���سɹ���ȡ���Ի���
//							dialog.dismiss();
						} else {
							// ����ʧ��toast������
							toast3s("��������ʧ�ܣ�" + ex.getErrorCode() + ","
									+ ex.getLocalizedMessage());
//							dialog.dismiss();
						}

					}
				});
	}

	/**
	 * ��¼��ť
	 * 
	 * @param v
	 */
	public void onLoginClick(View v) {
		// ��ȡ�û��������������
		mStrName = getViewContent(mEtName);
		mStrPwd = getViewContent(mEtPwd);

		System.out.println(mStrName + "  " + mStrPwd);
		loginDialog = showProgerssDialog(null, "��¼��...", null, false);

		// ��¼
		BmobUser.loginByAccount(mStrName, mStrPwd,
				new LogInListener<UserInfo>() {

					@Override
					public void done(UserInfo user, BmobException e) {
						if (user != null) {
							loginDialog.dismiss();
							toast3s("��¼�ɹ���");

							// ��¼�ɹ� ���û���Ϣ����
							MyApplication.currUserInfo = user;
							// �����û���Ϣ���أ���Ա��ֻ� �ڲ��洢 ��
							// ��ȡ�洢����

							// ��ȡ�༭��
							Editor editor = mPreferences.edit();
							// �洢����
							editor.putString("userName", mStrName);
							editor.putString("pwd", mStrPwd);
							// �ύ����
							editor.commit();
							// ��ת��ҳ
							JumpActivity(HomeActivity.class);
							finish(); // ����¼ҳ��ȡ��
						} else {
							loginDialog.dismiss();
							toast3s("�ֻ��Ż��������,���飡");
						}

					}
				});
	}

	/**
	 * ���ע������ ��ת��ע��ҳ��
	 */
	public void onRegisterNowClick(View v) {
		// ��ɫ�ı�������
		// mReTv.setTextColor(Color.parseColor("#ff0000"));
		startActivity(new Intent(activity, RegisterActivity.class));
	}

	/**
	 * �˳�Ӧ�ð�ť
	 * 
	 * @param v
	 */
	public void onLoginBackClick(View v) {
		// ��������Ի���
		AlertDialog.Builder builder = new Builder(activity);
		builder.setTitle("�Ƿ��˳�Ӧ��")
				.setNegativeButton("ȡ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setPositiveButton("�˳�", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						System.exit(0);
					}
				}).show();
	}

	/**
	 * ע��ҳ�� �����ֻ��ź����� ���ػص�
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// �����û���ע����Ϣ �Ի���
		UserInfo currUserInfo = MyApplication.currUserInfo;
		if (currUserInfo != null) {
			mEtName.setText(currUserInfo.getUsername());
			mEtPwd.setText(currUserInfo.getPwd2());
		}
	}
}
