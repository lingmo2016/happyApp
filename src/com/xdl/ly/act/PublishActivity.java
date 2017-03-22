package com.xdl.ly.act;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xdl.ly.R;
import com.xdl.ly.BaiduSearch.BaiduMapActivity;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Utils.CropPicUtils;
import com.xdl.ly.adapter.SpinnerAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;

public class PublishActivity extends BaseActivity implements BaseInterface,
		OnClickListener {
	// 批量上传文件的数组
	private String[] uploadFilePaths;

	// 获取活动名称、活动简介、活动详情、金额控件
	private EditText mEtActionName, mEtActionDesc, mEtActionDetails,
			mEtActionMoney;
	private String mStrName, mStrDesc, mStrDetails, mStrMoney, mStrLocation;

	// 获取显示地点的控件
	private TextView tvLocation;

	// 选择图片的控件 选六张图片
	private LinearLayout mLinearTop, mLinearBottom;
	// 添加图片的控件
	private ImageView mIvAddPic;
	// 存放图片
	private String saveActionPic = "sdcard/actionpic.jpg";
	// 存放用户选择的图片
	private List<Bitmap> mActionBitmaps = new ArrayList<Bitmap>();
	// 图片的宽和高
	private int width;
	private int height;

	// 返回 发布 图片的控件
	private ImageView mBack, mOk;
	// spinner下拉列表 选择活动类型
	private Spinner mSpinner;
	private String mStrActionType;// 活动类型
	// 数据源 名字 图片
	private String[] mTypeNames = { "周边", "少儿", "DIY", "健身", "集市", "演出", "展览",
			"沙龙", "品茶", "聚会" };
	private int[] mTypeImgs = { R.drawable.more_zhoubian,
			R.drawable.more_shaoer, R.drawable.more_diy,
			R.drawable.more_jianshen, R.drawable.more_jishi,
			R.drawable.more_yanchu, R.drawable.more_zhanlan,
			R.drawable.more_shalong, R.drawable.more_pincha,
			R.drawable.more_juhui };
	// 选择 活动名称 详情 地点 金额 时间 的控件
	private LinearLayout mLinearTime, mLinearLocation;
	// 显示时间 金额 地点的控件
	private TextView mTvTime, mTvLocation;
	// 时间 金额 地点的字符串
	private String mStrDate, mStrTime;
	// 日期对话框
	private DatePickerDialog mDatePicerDialog;
	private TimePickerDialog mTimeDialog;
	// 传过来的地点信息
	private PoiInfo poiInfo;

	private ActionInfo actionInfo;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_home_publish);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// 添加图片的LinearLayout
		mLinearBottom = (LinearLayout) viewById(R.id.act_home_publish_bottom);
		mLinearTop = (LinearLayout) viewById(R.id.act_home_publish_top);

		// 活动类型
		mSpinner = (Spinner) viewById(R.id.act_publish_spinner);
		// 初始化控件
		mBack = (ImageView) viewById(R.id.act_home_publish_back);
		mOk = (ImageView) viewById(R.id.act_home_publish_ok);
		// 时间控件的初始化
		mLinearTime = (LinearLayout) viewById(R.id.act_home_publish_lin5);
		mTvTime = (TextView) viewById(R.id.act_home_publish_lin5_time);
		// 地点控件的初始化
		mLinearLocation = (LinearLayout) viewById(R.id.act_home_publish_lin3);
		mTvLocation = (TextView) viewById(R.id.act_home_publish_lin3_location);

		// 初始化活动名称、活动简介、活动详情、金额的控件
		mEtActionDesc = (EditText) viewById(R.id.act_home_publish_et_desc);
		mEtActionDetails = (EditText) viewById(R.id.act_home_publish_et_details);
		mEtActionMoney = (EditText) viewById(R.id.act_home_publish_et_money);
		mEtActionName = (EditText) viewById(R.id.act_home_publish_et_actionname);

	}

	@Override
	public void initDatas() {
		// 设置LinearLayout中的控件的方向
		mLinearTop.setOrientation(LinearLayout.HORIZONTAL);
		// 动态获取图片
		mIvAddPic = new ImageView(this);
		width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		height = width / 3 * 2;
		// 设置属性
		mIvAddPic.setLayoutParams(new LayoutParams(width, height));
		mIvAddPic.setImageResource(R.drawable.gather_send_img_add);
		mLinearTop.addView(mIvAddPic);

		// 初始化时间
		initTime();
		// 初始化活动类型的数据源
		mSpinner.setAdapter(new SpinnerAdapter(this, mTypeNames, mTypeImgs));
	}

	@Override
	public void initOperas() {
		// Spinner的监听事件
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 设置Spinner的类型回显值
				mStrActionType = mTypeNames[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 获取活动时间的监听事件
		mLinearTime.setOnClickListener(this);
		// 获取活动地点的监听事件
		mLinearLocation.setOnClickListener(this);

		// 跳转相册 添加图片
		mIvAddPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// addPic();

				// 将一张张图片保存到本地
				CropPicUtils.getPic(PublishActivity.this,
						Uri.fromFile(new File(saveActionPic)));
			}
		});
		// 返回
		mBack.setOnClickListener(this);
		// 发布
		mOk.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		super.onActivityResult(requestCode, arg1, arg2);
		if (requestCode == 1) {
			// 从相册选完图片显示图片
			addPic();

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_publish_lin5:
			// 获取活动时间
			pickActionTime();
			break;
		case R.id.act_home_publish_lin3:
			// 跳转到百度地图
			JumpActivity(BaiduMapActivity.class);
			break;
		case R.id.act_home_publish_back:
			// 取消发布活动
			finish();
			break;
		case R.id.act_home_publish_ok:
			// 发布活动
			// toast3s("上传成功");
			publishAction();
			break;
		}
	}

	/**
	 * 发布活动的方法
	 */
	private void publishAction() {
		actionInfo = new ActionInfo();
		// 获取活动名称，简介，详情，金额
		mStrName = getViewContent(mEtActionName);
		mStrDesc = getViewContent(mEtActionDesc);
		mStrDetails = getViewContent(mEtActionDetails);
		mStrMoney = getViewContent(mEtActionMoney);
		// 将字符串转化为Double类型
		final Double mDbMoney = Double.parseDouble(mStrMoney);

		// logI(mStrName +"---" + mStrDesc +"---"+ mStrDetails
		// +"---"+mStrMoney+"---"+mStrLocation+"---"+mStrActionType
		// +"---"+mStrTime);

		// 判断输入的信息
		if (mStrName == null) {
			toast3s("活动名称不能为空！");
			return;
		}
		if (mStrDesc == null) {
			toast3s("活动简介不能为空！");
			return;
		}
		if (mStrDetails == null) {
			toast3s("活动详情不能为空！");
			return;
		} else {
			if (mStrDetails.length() < 6) {
				toast3s("活动详情不能少于6个字！");
			}
		}

		if (mStrMoney == null) {
			toast3s("活动金额不能为空！");
			return;
		}
		if (mDbMoney < 0.0) {
			toast3s("活动金额不能为负数！");
			return;
		}
		if (mStrTime == null) {
			toast3s("活动时间不能为空！");
			return;
		}
		if (mStrLocation == null) {
			toast3s("活动地点不能为空！");
			return;
		}

		// 获取文件数组的上传路径
		uploadFilePaths = new String[mActionBitmaps.size()];
		// 判断是否有上传的图片
		if (uploadFilePaths.length < 1) {
			toast3s("至少上传一张图片！");
			return;
		}

		// 将要上传的文件保存到本地
		for (int i = 0; i < uploadFilePaths.length; i++) {
			// File paths=new
			// File(Environment.getExternalStorageDirectory().getAbsoluteFile());
			// 保存文件的路径
			File paths = new File("sdcard/actionpic/upload");
			// 判断文件夹是否存在
			if (!paths.exists()) {
				paths.mkdirs();
			}
			// 单张图片的保存路径及名称
			File filePath = new File(paths, "actionpic--" + i + ".jpg");
			try {
				// 将图片压缩
				mActionBitmaps.get(i).compress(CompressFormat.JPEG, 100,
						new FileOutputStream(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 得到当前图片上传的绝对路径
			uploadFilePaths[i] = filePath.getAbsolutePath();
		}

		// 上传图片的进度条
		final ProgressDialog progressDialog = showProgerssDialog(null, "正在上传第:"
				+ "0/" + uploadFilePaths.length + "张图片", null, false);
		BmobFile.uploadBatch(uploadFilePaths, new UploadBatchListener() {
			int loadPicNum = 0;

			@Override
			public void onSuccess(List<BmobFile> actionPics, List<String> arg1) {
				progressDialog.setMessage("正在上传第:" + loadPicNum + "/"
						+ uploadFilePaths.length + "张图片");
				if (loadPicNum < uploadFilePaths.length) {
					return;
				}

				// 设置属性
				actionInfo.setActionName(mStrName);// 设置活动名称
				actionInfo.setActionClass(mStrActionType);// 设置活动类型
				actionInfo.setActionDesc(mStrDesc);// 设置活动简介
				actionInfo.setActionIntro(mStrDetails);// 设置活动详情
				actionInfo.setActionSite(mStrLocation);// 设置活动地点
				// 设置地点的经度和纬度
				actionInfo.setLocation(new BmobGeoPoint(
						poiInfo.location.longitude, poiInfo.location.latitude));

				actionInfo.setActionTime(mStrTime);// 设置活动的开启时间
           
				if (mDbMoney == 0.0) {
					actionInfo.setActionRMB("0");// 设置活动的金额
				}else {
					actionInfo.setActionRMB(mDbMoney+"");// 设置活动的金额
				}

				actionInfo.setActionUserId(MyApplication.currUserInfo
						.getObjectId());// 当前活动的发布者

				actionInfo.setActionCity(poiInfo.city);// 设置活动的城市

				actionInfo.setActionJPG(actionPics);// 设置当前活动的所有的图片
				logI("------");
				// 上传服务器
				actionInfo.save(new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException ex) {
						logI("******");
						if (ex == null) {
							toast3s("成功发布活动!");
							finish();
						} else {
							toast3s("失败发布活动!");
							logI("发布活动失败:" + ex.getErrorCode() + ","
									+ ex.getLocalizedMessage());
						}
						progressDialog.dismiss();

					}
				});

			}

			@Override
			public void onError(int statuscode, String errormsg) {
				progressDialog.dismiss();
				toast3s("错误码" + statuscode + ",错误描述：" + errormsg);
				toast3s("网络有问题，请检查网络！");
			}

			@Override
			public void onProgress(int curIndex, int curPercent, int total,
					int totalPercent) {
				// 1、curIndex--表示当前第几个文件正在上传
				// 2、curPercent--表示当前上传文件的进度值（百分比）
				// 3、total--表示总的上传文件数
				// 4、totalPercent--表示总的上传进度（百分比）
				loadPicNum = curIndex;
			}
		});

	}

	private void addPic() {
		// bitmap集合 把图片放入集合中
		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		Bitmap bitmap = BitmapFactory.decodeFile(saveActionPic);
		mActionBitmaps.add(bitmap);
		// 判断是否能放入
		if (mActionBitmaps.size() < 3) {// 放入第一张和第二张时
			// 移除所有控件
			mLinearTop.removeAllViews();
			for (int i = 0; i < mActionBitmaps.size(); i++) {
				// 获取当前图片
				Bitmap currentBitmap = mActionBitmaps.get(i);
				ImageView imageView = new ImageView(PublishActivity.this);
				// 设置图片充满
				imageView.setScaleType(ScaleType.FIT_CENTER);

				// 设置被添加图片的参数
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currentBitmap);
				mLinearTop.addView(imageView);
			}
			mLinearTop.addView(mIvAddPic);
		} else {// 3
			if (mActionBitmaps.size() == 3) {
				Bitmap curBitmap = mActionBitmaps.get(2);
				// 移除添加图片的标志
				mLinearTop.removeViewAt(2);
				// 添加第三张图片
				ImageView imageView = new ImageView(PublishActivity.this);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(curBitmap);
				mLinearTop.addView(imageView);
				mLinearBottom.addView(mIvAddPic);

			} else {// 4、5
				if (mActionBitmaps.size() == 6) {
					Bitmap curBitmap = mActionBitmaps.get(5);
					// 移除添加图片的标志
					mLinearBottom.removeViewAt(2);
					// 添加第六张图片
					ImageView imageView = new ImageView(PublishActivity.this);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					imageView.setLayoutParams(new LayoutParams(width, height));
					imageView.setImageBitmap(curBitmap);
					mLinearBottom.addView(imageView);
					// mLinearBottom.addView(mIvAddPic);
				} else {
					// 移除所有控件
					mLinearBottom.removeAllViews();
					for (int i = 3; i < mActionBitmaps.size(); i++) {
						// 获取当前图片
						Bitmap currentBitmap = mActionBitmaps.get(i);
						ImageView imageView = new ImageView(
								PublishActivity.this);
						// 设置图片充满
						imageView.setScaleType(ScaleType.FIT_CENTER);

						// 设置被添加图片的参数
						imageView.setLayoutParams(new LayoutParams(width,
								height));
						imageView.setImageBitmap(currentBitmap);
						mLinearBottom.addView(imageView);
					}
					mLinearBottom.addView(mIvAddPic);

				}
			}
		}
	}

	/**
	 * 选择时间
	 */
	private void pickActionTime() {
		if (mDatePicerDialog != null) {
			// 展示日期对话框
			mDatePicerDialog.show();
		}
	}

	private void initTime() {
		// 选择日期对话框
		final Calendar calendar = Calendar.getInstance();
		mDatePicerDialog = new DatePickerDialog(this, new OnDateSetListener() {
			// 用户选择的时间
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// 如果用户选择的年份小于当前年份
				if (calendar.get(Calendar.YEAR) > year) {
					toast3s("时间有误，请重新选择！");
					return;
				}
				if (calendar.get(Calendar.YEAR) == year) {
					// 如果用户选择的月份小于当前月份
					if (calendar.get(Calendar.MONTH) > monthOfYear) {
						toast3s("时间有误，请重新选择！");
						return;
					} else if (calendar.get(Calendar.MONTH) == monthOfYear) {
						// 如果用户选择的天小于当前天
						if (calendar.get(Calendar.DAY_OF_MONTH) >= dayOfMonth) {
							toast3s("活动不能当天举办，请重新选择！");
							return;
						}
					}
				}
				mStrDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
				if (mTimeDialog != null) {
					mTimeDialog.show();
				}
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		mTimeDialog = new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mStrTime = mStrDate + " " + hourOfDay + ":" + minute;
				mTvTime.setText(mStrTime);
			}
		}, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
	}

	/**
	 * 回显数据
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// 查询点的信息
		poiInfo = (PoiInfo) MyApplication.getPoiInfo(true, "actionPoiInfo");
		if (poiInfo != null) {
			// 设置地点信息
			mStrLocation = poiInfo.name;
			// 回显数据
			mTvLocation.setText(poiInfo.name);
		}

	}
}
