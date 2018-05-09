package com.tdr.registration.model;

import java.io.Serializable;

/**
 * 除四张常规照片之外的照片
 * Created by Linus_Xie on 2016/9/22.
 */
public class PhotoModel implements Serializable {

    private String INDEX;
    private String Photo;
    private String PhotoFile;
    private String Remark;

    public String getINDEX() {
        return INDEX;
    }

    public void setINDEX(String INDEX) {
        this.INDEX = INDEX;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPhotoFile() {
        return PhotoFile;
    }

    public void setPhotoFile(String photoFile) {
        PhotoFile = photoFile;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return "PhotoModel{" +
                "INDEX='" + INDEX + '\'' +
                ", Photo='" + Photo + '\'' +
                ", PhotoFile='" + PhotoFile + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
