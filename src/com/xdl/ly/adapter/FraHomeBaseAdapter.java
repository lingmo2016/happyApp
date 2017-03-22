package com.xdl.ly.adapter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.Utils.CountDistenceUtils;
import com.xdl.ly.Utils.FindUserInfoUtils;
import com.xdl.ly.Utils.FindUserInfoUtils.FindUserInfoListener;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.view.MyImageView;

import android.R.integer;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FraHomeBaseAdapter extends BaseAdapter {

	private List<ActionInfo> infos; // 数据源
	private Context context; // 上下文
	private LayoutInflater inflater;// 布局填充器
	private HodlerView hv; // 控件存储器

	/**
	 * 初始化 上下文 数据源 布局填充器
	 * 
	 * @param activity
	 * @param mActionInfos
	 */
	public FraHomeBaseAdapter(BaseActivity activity,
			List<ActionInfo> mActionInfos) {
		//非空判断
		if (mActionInfos==null) {
			mActionInfos=new ArrayList<ActionInfo>();
		}
		this.infos = mActionInfos;
		this.context = activity;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		hv = null;
		// ListView的一级优化
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_home_item, null);

			hv = new HodlerView();
			// 初始化View中各个控件
			hv.tvNick = (TextView) convertView
					.findViewById(R.id.fragment_home_item_nick);
			hv.tvTime = (TextView) convertView
					.findViewById(R.id.fragment_home_item_time);
			hv.tvDetails = (TextView) convertView
					.findViewById(R.id.fragment_home_item_details);
			hv.tvRmb = (TextView) convertView
					.findViewById(R.id.fragment_home_item_money);
			hv.tvDistence = (TextView) convertView
					.findViewById(R.id.fragment_home_item_distence);
			// 图片控件
			hv.ivActionPic = (ImageView) convertView
					.findViewById(R.id.fragment_home_item_actionpic);
			hv.ivHead = (MyImageView) convertView
					.findViewById(R.id.fragment_home_item_head);
			hv.ivSave = (ImageView) convertView
					.findViewById(R.id.fragment_home_item_shoucang);
			// 添加标记
			convertView.setTag(hv);
		} else {
			// 获取标记
			hv = (HodlerView) convertView.getTag();
		}
		// 当前活动的信息
		ActionInfo actionInfo = infos.get(position);
		// Log.i("myTag",
		// actionInfo.getActionIntro()+"--------"+actionInfo.getActionDesc());

		hv.tvDetails.setText(actionInfo.getActionIntro()); // 活动详情
		hv.tvRmb.setText(actionInfo.getActionRMB());// 活动金额 
		hv.tvTime.setText(actionInfo.getActionTime());// 活动时间
		
		//设置当前的活动距离
		BDLocation userLocation=MyApplication.mCurrBdLocation;
		//活动位置
		BmobGeoPoint actionPoint=actionInfo.getLocation();
		//获取活动距离
		double distence= CountDistenceUtils.Distance(userLocation.getLongitude(),
				userLocation.getLatitude(), actionPoint.getLongitude(), 
				actionPoint.getLatitude());
//		if (userLocation==null || actionPoint==null  ) {
//			hv.tvDistence.setText("");
//			Toast.makeText(context, "定位失败！", 0).show();
//		}
//		显示到主页
//		if (distence>1000) {
			distence=distence/1000.0;
			//创建十进制对象
			BigDecimal b=new BigDecimal(distence);
			//保留一位小数
			distence=b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			hv.tvDistence.setText(distence+"km");
//		}else {
			//创建十进制对象
