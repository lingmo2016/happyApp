package com.xdl.ly.application;

import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.xdl.ly.Bean.UserInfo;

import cn.bmob.push.a.a;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import android.app.Application;
import android.util.Log;

/**
 * Application����������� ����ȫ�����ݵĻ��� ȫ�ֳ�ʼ�������� Application id
 * 245529c8057338fa3d0d4e4a432c8ee1
 */

public class MyApplication extends Application {
	// ��ȡλ����Ϣ 1.������λ����
	public LocationClient mLocationClient = null;
	// 2.������λ����Ļص��ӿ�,��ȡλ����Ϣ// 3.������λ
	public static BDLocation mCurrBdLocation;//�����û��ĵ�ǰλ����Ϣ

	// ����ȫ������
	public static UserInfo currUserInfo;// ���浱ǰ�û���Ϣ
	public static PoiInfo currPoiInfo; // ���浱ǰ�û�ѡ��ĵص���Ϣ
	public static Map<String, Object> datas;// �洢������Ϣ

	// ��ʼ������
	@Override
	public void onCreate() {
		super.onCreate();
		// ��һ��Ĭ�ϳ�ʼ��
		Bmob.initialize(this, "245529c8057338fa3d0d4e4a432c8ee1");
		// ��ʼ��SMS
		BmobSMS.initialize(this, "245529c8057338fa3d0d4e4a432c8ee1");

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());

		// ��ʼ����ǰ��Ϣ
		datas = new HashMap<String, Object>();

		// ��ȡ��λ����
		mLocationClient = new LocationClient(getApplicationContext());
		// ע������ص��Ľӿ�
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
			    //��ȡ�û��ĵ�ַ��
//				Log.i("myTag", "��ȡ����λ�����"+location.getAddress().address);
				mCurrBdLocation=location;

			}
		});

		// ���ö�λ��Ϣ
        initLocation();
		// ������λ
		mLocationClient.start();
	}
   /**
    * ���ö�λ��Ϣ
    */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 5000;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���5000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	/**
	 * �洢ȫ������
	 * 
	 * @param key
	 *            �洢���ݵ�keyֵ
	 * @param value
	 *            �洢���ݵ�keyֵ
	 * @return ����ԭ��������
	 */
	public static Object putPoiInfo(String key, Object value) {

		return datas.put(key, value);
	}

	/**
	 * ��ȡ���ݵķ���
	 * 
	 * @param isDelete
	 *            �Ƿ�ɾ��ԭ�����������
	 * @param key
	 *            �������ݵ�key
	 * @return ����ԭ��������
	 */
	public static Object getPoiInfo(boolean isDelete, String key) {
		if (isDelete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

}
