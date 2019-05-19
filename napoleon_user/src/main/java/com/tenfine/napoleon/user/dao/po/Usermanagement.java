package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Usermanagement extends BasePO {
    private static final long serialVersionUID = -6926887442283591714L;

    private String username; //用户名称
    private String phone; //手机号码
    private String account; //用户账号
    private String password; //用户密码
    private String sex; //性别
    private String remark; //备注
    private String job; //职业
    private String orgId; //orgId

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
