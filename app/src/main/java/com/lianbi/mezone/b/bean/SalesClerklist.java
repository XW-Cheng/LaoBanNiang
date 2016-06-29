package com.lianbi.mezone.b.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 店铺员工
 * 
 * @time 下午9:55:21
 * @date 2016-1-20
 * @author hongyu.yang
 * 
 */
public class SalesClerklist implements Serializable {
	ArrayList<SalesMan> salesclerkList;
	private static final long serialVersionUID = 9100547298770665548L;

	public ArrayList<SalesMan> getSalesclerkList() {
		return salesclerkList;
	}

	public void setSalesclerkList(ArrayList<SalesMan> salesclerkList) {
		this.salesclerkList = salesclerkList;
	}

}
