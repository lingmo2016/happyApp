package com.xdl.ly.act;

import com.xdl.ly.R;
import com.xdl.ly.Utils.NetConnectUtils;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
/**
 * ��ӭҳ��
 * ��ӭ��������������   �����жϣ�
 */
public class WelcomeActivity extends BaseActivity implements BaseInterface {

	private ImageView mImageView;  //��ȡͼƬ�ؼ�
	private AlphaAnimation mAlphaAnimation;//���䶯������
	private boolean isNetFlag=false;  //�Ƿ�������
	private ProgressDialog progessDialog; //������صĽ��ȶԻ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wel);
		
		initViews();
		initDatas();
		initOperas();
		
	}

	@Override
	public void initViews() {
		mImageView=(ImageView) viewById(R.id.act_wel_iv);
	}

	@Override
	public void initDatas() {
		//��ȡ��������
		mAlphaAnimation=(AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_wel);
		
	}

	@Override
	public void initOperas() {
		
		//���ö�������
		mAlphaAnimation.setAnimationListener(new AnimationListener() {
			/**
			 * ������ʼ�ص�
			 * ��ȡ����   
			 * �ж��Ƿ���������
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				isNetFlag=NetConnectUtils.isNetConnect(WelcomeActivity.this);
			}
			/**
			 * �����ظ��ص�
			 */
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			/**
			 * ���������ص�
			 * �ж��Ƿ�������
			 * ��תҳ��
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				if (isNetFlag) {//������
					toast3s("���������ӣ���תҳ�棡");
					//��ת����¼ҳ��
					JumpActivity(LoginActivity.class);
					WelcomeActivity.this.finish();
				}else {//û������
				    //��ʾδ��������
					toast3s("δ�������磡");
					//��������Ի���
					showNetConnectDialog();
					
				}
			}
		});
		//��������
		mImageView.startAnimation(mAlphaAnimation);
	}
    /**
     * ��������Ի��� ��ʾ��������
     */
	protected void showNetConnectDialog() {
		AlertDialog.Builder builder=new Builder(this);
		//���þ���Ի��������
		builder.setTitle("������������").setCancelable(false  ).setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				System.exit(0);//�����˳���Ӧ��
//				System.exit(1);//�����쳣�˳�
	
				
			}
		}).setPositiveButton("����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ת�������������
				startActivity(new Intent(Settings.ACTION_SETTINGS));
				
			}
		}).show();
	}
	/**
	 * �����������ڻص� 
	 * �ж��Ƿ��Ѿ���������
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		
        /**
         * ����10���ȡ����
         */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//10��
				for (int i = 0; i <10; i++) {
					if (i==3) {
						isNetFlag=NetConnectUtils.isNetConnect(activity);
						if (isNetFlag) {//4���������
							break;
						}
					}
					    //���ȿ���ʾ����Ϣ
						String message=".  .  .  .  .  .  .";
						switch (i%6) {
						case 0:
							message=".";
							break;
						case 1:
							message=".  .";
							break;
						case 2:
							message=".  .  .";
							break;
						case 3:
							message=".  .  .  .";
							break;
						case 4:
							message=".  .  .  .  .";
							break;
						case 5:
							message=".  .  .  .  .  .";
							break;
						
						}
					
					final String message2=message;
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if (progessDialog==null) {
								progessDialog= showProgerssDialog("����ƴ��������,���Ժ� . . .",
										message2, null, false);
							}else {
								progessDialog.setMessage(message2);
							}
						}
					});
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				runOnUiThread( new Runnable() {
					public void run() {
						/**
						 * ����10���ȡ����
						 * ֮���ж��Ƿ��ȡ������
						 */
						isNetFlag=NetConnectUtils.isNetConnect(activity);
						if (isNetFlag) {
							progessDialog.dismiss();
							toast3s("��������ɹ�����תҳ�棡");
							//��ת��¼ҳ��
							JumpActivity(LoginActivity.class);
							WelcomeActivity.this.finish();
						}else {
							progessDialog.dismiss();
							progessDialog=null;
							toast3s("����û�����óɹ������������ã�");
							showNetConnectDialog();
						}
						
					}
				});
				
			}
		}).start();
		
	}
	
}
