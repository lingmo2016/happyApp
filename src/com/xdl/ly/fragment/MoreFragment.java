package com.xdl.ly.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;

import com.baidu.location.BDLocation;
import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Utils.FindActionInfoUtils;
import com.xdl.ly.Utils.FindActionInfoUtils.FindActionInfoListener;
import com.xdl.ly.act.showGridItemActivity;
import com.xdl.ly.adapter.MoreGridAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseFragment;
import com.xdl.ly.base.BaseInterface;

/**
 * Fragment�����Լ��Ĳ��� fragment��ҳ չʾ���Ϣ
 */
public class MoreFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	private EditText mEtSearch; // ������ؼ�
	private String mStrSearch;//�����ַ���
	private Button mBtnFree, mBtnHot, mBtnBackground; // ��� ���� ���� �ؼ�
	private GridView mGridView; // GridView�ؼ�
    private ImageView mIvSearch;//����ͼ��ؼ�
	// ����Դ ���� ͼƬ
	private String[] mTypeNames = { "�ܱ�", "�ٶ�", "DIY", "����", "����", "�ݳ�", "չ��",
			"ɳ��", "Ʒ��", "�ۻ�" };
	private int[] mTypeImgs = { R.drawable.more_zhoubian,
			R.drawable.more_shaoer, R.drawable.more_diy,
			R.drawable.more_jianshen, R.drawable.more_jishi,
			R.drawable.more_yanchu, R.drawable.more_zhanlan,
			R.drawable.more_shalong, R.drawable.more_pincha,
			R.drawable.more_juhui };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, null);
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
		// �༭���ı���
		mEtSearch = (EditText) viewById(R.id.fragment_more_et_chazhao);
		mIvSearch=(ImageView) viewById(R.id.fragment_more_iv_search);
		// ��� ���� ���� �ؼ��ĳ�ʼ��
		mBtnBackground = (Button) viewById(R.id.fragment_more_btn_zhoubian);
		mBtnFree = (Button) viewById(R.id.fragment_more_btn_free);
		mBtnHot = (Button) viewById(R.id.fragment_more_btn_remen);

		// ��ʼ���� GridView�ؼ�
		mGridView = (GridView) viewById(R.id.fragment_more_gridview);
		mGridView.setAdapter(new MoreGridAdapter(activity, mTypeNames,
				mTypeImgs));

	}

	@Override
	public void initDatas() {
		// ���� ���� ���
		mBtnBackground.setOnClickListener(this);
		mBtnFree.setOnClickListener(this);
		mBtnHot.setOnClickListener(this);
		mIvSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_more_btn_zhoubian:
			//��ȡ�û���λ��
		BDLocation bdLocation=	MyApplication.mCurrBdLocation;
		//��ȡ�û��ľ�γ��
		BmobGeoPoint location=new BmobGeoPoint(bdLocation.getLongitude(), bdLocation.getLatitude());
			//���Ҹ����Ļ 
			//���������û���ǰ��λ��
            FindActionInfoUtils.findAllActionInfos(7, location, 0, 0, new FindActionInfoListener() {
				
				@Override
				public void getActionInfo(List<ActionInfo> info, BmobException ex) {
					if (ex==null) {
						//�����ҵ������ݱ��浽ȫ��
						MyApplication.putPoiInfo("findActionInfos", info);
						MyApplication.putPoiInfo("findActionType", "����");
						//��ת��չʾҳ��
						JumpActivity(showGridItemActivity.class);
					}else {
						toast3s("����ʧ�ܣ�" + ex.getErrorCode() + ","
								+ ex.getLocalizedMessage());
					}
					
				}
			});
			break;

		case R.id.fragment_more_btn_free:
			//��ѻ
			FindActionInfoUtils.findAllActionInfos(5, "0", 0, 0, new FindActionInfoListener() {
				
				@Override
				public void getActionInfo(List<ActionInfo> info, BmobException ex) {
					if (ex == null) {
						// ����ͬһ���͵Ļ
						MyApplication.putPoiInfo("findActionInfos",
								info);
						// �������͵�����
						MyApplication.putPoiInfo("findActionType",
								"���");
						JumpActivity(showGridItemActivity.class);
					} else {
						toast3s("����ʧ�ܣ�" + ex.getErrorCode() + ","
								+ ex.getLocalizedMessage());
					}

					
				}
			});

			break;
		case R.id.fragment_more_btn_remen:
            FindActionInfoUtils.findAllActionInfos(8, null, 
            		0, 0, new FindActionInfoListener() {
						
						@Override
						public void getActionInfo(List<ActionInfo> info, BmobException ex) {
							if (ex==null) {
								//�����ݱ��浽ȫ��
								MyApplication.putPoiInfo("findActionInfos",info);
								MyApplication.putPoiInfo("findActionType", "����");
								JumpActivity(showGridItemActivity.class);
							} else {
								toast3s("����ʧ�ܣ�" + ex.getErrorCode() + ","
										+ ex.getLocalizedMessage());
							}
							
						}
					});
			break;
		case R.id.fragment_more_iv_search://ģ������
			//��ȡ�������е�����
			mStrSearch=mEtSearch.getText().toString().trim();
			//�ж�
			if (mStrSearch==null || mStrSearch.equals("")) {
				toast3s("������Ҫ�����Ĺؼ���");
				return;
			}
			FindActionInfoUtils.findAllActionInfos(6, mStrSearch, 0, 0,
					new FindActionInfoListener() {
						
						@Override
						public void getActionInfo(List<ActionInfo> info, BmobException ex) {
							if (ex==null) {
								// ���浽ȫ��
								MyApplication.putPoiInfo("findActionInfos", info);
								MyApplication.putPoiInfo("findActionType", mStrSearch);
								JumpActivity(showGridItemActivity.class);
							}else {
								toast3s("����ʧ�ܣ�" + ex.getErrorCode() + ","
										+ ex.getLocalizedMessage());
							}
							
						}
					});
			break;
		}
	}

	@Override
	public void initOperas() {
		// ���GridView��ÿ������Ŀ
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ȡ��ǰ����Ļ����
				final String actionClass = mTypeNames[position];
				FindActionInfoUtils.findAllActionInfos(4, actionClass, 0, 0,
						new FindActionInfoListener() {

							@Override
							public void getActionInfo(List<ActionInfo> info,
									BmobException ex) {
								if (ex == null) {
									// ����ͬһ���͵Ļ
									MyApplication.putPoiInfo("findActionInfos",
											info);
									// �������͵�����
									MyApplication.putPoiInfo("findActionType",
											actionClass);
									JumpActivity(showGridItemActivity.class);
								} else {
									toast3s("����ʧ�ܣ�" + ex.getErrorCode() + ","
											+ ex.getLocalizedMessage());
								}

							}
						});
			}
		});

	}

}
