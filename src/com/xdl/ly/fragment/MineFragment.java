package com.xdl.ly.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xdl.ly.R;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.Utils.FindUserInfoUtils;
import com.xdl.ly.Utils.FindUserInfoUtils.FindUserInfoListener;
import com.xdl.ly.Utils.ImageLoaderUtils;
import com.xdl.ly.adapter.FraMyGridViewBaseAdapter;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseFragment;
import com.xdl.ly.base.BaseInterface;
import com.xdl.ly.view.MyImageView;

/**
 * Fragment�����Լ��Ĳ��� fragment��������չʾ���Ϣ
 */
public class MineFragment extends BaseFragment implements BaseInterface,
		OnClickListener {
	// �ؼ��ǳ� �˺�
	private TextView mTvNick, mTvAcount;
	// �ؼ� ͷ��
	private MyImageView mIvHead;
	// GridView �ؼ�
	private GridView mGridView;
	private View view;
	// ����Դ
	private List<String> data;
	// �û�ͷ��Ļ�����
	private File saveUserHead = new File("sdcard/userhead.png");
	// �ϴ����������ĶԻ���
	private ProgressDialog upLoderDialog;
	// ���ػ���ͼƬ
	private ImageLoader mImageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ���ز��������
		view = inflater.inflate(R.layout.fragment_my, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// ��ʼ���ؼ�����
		mGridView = (GridView) viewById(R.id.fragment_my_gridview);
		mIvHead = (MyImageView) viewById(R.id.fragment_my_head);
		mTvAcount = (TextView) viewById(R.id.fragment_my_account);
		mTvNick = (TextView) viewById(R.id.fragment_my_nick);

	}

	@Override
	public void initDatas() {
		// ��ȡͼƬ���ض���
		mImageLoader = ImageLoaderUtils.getInstance(activity);
		// ��ȡ��ǰ�û�����Ϣ
		// UserInfo currenUserInfo=MyApplication.currUserInfo;
		// ��ѯ�û�
		FindUserInfoUtils.findUserInfo(
				MyApplication.currUserInfo.getObjectId(),
				new FindUserInfoListener() {

					@Override
					public void getUserInfo(UserInfo info, BmobException ex) {
						// Log.i("myTag",info.getObjectId()+"&&&&&&&&");
						Log.i("myTag", MyApplication.currUserInfo.getUsername()
								+ "&&&&&&&&");
						Log.i("myTag", MyApplication.currUserInfo.getNick()
								+ "&&&&&&&&");
						// �����û���Ϣ
						if (info != null) {
							// ͷ��ͼƬ��������
							BmobFile userFile = info.getUserHead();
							// ��ȡͼƬ��URL
							String userHeadurl = userFile.getFileUrl();

							DisplayImageOptions options = ImageLoaderUtils
									.getOpt(R.drawable.head,
											R.drawable.beishang);
							/**
							 * ����һ��ͼƬURL ����������ʾͼƬ�Ŀؼ� ����������ʾͼƬ��������Ϣ
							 */
							// ��ʾͼƬ
							mImageLoader.displayImage(userHeadurl, mIvHead,
									options);
							// �û��ǳ�
							mTvNick.setText(info.getNick());
							// �û��˺� ���ֻ���
							mTvAcount.setText(info.getUsername());
							// Log.i("myTag",MyApplication.currUserInfo.getObjectId()+"------");

						}
					}
				});

		// ��ʼ������Դ
		data = new ArrayList<String>();
		data.add("�Ż�");
		data.add("�ղ�");
		data.add("����");
		data.add("����");
		data.add("�");
		data.add("����");

		// ���ӵ�������
		mGridView.setAdapter(new FraMyGridViewBaseAdapter(activity, data));

	}

	@Override
	public void initOperas() {
		// ���ͷ�����
		// �����û�����ͷ��ļ����¼�
		mIvHead.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_my_head:
			// ����û�ͷ�񣬸���
			Intent intent = new Intent(Intent.ACTION_PICK);
			// ������Ƭ����
			intent.setType("image/*");
			// ����
			intent.putExtra("crop", "circleCrop");
			// ���ñ���
			intent.putExtra("aspectX", 1);
			intent.putExtra("ascepctY", 1);
			// ���õ����ص�
			intent.putExtra("outputX", 800);
			intent.putExtra("outputY", 800);
			// ��ŵ�λ��
			intent.putExtra("output", Uri.fromFile(saveUserHead));
			// ��ת����� (������ֵ����ת)
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}

	/**
	 * ͷ���ȡ�Ļص�����
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) { // ѡ��ͷ��
			// �û�ͷ���ϴ�
			loadHead();
		}
	}

	/**
	 * ��ѡ�е�ͷ�� �ϴ����� ������Ϣ
	 */
	private void loadHead() {
		// ��ȡ��ǰ�û�����
		final UserInfo userInfo = new UserInfo();
		// �����û�ͷ��
		userInfo.setUserHead(new BmobFile(saveUserHead));
		// �����ϴ��ĶԻ���
		upLoderDialog = showProgerssDialog(null, "�����ϴ������Ժ�", null, false);
		// �ϴ��û�ͷ����������
		userInfo.getUserHead().upload(new UploadFileListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					// ����ͷ��
					userInfo.update(MyApplication.currUserInfo.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										upLoderDialog.dismiss();
										toast3s("ͷ���ϴ��ɹ���");
										// �ϴ�������ȡ���Ի���
										
										Bitmap bm = BitmapFactory
												.decodeFile(saveUserHead
														.getAbsolutePath());
										mIvHead.setImageBitmap(bm);
										
									}
								}
							});

				}
			}
		});
	}

}
