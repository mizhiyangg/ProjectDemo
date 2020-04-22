package cn.itcast.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.itcast.zhbj.MainActivity;
import cn.itcast.zhbj.R;
import cn.itcast.zhbj.base.impl.NewsCenterPager;
import cn.itcast.zhbj.domain.NewsMenu;

public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsMenu.NewsMenuData> data;//分类的网络数据
    private int mCurrentPos;//当前选中的菜单位置
    private LeftMenuAdapter mAdpater;
    @ViewInject(R.id.lv_menu)
    private ListView lv_List;

    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.fragment_left_menu,null);
        ViewUtils.inject(this,view);
        return view;
    }

    //设置侧边栏数据的方法
    //从新闻中心页面将网络数据传递过来
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        System.out.println("侧边栏拿到数据啦："+data.size());

        //将当前选中位置归0，避免侧边栏选中位置和菜单详情页不同步
        mCurrentPos=0;

        this.data=data;
        mAdpater=new LeftMenuAdapter();
        lv_List.setAdapter(mAdpater);
        lv_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos=position;//更新当前点击位置
                //刷新ListView
                mAdpater.notifyDataSetChanged();

                //收回侧边栏
                toggle();
                setMenuDetailPager(position);
                //修改新闻中心的帧布局
            }
        });
    }

    //修改菜单详情页
    protected void setMenuDetailPager(int position) {
        //修改新闻中心的帧布局
        //在侧边栏的布局 要先获取新闻中心得对象
        MainActivity mainUI=(MainActivity) mActivity;
        ContentFragment fragment=mainUI.getContentFragment();
        NewsCenterPager pager=fragment.getNewsCenterPager();

        //由新闻中心修改菜单详情页
        pager.setMenuDetailPager(position);
    }

    //控制侧边栏的开关
    protected void toggle() {
        MainActivity mainUI=(MainActivity) mActivity;
        SlidingMenu slidingMenu=mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前为开，则关；反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter{
        //服务器上有几个返回几个
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mActivity,R.layout.list_item_left_menu,null);

            TextView tv_menu=view.findViewById(R.id.tv_menu);

            //设置Textview的可用或不可用来控制颜色
            if(mCurrentPos==position){
                //当前item被选中
                tv_menu.setEnabled(true);
            }else{
                //未选中
                tv_menu.setEnabled(false);
            }
            NewsMenu.NewsMenuData info=getItem(position);
            tv_menu.setText(info.title);
            return view;
        }
    }
}
