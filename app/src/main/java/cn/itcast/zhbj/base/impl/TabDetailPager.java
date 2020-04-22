package cn.itcast.zhbj.base.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import android.os.Handler;

import cn.itcast.zhbj.NewsDetailActivity;
import cn.itcast.zhbj.R;
import cn.itcast.zhbj.base.BaseMenuDetailPager;
import cn.itcast.zhbj.domain.NewsMenu;
import cn.itcast.zhbj.domain.NewsTab;
import cn.itcast.zhbj.global.GlobalConstants;
import cn.itcast.zhbj.utils.CacheUtils;
import cn.itcast.zhbj.utils.PrefUtils;
import cn.itcast.zhbj.view.RefreshLishView;
import cn.itcast.zhbj.view.TopNewsViewPager;

//页签详情页 北京、中国、国际……
//继承BaseMenuDetailPager是因为从代码角度来讲，比较简洁，但当前页不属于菜单详情页，干爹
public class TabDetailPager extends BaseMenuDetailPager {

    private BitmapUtils mBitmapUtils;

    private String mMoreUrl;

    private ArrayList<NewsTab.TopNews>mTopNewsList;

    private ArrayList<NewsTab.News> mNewList;

    private NewsMenu.NewsTabData newsTabData;//当前页签的网络数据

