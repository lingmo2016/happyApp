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
 * ��Fragment�еĳ��÷���
 *
 */
public class BaseFragment extends Fragment {

	public BaseActivity activity;  //��ǰ���
	private ProgressDialog progressDialog; //���ȶԻ���
	
	@Override
	public void onAttach(Activity act) {
		super.onAttach(activity);
		activity=(BaseActivity) act; //��ǰ�
	}
	/**
	 * 3s  ��˾������
	 * @param str ��˾������
	 */
	public void toast3s(String str){
		Toast.makeText(getActivity(), str, 0).show();
	}
	/**
	 * ��ʼ���ؼ�
	 * @param id �ؼ������id
	 * @return �ؼ�����
	 */
	public View viewById(int id){
		return getActivity().findViewById(id);
	}
	/**
	 * ��תҳ��
	 * @param actClass ��Ҫ��תҳ��
	 */
	public void JumpActivity(Class actClass){
		startActivity(new Intent(activity, actClass));
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
		progressDialog=new ProgressDialog(activity);
		progressDialog.setCancelable(isCancel);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setIcon(icon);
		progressDialog.show();
		return progressDialog;
	}
}
