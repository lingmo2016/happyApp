package com.xdl.ly.act;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;

import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Utils.FindActionInfoUtils;
import com.xdl.ly.Utils.FindActionInfoUtils.FindActionInfoListener;
import com.xdl.ly.adapter.MyPagerFagAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;
import com.xdl.ly.fragment.HomeFragment;
import com.xdl.ly.fragment.MessageFragment;
import com.xdl.ly.fragment.MoreFragment;
import com.xdl.ly.fragment.MineFragment;

public class HomeActivity extends BaseActivity implements BaseInterface,
		OnClickListener {

	private ViewPager mViewPager; // viewpager
	// 选项卡
	private LinearLayout[] linears = new LinearLayout[4];
	private ImageView[] imgs = new ImageView[4];
	private TextView[] tvs = new TextView[4];

	// 选项卡资源
	private int[] lineasId = { R.id.act_home_lin1, R.id.act_home_lin2,
			R.id.act_home_lin4, R.id.act_home_lin5 };
	private int[] imgsId = { R.id.act_home_lin1_iv, R.id.act_home_lin2_iv,
			R.id.act_home_lin4_iv, R.id.act_home_lin5_iv };
	private int[] tvsId = { R.id.act_home_lin1_tv, R.id.act_home_lin2_tv,
			R.id.act_home_lin4_tv, R.id.act_home_lin5_tv };
	// 图片资源
	private int[] imgOpen = { R.drawable.home_o, R.drawable.message_o,
			R.drawable.me_o, R.drawable.more_o };
	private int[] imgClose = { R.drawable.home, R.drawable.message,
			R.drawable.me, R.drawable.more };

	// fragment的集合
	private List<Fragment> fragments;
	// 发布活动的按钮
	private ImageView mPublishView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_home);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// 初始化选项卡的控件
		for (int i = 0; i < 4; i++) {
			linears[i] = (LinearLayout) viewById(lineasId[i]);
			imgs[i] = (ImageView) viewById(imgsId[i]);
			tvs[i] = (TextView) viewById(tvsId[i]);
			linears[i].setOnClickListener(this);// 设置每个按钮的监听事件
		}
		// ViewPager控件初始化
		mViewPager = (ViewPager) viewById(R.id.act_home_viewpager);
		// 发布活动的控件初始化
		mPublishView = (ImageView) viewById(R.id.act_home_lin3_iv);
	}

	@Override
	public void initDatas() {
		// fragment数据源初始化
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new MineFragment());
		fragments.add(new MoreFragment());

		// viewPager的适配器
		mViewPager.setAdapter(new MyPagerFagAdapter(
				getSupportFragmentManager(), fragments));

	}

	@Override
	public void initOperas() {
		// ViewPager的监听事件
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// 当选择时 滑动改变
			@Override
			public void onPageSelected(int arg0) {
				changeTab(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// 发布活动的点击事件
		mPublishView.setOnClickListener(this);
	}

	/**
	 * 选项卡的事件回调
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_lin1: // 首页
			changeTab(0);
			mViewPager.setCurrentItem(0);
			break;

		case R.id.act_home_lin2: // 信息
			changeTab(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.act_home_lin4:// 我的
			changeTab(2);
			mViewPager.setCurrentItem(2);
			break;
		case R.id.act_home_lin5:// 更多
			changeTab(3);
			mViewPager.setCurrentItem(3);
			break;
		case R.id.act_home_lin3_iv: // 发布活动
			JumpActivity(PublishActivity.class);
			break;
		}

	}

	/**
	 * 改变选项卡的状态
	 * 
	 * @param i
	 *            选项卡的id
	 */
	private void changeTab(int i) {
		for (int j = 0; j < 4; j++) {
			if (j == i) { // 选中的图片、文字颜色改变
				tvs[i].setTextColor(Color.parseColor("#1296db"));
				imgs[i].setImageResource(imgOpen[i]);
			} else {// 未选中文字颜色 和图片
				tvs[j].setTextColor(Color.parseColor("#bfbfbf"));
				imgs[j].setImageResource(imgClose[j]);
			}
		}

	}

	/**
	 * 点击两次退出
	 */
	private Boolean flag = false;

	@Override
	public void onBackPressed() {

		if (flag) {
			super.onBackPressed();
		} else {
			toast3s("双击退出");
			flag = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}).start();
		}

	}

}
