package cn.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import cn.itcast.zhbj.base.BasePager;

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("政务初始化---");
        //给空的帧布局动态添加布局对象

        TextView view=new TextView(mActivity);
        view.setTextSize(25);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        view.setText("政务");

        flContainer.addView(view);//给帧布局添加对象

        //修改标题
        tv_title.setText("人口管理");

    }
}
