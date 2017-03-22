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
 * 欢迎页面
 * 欢迎动画（加载数据   连网判断）
 */
public class WelcomeActivity extends BaseActivity implements BaseInterface {

	private ImageView mImageView;  //获取图片控件
	private AlphaAnimation mAlphaAnimation;//渐变动画对象
	private boolean isNetFlag=false;  //是否有网络
	private ProgressDialog progessDialog; //网络加载的进度对话框
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
		//获取动画对象
		mAlphaAnimation=(AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_wel);
		
	}

	@Override
	public void initOperas() {
		
		//设置动画监听
		mAlphaAnimation.setAnimationListener(new AnimationListener() {
			/**
			 * 动画开始回调
			 * 获取数据   
			 * 判断是否连接网络
			 */
			@Override
			public void onAnimationStart(Animation animation) {
				isNetFlag=NetConnectUtils.isNetConnect(WelcomeActivity.this);
			}
			/**
			 * 动画重复回调
			 */
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			/**
			 * 动画结束回调
			 * 判断是否有网络
			 * 跳转页面
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				if (isNetFlag) {//有网络
					toast3s("网络已连接，跳转页面！");
					//跳转到登录页面
					JumpActivity(LoginActivity.class);
					WelcomeActivity.this.finish();
				}else {//没有网络
				    //提示未连接网络
					toast3s("未连接网络！");
					//弹出警告对话框
					showNetConnectDialog();
					
				}
			}
		});
		//开启动画
		mImageView.startAnimation(mAlphaAnimation);
	}
    /**
     * 弹出警告对话框 提示连接网络
     */
	protected void showNetConnectDialog() {
		AlertDialog.Builder builder=new Builder(this);
		//设置警告对话框的属性
		builder.setTitle("建议连接网络").setCancelable(false  ).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				System.exit(0);//正常退出本应用
//				System.exit(1);//发生异常退出
	
				
			}
		}).setPositiveButton("设置", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 跳转到连接网络界面
				startActivity(new Intent(Settings.ACTION_SETTINGS));
				
			}
		}).show();
	}
	/**
	 * 利用生命周期回调 
	 * 判断是否已经连接网络
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		
        /**
         * 持续10秒获取网络
         */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//10秒
				for (int i = 0; i <10; i++) {
					if (i==3) {
						isNetFlag=NetConnectUtils.isNetConnect(activity);
						if (isNetFlag) {//4秒后有网络
							break;
						}
					}
					    //进度匡显示的信息
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
								progessDialog= showProgerssDialog("网络拼命加载中,请稍后 . . .",
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
						 * 持续10秒获取网络
						 * 之后判断是否获取到网络
						 */
						isNetFlag=NetConnectUtils.isNetConnect(activity);
						if (isNetFlag) {
							progessDialog.dismiss();
							toast3s("设置网络成功，跳转页面！");
							//跳转登录页面
							JumpActivity(LoginActivity.class);
							WelcomeActivity.this.finish();
						}else {
							progessDialog.dismiss();
							progessDialog=null;
							toast3s("网络没有设置成功，请重新设置！");
							showNetConnectDialog();
						}
						
					}
				});
				
			}
		}).start();
		
	}
	
}
