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
	// �����ϴ��ļ�������
	private String[] uploadFilePaths;

	// ��ȡ����ơ����顢����顢���ؼ�
	private EditText mEtActionName, mEtActionDesc, mEtActionDetails,
			mEtActionMoney;
	private String mStrName, mStrDesc, mStrDetails, mStrMoney, mStrLocation;

	// ��ȡ��ʾ�ص�Ŀؼ�
	private TextView tvLocation;

	// ѡ��ͼƬ�Ŀؼ� ѡ����ͼƬ
	private LinearLayout mLinearTop, mLinearBottom;
	// ���ͼƬ�Ŀؼ�
	private ImageView mIvAddPic;
	// ���ͼƬ
	private String saveActionPic = "sdcard/actionpic.jpg";
	// ����û�ѡ���ͼƬ
	private List<Bitmap> mActionBitmaps = new ArrayList<Bitmap>();
	// ͼƬ�Ŀ�͸�
	private int width;
	private int height;

	// ���� ���� ͼƬ�Ŀؼ�
	private ImageView mBack, mOk;
	// spinner�����б� ѡ������
	private Spinner mSpinner;
	private String mStrActionType;// �����
	// ����Դ ���� ͼƬ
	private String[] mTypeNames = { "�ܱ�", "�ٶ�", "DIY", "����", "����", "�ݳ�", "չ��",
			"ɳ��", "Ʒ��", "�ۻ�" };
	private int[] mTypeImgs = { R.drawable.more_zhoubian,
			R.drawable.more_shaoer, R.drawable.more_diy,
			R.drawable.more_jianshen, R.drawable.more_jishi,
			R.drawable.more_yanchu, R.drawable.more_zhanlan,
			R.drawable.more_shalong, R.drawable.more_pincha,
			R.drawable.more_juhui };
	// ѡ�� ����� ���� �ص� ��� ʱ�� �Ŀؼ�
	private LinearLayout mLinearTime, mLinearLocation;
	// ��ʾʱ�� ��� �ص�Ŀؼ�
	private TextView mTvTime, mTvLocation;
	// ʱ�� ��� �ص���ַ���
	private String mStrDate, mStrTime;
	// ���ڶԻ���
	private DatePickerDialog mDatePicerDialog;
	private TimePickerDialog mTimeDialog;
	// �������ĵص���Ϣ
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
		// ���ͼƬ��LinearLayout
		mLinearBottom = (LinearLayout) viewById(R.id.act_home_publish_bottom);
		mLinearTop = (LinearLayout) viewById(R.id.act_home_publish_top);

		// �����
		mSpinner = (Spinner) viewById(R.id.act_publish_spinner);
		// ��ʼ���ؼ�
		mBack = (ImageView) viewById(R.id.act_home_publish_back);
		mOk = (ImageView) viewById(R.id.act_home_publish_ok);
		// ʱ��ؼ��ĳ�ʼ��
		mLinearTime = (LinearLayout) viewById(R.id.act_home_publish_lin5);
		mTvTime = (TextView) viewById(R.id.act_home_publish_lin5_time);
		// �ص�ؼ��ĳ�ʼ��
		mLinearLocation = (LinearLayout) viewById(R.id.act_home_publish_lin3);
		mTvLocation = (TextView) viewById(R.id.act_home_publish_lin3_location);

		// ��ʼ������ơ����顢����顢���Ŀؼ�
		mEtActionDesc = (EditText) viewById(R.id.act_home_publish_et_desc);
		mEtActionDetails = (EditText) viewById(R.id.act_home_publish_et_details);
		mEtActionMoney = (EditText) viewById(R.id.act_home_publish_et_money);
		mEtActionName = (EditText) viewById(R.id.act_home_publish_et_actionname);

	}

	@Override
	public void initDatas() {
		// ����LinearLayout�еĿؼ��ķ���
		mLinearTop.setOrientation(LinearLayout.HORIZONTAL);
		// ��̬��ȡͼƬ
		mIvAddPic = new ImageView(this);
		width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		height = width / 3 * 2;
		// ��������
		mIvAddPic.setLayoutParams(new LayoutParams(width, height));
		mIvAddPic.setImageResource(R.drawable.gather_send_img_add);
		mLinearTop.addView(mIvAddPic);

		// ��ʼ��ʱ��
		initTime();
		// ��ʼ������͵�����Դ
		mSpinner.setAdapter(new SpinnerAdapter(this, mTypeNames, mTypeImgs));
	}

	@Override
	public void initOperas() {
		// Spinner�ļ����¼�
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// ����Spinner�����ͻ���ֵ
				mStrActionType = mTypeNames[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// ��ȡ�ʱ��ļ����¼�
		mLinearTime.setOnClickListener(this);
		// ��ȡ��ص�ļ����¼�
		mLinearLocation.setOnClickListener(this);

		// ��ת��� ���ͼƬ
		mIvAddPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// addPic();

				// ��һ����ͼƬ���浽����
				CropPicUtils.getPic(PublishActivity.this,
						Uri.fromFile(new File(saveActionPic)));
			}
		});
		// ����
		mBack.setOnClickListener(this);
		// ����
		mOk.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		super.onActivityResult(requestCode, arg1, arg2);
		if (requestCode == 1) {
			// �����ѡ��ͼƬ��ʾͼƬ
			addPic();

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_publish_lin5:
			// ��ȡ�ʱ��
			pickActionTime();
			break;
		case R.id.act_home_publish_lin3:
			// ��ת���ٶȵ�ͼ
			JumpActivity(BaiduMapActivity.class);
			break;
		case R.id.act_home_publish_back:
			// ȡ�������
			finish();
			break;
		case R.id.act_home_publish_ok:
			// �����
			// toast3s("�ϴ��ɹ�");
			publishAction();
			break;
		}
	}

	/**
	 * ������ķ���
	 */
	private void publishAction() {
		actionInfo = new ActionInfo();
		// ��ȡ����ƣ���飬���飬���
		mStrName = getViewContent(mEtActionName);
		mStrDesc = getViewContent(mEtActionDesc);
		mStrDetails = getViewContent(mEtActionDetails);
		mStrMoney = getViewContent(mEtActionMoney);
		// ���ַ���ת��ΪDouble����
		final Double mDbMoney = Double.parseDouble(mStrMoney);

		// logI(mStrName +"---" + mStrDesc +"---"+ mStrDetails
		// +"---"+mStrMoney+"---"+mStrLocation+"---"+mStrActionType
		// +"---"+mStrTime);

		// �ж��������Ϣ
		if (mStrName == null) {
			toast3s("����Ʋ���Ϊ�գ�");
			return;
		}
		if (mStrDesc == null) {
			toast3s("���鲻��Ϊ�գ�");
			return;
		}
		if (mStrDetails == null) {
			toast3s("����鲻��Ϊ�գ�");
			return;
		} else {
			if (mStrDetails.length() < 6) {
				toast3s("����鲻������6���֣�");
			}
		}

		if (mStrMoney == null) {
			toast3s("�����Ϊ�գ�");
			return;
		}
		if (mDbMoney < 0.0) {
			toast3s("�����Ϊ������");
			return;
		}
		if (mStrTime == null) {
			toast3s("�ʱ�䲻��Ϊ�գ�");
			return;
		}
		if (mStrLocation == null) {
			toast3s("��ص㲻��Ϊ�գ�");
			return;
		}

		// ��ȡ�ļ�������ϴ�·��
		uploadFilePaths = new String[mActionBitmaps.size()];
		// �ж��Ƿ����ϴ���ͼƬ
		if (uploadFilePaths.length < 1) {
			toast3s("�����ϴ�һ��ͼƬ��");
			return;
		}

		// ��Ҫ�ϴ����ļ����浽����
		for (int i = 0; i < uploadFilePaths.length; i++) {
			// File paths=new
			// File(Environment.getExternalStorageDirectory().getAbsoluteFile());
			// �����ļ���·��
			File paths = new File("sdcard/actionpic/upload");
			// �ж��ļ����Ƿ����
			if (!paths.exists()) {
				paths.mkdirs();
			}
			// ����ͼƬ�ı���·��������
			File filePath = new File(paths, "actionpic--" + i + ".jpg");
			try {
				// ��ͼƬѹ��
				mActionBitmaps.get(i).compress(CompressFormat.JPEG, 100,
						new FileOutputStream(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// �õ���ǰͼƬ�ϴ��ľ���·��
			uploadFilePaths[i] = filePath.getAbsolutePath();
		}

		// �ϴ�ͼƬ�Ľ�����
		final ProgressDialog progressDialog = showProgerssDialog(null, "�����ϴ���:"
				+ "0/" + uploadFilePaths.length + "��ͼƬ", null, false);
		BmobFile.uploadBatch(uploadFilePaths, new UploadBatchListener() {
			int loadPicNum = 0;

			@Override
			public void onSuccess(List<BmobFile> actionPics, List<String> arg1) {
				progressDialog.setMessage("�����ϴ���:" + loadPicNum + "/"
						+ uploadFilePaths.length + "��ͼƬ");
				if (loadPicNum < uploadFilePaths.length) {
					return;
				}

				// ��������
				actionInfo.setActionName(mStrName);// ���û����
				actionInfo.setActionClass(mStrActionType);// ���û����
				actionInfo.setActionDesc(mStrDesc);// ���û���
				actionInfo.setActionIntro(mStrDetails);// ���û����
				actionInfo.setActionSite(mStrLocation);// ���û�ص�
				// ���õص�ľ��Ⱥ�γ��
				actionInfo.setLocation(new BmobGeoPoint(
						poiInfo.location.longitude, poiInfo.location.latitude));

				actionInfo.setActionTime(mStrTime);// ���û�Ŀ���ʱ��
           
				if (mDbMoney == 0.0) {
					actionInfo.setActionRMB("0");// ���û�Ľ��
				}else {
					actionInfo.setActionRMB(mDbMoney+"");// ���û�Ľ��
				}

				actionInfo.setActionUserId(MyApplication.currUserInfo
						.getObjectId());// ��ǰ��ķ�����

				actionInfo.setActionCity(poiInfo.city);// ���û�ĳ���

				actionInfo.setActionJPG(actionPics);// ���õ�ǰ������е�ͼƬ
				logI("------");
				// �ϴ�������
				actionInfo.save(new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException ex) {
						logI("******");
						if (ex == null) {
							toast3s("�ɹ������!");
							finish();
						} else {
							toast3s("ʧ�ܷ����!");
							logI("�����ʧ��:" + ex.getErrorCode() + ","
									+ ex.getLocalizedMessage());
						}
						progressDialog.dismiss();

					}
				});

			}

			@Override
			public void onError(int statuscode, String errormsg) {
				progressDialog.dismiss();
				toast3s("������" + statuscode + ",����������" + errormsg);
				toast3s("���������⣬�������磡");
			}

			@Override
			public void onProgress(int curIndex, int curPercent, int total,
					int totalPercent) {
				// 1��curIndex--��ʾ��ǰ�ڼ����ļ������ϴ�
				// 2��curPercent--��ʾ��ǰ�ϴ��ļ��Ľ���ֵ���ٷֱȣ�
				// 3��total--��ʾ�ܵ��ϴ��ļ���
				// 4��totalPercent--��ʾ�ܵ��ϴ����ȣ��ٷֱȣ�
				loadPicNum = curIndex;
			}
		});

	}

	private void addPic() {
		// bitmap���� ��ͼƬ���뼯����
		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		Bitmap bitmap = BitmapFactory.decodeFile(saveActionPic);
		mActionBitmaps.add(bitmap);
		// �ж��Ƿ��ܷ���
		if (mActionBitmaps.size() < 3) {// �����һ�ź͵ڶ���ʱ
			// �Ƴ����пؼ�
			mLinearTop.removeAllViews();
			for (int i = 0; i < mActionBitmaps.size(); i++) {
				// ��ȡ��ǰͼƬ
				Bitmap currentBitmap = mActionBitmaps.get(i);
				ImageView imageView = new ImageView(PublishActivity.this);
				// ����ͼƬ����
				imageView.setScaleType(ScaleType.FIT_CENTER);

				// ���ñ����ͼƬ�Ĳ���
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currentBitmap);
				mLinearTop.addView(imageView);
			}
			mLinearTop.addView(mIvAddPic);
		} else {// 3
			if (mActionBitmaps.size() == 3) {
				Bitmap curBitmap = mActionBitmaps.get(2);
				// �Ƴ����ͼƬ�ı�־
				mLinearTop.removeViewAt(2);
				// ��ӵ�����ͼƬ
				ImageView imageView = new ImageView(PublishActivity.this);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(curBitmap);
				mLinearTop.addView(imageView);
				mLinearBottom.addView(mIvAddPic);

			} else {// 4��5
				if (mActionBitmaps.size() == 6) {
					Bitmap curBitmap = mActionBitmaps.get(5);
					// �Ƴ����ͼƬ�ı�־
					mLinearBottom.removeViewAt(2);
					// ��ӵ�����ͼƬ
					ImageView imageView = new ImageView(PublishActivity.this);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					imageView.setLayoutParams(new LayoutParams(width, height));
					imageView.setImageBitmap(curBitmap);
					mLinearBottom.addView(imageView);
					// mLinearBottom.addView(mIvAddPic);
				} else {
					// �Ƴ����пؼ�
					mLinearBottom.removeAllViews();
					for (int i = 3; i < mActionBitmaps.size(); i++) {
						// ��ȡ��ǰͼƬ
						Bitmap currentBitmap = mActionBitmaps.get(i);
						ImageView imageView = new ImageView(
								PublishActivity.this);
						// ����ͼƬ����
						imageView.setScaleType(ScaleType.FIT_CENTER);

						// ���ñ����ͼƬ�Ĳ���
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
	 * ѡ��ʱ��
	 */
	private void pickActionTime() {
		if (mDatePicerDialog != null) {
			// չʾ���ڶԻ���
			mDatePicerDialog.show();
		}
	}

	private void initTime() {
		// ѡ�����ڶԻ���
		final Calendar calendar = Calendar.getInstance();
		mDatePicerDialog = new DatePickerDialog(this, new OnDateSetListener() {
			// �û�ѡ���ʱ��
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// ����û�ѡ������С�ڵ�ǰ���
				if (calendar.get(Calendar.YEAR) > year) {
					toast3s("ʱ������������ѡ��");
					return;
				}
				if (calendar.get(Calendar.YEAR) == year) {
					// ����û�ѡ����·�С�ڵ�ǰ�·�
					if (calendar.get(Calendar.MONTH) > monthOfYear) {
						toast3s("ʱ������������ѡ��");
						return;
					} else if (calendar.get(Calendar.MONTH) == monthOfYear) {
						// ����û�ѡ�����С�ڵ�ǰ��
						if (calendar.get(Calendar.DAY_OF_MONTH) >= dayOfMonth) {
							toast3s("����ܵ���ٰ죬������ѡ��");
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
	 * ��������
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// ��ѯ�����Ϣ
		poiInfo = (PoiInfo) MyApplication.getPoiInfo(true, "actionPoiInfo");
		if (poiInfo != null) {
			// ���õص���Ϣ
			mStrLocation = poiInfo.name;
			// ��������
			mTvLocation.setText(poiInfo.name);
		}

	}
}
