package cn.itcast.zhbj.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.itcast.zhbj.R;
import cn.itcast.zhbj.base.BaseMenuDetailPager;
import cn.itcast.zhbj.domain.PhotosBean;
import cn.itcast.zhbj.global.GlobalConstants;
import cn.itcast.zhbj.utils.CacheUtils;

//菜单详情页-组图
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    @ViewInject(R.id.gv_list)
    private GridView gvList;

    private ImageButton btn_display;

    private ArrayList<PhotosBean.PhotoNews> mPhotoList;

    public PhotosMenuDetailPager(Activity activity, ImageButton btn_display) {
        super(activity);
        this.btn_display=btn_display;
        btn_display.setOnClickListener(this);
    }

    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.pager_photos_menu_detail,null);

        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        String cache=CacheUtils.getCache(mActivity,GlobalConstants.PHOTOS_URL);

        if(!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                processData(result);

                CacheUtils.setCache(mActivity,GlobalConstants.PHOTOS_URL,result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity,s,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processData(String result) {
        Gson gson=new Gson();
        PhotosBean photosBean=gson.fromJson(result, PhotosBean.class);

        mPhotoList=photosBean.data.news;

        lvList.setAdapter(new PhotosAdapter());

        gvList.setAdapter(new PhotosAdapter());
    }
    private BitmapUtils mBitmapUtils;

    private boolean isListView=true;

    @Override
    public void onClick(View v) {
        if(isListView){
            lvList.setVisibility(View.GONE);
            gvList.setVisibility(View.VISIBLE);

            btn_display.setImageResource(R.drawable.icon_pic_list_type);

            isListView=false;
        }else{
            //显示listview
            lvList.setVisibility(View.VISIBLE);
            gvList.setVisibility(View.GONE);

            btn_display.setImageResource(R.drawable.icon_pic_grid_type);

            isListView=true;
        }

    }

    class PhotosAdapter extends BaseAdapter{

        public PhotosAdapter(){
            mBitmapUtils=new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_item_photo,null);
                holder=new ViewHolder();
                holder.tvTitle=convertView.findViewById(R.id.tv_title);
                holder.ivPic=convertView.findViewById(R.id.iv_pic);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            PhotosBean.PhotoNews item=getItem(position);

            holder.tvTitle.setText(item.title);

            mBitmapUtils.display(holder.ivPic,item.listimage);

            return convertView;
        }
    }

    static  class  ViewHolder{
        public TextView tvTitle;
        public ImageView ivPic;
    }
}
