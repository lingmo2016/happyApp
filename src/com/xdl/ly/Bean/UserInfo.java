package com.xdl.ly.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class UserInfo extends BmobUser {

	private String nick; //�û��ǳ�
	private BmobFile userHead; //�û�ͷ�� 
	private String pwd2; //�û�������
	private List<String> praiseAction;//��ǰ�û��ղصĻ
	public UserInfo() {
		super();
	}
	public UserInfo(String nick, BmobFile userHead, String pwd2) {
		super();
		this.nick = nick;
		this.userHead = userHead;
		this.pwd2=pwd2;
	}
	
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public BmobFile getUserHead() {
		return userHead;
	}
	public void setUserHead(BmobFile userHead) {
		this.userHead = userHead;
	}
	@Override
	public String toString() {
		return "UserInfo [nick=" + nick + ", userHead=" + userHead + ", pwd2="
				+ pwd2 + "]";
	}
	public List<String> getPraiseAction() {
		if (praiseAction==null) {
			praiseAction=new ArrayList<String>();
		}
		return praiseAction;
	}
	public void setPraiseAction(List<String> praiseAction) {
		this.praiseAction = praiseAction;
	}
	
	
}
