package com.xdl.ly.adapter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.xdl.ly.R;
import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.Bean.UserInfo;
import com.xdl.ly.Utils.CountDistenceUtils;
import com.xdl.ly.Utils.FindUserInfoUtils;
import com.xdl.ly.Utils.FindUserInfoUtils.FindUserInfoListener;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.view.MyImageView;

import android.R.integer;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FraHomeBaseAdapter extends BaseAdapter {

	private List<ActionInfo> infos; // ����Դ
	private Context context; // ������
	private LayoutInflater inflater;// ���������
	private HodlerView hv; // �ؼ��洢��

	/**
	 * ��ʼ�� ������ ����Դ ���������
	 * 
	 * @param activity
	 * @param mActionInfos
	 */
	public FraHomeBaseAdapter(BaseActivity activity,
			List<ActionInfo> mActionInfos) {
		//�ǿ��ж�
		if (mActionInfos==null) {
			mActionInfos=new ArrayList<ActionInfo>();
		}
		this.infos = mActionInfos;
		this.context = activity;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		hv = null;
		// ListView��һ���Ż�
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_home_item, null);

			hv = new HodlerView();
			// ��ʼ��View�и����ؼ�
			hv.tvNick = (TextView) convertView
					.findViewById(R.id.fragment_home_item_nick);
			hv.tvTime = (TextView) convertView
					.findViewById(R.id.fragment_home_item_time);
			hv.tvDetails = (TextView) convertView
					.findViewById(R.id.fragment_home_item_details);
			hv.tvRmb = (TextView) convertView
					.findViewById(R.id.fragment_home_item_money);
			hv.tvDistence = (TextView) convertView
					.findViewById(R.id.fragment_home_item_distence);
			// ͼƬ�ؼ�
			hv.ivActionPic = (ImageView) convertView
					.findViewById(R.id.fragment_home_item_actionpic);
			hv.ivHead = (MyImageView) convertView
					.findViewById(R.id.fragment_home_item_head);
			hv.ivSave = (ImageView) convertView
					.findViewById(R.id.fragment_home_item_shoucang);
			// ��ӱ��
			convertView.setTag(hv);
		} else {
			// ��ȡ���
			hv = (HodlerView) convertView.getTag();
		}
		// ��ǰ�����Ϣ
		ActionInfo actionInfo = infos.get(position);
		// Log.i("myTag",
		// actionInfo.getActionIntro()+"--------"+actionInfo.getActionDesc());

		hv.tvDetails.setText(actionInfo.getActionIntro()); // �����
		hv.tvRmb.setText(actionInfo.getActionRMB());// ���� 
		hv.tvTime.setText(actionInfo.getActionTime());// �ʱ��
		
		//���õ�ǰ�Ļ����
		BDLocation userLocation=MyApplication.mCurrBdLocation;
		//�λ��
		BmobGeoPoint actionPoint=actionInfo.getLocation();
		//��ȡ�����
		double distence= CountDistenceUtils.Distance(userLocation.getLongitude(),
				userLocation.getLatitude(), actionPoint.getLongitude(), 
				actionPoint.getLatitude());
//		if (userLocation==null || actionPoint==null  ) {
//			hv.tvDistence.setText("");
//			Toast.makeText(context, "��λʧ�ܣ�", 0).show();
//		}
//		��ʾ����ҳ
//		if (distence>1000) {
			distence=distence/1000.0;
			//����ʮ���ƶ���
			BigDecimal b=new BigDecimal(distence);
			//����һλС��
			distence=b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			hv.tvDistence.setText(distence+"km");
//		}else {
			//����ʮ���ƶ���
//			BigDecimal b=new BigDecimal(distence);
//			//����һλС��
//			distence=b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//			hv.tvDistence.setText(distence+"m");
//		}
		

		// http://bmob-cdn-6787.b0.upaiyun.com/2016/11/16/98daa7a73ceb472ba6b64f890dc87037.jpg
		// http://bmob-cdn-6787.b0.upaiyun.com/2016/11/16/458dd96d70da4f23b39b9f9ae2136dd9.jpg
		// ��ȡͼƬ������http://2136dd9.jpg
		String actionPicName = actionInfo
				.getActionJPG()
				.get(0)
				.getFileUrl()
				.substring(
						actionInfo.getActionJPG().get(0).getFileUrl().length() - 4 - 32,
						actionInfo.getActionJPG().get(0).getFileUrl().length() - 4);
		// �������ص�ͼƬ
		File actionPicFile = new File("sdcard/ly.actionpic--" + actionPicName
				+ ".jpg");
		// ��ȡ�ͼƬ������
