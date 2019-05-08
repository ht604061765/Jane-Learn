package com.tenfine.napoleon.framework.bean;

import java.io.Serializable;
import java.util.List;

public class TreeGrid<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public TreeGrid(int total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public TreeGrid(int total, List<T> rows, List<T> footer) {
        this.total = total;
        this.rows = rows;
        this.footer = footer;
    }

    private int total; // 总记录数

    private List<T> rows; // 记录

    private List<T> footer; // 页脚汇总

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<T> getFooter() {
        return footer;
    }

    public void setFooter(List<T> footer) {
        this.footer = footer;
    }
}

