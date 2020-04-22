package cn.itcast.zhbj.base.impl.menudetail;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import cn.itcast.zhbj.MainActivity;
import cn.itcast.zhbj.R;
import cn.itcast.zhbj.base.BaseMenuDetailPager;
import cn.itcast.zhbj.base.impl.TabDetailPager;
import cn.itcast.zhbj.domain.NewsMenu;

//菜单详情页-新闻
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private  TabPageIndicator mIndicator;

    private ArrayList<NewsMenu.NewsTabData> children;

    private ArrayList<TabDetailPager> mPagers;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        this.children=children;
    }

    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
        ViewUtils.inject(this,view);
        return view;
    }

    public void initData(){
        //初始化12个页签
        //数量以服务器为准
        mPagers=new ArrayList<TabDetailPager>();
        for(int i=0;i<children.size();i++){
            TabDetailPager pager=new TabDetailPager(mActivity,children.get(i));//传递数据
            mPagers.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuDetailAdapter());

        mIndicator.setViewPager(mViewPager);//将ViewPager和Indicator关联起来

        //页面的切换监听
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("当前页面"+position);
                if(position==0){
                    //打开侧边栏
                    setSlidingMenuEnable(true);
                }else{
                    //禁用侧边栏
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class NewsMenuDetailAdapter extends PagerAdapter{

        //返回指示器的标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            TabDetailPager pager=mPagers.get(position);

            pager.initData();//初始化数据

            container.addView(pager.mRootView);

            return pager.mRootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
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
