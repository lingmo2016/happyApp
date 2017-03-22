package com.xdl.ly.adapter;

import java.util.List;

import com.xdl.ly.R;
import com.xdl.ly.base.BaseActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FraMyGridViewBaseAdapter extends BaseAdapter {
 
	//������
	private Context context;
	 //����Դ
	private  List<String> data;
	//���������
	private LayoutInflater inflater;
	//ͼƬ��Դ
	private int[]imgRes={R.drawable.my_youhui,R.drawable.my_shoucang,
			R.drawable.my_send,R.drawable.my_order	,
			R.drawable.my_gather,R.drawable.my_shezhi
	};
	//�������  ��������  ����һ��������   ��������������Դ
	public FraMyGridViewBaseAdapter(BaseActivity activity, List<String> data) {
		this.context=activity;
		this.data=data;
		inflater=LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
   /**
    * ��ȡGridView����item
    */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.fragment_my_item, null);
		}
		ImageView iv=(ImageView) convertView.findViewById(R.id.fragment_my_item_iv);
        TextView tv=(TextView) convertView.findViewById(R.id.fragment_my_item_tv);
        //������Դ
        iv.setImageResource(imgRes[position]);
        tv.setText(data.get(position));
		return convertView;
	}

}
