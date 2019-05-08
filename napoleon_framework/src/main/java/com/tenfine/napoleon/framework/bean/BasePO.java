package com.tenfine.napoleon.framework.bean;

public abstract class BasePO extends BaseBean implements Queryable {

    private static final long serialVersionUID = 7501495418917718118L;

    protected String id;

    protected String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
  