//		Log.i("myTag", "��ȡͼƬ������:" + actionPicName);
		// �ж��ļ��Ƿ����
		if (actionPicFile.exists()) {
			// ���ڵĻ�ֱ����ʾ
			hv.ivActionPic.setImageBitmap(BitmapFactory
					.decodeFile(actionPicFile.getAbsolutePath()));
		} else {
			// ����ͼƬ
			actionInfo.getActionJPG().get(0)
			/**
			 * ����һ  ָ��������·��
			 */
					.download(actionPicFile,new DownloadFileListener() {

						@Override
						public void onProgress(Integer arg0, long arg1) {
							// ��ʾ����
						}

						@Override
						public void done(String path, BmobException ex) {
							if (ex == null) {
								Log.i("myTag", "�ͼƬ����·����" + path);
								hv.ivActionPic.setImageBitmap(BitmapFactory
										.decodeFile(path));
							}
						}
					});
		}

		/**
		 * ��ʾ�û���ͷ���ǳ�
		 */
		FindUserInfoUtils.findUserInfo(actionInfo.getActionUserId(),
				new FindUserInfoListener() {

					@Override
					public void getUserInfo(UserInfo info, BmobException ex) {
						if (ex == null) {
							// ��ʾ�û����ǳ�
							hv.tvNick.setText(info.getNick());
							//��ȡͷ�������   ��ͷ�����������(���浽Ӳ��)
							String userHeadPicName=info.getUserHead().getFileUrl().substring(
									info.getUserHead().getFileUrl().length()-4-32,
									info.getUserHead().getFileUrl().length()-4);
							//�û�ͷ��ı���·��
							File userHeadFile=new File("sdcard/ly.userheadpic--" + userHeadPicName
									+ ".jpg");
							Log.i("myTag", "��ȡͷ��ͼƬ������:" + userHeadPicName);
							
							if (userHeadFile.exists()) {
								//����ͷ��
								hv.ivHead.setImageBitmap(BitmapFactory
										.decodeFile(userHeadFile.getAbsolutePath()));
							}else {
								// �ӷ����������û���ͷ��
								info.getUserHead().download(userHeadFile,new DownloadFileListener() {

									@Override
									public void onProgress(Integer arg0, long arg1) {
									}

									@Override
									public void done(String path, BmobException ex) {
										if (ex == null) {
											Log.i("myTag", "ͷ��ͼƬ����·��---" + path);
											// �����û�ͷ��
											hv.ivHead.setImageBitmap(BitmapFactory
													.decodeFile(path));
										}

									}
								});
							}
							
						}
					}
				});
		
		//��¼��ǰ��Ƿ񱻵����
		boolean isPraiseFlag=true;  //��ǰ�δ���ղ�
		//��ʾ����ղ�״̬
		List<String> praiseActions=MyApplication.currUserInfo.getPraiseAction();
		
        //�ж��ղػ�Ƿ������ǰ�
		if (praiseActions.contains(actionInfo.getObjectId())) {
			hv.ivSave.setImageResource(R.drawable.xin);
			isPraiseFlag=false;  //���ղ�
		}else {
			hv.ivSave.setImageResource(R.drawable.xin_off);
			isPraiseFlag=true; //δ�ղ�
		}
		
		//�����ؼ��洢��
		final ActionpraiseViewHodler hodler=new ActionpraiseViewHodler();
		hodler.actionInfo=actionInfo;
		hodler.flag=isPraiseFlag;
		//�ղز���
		hv.ivSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ղ�
				praiseAction(hodler,hv.ivSave);
			}
		});
		return convertView;
	}
	/**
	 * ����ղز���
	 * @param hodler
	 */
	private void praiseAction(final ActionpraiseViewHodler hodler,final ImageView ivSave) {
		if (hodler.flag) {//δ�ղ�   ���ղز���
			//���·������еĻ��
			//��������  Bmob���ݵ�ԭ����
			ActionInfo actionInfo=new ActionInfo();
			//����ǰ�û���id���뵱ǰ��ղ�������(����е�����)
			actionInfo.add("praiseUsers", MyApplication.currUserInfo.getObjectId());
			
			//ԭ���Ե������ղ�����
			actionInfo.increment("saveNum");
			
			//�����ղػ,��������
			actionInfo.update(hodler.actionInfo.getObjectId(),
					new UpdateListener() {
			
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//����ͼƬ���±�������
						ivSave.setImageResource(R.drawable.xin);
						hodler.actionInfo.getPraiseUsers()
						.add(MyApplication.currUserInfo.getObjectId());
					}
					
				}
			});
			
			//���·������е��û���
			//�����û�����
			UserInfo userInfo=new UserInfo();
			//���û������������
			userInfo.add("praiseAction", hodler.actionInfo.getObjectId());
			//�����û���
			userInfo.update(MyApplication.currUserInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//���±��ص�����
						MyApplication.currUserInfo.getPraiseAction()
						.add(hodler.actionInfo.getObjectId());
					}
				}
			});
			
		}else {//���ղ�   ��ȡ���
			//������Ϣ����
			ActionInfo actionInfo=new ActionInfo();
			//�����û���id����
			ArrayList<String> removeUserid=new ArrayList<String>();
			//��ӵ�ǰ�û���id
			removeUserid.add(MyApplication.currUserInfo.getObjectId());
			//����û��Ƴ����ղ�����
			actionInfo.removeAll("praiseUsers", removeUserid);
			//ԭ���Եļ����ղ�����
			actionInfo.increment("saveNum",-1);
			//�����û���
			actionInfo.update(hodler.actionInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					//���±������� �Ƴ���ǰ�û�
					hodler.actionInfo.getPraiseUsers() 
					.remove(MyApplication.currUserInfo.getObjectId());
					//����ͼƬ
					ivSave.setImageResource(R.drawable.xin_off);
					
				}
			});
			
			//�����û���
			UserInfo userInfo=new UserInfo();
			//������ǰ�û��ղصĻ
			ArrayList<String> removeActionId=new ArrayList<String>();
			//���Ҫ�Ƴ��Ļ
			removeActionId.add(hodler.actionInfo.getObjectId());
			//�Ƴ���ӵĻ
			userInfo.removeAll("praiseAction", removeActionId);
			//��������
			userInfo.update(MyApplication.currUserInfo.getObjectId(),
					new UpdateListener() {
				
				@Override
				public void done(BmobException ex) {
					if (ex==null) {
						//���±������� �Ƴ�ȡ�����޵Ļ
						MyApplication.currUserInfo.getPraiseAction()
						.remove(hodler.actionInfo.getObjectId());
					}else {
						Log.i("myTag", "ʧ�ܣ�"+ex.getErrorCode()+","+ex.getLocalizedMessage());
					} 
					
				}
			});
		}
		//�ı���
		hodler.flag=!hodler.flag;
		
	}
    class ActionpraiseViewHodler{
    	ActionInfo actionInfo;//��ǰ�����
    	boolean flag;		//��ǰ�������ղر��
    	
    }
	class HodlerView {
		// �ǳƣ�����ʱ�䣬�����飬����
		TextView tvNick, tvTime, tvRmb, tvDetails, tvDistence;
		// ͷ�񣬷������ͼƬ���ղ�
		ImageView ivActionPic, ivSave;
		MyImageView ivHead;
	}

	/**
	 * ���Ⱪ¶����ǰ��XListView��������������Դ
	 * 
	 * @param infos
	 *            ����Դ
	 */
	public void updateDate(List<ActionInfo> infos) {
		// �����ǰ����Դ
//		this.infos.clear();
//		// �������ݸı䲢ˢ��
//		notifyDataSetChanged();
		// ����ԭ��������
		this.infos = infos;
		notifyDataSetChanged();
	}

}
