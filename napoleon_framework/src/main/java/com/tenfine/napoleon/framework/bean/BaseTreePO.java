package com.tenfine.napoleon.framework.bean;

public abstract class BaseTreePO extends BasePO implements Queryable {

    private static final long serialVersionUID = 7501495418917718118L;

    protected String pid;

    protected String orderCode;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
  
