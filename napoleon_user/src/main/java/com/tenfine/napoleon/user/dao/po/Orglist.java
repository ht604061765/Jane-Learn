package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.bean.BasePO;

public class Orglist extends BasePO{
    private static final long serialVersionUID = -6926887442283591714L;

    private String orgname; //机构名称
    private String duty; //职责
    private String remark; //备注
    private String pid; //上级ID

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
