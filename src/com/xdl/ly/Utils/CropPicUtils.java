package com.xdl.ly.Utils;

import com.xdl.ly.base.BaseActivity;

import android.content.Intent;
import android.net.Uri;

public class CropPicUtils {
	/**
	 * ��ת��� �� ����ͼƬ�ϴ��ķ���
	 */
	public  static  void getPic(BaseActivity context,Uri path) {
		// ��ת���
		Intent intent = new Intent(Intent.ACTION_PICK);
		// ������ת������
		intent.setType("image/*");
		// ����
		intent.putExtra("crop", "circleCrop"); 
		// ���ñ���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// ���õ����ص�
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		// ��ŵ�λ��
		intent.putExtra("output", path);
		// ��ת���
		context.startActivityForResult(intent, 1);
	}
}
