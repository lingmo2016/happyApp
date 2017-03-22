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
 * 继承FragmentActivity（ViewPager）
 * 简化Activity中常用的方法
 */
public class BaseActivity extends FragmentActivity {
	public static final  String MYTAG="myTag";
	private ProgressDialog progressDialog;  //进度条对话框对象
    public BaseActivity activity;
    
    @Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    	activity=this;
    }
	/**
	 * 在LogCat上输出   检测
	 * @param str 输出的内容
	 */
	public void logI(String str){
		Log.i(MYTAG, str);
	}
	/**
	 * 在LogCat上输出   检测
	 * @param str 输出的错误内容
	 */
	public void logE(String str){
		Log.i(MYTAG, str);
	}
	/**
	 * 提示内容   3s
	 * @param str 提示的内容
	 */
	public void toast3s(String str){
		Toast.makeText(this, str, 0).show();
	}
	/**
	 * 提示内容   显示时间5s
	 * @param str 提示的内容
	 */
	public void toast5s(String str){
		Toast.makeText(this, str, 1).show();
	}
	/**
	 * 获取控件对象
	 * @param id 获取控件对象的ID
	 * @return 控件对象
	 */
	public View  viewById(int id){
		return  findViewById(id);
	}
	/**
	 * 获取指定控件的内容
	 * @param textView 需获取内容的控件对象
	 * @return 控件内容
	 */
	public String getViewContent(TextView textView){
		if (textView!=null) {
			String str=textView.getText().toString().trim();
			//非空判断
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
	 * 进度条对话框
	 * @param title  对话框的标题
	 * @param message  对话框的内容
	 * @param icon   对话框的图标
	 * @param isCancel  此对话框是否可取消（true 可以取消）
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
	 * 跳转页面
	 * @param actClass 需要跳转页面
	 */
	public void JumpActivity(Class actClass){
		startActivity(new Intent(activity, actClass));
	}
}
