package cn.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import cn.itcast.zhbj.base.BasePager;

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("智慧服务初始化---");
        //给空的帧布局动态添加布局对象

        tv_title.setText("生活");
        setSlidingMenuEnable(true);// 打开侧边栏

        TextView view=new TextView(mActivity);
        view.setTextSize(25);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        view.setText("智慧服务");

        flContainer.addView(view);//给帧布局添加对象

    }
}
