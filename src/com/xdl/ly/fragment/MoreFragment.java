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
 * Fragment加载自己的布局 fragment首页 展示活动信息
 */
public class MoreFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	private EditText mEtSearch; // 搜索框控件
	private String mStrSearch;//搜索字符串
	private Button mBtnFree, mBtnHot, mBtnBackground; // 免费 热门 附近 控件
	private GridView mGridView; // GridView控件
    private ImageView mIvSearch;//搜索图标控件
	// 数据源 名字 图片
	private String[] mTypeNames = { "周边", "少儿", "DIY", "健身", "集市", "演出", "展览",
			"沙龙", "品茶", "聚会" };
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
		// 编辑的文本框
		mEtSearch = (EditText) viewById(R.id.fragment_more_et_chazhao);
		mIvSearch=(ImageView) viewById(R.id.fragment_more_iv_search);
		// 免费 热门 附近 控件的初始化
		mBtnBackground = (Button) viewById(R.id.fragment_more_btn_zhoubian);
		mBtnFree = (Button) viewById(R.id.fragment_more_btn_free);
		mBtnHot = (Button) viewById(R.id.fragment_more_btn_remen);

		// 初始化呢 GridView控件
		mGridView = (GridView) viewById(R.id.fragment_more_gridview);
		mGridView.setAdapter(new MoreGridAdapter(activity, mTypeNames,
				mTypeImgs));

	}

	@Override
	public void initDatas() {
		// 附近 热门 免费
		mBtnBackground.setOnClickListener(this);
		mBtnFree.setOnClickListener(this);
		mBtnHot.setOnClickListener(this);
		mIvSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_more_btn_zhoubian:
			//获取用户的位置
		BDLocation bdLocation=	MyApplication.mCurrBdLocation;
		//获取用户的经纬度
		BmobGeoPoint location=new BmobGeoPoint(bdLocation.getLongitude(), bdLocation.getLatitude());
			//查找附近的活动 
			//参数二：用户当前的位置
            FindActionInfoUtils.findAllActionInfos(7, location, 0, 0, new FindActionInfoListener() {
				
				@Override
				public void getActionInfo(List<ActionInfo> info, BmobException ex) {
					if (ex==null) {
						//将查找到的数据保存到全局
						MyApplication.putPoiInfo("findActionInfos", info);
						MyApplication.putPoiInfo("findActionType", "附近");
						//跳转到展示页面
						JumpActivity(showGridItemActivity.class);
					}else {
						toast3s("加载失败：" + ex.getErrorCode() + ","
								+ ex.getLocalizedMessage());
					}
					
				}
			});
			break;

		case R.id.fragment_more_btn_free:
			//免费活动
			FindActionInfoUtils.findAllActionInfos(5, "0", 0, 0, new FindActionInfoListener() {
				
				@Override
				public void getActionInfo(List<ActionInfo> info, BmobException ex) {
					if (ex == null) {
						// 保存同一类型的活动
						MyApplication.putPoiInfo("findActionInfos",
								info);
						// 保存活动类型的名称
						MyApplication.putPoiInfo("findActionType",
								"免费");
						JumpActivity(showGridItemActivity.class);
					} else {
						toast3s("加载失败：" + ex.getErrorCode() + ","
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
								//把数据保存到全局
								MyApplication.putPoiInfo("findActionInfos",info);
								MyApplication.putPoiInfo("findActionType", "热门");
								JumpActivity(showGridItemActivity.class);
							} else {
								toast3s("加载失败：" + ex.getErrorCode() + ","
										+ ex.getLocalizedMessage());
							}
							
						}
					});
			break;
		case R.id.fragment_more_iv_search://模糊搜索
			//获取搜索框中的内容
			mStrSearch=mEtSearch.getText().toString().trim();
			//判断
			if (mStrSearch==null || mStrSearch.equals("")) {
				toast3s("请输入要检索的关键字");
				return;
			}
			FindActionInfoUtils.findAllActionInfos(6, mStrSearch, 0, 0,
					new FindActionInfoListener() {
						
						@Override
						public void getActionInfo(List<ActionInfo> info, BmobException ex) {
							if (ex==null) {
								// 保存到全局
								MyApplication.putPoiInfo("findActionInfos", info);
								MyApplication.putPoiInfo("findActionType", mStrSearch);
								JumpActivity(showGridItemActivity.class);
							}else {
								toast3s("加载失败：" + ex.getErrorCode() + ","
										+ ex.getLocalizedMessage());
							}
							
						}
					});
			break;
		}
	}

	@Override
	public void initOperas() {
		// 点击GridView的每个子项目
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取当前点击的活动类型
				final String actionClass = mTypeNames[position];
				FindActionInfoUtils.findAllActionInfos(4, actionClass, 0, 0,
						new FindActionInfoListener() {

							@Override
							public void getActionInfo(List<ActionInfo> info,
									BmobException ex) {
								if (ex == null) {
									// 保存同一类型的活动
									MyApplication.putPoiInfo("findActionInfos",
											info);
									// 保存活动类型的名称
									MyApplication.putPoiInfo("findActionType",
											actionClass);
									JumpActivity(showGridItemActivity.class);
								} else {
									toast3s("加载失败：" + ex.getErrorCode() + ","
											+ ex.getLocalizedMessage());
								}

							}
						});
			}
		});

	}

}
