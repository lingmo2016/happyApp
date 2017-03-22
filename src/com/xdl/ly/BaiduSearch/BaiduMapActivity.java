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
 * ������Կ��bo9MPiw6LGfCtOGGCeupxYu6CddtrwB2
 * 
 * @author ����
 * 
 */
public class BaiduMapActivity extends BaseActivity implements BaseInterface {

	private MapView mBaiMapView; // ��ͼ�ؼ�
	private EditText mEtLocation; // ������ؼ�
	private ImageView mSearch; // �����ؼ�
	private BaiduMap baiMap;// �ٶȵ�ͼ������
	private PoiSearch mPoiSearch;//POI����
    private String mStrLocation;//�����ĵص�
	private List<PoiInfo> poiInfos; //�����Ľ����
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
		// ��ʼ���ٶȵ�ͼ
		mBaiMapView = (MapView) viewById(R.id.baidu_search_bmapView);
		mEtLocation = (EditText) viewById(R.id.baidu_search_et_chazhao);
		mSearch = (ImageView) viewById(R.id.baidu_search_iv_sousuo);
		// ��ȡ��ͼ�ؼ�
		baiMap = mBaiMapView.getMap();
	}

	@Override
	public void initDatas() {
		
	}

	/**
	 * ��һ��������POI����ʵ�� mPoiSearch = PoiSearch.newInstance(); �ڶ���������POI���������ߣ�
	 * OnGetPoiSearchResultListener poiListener = new
	 * OnGetPoiSearchResultListener(){ public void onGetPoiResult(PoiResult
	 * result){ //��ȡPOI������� } public void onGetPoiDetailResult(PoiDetailResult
	 * result){ //��ȡPlace����ҳ������� } }; ������������POI���������ߣ�
	 * mPoiSearch.setOnGetPoiSearchResultListener(poiListener); ���Ĳ��������������
	 * mPoiSearch.searchInCity((new PoiCitySearchOption()) .city(��������)
	 * .keyword(����ʳ��) .pageNum(10)); ���岽���ͷ�POI����ʵ���� mPoiSearch.destroy();
	 */
	@Override
	public void initOperas() {
		// ��ͨ��ͼ
	    baiMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	    //  ��һ��������POI����ʵ��
	    mPoiSearch=  PoiSearch.newInstance();
	    //�ڶ���������POI����������
		//������������POI����������
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				//��ȡPOI�������
//				if (result==null) {
//					toast3s("δ�����������������룡");
//					return;
//				}else {
//					//����������ŵ�������
//					List<PoiInfo> resultPoInfos=result.getAllPoi();
//					PoiInfo poiInfo=resultPoInfos.get(0);
//					StringBuffer sb=new StringBuffer();
//					sb.append(poiInfo.name+",λ�ã�"+poiInfo.address
//							+"����ϵ�绰:"
//							+poiInfo.phoneNum);
//					Log.i("myTag", sb.toString());
//				}
//				baiMap.clear();
				if (result == null) {
					return;
				}
                poiInfos = result.getAllPoi();
				baiMap.clear();
				// ����PoiOverlay
				PoiOverlay overlay = new MyPoiOverlay(baiMap);
				// ����overlay���Դ����ע����¼�
				baiMap.setOnMarkerClickListener(overlay);
				// ����PoiOverlay����
				overlay.setData(result);
				// ���PoiOverlay����ͼ��
				overlay.addToMap();
				overlay.zoomToSpan();
				return;
			}
			
			@Override
			public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
				//��ȡPlace����ҳ�������
				
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				
			}
		});
	}
	
	/**
	 * �����ص�ķ���
	 * @param v
	 */
	public void onBaiduSearchClick(View v){
		//���Ĳ��������������
		//��ȡ����ص�
		mStrLocation=getViewContent(mEtLocation);
		mPoiSearch.searchInCity(new PoiCitySearchOption().city("����")
				.keyword(mEtLocation.getText().toString().trim()).pageNum(0));//ע���ҳ
		
	}
	
	/**
	 * �����Զ�����
	 * @author ����
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
	        //�õ���ǰ��������
           final PoiInfo poiInfo= poiInfos.get(index);
	      //����InfoWindowչʾ��view  
	      view=  getLayoutInflater().inflate(R.layout.baidu_poi, null);
	      //��ȡ�ؼ�����
	      TextView tvName=(TextView) view.findViewById(R.id.baidu_poi_tv_name);
	      TextView tvDesc=(TextView) view.findViewById(R.id.baidu_poi_tv_desc);
	      Button btnCancel=(Button) view.findViewById(R.id.baidu_poi_btn_cancel);
	      Button btnPick=(Button) view.findViewById(R.id.baidu_poi_btn_pick);
	      //�ؼ���ȡ����
	      tvName.setText(poiInfo.name);
	      tvDesc.setText(poiInfo.address);
	      //�ؼ���ť�ļ����¼�
	      btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//���ظ�����
				mBaiduMap.hideInfoWindow();
				
			}
		});
	      btnPick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ȡ��ǰ��������� ���Ե�����ҳ��
				MyApplication.putPoiInfo("actionPoiInfo", poiInfo);
				//������ǰҳ��
				finish();
			}
		});
	      
	        //����������ʾ��InfoWindow�������  
//	        LatLng pt = new LatLng(39.86923, 116.397428);  
	        //����InfoWindow , ���� view�� �������꣬ y ��ƫ���� 
	        InfoWindow mInfoWindow = new InfoWindow(view, poiInfo.location, -47);  
	        //��ʾInfoWindow  
	        baiMap.showInfoWindow(mInfoWindow);
	        return true;  
	    }  

	}

	
}
