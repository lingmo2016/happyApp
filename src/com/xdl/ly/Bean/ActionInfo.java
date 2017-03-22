package com.xdl.ly.Bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/**
 * 活动对象的Bean类
 *
 */
public class ActionInfo  extends BmobObject{
	private String actionClass;//活动类型  用户选择 ````
	private String actionTime;//活动开始时间   用户选择 ````
	private String actionName;//活动名称   用户输入 ````
	private String actionUserId;//活动发起用户Id 用来查询用户表  系统自动获取填入
	
	private String actionCity;//活动所在城市   用户选择/输入
	private String actionSite;//活动集合地点   用户选择/输入 
	private String actionRMB; //活动价格       用户输入 ````
	private String actionIntro;//活动详细介绍  用户输入 ````
	private String actionDesc;//活动简介
	
	private BmobGeoPoint location;//活动经纬度
//	private BDLocation location;//活动经纬度   用户选择/输入
	private List<String> praiseUsers;//记录点赞的用户名  //系统录入
//	private List<UserMsg> msgs;//用户评论内容   //系统录入
	
	private List<BmobFile> actionJPG;//活动图片    //用户上传````
	
	private List<String> paymentUserId;//参与付款的用户Id   系统录入
	private List<String> startUserId;//活动开始 ，到活动现场的用户Id   系统录入
	private Boolean flag;// 当前活动状态(是否开始)   系统录入
	
	private Integer saveNum; //收藏数量 注意：Integer对象类型（不能用int）
	
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
	 * 获取用户收藏的数量
	 * @return
	 */
	public Integer getSaveNum() {
		return saveNum;
	}
	/**
	 * 设置用户的收藏数量
	 * @param saveNum
	 */
	public void setSaveNum(Integer saveNum) {
		this.saveNum = saveNum;
	}
	
	
	
}
