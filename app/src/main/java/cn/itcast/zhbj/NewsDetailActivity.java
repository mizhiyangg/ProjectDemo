package cn.itcast.zhbj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.btn_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.webview)
    private WebView mWebView;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);

        initViews();

        //获取webview的设置对象
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//启用js功能
        settings.setBuiltInZoomControls(true);//显示放大缩小的按钮(不支持已经适配好移动端的页面)
        settings.setUseWideViewPort(true);//双击缩放

        String url=getIntent().getStringExtra("url");

        //加载网页
        mWebView.loadUrl(url);

        //给WebView设置监听
        mWebView.setWebViewClient(new WebViewClient() {
            //开始加载页面
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }

            //加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }

            //跳转链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient(){
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //进度发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    private void initViews(){
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }


    //拦截物理返回键
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){//判断是否可以返回
            mWebView.goBack();//返回上一网页

            //mWebView.goForward();//跳到下一个网页(前提是之前浏览过，有历史记录)
            //mWebView.canGoForward();//判断是否可以跳到下一个网页
        }else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                share();
                break;
            default:
                break;
        }
    }

    //shareSDK分享
    private void share() {
        //java
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享的标题");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
        //oks.setImageUrl("http://192.168.0.120:8080/img/d.jpg");
        // url在微信、Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(MobSDK.getContext());
    }

    private int mTempWhich;

    private int mCurrentWhich=2;//当前选中的字体位置

    private void showChooseDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String[] items=new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        //显示单选框
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich=which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings=mWebView.getSettings();
                switch (mTempWhich){
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        //settings.setTextZoom();
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }
            mCurrentWhich=mTempWhich;//更新字体位置

            }
        });

        builder.setNegativeButton("取消",null);

        builder.show();
    }
}
