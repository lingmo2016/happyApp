package com.xdl.ly.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * �ж������Ƿ����ӵĹ�����
 *
 */
public class NetConnectUtils {
    public static boolean isNetConnect(Context context){
    	//ConnectivityManager�������ӹ������
    	ConnectivityManager manager= (ConnectivityManager) context
    			.getSystemService(Context.CONNECTIVITY_SERVICE);
    	//������Ϣ����
    	NetworkInfo networkInfo= manager.getActiveNetworkInfo();
    	if (networkInfo==null) {
			return false;
		}
    	return networkInfo.isConnected();
    	
    }
}
