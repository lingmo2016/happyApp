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
 * ע��ҳ��
 * 
 */
public class RegisterActivity extends BaseActivity implements BaseInterface {

	private EditText mEtPhone, mEtSmsCod, mEtPwd, mEtPwd2, mEtNick; // ��ȡ�ؼ�����
	// �Ԓ̖�a ��C�a ���� �ǳ�
	private String mStrPhoneNum, mStrSmsCode, mStrPwd, mStrPwd2, mStrNick;
	private Button mBtnCode; // ��ȡ��֤�밴ť

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
		// ��ʼ���ؼ�����
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
		// ע���Ϊ�۲���
		SMSCodeReceiver
				.setISMSCodeListener(new SMSCodeReceiver.ISMSCodeListener() {
					/**
					 * ��ȡ����֤��ʱ�ص� ����֤��ŵ��ı�����
					 */
					@Override
					public void setCode(String code) {
						if (code != null) {
							mEtSmsCod.setText(code);// ��֤�����
							// mStrSmsCode=code;
						}

					}
				});

	}

	/**
	 * ��ȡ��֤��
	 * 
	 * @param v
	 */
	public void onGetCodeClick(View v) {
		// �@ȡ�û����ֻ���
		mStrPhoneNum = getViewContent(mEtPhone);

		// �ж�������ֻ����Ƿ�Ϸ�
		if (!mStrPhoneNum
				.matches("^(17[0|4|7|8]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[1|2|3|5|6|7|8|9])\\d{8}$")) {
			Toast.makeText(this, "��������ֻ�������������", 0).show();
			return;
		}
		// �ж��Ƿ����ظ�ע��
//		if (mStrPhoneNum.equals(phoneNum)) {
//			toast3s("���û��Ѿ�ע�ᣡ");
//			return;
//		}

		mBtnCode.setClickable(false);// ���ð�ť���ɱ����
		// ����ʱ���� �첽 �����߳��н��м���Ȼ��֪ͨ���߳�
		CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

			// һֱ�ص��˷���
			@Override
			public void onTick(long millisUntilFinished) {
				mBtnCode.setText(millisUntilFinished / 1000 + "s");

			}

			@Override
			public void onFinish() {
				mBtnCode.setClickable(true); // ��ť�Ƿ�ɵ��
				mBtnCode.setText("Get Code"); // ��������������

			}
		};
		countDownTimer.start();

		// �����Ͷ�����֤��
		BmobSMS.requestSMSCode(activity, mStrPhoneNum, "�����Ρ�",
				new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// ��֤�뷢�ͳɹ�
							Log.i("bmob", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
						} else {
							Log.i("bmob", "����ʧ�ܣ�" + ex.getLocalizedMessage());
						}

					}
				});
	}

	/**
	 * ����
	 * 
	 * @param v
	 */
	public void onRegBackClick(View v) {
		finish();
	}

	/**
	 * ���ע��
	 * 
	 * @param v
	 */
	public void onRegisterClick(View v) {
		// ��ȡ�û������� �ǳ�
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
//					toast3s("���û��Ѿ�ע�ᣡ");
//					mBtnCode.setClickable(false);// ���ð�ť���ɱ����
//					return;
//		        }
//				
//			}
//		});

		// Log.i("myTag", mStrSmsCode+"-----------");

		// �ж��û�����
		if (mStrNick.equals("") || mStrNick == null || mStrPwd.equals("")
				|| mStrPwd == null || mStrPwd2.equals("") || mStrPwd2 == null) {
			toast3s("ע����Ϣ����Ϊ�գ�");
			return;
		}

		// ȷ�������Ƿ���ȷ
		if (!mStrPwd.equals(mStrPwd2)) {
			toast3s("�������벻ͬ,���������룡");
			return;
		}

		// �ж�����������Ƿ��Ǹ�ǿ��
		// if (!mStrPwd.matches("^[a-zA-Z]\\w{5,17}$")) {
		// Toast.makeText(RegisterActivity.this, "�����������Ӧ������ĸ������", 0).show();
		// return;
		// }

		// �ȴ�ע��Ľ��ȿ�
		final ProgressDialog registerDialog = showProgerssDialog(null,
				"����ע�ᣬ���Ժ�...", null, false);

		// ��֤��֤��
		BmobSMS.verifySmsCode(activity, mStrPhoneNum, mStrSmsCode,
				new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {// ������֤������֤�ɹ�
							Log.i("bmob", "��֤ͨ��");
							// ���û���Ϣ���浽������
							final UserInfo userInfo = new UserInfo();
							userInfo.setUsername(mStrPhoneNum);// ���ֻ�����Ϊ�û���
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
										// ע��ɹ� ���浱ǰ�û���Ϣ
										MyApplication.currUserInfo = userInfo;
										// ����ע��ҳ��
										finish();
										registerDialog.dismiss();
										toast3s("ע��ɹ�");
									} else {
										registerDialog.dismiss();
									}
								}

							});

						} else {
							Log.i("bmob", "��֤ʧ�ܣ�code =" + ex.getErrorCode()
									+ ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
	}

	/**
	 * ע��ҳ������ʱ �ͷŶ��ŵļ���
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSCodeReceiver.setISMSCodeListener(null);
	}
}
