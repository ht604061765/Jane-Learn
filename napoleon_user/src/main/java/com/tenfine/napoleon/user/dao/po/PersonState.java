package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class PersonState extends BasePO{

	private static final long serialVersionUID = -1431420811439909010L;
	
	private String personId; //人员ID
	private String personName; //人员名称
	private String state; //同步状态，是否已同步，1：是，0：否
	private Integer cardFront; //是否写入身份证正面，1：写入，0：未写入
	private Integer cardBack; //是否写入身份证背面，1：写入，0：未写入
	private Integer colletImg; //是否写入采集照片，1：写入，0：未写入
	private Integer pushTime; //推送次数
	
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getCardFront() {
		return cardFront;
	}
	public void setCardFront(Integer cardFront) {
		this.cardFront = cardFront;
	}
	public Integer getCardBack() {
		return cardBack;
	}
	public void setCardBack(Integer cardBack) {
		this.cardBack = cardBack;
	}
	public Integer getColletImg() {
		return colletImg;
	}
	public void setColletImg(Integer colletImg) {
		this.colletImg = colletImg;
	}
	public Integer getPushTime() {
		return pushTime;
	}
	public void setPushTime(Integer pushTime) {
		this.pushTime = pushTime;
	}

}
