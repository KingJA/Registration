package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/8/7.
 */
public class SortModel implements Serializable {

    private static final long serialVersionUID = 2023957613200683483L;

    private String guid;// 数据ID
    private String name; // 显示的数据
    private String sortLetters; // 显示数据拼音的首字母
    private String sort;//热门品牌

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public SortModel() {
    }

    public SortModel(String name, String sortLetters) {
        this.name = name;
        this.sortLetters = sortLetters;
    }
}
