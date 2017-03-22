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
	 *���Ž�����
	 *���嵥�ļ���ע��
	 *
	 *���յ����ŵĻص�����
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.i("myTag", "-----");
//		Toast.makeText(context, "��������", 0).show();
		
		Bundle bundle= intent.getExtras(); //���͵���Ϣ����Intent
		
		Object[] objects= (Object[]) bundle.get("pdus"); //��������
		for (int i = 0; i < objects.length; i++) {
//			Log.i("myTag", "1-----");
			byte[] bytes= (byte[]) objects[i];
			SmsMessage message=SmsMessage.createFromPdu(bytes);
			String data= message.getMessageBody(); //��ȡ��������
			String code=data.substring(6, 12);//��ȡ������֤��
			Log.i("myTag", data+"-----"+code);
			Log.i("myTag", data);
			getCodeSetCode(code);
		}
//		Log.i("myTag", "2-----");
	}
	
	//�����۲��߽ӿ�
	public interface ISMSCodeListener{
		void setCode(String code);
	}
	//ȫ��ά���ӿڶ���
	public static ISMSCodeListener listener;
	//ע��۲���
	public static void setISMSCodeListener(ISMSCodeListener listener){
		SMSCodeReceiver.listener=listener;
	}
	//��ȡ��������֤��
	public void getCodeSetCode(String code){
		if (listener!=null) { //���û�ע��
			listener.setCode(code);
		}
	}

}
