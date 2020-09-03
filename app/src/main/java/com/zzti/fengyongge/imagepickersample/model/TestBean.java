package com.zzti.fengyongge.imagepickersample.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TestBean implements Serializable {

    private  String  img_thumb = "";
    private List<String> pic=new ArrayList<>();


    public String getImg_thumb() {
        return img_thumb;
    }

    public void setImg_thumb(String img_thumb) {
        this.img_thumb = img_thumb;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }
}
