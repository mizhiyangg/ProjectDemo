package cn.itcast.zhbj.domain;

import java.util.ArrayList;

public class PhotosBean {

    public PhotosData data;

    public class PhotosData{
        public ArrayList<PhotoNews> news;

    }

    public class PhotoNews{
        public String title;
        public String listimage;
    }
}
