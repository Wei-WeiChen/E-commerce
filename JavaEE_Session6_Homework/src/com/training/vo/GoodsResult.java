package com.training.vo;

import java.util.Set;

import com.training.model.Goods;

public class GoodsResult {
	private Set<Goods> goods;
    private int totalRecords;

    public GoodsResult() {
		super();
	}

	public GoodsResult(Set<Goods> goods, int totalRecords) {
        this.goods = goods;
        this.totalRecords = totalRecords;
    }

    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
