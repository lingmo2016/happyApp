package com.xdl.ly.Bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/**
 * ������Bean��
 *
 */
public class ActionInfo  extends BmobObject{
	private String actionClass;//�����  �û�ѡ�� ````
	private String actionTime;//���ʼʱ��   �û�ѡ�� ````
	private String actionName;//�����   �û����� ````
	private String actionUserId;//������û�Id ������ѯ�û���  ϵͳ�Զ���ȡ����
	
	private String actionCity;//����ڳ���   �û�ѡ��/����
	private String actionSite;//����ϵص�   �û�ѡ��/���� 
	private String actionRMB; //��۸�       �û����� ````
	private String actionIntro;//���ϸ����  �û����� ````
	private String actionDesc;//����
	
	private BmobGeoPoint location;//���γ��
//	private BDLocation location;//���γ��   �û�ѡ��/����
	private List<String> praiseUsers;//��¼���޵��û���  //ϵͳ¼��
//	private List<UserMsg> msgs;//�û���������   //ϵͳ¼��
	
	private List<BmobFile> actionJPG;//�ͼƬ    //�û��ϴ�````
	
	private List<String> paymentUserId;//���븶����û�Id   ϵͳ¼��
	private List<String> startUserId;//���ʼ ������ֳ����û�Id   ϵͳ¼��
	private Boolean flag;// ��ǰ�״̬(�Ƿ�ʼ)   ϵͳ¼��
	
	private Integer saveNum; //�ղ����� ע�⣺Integer�������ͣ�������int��
	
	public ActionInfo() {
		super();
	}
	public ActionInfo(String actionClass, String actionTime, String actionName,
			String actionUserId, String actionCity, String actionSite,
			String actionRMB, String actionIntro, List<String> praiseUsers,
			 List<BmobFile> actionJPG, List<String> paymentUserId,
			List<String> startUserId, Boolean flag) {
		super();
		this.actionClass = actionClass;
		this.actionTime = actionTime;
		this.actionName = actionName;
		this.actionUserId = actionUserId;
		this.actionCity = actionCity;
		this.actionSite = actionSite;
		this.actionRMB = actionRMB;
		this.actionIntro = actionIntro;
		this.praiseUsers = praiseUsers;
		this.actionJPG = (List<BmobFile>) actionJPG;
		this.paymentUserId = paymentUserId;
		this.startUserId = startUserId;
		this.flag = flag;
	}
	public String getActionClass() {
		return actionClass;
	}
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionUserId() {
		return actionUserId;
	}
	public void setActionUserId(String actionUserId) {
		this.actionUserId = actionUserId;
	}
	public String getActionCity() {
		return actionCity;
	}
	public void setActionCity(String actionCity) {
		this.actionCity = actionCity;
	}
	public String getActionSite() {
		return actionSite;
	}
	public void setActionSite(String actionSite) {
		this.actionSite = actionSite;
	}
	public String getActionRMB() {
		return actionRMB;
	}
	public void setActionRMB(String actionRMB) {
		this.actionRMB = actionRMB;
	}
	public String getActionIntro() {
		return actionIntro;
	}
	public void setActionIntro(String actionIntro) {
		this.actionIntro = actionIntro;
	}
	public List<String> getPraiseUsers() {
		return praiseUsers;
	}
	public void setPraiseUsers(List<String> praiseUsers) {
		this.praiseUsers = praiseUsers;
	}
	public List<BmobFile> getActionJPG() {
		return (List<BmobFile>) actionJPG;
	}
	public void setActionJPG(List<BmobFile> actionPicFiles) {
		this.actionJPG = (List<BmobFile>) actionPicFiles;
	}
	public List<String> getPaymentUserId() {
		return paymentUserId;
	}
	public void setPaymentUserId(List<String> paymentUserId) {
		this.paymentUserId = paymentUserId;
	}
	public List<String> getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(List<String> startUserId) {
		this.startUserId = startUserId;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "ActionInfo [actionClass=" + actionClass + ", actionTime="
				+ actionTime + ", actionName=" + actionName + ", actionUserId="
				+ actionUserId + ", actionCity=" + actionCity + ", actionSite="
				+ actionSite + ", actionRMB=" + actionRMB + ", actionIntro="
				+ actionIntro + ", praiseUsers=" + praiseUsers + ", actionJPG="
				+ actionJPG + ", paymentUserId=" + paymentUserId
				+ ", startUserId=" + startUserId + ", flag=" + flag + "]";
	}
	public String getActionDesc() {
		return actionDesc;
	}
	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	/**
	 * ��ȡ�û��ղص�����
	 * @return
	 */
	public Integer getSaveNum() {
		return saveNum;
	}
	/**
	 * �����û����ղ�����
	 * @param saveNum
	 */
	public void setSaveNum(Integer saveNum) {
		this.saveNum = saveNum;
	}
	
	
	
}
