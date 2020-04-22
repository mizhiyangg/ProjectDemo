package cn.itcast.zhbj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.itcast.zhbj.utils.PrefUtils;

public class GuideActivity extends AppCompatActivity {
    private ViewPager vp_Guide;
    private Button btn_Start;
    private LinearLayout ll_container;//小圆点容器

    private int[] mImageIDs=new int[]{R.drawable.fan,R.drawable.yue,R.drawable.yu};

    private int mCurrentIndex = 0;//当前小圆点的位置

    private ArrayList<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initData();
    }


    //初始化布局
    private  void initViews(){
        vp_Guide=(ViewPager)findViewById(R.id.vp_guide);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        btn_Start=(Button)findViewById(R.id.btn_start);
    }
    //初始化数据
    private void initData(){
        //初始化3个imageView对象
        imageViews=new ArrayList<ImageView>();
        for(int i=0;i<mImageIDs.length;i++){
            ImageView view=new ImageView(this);
            view.setBackgroundResource(mImageIDs[i]);
            imageViews.add(view);
            ImageView dot = new ImageView(this);
            if (i == mCurrentIndex) {
                dot.setImageResource(R.drawable.shape_point_selected);//设置当前页的圆点
            } else {
                dot.setImageResource(R.drawable.shape_point_normal);//其余页的圆点
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 15;//设置圆点边距
            }
            dot.setLayoutParams(params);
            ll_container.addView(dot);//将圆点添加到容器中
        }
        vp_Guide.setAdapter(new GuideAdapter());
        vp_Guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==mImageIDs.length-1){
                    btn_Start.setVisibility(View.VISIBLE);
                }else{
                    btn_Start.setVisibility(View.GONE);
                }
                //根据监听的页面改变当前页对应的小圆点
                mCurrentIndex = position;
                for (int i = 0; i < ll_container.getChildCount(); i++) {
                    ImageView imageView = (ImageView) ll_container.getChildAt(i);
                    if (i == position) {
                        imageView.setImageResource(R.drawable.shape_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.shape_point_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //按钮点击
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在sp中记录访问过引导页的状态
                PrefUtils.putBoolean(getApplicationContext(),"is_guide_show",true);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIDs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        //初始化布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =imageViews.get(position);
            container.addView(view);
            return view;
        }

        //销毁布局
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
