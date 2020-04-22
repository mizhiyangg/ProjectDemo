package cn.itcast.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


import java.util.ArrayList;

import cn.itcast.zhbj.MainActivity;
import cn.itcast.zhbj.base.BaseMenuDetailPager;
import cn.itcast.zhbj.base.BasePager;
import cn.itcast.zhbj.base.impl.menudetail.InteractMenuDetailPager;
import cn.itcast.zhbj.base.impl.menudetail.NewsMenuDetailPager;
import cn.itcast.zhbj.base.impl.menudetail.PhotosMenuDetailPager;
import cn.itcast.zhbj.base.impl.menudetail.TopicMenuDetailPager;
import cn.itcast.zhbj.domain.NewsMenu;
import cn.itcast.zhbj.fragment.LeftMenuFragment;
import cn.itcast.zhbj.global.GlobalConstants;
import cn.itcast.zhbj.utils.CacheUtils;

public class NewsCenterPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mPagers;
    private  NewsMenu mNewsMenu;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("新闻中心初始化---");
        //给空的帧布局动态添加布局对象

        setSlidingMenuEnable(true);// 打开侧边栏

        //TextView view=new TextView(mActivity);
        //view.setTextSize(25);
        //view.setTextColor(Color.RED);
        //view.setGravity(Gravity.CENTER);
        //view.setText("新闻中心");

        //flContainer.addView(view);//给帧布局添加对象

        //修改标题
        tv_title.setText("新闻");

        String cache=CacheUtils.getCache(mActivity,GlobalConstants.CATEGORY_URL);
        if(!TextUtils.isEmpty(cache)){
            System.out.println("发现缓存");
            //有缓存
            processData(cache);
        }
        //else{
            //从服务器获取数据
            //getDataFromServer();
        //}
        //继续请求服务器数据，保存缓存最新
        getDataFromServer();
    }

    //从服务器获取数据,加网络权限
    private void getDataFromServer() {
        //XUtils
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL,
                new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                System.out.println("服务器分类数据"+result);

                processData(result);

                //写缓存
                CacheUtils.setCache(mActivity,GlobalConstants.CATEGORY_URL,result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void processData(String json) {
        //setMenuDetailPager(0);
        Gson gson=new Gson();
        //通过json和对象类，生成一个对象
        mNewsMenu=gson.fromJson(json, NewsMenu.class);
        System.out.println("解析的结果："+mNewsMenu);

        //找到侧边栏对象
        MainActivity mainUI=(MainActivity) mActivity;
        LeftMenuFragment fragment=mainUI.getLeftMenuFragment();
        fragment.setMenuData(mNewsMenu.data);

        //网络请求成功之后，初始化四个菜单详情页
        mPagers=new ArrayList<BaseMenuDetailPager>();
        //通过修改构造方法传递数据
        mPagers.add(new NewsMenuDetailPager(mActivity,mNewsMenu.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotosMenuDetailPager(mActivity,btn_display));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        //设置新闻菜单详情页为默认页面
        setMenuDetailPager(0);
    }

    //修改菜单详情页
    public void setMenuDetailPager(int position) {
        System.out.println("新闻中心要修改菜单详情页啦："+position);

        BaseMenuDetailPager pager=mPagers.get(position);

        //判断是否是组图，如果是，显示切换按钮，否则隐藏
        if(pager instanceof PhotosMenuDetailPager){
            btn_display.setVisibility(View.VISIBLE);
        }else{
            btn_display.setVisibility(View.GONE);
        }

        //清楚之前的帧布局显示的内容
        flContainer.removeAllViews();//删除所有布局

        //修改当前帧布局显示的内容
        flContainer.addView(pager.mRootView);
        //初始化当前页面的数据
        pager.initData();

        //修改标题
        tv_title.setText(mNewsMenu.data.get(position).title);
    }
}
