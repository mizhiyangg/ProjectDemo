package cn.itcast.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import cn.itcast.zhbj.MainActivity;
import cn.itcast.zhbj.R;

/**
 * 5个标签页的基类
 *
 * 子类都有标题栏，在父类直接加载布局页面
 */
public class BasePager {
    public Activity mActivity;
    public TextView tv_title;
    public FrameLayout flContainer;
    public ImageButton btn_menu;
    public View mRootView;
    public ImageButton btn_display;
    public BasePager(Activity activity){
        mActivity=activity;
        //在页面对象创建时就初始化了布局
        initViews();
    }

    public void initViews(){
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title=mRootView.findViewById(R.id.tv_title);
        btn_menu=mRootView.findViewById(R.id.btn_menu);
        flContainer=mRootView.findViewById(R.id.fl_container);
        btn_display= mRootView.findViewById(R.id.btn_display);
        //点击菜单按钮，控制侧边栏开关
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });

    }
    protected void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
    }

    public void initData(){

    }
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

}