//			BigDecimal b=new BigDecimal(distence);
//			//保留一位小数
//			distence=b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//			hv.tvDistence.setText(distence+"m");
//		}
		

		// http://bmob-cdn-6787.b0.upaiyun.com/2016/11/16/98daa7a73ceb472ba6b64f890dc87037.jpg
		// http://bmob-cdn-6787.b0.upaiyun.com/2016/11/16/458dd96d70da4f23b39b9f9ae2136dd9.jpg
		// 获取图片的名称http://2136dd9.jpg
		String actionPicName = actionInfo
				.getActionJPG()
				.get(0)
				.getFileUrl()
				.substring(
						actionInfo.getActionJPG().get(0).getFileUrl().length() - 4 - 32,
						actionInfo.getActionJPG().get(0).getFileUrl().length() - 4);
		// 保存下载的图片
		File actionPicFile = new File("sdcard/ly.actionpic--" + actionPicName
				+ ".jpg");
		// 获取活动图片的名称
//		Log.i("myTag", "截取图片的名称:" + actionPicName);
		// 判断文件是否存在
		if (actionPicFile.exists()) {
			// 存在的话直接显示
			hv.ivActionPic.setImageBitmap(BitmapFactory
					.decodeFile(actionPicFile.getAbsolutePath()));
		} else {
			// 下载图片
			actionInfo.getActionJPG().get(0)
			/**
			 * 参数一  指定的下载路径
			 */
					.download(actionPicFile,new DownloadFileListener() {

						@Override
						public void onProgress(Integer arg0, long arg1) {
							// 显示进度
						}

						@Override
						public void done(String path, BmobException ex) {
							if (ex == null) {
								Log.i("myTag", "活动图片下载路径：" + path);
								hv.ivActionPic.setImageBitmap(BitmapFactory
										.decodeFile(path));
							}
						}
					});
		}

		/**
		 * 显示用户的头像、昵称
		 */
		FindUserInfoUtils.findUserInfo(actionInfo.getActionUserId(),
				new FindUserInfoListener() {

					@Override
					public void getUserInfo(UserInfo info, BmobException ex) {
						if (ex == null) {
							// 显示用户的昵称
							hv.tvNick.setText(info.getNick());
							//获取头像的名称   用头像的三级缓存(保存到硬盘)
							String userHeadPicName=info.getUserHead().getFileUrl().substring(
									info.getUserHead().getFileUrl().length()-4-32,
									info.getUserHead().getFileUrl().length()-4);
							//用户头像的保存路径
							File userHeadFile=new File("sdcard/ly.userheadpic--" + userHeadPicName
									+ ".jpg");
							Log.i("myTag", "截取头像图片的名称:" + userHeadPicName);
							
							if (userHeadFile.exists()) {
								//加载头像
								hv.ivHead.setImageBitmap(BitmapFactory
										.decodeFile(userHeadFile.getAbsolutePath()));
							}else {
								// 从服务器下载用户的头像
								info.getUserHead().download(userHeadFile,new DownloadFileListener() {

									@Override
									public void onProgress(Integer arg0, long arg1) {
									}

									@Override
									public void done(String path, BmobException ex) {
										if (ex == null) {
											Log.i("myTag", "头像图片下载路径---" + path);
											// 加载用户头像
											hv.ivHead.setImageBitmap(BitmapFactory
													.decodeFile(path));
										}

									}
								});
							}
							
						}
					}
				});
		
		//记录当前活动是否被点击过
		boolean isPraiseFlag=true;  //当前活动未被收藏
		//显示活动的收藏状态
		List<String> praiseActions=MyApplication.currUserInfo.getPraiseAction();
		
        //判断收藏活动是否包含当前活动
		if (praiseActions.contains(actionInfo.getObjectId())) {
			hv.ivSave.setImageResource(R.drawable.xin);
			isPraiseFlag=false;  //已收藏
		}else {
			hv.ivSave.setImageResource(R.drawable.xin_off);
			isPraiseFlag=true; //未收藏
		}
		
		//创建控件存储器
		final ActionpraiseViewHodler hodler=new ActionpraiseViewHodler();
		hodler.actionInfo=actionInfo;
		hodler.flag=isPraiseFlag;
		//收藏操作
		hv.ivSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//收藏
				praiseAction(hodler,hv.ivSave);
			}
		});
		return convertView;
	}
	/**
	 * 活动的收藏操作
	 * @param hodler
	 */
	private void praiseAction(final ActionpraiseViewHodler hodler,final ImageView ivSave) {
		if (hodler.flag) {//未收藏   做收藏操作
			//更新服务器中的活动表
			//更新数据  Bmob数据的原子性
			ActionInfo actionInfo=new ActionInfo();
			//将当前用户的id加入当前活动收藏属性中(活动表中的属性)
			actionInfo.add("praiseUsers", MyApplication.currUserInfo.getObjectId());
			
			//原子性的增加收藏数量
			actionInfo.increment("saveNum");
			
			//更新收藏活动,服务器中
			actionInfo.update(hodler.actionInfo.getObjectId(),
					new UpdateListener() {
			
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//设置图片更新本地数据
						ivSave.setImageResource(R.drawable.xin);
						hodler.actionInfo.getPraiseUsers()
						.add(MyApplication.currUserInfo.getObjectId());
					}
					
				}
			});
			
			//更新服务器中的用户表
			//创建用户对象
			UserInfo userInfo=new UserInfo();
			//在用户表中添加属性
			userInfo.add("praiseAction", hodler.actionInfo.getObjectId());
			//更新用户表
			userInfo.update(MyApplication.currUserInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//更新本地的数据
						MyApplication.currUserInfo.getPraiseAction()
						.add(hodler.actionInfo.getObjectId());
					}
				}
			});
			
		}else {//已收藏   做取消活动
			//创建消息对象
			ActionInfo actionInfo=new ActionInfo();
			//创建用户的id集合
			ArrayList<String> removeUserid=new ArrayList<String>();
			//添加当前用户的id
			removeUserid.add(MyApplication.currUserInfo.getObjectId());
			//添加用户移除的收藏属性
			actionInfo.removeAll("praiseUsers", removeUserid);
			//原子性的减少收藏数量
			actionInfo.increment("saveNum",-1);
			//更新用户表
			actionInfo.update(hodler.actionInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					//更新本地数据 移除当前用户
					hodler.actionInfo.getPraiseUsers() 
					.remove(MyApplication.currUserInfo.getObjectId());
					//更改图片
					ivSave.setImageResource(R.drawable.xin_off);
					
				}
			});
			
			//更新用户表
			UserInfo userInfo=new UserInfo();
			//创建当前用户收藏的活动
			ArrayList<String> removeActionId=new ArrayList<String>();
			//添加要移除的活动
			removeActionId.add(hodler.actionInfo.getObjectId());
			//移除添加的活动
			userInfo.removeAll("praiseAction", removeActionId);
			//更新数据
			userInfo.update(MyApplication.currUserInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//更新本地数据 移除取消点赞的活动
						MyApplication.currUserInfo.getPraiseAction()
						.remove(hodler.actionInfo.getObjectId());
					}else {
						Log.i("myTag", "失败："+ex.getErrorCode()+","+ex.getLocalizedMessage());
					} 
					
				}
			});
		}
		//改变标记
		hodler.flag=!hodler.flag;
		
	}
    class ActionpraiseViewHodler{
    	ActionInfo actionInfo;//当前活动对象
    	boolean flag;		//当前活动对象的收藏标记
    	
    }
	class HodlerView {
		// 昵称，发布时间，金额，详情，距离
		TextView tvNick, tvTime, tvRmb, tvDetails, tvDistence;
		// 头像，发布活动的图片，收藏
		ImageView ivActionPic, ivSave;
		MyImageView ivHead;
	}

	/**
	 * 向外暴露更新前的XListView的适配器的数据源
	 * 
	 * @param infos
	 *            数据源
	 */
	public void updateDate(List<ActionInfo> infos) {
		// 清除当前数据源
//		this.infos.clear();
//		// 监听数据改变并刷新
//		notifyDataSetChanged();
		// 覆盖原来的数据
		this.infos = infos;
		notifyDataSetChanged();
	}

}
