package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/10/15.
 */

public class ImageInfo implements Serializable {
    public String url;
    public float width;
    public float height;

    @Override
    public String toString() {
        return "ImageInfo{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
