package com.tdr.registration.model;

/**
 * 按钮跳转对象
 * Created by Linus_Xie on 2016/9/17.
 */
public class ItemModel {
    private String rolePower;//权限编号
    private String itemName;//该Item的名字
    private int itemBitResc;//本地图片资源
    private int size;//指令条数

    public String getRolePower() {
        return rolePower;
    }

    public void setRolePower(String rolePower) {
        this.rolePower = rolePower;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemBitResc() {
        return itemBitResc;
    }

    public void setItemBitResc(int itemBitResc) {
        this.itemBitResc = itemBitResc;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
