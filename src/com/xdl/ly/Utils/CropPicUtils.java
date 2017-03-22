package com.xdl.ly.Utils;

import com.xdl.ly.base.BaseActivity;

import android.content.Intent;
import android.net.Uri;

public class CropPicUtils {
	/**
	 * 跳转相册 并 剪裁图片上传的方法
	 */
	public  static  void getPic(BaseActivity context,Uri path) {
		// 跳转相册
		Intent intent = new Intent(Intent.ACTION_PICK);
		// 设置跳转的类型
		intent.setType("image/*");
		// 剪裁
		intent.putExtra("crop", "circleCrop"); 
		// 剪裁比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 剪裁的像素点
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		// 存放的位置
		intent.putExtra("output", path);
		// 跳转相册
		context.startActivityForResult(intent, 1);
	}
}
