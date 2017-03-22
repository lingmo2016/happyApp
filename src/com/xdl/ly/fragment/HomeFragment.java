package com.xdl.ly.fragment;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import cn.bmob.v3.exception.BmobException;

import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Utils.FindActionInfoUtils;
import com.xdl.ly.Utils.FindActionInfoUtils.FindActionInfoListener;
import com.xdl.ly.act.ActionDetailsActivity;
import com.xdl.ly.adapter.FraHomeBaseAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseFragment;
import com.xdl.ly.base.BaseInterface;
import com.xdl.ly.view.XListView;
import com.xdl.ly.view.XListView.IXListViewListener;

/**
 * Fragment�����Լ��Ĳ��� fragment��ҳ չʾ���Ϣ
 */
public class HomeFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	private View view;
	// ���� ���� ����Ŀؼ�
	private ImageView mIvBack, mIvSreach, mIvMore;
	private EditText mEtSreach;
	// ListView �ؼ�
	private XListView mListView;
	// ����Դ
	private List<ActionInfo> mActionInfos;
	private FraHomeBaseAdapter adapter;// XlistView��������
	
	private ActionInfo actionInfo;//�û���Ϣ

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home, null);
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
		// ��ʼ��
		mIvBack = (ImageView) viewById(R.id.fragment_home_iv_fanhui);
		mIvSreach = (ImageView) viewById(R.id.fragment_home_iv_sousuo);
		mIvMore = (ImageView) viewById(R.id.fragment_home_iv_gengduo);
		mEtSreach = (EditText) viewById(R.id.fragment_home_et_chazhao);
		mListView = (XListView) viewById(R.id.fragment_home_listview);
	}

	@Override
	public void initDatas() {
		// ����Դ ������
		// ��ȫ��ȡ���� �����������
		mActionInfos = (List<ActionInfo>) MyApplication.getPoiInfo(false,
				"actionInfo");
		if (mActionInfos == null) {
			// ����Ϊ�� ���´���
			mActionInfos = new ArrayList<ActionInfo>();
		}

		// �Զ���XLisstView��������
		adapter = new FraHomeBaseAdapter(activity, mActionInfos);
		// �첽��������
		mListView.setAdapter(adapter);

	}

	@Override
	public void initOperas() {

		// ÿһ���������ĵ���¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//���浱ǰ���λ��
				MyApplication.putPoiInfo("position", position);
				// ��ת�����ҳ��
				JumpActivity(ActionDetailsActivity.class);

			}
		});

		// ���ñ������ĵ���¼�
		mIvBack.setOnClickListener(this);
		mIvMore.setOnClickListener(this);
		// ���� ����������¼�
		mIvSreach.setOnClickListener(sreachDataListener);

		// ����ˢ��
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);// ������ظ���
		/**
		 * ����ˢ�� ����ظ���
		 */
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// �ӷ�������ȡ��������
				FindActionInfoUtils.findAllActionInfos(1, null, 0, 0,
						new FindActionInfoListener() {

							@Override
							public void getActionInfo(List<ActionInfo> info,
									BmobException ex) {
								// ���������ݻ��浽����
								MyApplication.putPoiInfo("actionInfo", info);
								// ����������
								adapter.updateDate(info);
								// ˢ������֮��ر�
								mListView.stopRefresh();

							}
						});

			}

			// ��ѯ����
			@Override
			public void onLoadMore() {

				FindActionInfoUtils.findAllActionInfos(2, null,
						mActionInfos.size(), 5, new FindActionInfoListener() {

							@Override
							public void getActionInfo(List<ActionInfo> info,
									BmobException ex) {
								if (ex == null) {
									mActionInfos.addAll(info);
									MyApplication.putPoiInfo("actionInfos",
											mActionInfos);
									// ����XListView
									adapter.updateDate(mActionInfos);
									if (info.size() == 0) {
										toast3s("û�и��������ˣ�");
									}
								} else {
									Log.i("myTag", "����ʧ��" + ex.getErrorCode()
											+ "," + ex.getLocalizedMessage());
								}
								// ����n�� �ӷ���������n�����ݺ�ر�
								mListView.stopLoadMore();
							}
						});

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_home_iv_fanhui:
			// �Ƿ��˳���Ӧ�õĶԻ���
			exitDialog();
			break;
		case R.id.fragment_home_iv_gengduo:

			break;
		case R.id.fragment_home_et_chazhao:
			// ��ʾ������

			break;
		}
	}

	/**
	 * ��һ�ε����� ���������� ���ط��ؼ�
	 */
	private OnClickListener sreachDataListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// �������е�����Ϊ��
			mEtSreach.setText("");
			// ���ط��ؼ� ��ʾ������
			mIvBack.setVisibility(View.INVISIBLE);
			mEtSreach.setVisibility(View.VISIBLE);
			// �ٵ������ʱ��ʼ����
			mIvSreach.setOnClickListener(sreachDataListener2);

		}
	};
	/**
	 * �ٵ������ʱ��ʼ���� �������� ��ʾ�򷵻ؼ�
	 */
	private OnClickListener sreachDataListener2 = new OnClickListener() {

		@Override
		public void onClick(View v) {

			inintTitleView();
			// ����
			toast3s("�����С�����");
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		inintTitleView();
	}

	/**
	 * ��ʼ��������
	 */
	public void inintTitleView() {
		mIvSreach.setOnClickListener(sreachDataListener);
		// �������� ��ʾ�򷵻ؼ�
		mIvBack.setVisibility(View.VISIBLE);
		mEtSreach.setVisibility(View.INVISIBLE);
	}

	/**
	 * �˳���Ӧ�õĶԻ���
	 */
	private void exitDialog() {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setTitle("�Ƿ��˳���Ӧ��")
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("�˳�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0); // �����˳���Ӧ��
					}
				}).show();
	}

}
