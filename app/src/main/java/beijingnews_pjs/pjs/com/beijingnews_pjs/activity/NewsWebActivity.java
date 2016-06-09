package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;

public class NewsWebActivity extends Activity implements View.OnClickListener {


    private WebView webview;

    //返回键
    private ImageButton ib_back;

    //字体大小
    private ImageButton ib_textsize;

    //分享
    private ImageButton ib_share;

    //加载圈
    private ProgressBar pb_loading;

    private String url;

    private  WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);

        init();
        setData();
    }

    private void init() {
        webview = (WebView)findViewById(R.id.webview);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_back.setVisibility(View.VISIBLE);

        ib_textsize = (ImageButton)findViewById(R.id.ib_textsize);
        ib_textsize.setVisibility(View.VISIBLE);

        ib_share = (ImageButton)findViewById(R.id.ib_share);
        ib_share.setVisibility(View.VISIBLE);

        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);


        ib_back.setOnClickListener(this);
        ib_textsize.setOnClickListener(this);
        ib_share.setOnClickListener(this);

    }


    private void setData() {
        //得到地址
        url=getIntent().getStringExtra("url");
        url=url.replace("10.0.2.2","192.168.10.53");

        //加载网页
        webview.loadUrl(url);

        //得到webview的设置
        webSettings = webview.getSettings();

        //设置WebView支持javaScript
        webSettings.setJavaScriptEnabled(true);

        //用户双击页面页面变大变小-页面要支持才可以
        webSettings.setUseWideViewPort(true);

        //增加缩放按钮 --页面要支持才可以
        webSettings.setBuiltInZoomControls(true);

        //当webview请求成功
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_loading.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back://返回
                finish();
                break;

            case R.id.ib_textsize://字体大小
                showDialogText();
                break;

            case R.id.ib_share://分享

                break;
        }
    }

    private int currentSize=2;
    private int tempSize;

    private void showDialogText() {
        String[] items ={"超大字体","大字体","正常","小字体","超小字体"};
        new AlertDialog.Builder(this)
                .setTitle("选择字体大小")
                .setSingleChoiceItems(items, currentSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //把当前item的下标赋值给临时下标
                        tempSize=which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //赋值给真正要保存的下标
                        currentSize=tempSize;
                        setWebViewSize(currentSize);

                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void setWebViewSize(int currentSize) {

        switch (currentSize){
            case 0:
                webSettings.setTextZoom(200);
                break;

            case 1:
                webSettings.setTextZoom(150);
                break;

            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(70);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;


        }
    }
}
