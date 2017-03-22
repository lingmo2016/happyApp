package com.xdl.ly.BaiduSearch;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xdl.ly.R;
import com.xdl.ly.application.MyApplication;
import com.xdl.ly.base.BaseActivity;
import com.xdl.ly.base.BaseInterface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 开发秘钥：bo9MPiw6LGfCtOGGCeupxYu6CddtrwB2
 * 
 * @author 邢悦
 * 
 */
public class BaiduMapActivity extends BaseActivity implements BaseInterface {

	private MapView mBaiMapView; // 地图控件
	private EditText mEtLocation; // 搜索框控件
	private ImageView mSearch; // 搜索控件
	private BaiduMap baiMap;// 百度地图控制器
	private PoiSearch mPoiSearch;//POI检索
    private String mStrLocation;//搜索的地点
	private List<PoiInfo> poiInfos; //搜索的结果集
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.baidu_search);
		initViews();
		initDatas();
		initOperas();
	}

	@Override
	public void initViews() {
		// 初始化百度地图
		mBaiMapView = (MapView) viewById(R.id.baidu_search_bmapView);
		mEtLocation = (EditText) viewById(R.id.baidu_search_et_chazhao);
		mSearch = (ImageView) viewById(R.id.baidu_search_iv_sousuo);
		// 获取地图控件
		baiMap = mBaiMapView.getMap();
	}

	@Override
	public void initDatas() {
		
	}

	/**
	 * 第一步，创建POI检索实例 mPoiSearch = PoiSearch.newInstance(); 第二步，创建POI检索监听者；
	 * OnGetPoiSearchResultListener poiListener = new
	 * OnGetPoiSearchResultListener(){ public void onGetPoiResult(PoiResult
	 * result){ //获取POI检索结果 } public void onGetPoiDetailResult(PoiDetailResult
	 * result){ //获取Place详情页检索结果 } }; 第三步，设置POI检索监听者；
	 * mPoiSearch.setOnGetPoiSearchResultListener(poiListener); 第四步，发起检索请求；
	 * mPoiSearch.searchInCity((new PoiCitySearchOption()) .city(“北京”)
	 * .keyword(“美食”) .pageNum(10)); 第五步，释放POI检索实例； mPoiSearch.destroy();
	 */
	@Override
	public void initOperas() {
		// 普通地图
	    baiMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	    //  第一步，创建POI检索实例
	    mPoiSearch=  PoiSearch.newInstance();
	    //第二步，创建POI检索监听者
		//第三步，设置POI检索监听者
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				//获取POI检索结果
//				if (result==null) {
//					toast3s("未检索到，请重新输入！");
//					return;
//				}else {
//					//将搜索结果放到集合中
//					List<PoiInfo> resultPoInfos=result.getAllPoi();
//					PoiInfo poiInfo=resultPoInfos.get(0);
//					StringBuffer sb=new StringBuffer();
//					sb.append(poiInfo.name+",位置："+poiInfo.address
//							+"，联系电话:"
//							+poiInfo.phoneNum);
//					Log.i("myTag", sb.toString());
//				}
//				baiMap.clear();
				if (result == null) {
					return;
				}
                poiInfos = result.getAllPoi();
				baiMap.clear();
				// 创建PoiOverlay
				PoiOverlay overlay = new MyPoiOverlay(baiMap);
				// 设置overlay可以处理标注点击事件
				baiMap.setOnMarkerClickListener(overlay);
				// 设置PoiOverlay数据
				overlay.setData(result);
				// 添加PoiOverlay到地图中
				overlay.addToMap();
				overlay.zoomToSpan();
				return;
			}
			
			@Override
			public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
				//获取Place详情页检索结果
				
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				
			}
		});
	}
	
	/**
	 * 搜索地点的方法
	 * @param v
	 */
	public void onBaiduSearchClick(View v){
		//第四步，发起检索请求
		//获取输入地点
		mStrLocation=getViewContent(mEtLocation);
		mPoiSearch.searchInCity(new PoiCitySearchOption().city("北京")
				.keyword(mEtLocation.getText().toString().trim()).pageNum(0));//注意分页
		
	}
	
	/**
	 * 构建自定义类
	 * @author 邢悦
	 *
	 */
	public class MyPoiOverlay extends PoiOverlay {
		private View view;
		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
		@Override  
	    public boolean onPoiClick(int index) {  
	        super.onPoiClick(index); 
	        //得到当前的搜索点
           final PoiInfo poiInfo= poiInfos.get(index);
	      //创建InfoWindow展示的view  
	      view=  getLayoutInflater().inflate(R.layout.baidu_poi, null);
	      //获取控件对象
	      TextView tvName=(TextView) view.findViewById(R.id.baidu_poi_tv_name);
	      TextView tvDesc=(TextView) view.findViewById(R.id.baidu_poi_tv_desc);
	      Button btnCancel=(Button) view.findViewById(R.id.baidu_poi_btn_cancel);
	      Button btnPick=(Button) view.findViewById(R.id.baidu_poi_btn_pick);
	      //控件获取数据
	      tvName.setText(poiInfo.name);
	      tvDesc.setText(poiInfo.address);
	      //控件按钮的监听事件
	      btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//隐藏覆盖物
				mBaiduMap.hideInfoWindow();
				
			}
		});
	      btnPick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取当前缓存的数据 回显到发布页面
				MyApplication.putPoiInfo("actionPoiInfo", poiInfo);
				//结束当前页面
				finish();
			}
		});
	      
	        //定义用于显示该InfoWindow的坐标点  
//	        LatLng pt = new LatLng(39.86923, 116.397428);  
	        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
	        InfoWindow mInfoWindow = new InfoWindow(view, poiInfo.location, -47);  
	        //显示InfoWindow  
	        baiMap.showInfoWindow(mInfoWindow);
	        return true;  
	    }  

	}

	
}
