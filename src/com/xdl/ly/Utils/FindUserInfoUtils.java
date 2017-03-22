package com.xdl.ly.Utils;

import com.xdl.ly.Bean.UserInfo;

import android.provider.MediaStore.Video;
import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;



/**
 * 
 *�ӷ�������ѯ�û���Ϣ�Ĺ�����
 */
public class FindUserInfoUtils {
    //�۲������ģʽ
	public static void findUserInfo(String userObjectId,
			final FindUserInfoListener listener){
//		��ѯ��������
//		������֪��ĳ�����ݵ�objectIdʱ���Ϳ��Ը���objectIdֱ�ӻ�ȡ�������ݶ���
		BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
		query.getObject(userObjectId, new QueryListener<UserInfo>() {

			@Override
			public void done(UserInfo object, BmobException e) {
				listener.getUserInfo(object,e);
			}

		});
	}
	//�ص��Ľӿ�
	public interface FindUserInfoListener{
		void getUserInfo(UserInfo info,BmobException exception);
	}
}
