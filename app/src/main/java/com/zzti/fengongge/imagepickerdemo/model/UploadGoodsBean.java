package com.zzti.fengongge.imagepickerdemo.model;



import java.io.Serializable;


public class UploadGoodsBean implements Serializable {
    private String url;
    private Boolean isNet;

    public UploadGoodsBean() {
        super();
    }
    public UploadGoodsBean(String url, Boolean isNet) {
        super();
        this.url = url;
        this.isNet = isNet;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Boolean getIsNet() {
        return isNet;
    }
    public void setIsNet(Boolean isNet) {
        this.isNet = isNet;
    }



}
