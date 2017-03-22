package com.xdl.ly.adapter;

import java.util.zip.Inflater;

import javax.security.auth.PrivateCredentialPermission;

import com.baidu.location.b.i;
import com.xdl.ly.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
/**
 * GridView控件的适配器
 * @author Administrator
 *
 */
public class MoreGridAdapter extends BaseAdapter {
   
	private Context context; //上下文
	private  String[] mTypeNames;// 数据源 名字 图片
	private int[] mTypeImgs;
	private LayoutInflater inflater;//布局填充器
	
	public MoreGridAdapter(Context context, String[] mTypeNames, int[] mTypeImgs) {
		super();
		this.context = context;
		this.mTypeNames = mTypeNames;
		this.mTypeImgs = mTypeImgs;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mTypeImgs.length;
	}

	@Override
	public Object getItem(int position) {
		return mTypeNames[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View c, ViewGroup parent) {
		if (c==null) {
			c=inflater.inflate(R.layout.fragment_more_gridview, null);
		}
			ImageView iv=(ImageView) c.findViewById(R.id.fragment_more_gridview_iv);
			TextView tv=(TextView) c.findViewById(R.id.fragment_more_gridview_tv);
			//初始化控件
			iv.setImageResource(mTypeImgs[position]);
			tv.setText(mTypeNames[position]);
			

		return c;
	}

}