    private NewsAdapter mNewsAdapter;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.lv_list)
    private RefreshLishView lv_list;

    private Handler mHandler;

    //private TextView view;

    @ViewInject(R.id.vp_tab_detail)
    private TopNewsViewPager mViewPager;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        this.newsTabData=newsTabData;
    }

    @Override
    public View initViews() {
        View view=View.inflate(mActivity,R.layout.pager_tab_detail,null);

        //加载头条新闻头布局
        View headerView=View.inflate(mActivity,R.layout.list_item_header,null);

        ViewUtils.inject(this,view);
        ViewUtils.inject(this,headerView);

        lv_list.addHeaderView(headerView);//给ListView添加头布局

        lv_list.setOnRefreshListener(new RefreshLishView.OnRefreshListener() {
            //设置下拉刷新监听
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if(mMoreUrl!=null){
                    getMoreDataFromServer();
                }else{
                    Toast.makeText(mActivity,"没有更多数据啦",Toast.LENGTH_SHORT).show();

                    lv_list.onRefreshComplete();
                }
            }
        });

        //设置点击事件
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //头布局也算位置，所有使用position时要讲头布局个数减掉
                int headerViewsCount=lv_list.getHeaderViewsCount();
                position-=headerViewsCount;

                NewsTab.News news=mNewList.get(position);

                //标记已读未读：将新闻id保存在sp中
                //key,value "read_ids=10000,10001,10002"

                String readIds=PrefUtils.getString(mActivity,"read_ids","");
                //判断之前是否已经保存过该id
                if(!readIds.contains(news.id)){
                    //在现有的id基础上追加新的id
                    readIds+=news.id+",";
                    //保存最新的ids
                    PrefUtils.putString(mActivity,"read_ids",readIds);
                }

                //刷新listview,全局刷新
                //mNewsAdapter.notifyDataSetChanged();
                //局部刷新
                TextView tvTitle=(TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                //跳到新闻详情页
                Intent intent=new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.url);//传递网络链接
                mActivity.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        //view.setText(newsTabData.title);//修改当前布局的数据
        String cache=CacheUtils.getCache(mActivity,GlobalConstants.SERVER_URL+newsTabData.url);
        if(!TextUtils.isEmpty(cache)){
            processData(cache,false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.SERVER_URL + newsTabData.url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;

                processData(result,false);

                CacheUtils.setCache(mActivity,GlobalConstants.SERVER_URL + newsTabData.url,result);

                //隐藏下拉刷新控件
                lv_list.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String msg) {
                e.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
                //隐藏下拉刷新控件
                lv_list.onRefreshComplete();
            }
        });
    }

    //请求下一页网络数据
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;

                processData(result,true);

                //隐藏下拉刷新控件
                lv_list.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String msg) {
                e.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                //隐藏下拉刷新控件
                lv_list.onRefreshComplete();
            }
        });
    }


    @SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
    protected void processData(String result, Boolean isMore) {
        Gson gson=new Gson();
        NewsTab newsTab=gson.fromJson(result, NewsTab.class);
        System.out.println("页签结果："+newsTab);

        //获取下一页数据的地址
        String more=newsTab.data.more;
        if(!TextUtils.isEmpty((more))){
            mMoreUrl=GlobalConstants.SERVER_URL+more;
        }else{
            mMoreUrl=null;
        }

        if(!isMore){

            //初始化头条新闻
            mTopNewsList=newsTab.data.topnews ;
            if(mTopNewsList!=null){
                mViewPager.setAdapter(new TopNewsAdapter());

                mIndicator.setViewPager(mViewPager);//将圆形指示器与viewpager绑定
                mIndicator.setSnap(true);//快照展示方式
                mIndicator.onPageSelected(0);//将原点位置归0 保证原点和页面同步

                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //更新头条标题
                        tv_title.setText(mTopNewsList.get(position).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //初始化第一页头条新闻标题
                tv_title.setText(mTopNewsList.get(0).title);

                //启动自动轮播效果
                if(mHandler==null){
                   mHandler=new Handler(){
                       @Override
                       public void handleMessage(@NonNull Message msg) {
                           super.handleMessage(msg);
                           int currentItem=mViewPager.getCurrentItem();
                           if(currentItem<mTopNewsList.size()-1){
                               currentItem++;
                           }else{
                               currentItem=0;
                           }
                           mViewPager.setCurrentItem(currentItem);
                           mHandler.sendEmptyMessageDelayed(0,2000);
                       }
                   };
                   //发送延迟消息，启动自动轮播
                   mHandler.sendEmptyMessageDelayed(0,2000);

                }
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                //事件取消
                                mHandler.sendEmptyMessageDelayed(0,2000);
                                break;
                            case MotionEvent.ACTION_UP:
                                //发送演示消息，启动自动轮播
                                mHandler.sendEmptyMessageDelayed(0,2000);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
            mNewList=newsTab.data.news;
            if(mNewList!=null){
                mNewsAdapter=new NewsAdapter();
                lv_list.setAdapter(mNewsAdapter);
            }
        }else{
            //加载更多
            ArrayList<NewsTab.News> moreNews=newsTab.data.news;
            mNewList.addAll(moreNews);//追加更多数据

            //刷新listview
            mNewsAdapter.notifyDataSetChanged();
        }
    }


    //头条新闻的数据适配器
    class TopNewsAdapter extends PagerAdapter{

        //加载图片的工具类
        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter(){
            mBitmapUtils=new BitmapUtils(mActivity);
            //mBitmapUtils.configDefaultLoadingImage(tupian); 加载时图片
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view=new ImageView(mActivity);

            NewsTab.TopNews topNews=mTopNewsList.get(position);
            String topimage=topNews.topimage;//图片的下载链接

            view.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放模式，图片宽高匹配

            //根据url下载图片 将图片设置给imageview 图片的缓存 避免内存溢出
            //BitmapUtils:xUtils
            mBitmapUtils.display(view,topimage);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    //新闻列表适配器
    class NewsAdapter extends BaseAdapter{

        public NewsAdapter(){
            mBitmapUtils=new BitmapUtils(mActivity);
            //mBitmapUtils.configDefaultLoadingImage(R.drawable.new);默认加载图片
        }

        @Override
        public int getCount() {
            return mNewList.size();
        }

        @Override
        public NewsTab.News getItem(int position) {
            return mNewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_item,null);
                holder=new ViewHolder();
                holder.iv_Icon=(ImageView)convertView.findViewById(R.id.iv_icon);
                holder.tv_Title=(TextView)convertView.findViewById(R.id.tv_title);
                holder.tv_Time=(TextView)convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            NewsTab.News info=getItem(position);

            holder.tv_Title.setText(info.title);
            holder.tv_Time.setText(info.pubdate);

            mBitmapUtils.display(holder.iv_Icon,info.listimage);

            //判断已读
            String readIds=PrefUtils.getString(mActivity,"read_ids","");
            if(readIds.contains(info.id)){
                holder.tv_Title.setTextColor(Color.GRAY);
            }else{
                holder.tv_Title.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView iv_Icon;
        public TextView tv_Time;
        public TextView tv_Title;
    }
}
