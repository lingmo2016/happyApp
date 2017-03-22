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
	//活动详情，收藏的人数，优惠券张数，活动时间，订阅的人数，联系人电话，评论内容
	private TextView mDetails,mSaveCount,mDiscount,mTime,mOrderNum,mPhoneNum,mTalk;
	
	//活动的图片
	private ImageView mActionPic;
	
	//返回，参与按键
	private ImageView mBack,mOk;
	
	//评论的内容
	private EditText mEdTalk;
	
	//发送评论的控件
	private Button mBtnSend;
	
	private List<ActionInfo> mActionInfos; //所有的信息
	
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
		//初始化Textview
		mDetails=(TextView) viewById(R.id.act_home_publish_tv_details);
		mSaveCount=(TextView) viewById(R.id.act_details_tv_count_num);
		mDiscount=(TextView) viewById(R.id.act_details_tv_youhui_num);
		mTime=(TextView) viewById(R.id.act_details_tv_time);
		mOrderNum=(TextView) viewById(R.id.act_details_tv_order_num);
		mPhoneNum=(TextView) viewById(R.id.act_details_tv_phonenum);
		mTalk=(TextView) viewById(R.id.act_details_tv_talk);
		
		//初始化ImageView
		mActionPic=(ImageView) viewById(R.id.act_home_publish_iv_actionpic);
	    mBack=(ImageView) viewById(R.id.act_details_back);
	    mOk=(ImageView) viewById(R.id.act_details_ok);
        //初始化EditText
	    mEdTalk=(EditText) viewById(R.id.act_details_et_talk);
	    
	    //初始化Button
	    mBtnSend=(Button) viewById(R.id.act_details_btn_send);
		
	}

	@Override
	public void initDatas() {
		//返回
        mBack.setOnClickListener(this);
        //参与活动
        mOk.setOnClickListener(this);
        //发送评论
        mBtnSend.setOnClickListener(this);
        
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initOperas() {

		mActionInfos=(List<ActionInfo>) MyApplication.getPoiInfo(false, "actionInfo");
      //获取当前点击事件
		int position=   (Integer) MyApplication.getPoiInfo(false, "position");
		  ActionInfo actionInfo=mActionInfos.get(position-1);
	    logI(actionInfo+"");
		
	    //设置信息属性
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
			//参与活动
			break;
		case R.id.act_details_btn_send:
			//发送评论
			break;
		}
		
	}

}
