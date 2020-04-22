package cn.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.itcast.zhbj.base.BasePager;

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("设置初始化---");
        //给空的帧布局动态添加布局对象


        btn_menu.setVisibility(View.GONE);// 隐藏菜单按钮
        setSlidingMenuEnable(false);// 关闭侧边栏

        TextView view=new TextView(mActivity);
        view.setTextSize(25);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        view.setText("设置");

        flContainer.addView(view);//给帧布局添加对象

        //修改标题
        tv_title.setText("设置");
    }
}
