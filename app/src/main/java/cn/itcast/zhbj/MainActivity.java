package cn.itcast.zhbj;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.itcast.zhbj.fragment.ContentFragment;
import cn.itcast.zhbj.fragment.LeftMenuFragment;


public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_CONTENT ="TAG_CONTENT" ;
    private static final String TAG_LEFT ="TAG_LEFT" ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //设置侧边栏的布局
        setBehindContentView(R.layout.left_menu);

        //获取当前侧边栏对象
        SlidingMenu slidingMenu=getSlidingMenu();
        //设置触摸模式为全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置宽度
        //slidingMenu.setBehindOffset(250);
        slidingMenu.setBehindWidth(550);
        initFragment();
    }

    //初始化Fragment
    private void initFragment(){
        //获取fragment管理器
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();//开启一个事务
        transaction.replace(R.id.fl_content,new ContentFragment(),TAG_CONTENT);//使用fragment替换现有布局
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT);
        transaction.commit();

        //通过tag找到fragment
        //ContentFragment fragment=(ContentFragment)fm.findFragmentByTag(TAG_CONTENT);
    }

    //获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm=getSupportFragmentManager();
        LeftMenuFragment fragment=(LeftMenuFragment)fm.findFragmentByTag(TAG_LEFT);
        return fragment;
    }

    //获取主页fragment对象
    public ContentFragment getContentFragment(){
        FragmentManager fm=getSupportFragmentManager();
        ContentFragment fragment=(ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }

}
