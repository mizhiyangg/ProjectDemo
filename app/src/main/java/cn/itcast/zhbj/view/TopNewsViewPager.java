package cn.itcast.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class TopNewsViewPager extends ViewPager {
    private int startX;
    private int startY;
    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    //分情况拦截 上下滑动需要拦截
    //左滑时最后一个页面拦截
    //右滑时第一个页面拦截
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求不拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //按下
                startX=(int) ev.getX();
                startY=(int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                int endX=(int) ev.getX();
                int endY=(int) ev.getY();

                int dx=endX-startX;
                int dy=endY-startY;
                if(Math.abs(dx)>Math.abs(dy)){
                    //左右滑动
                    int currentItem=getCurrentItem();
                    if(dx>0){
                        //右滑
                        if(currentItem==0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //左滑
                        int count=getAdapter().getCount();//当前viewpager item总数
                        if(currentItem==count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else{
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
