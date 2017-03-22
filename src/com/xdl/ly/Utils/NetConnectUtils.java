package com.xdl.ly.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络是否连接的工具类
 *
 */
public class NetConnectUtils {
    public static boolean isNetConnect(Context context){
    	//ConnectivityManager网络连接管理对象
    	ConnectivityManager manager= (ConnectivityManager) context
    			.getSystemService(Context.CONNECTIVITY_SERVICE);
    	//网络信息对象
    	NetworkInfo networkInfo= manager.getActiveNetworkInfo();
    	if (networkInfo==null) {
			return false;
		}
    	return networkInfo.isConnected();
    	
    }
}
