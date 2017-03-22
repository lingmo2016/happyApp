package com.xdl.ly.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSCodeReceiver extends BroadcastReceiver {

	/**
	 *短信接收者
	 *在清单文件中注册
	 *
	 *接收到短信的回调方法
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.i("myTag", "-----");
//		Toast.makeText(context, "短信来啦", 0).show();
		
		Bundle bundle= intent.getExtras(); //发送的信息存入Intent
		
		Object[] objects= (Object[]) bundle.get("pdus"); //短信数组
		for (int i = 0; i < objects.length; i++) {
//			Log.i("myTag", "1-----");
			byte[] bytes= (byte[]) objects[i];
			SmsMessage message=SmsMessage.createFromPdu(bytes);
			String data= message.getMessageBody(); //获取短信内容
			String code=data.substring(6, 12);//截取短信验证码
			Log.i("myTag", data+"-----"+code);
			Log.i("myTag", data);
			getCodeSetCode(code);
		}
//		Log.i("myTag", "2-----");
	}
	
	//声明观察者接口
	public interface ISMSCodeListener{
		void setCode(String code);
	}
	//全局维护接口对象
	public static ISMSCodeListener listener;
	//注册观察者
	public static void setISMSCodeListener(ISMSCodeListener listener){
		SMSCodeReceiver.listener=listener;
	}
	//获取并设置验证码
	public void getCodeSetCode(String code){
		if (listener!=null) { //有用户注册
			listener.setCode(code);
		}
	}

}
