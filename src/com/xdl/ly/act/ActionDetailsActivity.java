package com.xdl.ly.act;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;

import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Utils.FindActionInfoUtils;
import com.xdl.ly.Utils.FindActionInfoUtils.FindActionInfoListener;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;

public class ActionDetailsActivity extends BaseActivity implements
		BaseInterface,OnClickListener {
	//����飬�ղص��������Ż�ȯ�������ʱ�䣬���ĵ���������ϵ�˵绰����������
	private TextView mDetails,mSaveCount,mDiscount,mTime,mOrderNum,mPhoneNum,mTalk;
	
	//���ͼƬ
	private ImageView mActionPic;
	
	//���أ����밴��
	private ImageView mBack,mOk;
	
	//���۵�����
	private EditText mEdTalk;
	
	//�������۵Ŀؼ�
	private Button mBtnSend;
	
	private List<ActionInfo> mActionInfos; //���е���Ϣ
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_details);
		
		initViews();
		initDatas();
		initOperas();
		
	}

	@Override
	public void initViews() {
		//��ʼ��Textview
		mDetails=(TextView) viewById(R.id.act_home_publish_tv_details);
		mSaveCount=(TextView) viewById(R.id.act_details_tv_count_num);
		mDiscount=(TextView) viewById(R.id.act_details_tv_youhui_num);
		mTime=(TextView) viewById(R.id.act_details_tv_time);
		mOrderNum=(TextView) viewById(R.id.act_details_tv_order_num);
		mPhoneNum=(TextView) viewById(R.id.act_details_tv_phonenum);
		mTalk=(TextView) viewById(R.id.act_details_tv_talk);
		
		//��ʼ��ImageView
		mActionPic=(ImageView) viewById(R.id.act_home_publish_iv_actionpic);
	    mBack=(ImageView) viewById(R.id.act_details_back);
	    mOk=(ImageView) viewById(R.id.act_details_ok);
        //��ʼ��EditText
	    mEdTalk=(EditText) viewById(R.id.act_details_et_talk);
	    
	    //��ʼ��Button
	    mBtnSend=(Button) viewById(R.id.act_details_btn_send);
		
	}

	@Override
	public void initDatas() {
		//����
        mBack.setOnClickListener(this);
        //����
        mOk.setOnClickListener(this);
        //��������
        mBtnSend.setOnClickListener(this);
        
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initOperas() {

		mActionInfos=(List<ActionInfo>) MyApplication.getPoiInfo(false, "actionInfo");
      //��ȡ��ǰ����¼�
		int position=   (Integer) MyApplication.getPoiInfo(false, "position");
		  ActionInfo actionInfo=mActionInfos.get(position-1);
	    logI(actionInfo+"");
		
	    //������Ϣ����
		mDetails.setText(actionInfo.getActionIntro());
		mSaveCount.setText(actionInfo.getSaveNum()+"");
		
		mTime.setText(actionInfo.getActionTime());
//		mPhoneNum.setText(actionInfo.getaction)
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_details_back:
			finish();
			break;
		case R.id.act_details_ok:
			//����
			break;
		case R.id.act_details_btn_send:
			//��������
			break;
		}
		
	}

}
