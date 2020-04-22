package cn.itcast.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写父类 此处什么都不做，从而达到禁用时间的目的
        return true;
    }


    //事件的中断，拦截
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        //true表示拦截，false表示不拦截，传给子控件
        return false;
    }
}
