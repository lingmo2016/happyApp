package com.xdl.ly.act;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.adapter.FraHomeBaseAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;
/**
 * 展示GridView子项目的搜索结果
 * @author Administrator
 *
 */
public class showGridItemActivity extends BaseActivity implements BaseInterface {

	private TextView mTvTitle;  //标题的控件
	private ImageView mBack;  //返回键
	private ListView mListView; //检索结果的显示控件
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_more_gridview_item_search);
		initViews();
		initDatas();
		initOperas();
	}
	@Override
	public void initViews() {
		//初始化控件
          mBack=(ImageView) viewById(R.id.act_home_publish_back);
          mListView=(ListView) viewById(R.id.act_more_listview);
          mTvTitle=(TextView) viewById(R.id.act_more_gridview_item_tv);
          
	}

	@Override
	public void initDatas() {
         //从全局获取数据，展示到ListView上
		List<ActionInfo> actionInfos= (List<ActionInfo>) MyApplication.getPoiInfo(true, "findActionInfos");
		String actionClass=(String) MyApplication.getPoiInfo(true, "findActionType");
		//绑定适配器
		  mListView.setAdapter(new FraHomeBaseAdapter(activity, actionInfos));
		  mTvTitle.setText("检索到"+actionClass+"的结果");
	}

	@Override
	public void initOperas() {
		 //返回键
        mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//结束自己 回到原界面
				finish();
				
			}
		});
	}

}
