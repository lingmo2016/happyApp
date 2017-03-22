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
	// ѡ�
	private LinearLayout[] linears = new LinearLayout[4];
	private ImageView[] imgs = new ImageView[4];
	private TextView[] tvs = new TextView[4];

	// ѡ���Դ
	private int[] lineasId = { R.id.act_home_lin1, R.id.act_home_lin2,
			R.id.act_home_lin4, R.id.act_home_lin5 };
	private int[] imgsId = { R.id.act_home_lin1_iv, R.id.act_home_lin2_iv,
			R.id.act_home_lin4_iv, R.id.act_home_lin5_iv };
	private int[] tvsId = { R.id.act_home_lin1_tv, R.id.act_home_lin2_tv,
			R.id.act_home_lin4_tv, R.id.act_home_lin5_tv };
	// ͼƬ��Դ
	private int[] imgOpen = { R.drawable.home_o, R.drawable.message_o,
			R.drawable.me_o, R.drawable.more_o };
	private int[] imgClose = { R.drawable.home, R.drawable.message,
			R.drawable.me, R.drawable.more };

	// fragment�ļ���
	private List<Fragment> fragments;
	// ������İ�ť
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
		// ��ʼ��ѡ��Ŀؼ�
		for (int i = 0; i < 4; i++) {
			linears[i] = (LinearLayout) viewById(lineasId[i]);
			imgs[i] = (ImageView) viewById(imgsId[i]);
			tvs[i] = (TextView) viewById(tvsId[i]);
			linears[i].setOnClickListener(this);// ����ÿ����ť�ļ����¼�
		}
		// ViewPager�ؼ���ʼ��
		mViewPager = (ViewPager) viewById(R.id.act_home_viewpager);
		// ������Ŀؼ���ʼ��
		mPublishView = (ImageView) viewById(R.id.act_home_lin3_iv);
	}

	@Override
	public void initDatas() {
		// fragment����Դ��ʼ��
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new MineFragment());
		fragments.add(new MoreFragment());

		// viewPager��������
		mViewPager.setAdapter(new MyPagerFagAdapter(
				getSupportFragmentManager(), fragments));

	}

	@Override
	public void initOperas() {
		// ViewPager�ļ����¼�
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// ��ѡ��ʱ �����ı�
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

		// ������ĵ���¼�
		mPublishView.setOnClickListener(this);
	}

	/**
	 * ѡ����¼��ص�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_lin1: // ��ҳ
			changeTab(0);
			mViewPager.setCurrentItem(0);
			break;

		case R.id.act_home_lin2: // ��Ϣ
			changeTab(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.act_home_lin4:// �ҵ�
			changeTab(2);
			mViewPager.setCurrentItem(2);
			break;
		case R.id.act_home_lin5:// ����
			changeTab(3);
			mViewPager.setCurrentItem(3);
			break;
		case R.id.act_home_lin3_iv: // �����
			JumpActivity(PublishActivity.class);
			break;
		}

	}

	/**
	 * �ı�ѡ���״̬
	 * 
	 * @param i
	 *            ѡ���id
	 */
	private void changeTab(int i) {
		for (int j = 0; j < 4; j++) {
			if (j == i) { // ѡ�е�ͼƬ��������ɫ�ı�
				tvs[i].setTextColor(Color.parseColor("#1296db"));
				imgs[i].setImageResource(imgOpen[i]);
			} else {// δѡ��������ɫ ��ͼƬ
				tvs[j].setTextColor(Color.parseColor("#bfbfbf"));
				imgs[j].setImageResource(imgClose[j]);
			}
		}

	}

	/**
	 * ��������˳�
	 */
	private Boolean flag = false;

	@Override
	public void onBackPressed() {

		if (flag) {
			super.onBackPressed();
		} else {
			toast3s("˫���˳�");
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
