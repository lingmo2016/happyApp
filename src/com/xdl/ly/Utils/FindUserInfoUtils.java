package com.xdl.ly.Utils;

import com.xdl.ly.Bean.UserInfo;

import android.provider.MediaStore.Video;
import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;



/**
 * 
 *从服务器查询用户信息的工具类
 */
public class FindUserInfoUtils {
    //观察者设计模式
	public static void findUserInfo(String userObjectId,
			final FindUserInfoListener listener){
//		查询单条数据
//		当我们知道某条数据的objectId时，就可以根据objectId直接获取单条数据对象。
		BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
		query.getObject(userObjectId, new QueryListener<UserInfo>() {

			@Override
			public void done(UserInfo object, BmobException e) {
				listener.getUserInfo(object,e);
			}

		});
	}
	//回调的接口
	public interface FindUserInfoListener{
		void getUserInfo(UserInfo info,BmobException exception);
	}
}
