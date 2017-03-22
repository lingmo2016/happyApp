package com.xdl.ly.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �̳�FragmentActivity��ViewPager��
 * ��Activity�г��õķ���
 */
public class BaseActivity extends FragmentActivity {
	public static final  String MYTAG="myTag";
	private ProgressDialog progressDialog;  //�������Ի������
    public BaseActivity activity;
    
    @Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    	activity=this;
    }
	/**
	 * ��LogCat�����   ���
	 * @param str ���������
	 */
	public void logI(String str){
		Log.i(MYTAG, str);
	}
	/**
	 * ��LogCat�����   ���
	 * @param str ����Ĵ�������
	 */
	public void logE(String str){
		Log.i(MYTAG, str);
	}
	/**
	 * ��ʾ����   3s
	 * @param str ��ʾ������
	 */
	public void toast3s(String str){
		Toast.makeText(this, str, 0).show();
	}
	/**
	 * ��ʾ����   ��ʾʱ��5s
	 * @param str ��ʾ������
	 */
	public void toast5s(String str){
		Toast.makeText(this, str, 1).show();
	}
	/**
	 * ��ȡ�ؼ�����
	 * @param id ��ȡ�ؼ������ID
	 * @return �ؼ�����
	 */
	public View  viewById(int id){
		return  findViewById(id);
	}
	/**
	 * ��ȡָ���ؼ�������
	 * @param textView ���ȡ���ݵĿؼ�����
	 * @return �ؼ�����
	 */
	public String getViewContent(TextView textView){
		if (textView!=null) {
			String str=textView.getText().toString().trim();
			//�ǿ��ж�
			if (str==null) {
				str="";
				return str;
			}
			return str;
		}
		
		return "";
	}
	/**
	 * 
	 * �������Ի���
	 * @param title  �Ի���ı���
	 * @param message  �Ի��������
	 * @param icon   �Ի����ͼ��
	 * @param isCancel  �˶Ի����Ƿ��ȡ����true ����ȡ����
	 */
	public ProgressDialog showProgerssDialog(CharSequence title,CharSequence message,
			Drawable icon,boolean isCancel){
		progressDialog=new ProgressDialog(this);
		progressDialog.setCancelable(isCancel);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setIcon(icon);
		progressDialog.show();
		return progressDialog;
	}
	/**
	 * ��תҳ��
	 * @param actClass ��Ҫ��תҳ��
	 */
	public void JumpActivity(Class actClass){
		startActivity(new Intent(activity, actClass));
	}
}
