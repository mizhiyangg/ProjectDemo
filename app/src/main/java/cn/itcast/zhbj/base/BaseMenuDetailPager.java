package cn.itcast.zhbj.base;

import android.app.Activity;
import android.view.View;

//菜单详情页基类，新闻专题组图互动
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView=initViews();
    }

    //必须子类实现
    public abstract View initViews();

    public void initData(){

    }
}
