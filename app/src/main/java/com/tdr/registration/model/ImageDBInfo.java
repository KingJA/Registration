package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/10/15.
 */

public class ImageDBInfo implements Serializable {
    public float x;
    public float y;
    public float width;
    public float height;

    @Override
    public String toString() {
        return "ImageBDInfo{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
