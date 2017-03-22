package com.xdl.ly.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * ViewPager+Fragment  ������
 * Fragment����Դ   FragmentManager
 */
public class MyPagerFagAdapter extends FragmentPagerAdapter {
	//ά��һ��fragment����
	private List<Fragment> fragments;
    //���췽��  ���ڴ��ݲ���
	public MyPagerFagAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments=fragments;
	}
    //��ȡ������fragment
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}
    //��ȡfragment�ĸ���
	@Override
	public int getCount() {
		return fragments.size();
	}
     
}
