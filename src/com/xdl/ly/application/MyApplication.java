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
 * Application的生命周期最长 用于全局数据的缓存 全局初始化话操作 Application id
 * 245529c8057338fa3d0d4e4a432c8ee1
 */

public class MyApplication extends Application {
	// 获取位置信息 1.创建定位对象
	public LocationClient mLocationClient = null;
	// 2.创建定位结果的回调接口,获取位置信息// 3.开启定位
	public static BDLocation mCurrBdLocation;//保存用户的当前位置信息

	// 缓存全局数据
	public static UserInfo currUserInfo;// 缓存当前用户信息
	public static PoiInfo currPoiInfo; // 缓存当前用户选择的地点信息
	public static Map<String, Object> datas;// 存储缓存信息

	// 初始化操作
	@Override
	public void onCreate() {
		super.onCreate();
		// 第一：默认初始化
		Bmob.initialize(this, "245529c8057338fa3d0d4e4a432c8ee1");
		// 初始化SMS
		BmobSMS.initialize(this, "245529c8057338fa3d0d4e4a432c8ee1");

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		// 初始化当前信息
		datas = new HashMap<String, Object>();

		// 获取定位对象
		mLocationClient = new LocationClient(getApplicationContext());
		// 注册监听回调的接口
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
			    //获取用户的地址：
//				Log.i("myTag", "获取到定位结果："+location.getAddress().address);
				mCurrBdLocation=location;

			}
		});

		// 配置定位信息
        initLocation();
		// 开启定位
		mLocationClient.start();
	}
   /**
    * 配置定位信息
    */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 5000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于5000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	/**
	 * 存储全局数据
	 * 
	 * @param key
	 *            存储数据的key值
	 * @param value
	 *            存储数据的key值
	 * @return 返回原来的数据
	 */
	public static Object putPoiInfo(String key, Object value) {

		return datas.put(key, value);
	}

	/**
	 * 获取数据的方法
	 * 
	 * @param isDelete
	 *            是否删除原来缓存的数据
	 * @param key
	 *            缓存数据的key
	 * @return 返回原来的数据
	 */
	public static Object getPoiInfo(boolean isDelete, String key) {
		if (isDelete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

}
