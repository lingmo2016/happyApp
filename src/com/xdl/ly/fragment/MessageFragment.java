package com.xdl.ly.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdl.ly.R;
import com.xdl.ly.base.BaseFragment;
import com.xdl.ly.base.BaseInterface;
/**
 * Fragment�����Լ��Ĳ���
 * fragment��ҳ չʾ���Ϣ
 */
public class MessageFragment extends BaseFragment implements BaseInterface{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_message, null);
		
		return view;
	}
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	initViews();
		initDatas();
		initOperas();
    }
	@Override
	public void initViews() {
		
	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initOperas() {
		
	}
	
}
