package com.xdl.ly.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * ViewPager+Fragment  适配器
 * Fragment数据源   FragmentManager
 */
public class MyPagerFagAdapter extends FragmentPagerAdapter {
	//维护一个fragment集合
	private List<Fragment> fragments;
    //构造方法  用于传递参数
	public MyPagerFagAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments=fragments;
	}
    //获取单个的fragment
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}
    //获取fragment的个数
	@Override
	public int getCount() {
		return fragments.size();
	}
     
}
