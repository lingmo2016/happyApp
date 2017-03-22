package com.xdl.ly.Utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.application.MyApplication;

/**
 * 从服务器获取活动数据的工具类
 * 
 * @author Administrator
 * 
 */
public class FindActionInfoUtils {

	/**
	 * 从服务器获取数据的方法
	 * 
	 * @param type
	 *            查询类型
	 * 
	 *            type=1.默认查询当前服务器中的活动表10条，当正常从服务器获取数据、刷新操作时
	 *            type=2.使用参数3，查询count条数据，跳过skip条，加载更多时使用
	 *            type=3.结合参数2，查询一个活动（参数2，活动的id）
	 *            type=4.结合参数2，查询所有活动（参数2，活动的类型）
	 *            type=5.结合参数2，查询所有免费活动（参数2，价格0）
	 *            type=6  结合参数2，查询包含指定关键字的活动（参数2，用户输入的关键字）
	 * @param data
	 * @param skip
	 * @param count
	 * @param listener
	 */
	public static void findAllActionInfos(int type, Object data, int skip, // 忽略前n条数据（即第一页数据结果）
			int count, // 查询的条数(默认10条)
			final FindActionInfoListener listener) {
		BmobQuery<ActionInfo> query = new BmobQuery<ActionInfo>();
		switch (type) {
		case 1:
			// 正常查询(默认10条)
			query.setLimit(10);
			break;

		case 2:
			query.setLimit(count);
			query.setSkip(skip);
			break;
		case 3:
			//查询一条数据
			query.addWhereEqualTo("objectId", data);
			break;
		case 4:
			//查询同一类型的所有数据
			query.addWhereEqualTo("actionClass", data);
			break;
		case 5:
			//查询所有免费的数据
			query.addWhereEqualTo("actionRMB", data);
			break;
		case 6:
			//查询包含指定关键字的数据
//			query.addWhereContains("actionName",(String)data); 模糊查询 收费
			//准确查询
			query.addWhereEqualTo("actionName",(String)data);
			break;
		case 7:
			//根据距离查找
			/**
			 * 参数一：表中的位置字段
			 * 参数二：用户当前的位置
			 */
			query.addWhereNear("location",(BmobGeoPoint) data);
			break;
			
		case 8:
			//按收藏数即热度查询
			//根据该字段的降序显示数据
			query.order("-saveNum,-createdAt");
			
			break;
		}
		query.findObjects(new FindListener<ActionInfo>() {

			@Override
			public void done(List<ActionInfo> info, BmobException ex) {
				listener.getActionInfo(info, ex);

			}
		});
	}

	/**
	 * 回调接口
	 * 
	 */
	public interface FindActionInfoListener {
		void getActionInfo(List<ActionInfo> info, BmobException ex);
	}
}
