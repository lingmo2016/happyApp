package com.xdl.ly.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
/**
 * 简化Fragment中的常用方法
 *
 */
public class BaseFragment extends Fragment {

	public BaseActivity activity;  //當前活動
	private ProgressDialog progressDialog; //进度对话框
	
	@Override
	public void onAttach(Activity act) {
		super.onAttach(activity);
		activity=(BaseActivity) act; //当前活动
	}
	/**
	 * 3s  吐司的内容
	 * @param str 吐司的内容
	 */
	public void toast3s(String str){
		Toast.makeText(getActivity(), str, 0).show();
	}
	/**
	 * 初始化控件
	 * @param id 控件对象的id
	 * @return 控件对象
	 */
	public View viewById(int id){
		return getActivity().findViewById(id);
	}
	/**
	 * 跳转页面
	 * @param actClass 需要跳转页面
	 */
	public void JumpActivity(Class actClass){
		startActivity(new Intent(activity, actClass));
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
		progressDialog=new ProgressDialog(activity);
		progressDialog.setCancelable(isCancel);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setIcon(icon);
		progressDialog.show();
		return progressDialog;
	}
}
