package com.xdl.ly.adapter;

import com.xdl.ly.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {
	//上下文
	private Context context;
	//数据源
	private String[] typeNames;
	private int[] typeImgs;
	//布局填充器
	private LayoutInflater inflater;
   //构造方法  传递数据
	public SpinnerAdapter(Context context, String[] typeNames, int[] typeImgs) {
		super();
		this.context = context;
		this.typeNames = typeNames;
		this.typeImgs = typeImgs;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return typeNames.length;
	}

	@Override
	public Object getItem(int position) {
		return typeNames[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		if(v==null){
			//布局填充器填充item
			v=inflater.inflate(R.layout.act_publish_spinner_adapter, null);
		}
		//设置数据
		ImageView iv=(ImageView) v.findViewById(R.id.act_publish_spinner_iv);
		TextView tv=(TextView) v.findViewById(R.id.act_publish_spinner_tv);
		
		iv.setImageResource(typeImgs[position]);
		tv.setText(typeNames[position]);
		return v;
	}

}
