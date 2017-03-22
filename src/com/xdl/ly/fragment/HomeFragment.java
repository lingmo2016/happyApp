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
 * Fragment加载自己的布局 fragment首页 展示活动信息
 */
public class HomeFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	private View view;
	// 返回 查找 更多的控件
	private ImageView mIvBack, mIvSreach, mIvMore;
	private EditText mEtSreach;
	// ListView 控件
	private XListView mListView;
	// 数据源
	private List<ActionInfo> mActionInfos;
	private FraHomeBaseAdapter adapter;// XlistView的适配器
	
	private ActionInfo actionInfo;//用户信息

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
		// 初始化
		mIvBack = (ImageView) viewById(R.id.fragment_home_iv_fanhui);
		mIvSreach = (ImageView) viewById(R.id.fragment_home_iv_sousuo);
		mIvMore = (ImageView) viewById(R.id.fragment_home_iv_gengduo);
		mEtSreach = (EditText) viewById(R.id.fragment_home_et_chazhao);
		mListView = (XListView) viewById(R.id.fragment_home_listview);
	}

	@Override
	public void initDatas() {
		// 数据源 适配器
		// 从全局取数据 不能清除缓存
		mActionInfos = (List<ActionInfo>) MyApplication.getPoiInfo(false,
				"actionInfo");
		if (mActionInfos == null) {
			// 数据为空 重新创建
			mActionInfos = new ArrayList<ActionInfo>();
		}

		// 自定义XLisstView的适配器
		adapter = new FraHomeBaseAdapter(activity, mActionInfos);
		// 异步加载数据
		mListView.setAdapter(adapter);

	}

	@Override
	public void initOperas() {

		// 每一个活动的详情的点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//缓存当前活动的位置
				MyApplication.putPoiInfo("position", position);
				// 跳转活动详情页面
				JumpActivity(ActionDetailsActivity.class);

			}
		});

		// 设置标题栏的点击事件
		mIvBack.setOnClickListener(this);
		mIvMore.setOnClickListener(this);
		// 搜索 有两个点击事件
		mIvSreach.setOnClickListener(sreachDataListener);

		// 允许刷新
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);// 允许加载更多
		/**
		 * 下拉刷新 或加载更多
		 */
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// 从服务器获取最新数据
				FindActionInfoUtils.findAllActionInfos(1, null, 0, 0,
						new FindActionInfoListener() {

							@Override
							public void getActionInfo(List<ActionInfo> info,
									BmobException ex) {
								// 将最新数据缓存到本地
								MyApplication.putPoiInfo("actionInfo", info);
								// 更新适配器
								adapter.updateDate(info);
								// 刷新数据之后关闭
								mListView.stopRefresh();

							}
						});

			}

			// 查询更多
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
									// 更新XListView
									adapter.updateDate(mActionInfos);
									if (info.size() == 0) {
										toast3s("没有更多数据了！");
									}
								} else {
									Log.i("myTag", "加载失败" + ex.getErrorCode()
											+ "," + ex.getLocalizedMessage());
								}
								// 忽略n条 从服务器加载n条数据后关闭
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
			// 是否退出本应用的对话框
			exitDialog();
			break;
		case R.id.fragment_home_iv_gengduo:

			break;
		case R.id.fragment_home_et_chazhao:
			// 显示搜索框

			break;
		}
	}

	/**
	 * 第一次点搜索 弹出搜索框 隐藏返回键
	 */
	private OnClickListener sreachDataListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 搜索框中的内容为空
			mEtSreach.setText("");
			// 隐藏返回键 显示搜索框
			mIvBack.setVisibility(View.INVISIBLE);
			mEtSreach.setVisibility(View.VISIBLE);
			// 再点击搜索时开始搜索
			mIvSreach.setOnClickListener(sreachDataListener2);

		}
	};
	/**
	 * 再点击搜索时开始搜索 隐藏搜索 显示框返回键
	 */
	private OnClickListener sreachDataListener2 = new OnClickListener() {

		@Override
		public void onClick(View v) {

			inintTitleView();
			// 搜索
			toast3s("搜索中。。。");
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		inintTitleView();
	}

	/**
	 * 初始化标题栏
	 */
	public void inintTitleView() {
		mIvSreach.setOnClickListener(sreachDataListener);
		// 隐藏搜索 显示框返回键
		mIvBack.setVisibility(View.VISIBLE);
		mEtSreach.setVisibility(View.INVISIBLE);
	}

	/**
	 * 退出本应用的对话框
	 */
	private void exitDialog() {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setTitle("是否退出本应用")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0); // 正常退出本应用
					}
				}).show();
	}

}
