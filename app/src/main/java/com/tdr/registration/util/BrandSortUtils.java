package com.tdr.registration.util;

import com.tdr.registration.model.SortModel;

import java.util.Comparator;

/**
 * 根据sort排序
 * Created by Linus_Xie on 2016/11/29.
 */

public class BrandSortUtils implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int firstSort = Integer.valueOf(((SortModel) o1).getSort());
        int secondSort = Integer.valueOf(((SortModel) o2).getSort());
        int dif = secondSort - firstSort;
        if (dif >0){
            return 1;
        } else if (dif <0){
            return  -1;
        } else {
            return 0;
        }
    }
}
