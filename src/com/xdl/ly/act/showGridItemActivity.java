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
 * չʾGridView����Ŀ���������
 * @author Administrator
 *
 */
public class showGridItemActivity extends BaseActivity implements BaseInterface {

	private TextView mTvTitle;  //����Ŀؼ�
	private ImageView mBack;  //���ؼ�
	private ListView mListView; //�����������ʾ�ؼ�
	
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
		//��ʼ���ؼ�
          mBack=(ImageView) viewById(R.id.act_home_publish_back);
          mListView=(ListView) viewById(R.id.act_more_listview);
          mTvTitle=(TextView) viewById(R.id.act_more_gridview_item_tv);
          
	}

	@Override
	public void initDatas() {
         //��ȫ�ֻ�ȡ���ݣ�չʾ��ListView��
		List<ActionInfo> actionInfos= (List<ActionInfo>) MyApplication.getPoiInfo(true, "findActionInfos");
		String actionClass=(String) MyApplication.getPoiInfo(true, "findActionType");
		//��������
		  mListView.setAdapter(new FraHomeBaseAdapter(activity, actionInfos));
		  mTvTitle.setText("������"+actionClass+"�Ľ��");
	}

	@Override
	public void initOperas() {
		 //���ؼ�
        mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����Լ� �ص�ԭ����
				finish();
				
			}
		});
	}

}
