package com.xdl.ly.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xdl.ly.R;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.Utils.FindUserInfoUtils;
import com.xdl.ly.Utils.FindUserInfoUtils.FindUserInfoListener;
import com.xdl.ly.Utils.ImageLoaderUtils;
import com.xdl.ly.adapter.FraMyGridViewBaseAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseFragment;
import com.xdl.ly.base.BaseInterface;
import com.xdl.ly.view.MyImageView;

/**
 * Fragment加载自己的布局 fragment个人中心展示活动信息
 */
public class MineFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	// 控件昵称 账号
	private TextView mTvNick, mTvAcount;
	// 控件 头像
	private MyImageView mIvHead;
	// GridView 控件
	private GridView mGridView;
	private View view;
	// 数据源
	private List<String> data;
	// 用户头像的缓存流
	private File saveUserHead = new File("sdcard/userhead.png");
	// 上传至服务器的对话框
	private ProgressDialog upLoderDialog;
	// 加载缓存图片
	private ImageLoader mImageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载布局填充器
		view = inflater.inflate(R.layout.fragment_my, null);
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
		// 初始化控件对象
		mGridView = (GridView) viewById(R.id.fragment_my_gridview);
		mIvHead = (MyImageView) viewById(R.id.fragment_my_head);
		mTvAcount = (TextView) viewById(R.id.fragment_my_account);
		mTvNick = (TextView) viewById(R.id.fragment_my_nick);

	}

	@Override
	public void initDatas() {
		// 获取图片加载对象
		mImageLoader = ImageLoaderUtils.getInstance(activity);
		// 获取当前用户的信息
		// UserInfo currenUserInfo=MyApplication.currUserInfo;
		// 查询用户
		FindUserInfoUtils.findUserInfo(
				MyApplication.currUserInfo.getObjectId(),
				new FindUserInfoListener() {

					@Override
					public void getUserInfo(UserInfo info, BmobException ex) {
						// Log.i("myTag",info.getObjectId()+"&&&&&&&&");
						Log.i("myTag", MyApplication.currUserInfo.getUsername()
								+ "&&&&&&&&");
						Log.i("myTag", MyApplication.currUserInfo.getNick()
								+ "&&&&&&&&");
						// 更新用户信息
						if (info != null) {
							// 头像图片的数据流
							BmobFile userFile = info.getUserHead();
							// 获取图片的URL
							String userHeadurl = userFile.getFileUrl();

							DisplayImageOptions options = ImageLoaderUtils
									.getOpt(R.drawable.head,
											R.drawable.beishang);
							/**
							 * 参数一：图片URL 参数二：显示图片的控件 参数三：显示图片的配置信息
							 */
							// 显示图片
							mImageLoader.displayImage(userHeadurl, mIvHead,
									options);
							// 用户昵称
							mTvNick.setText(info.getNick());
							// 用户账号 即手机号
							mTvAcount.setText(info.getUsername());
							// Log.i("myTag",MyApplication.currUserInfo.getObjectId()+"------");

						}
					}
				});

		// 初始化数据源
		data = new ArrayList<String>();
		data.add("优惠");
		data.add("收藏");
		data.add("发起");
		data.add("订单");
		data.add("活动");
		data.add("设置");

		// 连接到适配器
		mGridView.setAdapter(new FraMyGridViewBaseAdapter(activity, data));

	}

	@Override
	public void initOperas() {
		// 点击头像更换
		// 设置用户更换头像的监听事件
		mIvHead.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_my_head:
			// 点击用户头像，更换
			Intent intent = new Intent(Intent.ACTION_PICK);
			// 设置照片类型
			intent.setType("image/*");
			// 剪裁
			intent.putExtra("crop", "circleCrop");
			// 剪裁比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("ascepctY", 1);
			// 剪裁的像素点
			intent.putExtra("outputX", 800);
			intent.putExtra("outputY", 800);
			// 存放的位置
			intent.putExtra("output", Uri.fromFile(saveUserHead));
			// 跳转到相册 (带返回值的跳转)
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}

	/**
	 * 头像获取的回调方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) { // 选中头像
			// 用户头像上传
			loadHead();
		}
	}

	/**
	 * 将选中的头像 上传服务 更新信息
	 */
	private void loadHead() {
		// 获取当前用户对象
		final UserInfo userInfo = new UserInfo();
		// 设置用户头像
		userInfo.setUserHead(new BmobFile(saveUserHead));
		// 正在上传的对话框
		upLoderDialog = showProgerssDialog(null, "正在上传，请稍后！", null, false);
		// 上传用户头像至服务器
		userInfo.getUserHead().upload(new UploadFileListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					// 更新头像
					userInfo.update(MyApplication.currUserInfo.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										upLoderDialog.dismiss();
										toast3s("头像上传成功！");
										// 上传结束后取消对话框
										
										Bitmap bm = BitmapFactory
												.decodeFile(saveUserHead
														.getAbsolutePath());
										mIvHead.setImageBitmap(bm);
										
									}
								}
							});

				}
			}
		});
	}

}
