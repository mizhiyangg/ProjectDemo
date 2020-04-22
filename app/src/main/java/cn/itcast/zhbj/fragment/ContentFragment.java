package cn.itcast.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import cn.itcast.zhbj.MainActivity;
import cn.itcast.zhbj.R;
import cn.itcast.zhbj.base.BasePager;
import cn.itcast.zhbj.base.impl.GovAffairsPager;
import cn.itcast.zhbj.base.impl.HomePager;
import cn.itcast.zhbj.base.impl.NewsCenterPager;
import cn.itcast.zhbj.base.impl.SettingPager;
import cn.itcast.zhbj.base.impl.SmartServicePager;
import cn.itcast.zhbj.view.NoScrollViewPager;

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mList;//5个标签页的集合
    private RadioGroup rg_group;
    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.fragment_content,null);
        mViewPager=view.findViewById(R.id.vp_content);
        rg_group=view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        //初始化5个标签页面对象
        mList=new ArrayList<BasePager>();
        mList.add(new HomePager(mActivity));
        mList.add(new NewsCenterPager(mActivity));
        mList.add(new SmartServicePager(mActivity));
        mList.add(new GovAffairsPager(mActivity));
        mList.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());


        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //监听
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        //mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0,false);//去掉页面切换的动画
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_seeting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });

        //监听viewpager页面的也换事件，初始化当前页面数据
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager=mList.get(position);
                pager.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动初始化第一个页面的数据
        mList.get(0).initData();
        setSlidingMenuEnable(false);

    }

    //开启/禁用侧边栏
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //获取当前页面的对象
            BasePager pager=mList.get(position);

            //此方法导致每次都提前加载下一页数据浪费流量和性能，不建议在此处初始化数据
            //pager.initData();//初始化布局,以子类实现为准

            //获取布局对象
            //pager.mRootView当前页面的根布局
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    //获取新闻中心对象
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager=(NewsCenterPager) mList.get(1);//新闻中心在集合的第二个位置
        return pager;
    }
}
