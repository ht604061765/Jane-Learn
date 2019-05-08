package com.tenfine.napoleon.framework.bean;

import java.util.List;

/**
 * 分页对象
 */
public class Pager<T> extends BaseBean {

    /** 
	 * serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 1L;
	
	private int pageNo;     	// 页面编号
    private int pageSize;       // 每页条数
    private int totalRecord;   	// 总记录数
    private int totalPage;     	// 总页面数
    private List<T> recordList; // 数据列表
    
    public Pager(int pageNo, int pageSize, int totalRecord, List<T> recordList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.recordList = recordList;
        if (pageSize != 0) {
            totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    // ----------------------------------------------------------------------------------------------------

    public boolean isFirstPage() {
        return pageNo == 1;
    }

    public boolean isLastPage() {
        return pageNo == totalPage;
    }

    public boolean isPrevPage() {
        return pageNo > 1 && pageNo <= totalPage;
    }

    public boolean isNextPage() {
        return pageNo < totalPage;
    }
}

