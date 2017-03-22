package com.xdl.ly.Utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.xdl.ly.Bean.ActionInfo;
import com.xdl.ly.application.MyApplication;

/**
 * �ӷ�������ȡ����ݵĹ�����
 * 
 * @author Administrator
 * 
 */
public class FindActionInfoUtils {

	/**
	 * �ӷ�������ȡ���ݵķ���
	 * 
	 * @param type
	 *            ��ѯ����
	 * 
	 *            type=1.Ĭ�ϲ�ѯ��ǰ�������еĻ��10�����������ӷ�������ȡ���ݡ�ˢ�²���ʱ
	 *            type=2.ʹ�ò���3����ѯcount�����ݣ�����skip�������ظ���ʱʹ��
	 *            type=3.��ϲ���2����ѯһ���������2�����id��
	 *            type=4.��ϲ���2����ѯ���л������2��������ͣ�
	 *            type=5.��ϲ���2����ѯ������ѻ������2���۸�0��
	 *            type=6  ��ϲ���2����ѯ����ָ���ؼ��ֵĻ������2���û�����Ĺؼ��֣�
	 * @param data
	 * @param skip
	 * @param count
	 * @param listener
	 */
	public static void findAllActionInfos(int type, Object data, int skip, // ����ǰn�����ݣ�����һҳ���ݽ����
			int count, // ��ѯ������(Ĭ��10��)
			final FindActionInfoListener listener) {
		BmobQuery<ActionInfo> query = new BmobQuery<ActionInfo>();
		switch (type) {
		case 1:
			// ������ѯ(Ĭ��10��)
			query.setLimit(10);
			break;

		case 2:
			query.setLimit(count);
			query.setSkip(skip);
			break;
		case 3:
			//��ѯһ������
			query.addWhereEqualTo("objectId", data);
			break;
		case 4:
			//��ѯͬһ���͵���������
			query.addWhereEqualTo("actionClass", data);
			break;
		case 5:
			//��ѯ������ѵ�����
			query.addWhereEqualTo("actionRMB", data);
			break;
		case 6:
			//��ѯ����ָ���ؼ��ֵ�����
//			query.addWhereContains("actionName",(String)data); ģ����ѯ �շ�
			//׼ȷ��ѯ
			query.addWhereEqualTo("actionName",(String)data);
			break;
		case 7:
			//���ݾ������
			/**
			 * ����һ�����е�λ���ֶ�
			 * ���������û���ǰ��λ��
			 */
			query.addWhereNear("location",(BmobGeoPoint) data);
			break;
			
		case 8:
			//���ղ������ȶȲ�ѯ
			//���ݸ��ֶεĽ�����ʾ����
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
	 * �ص��ӿ�
	 * 
	 */
	public interface FindActionInfoListener {
		void getActionInfo(List<ActionInfo> info, BmobException ex);
	}
}
