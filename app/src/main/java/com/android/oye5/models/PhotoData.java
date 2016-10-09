package com.android.oye5.models;

import java.io.Serializable;

public class PhotoData implements Serializable {
    private String path = "";
    private int resId = 0;

    public PhotoData() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
