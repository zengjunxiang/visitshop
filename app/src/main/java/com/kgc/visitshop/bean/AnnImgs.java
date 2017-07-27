package com.kgc.visitshop.bean;

import org.litepal.crud.DataSupport;

public class AnnImgs extends DataSupport {
    private int id;
    private String imgUrl;

    @Override
    public String toString() {
        return "Body{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
