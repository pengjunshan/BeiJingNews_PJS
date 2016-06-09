package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.activity.NewsWebActivity;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuDetailBasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.LeftMenuBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.TabDetailBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.URL;
import beijingnews_pjs.pjs.com.beijingnews_pjs.view.HorizontalScrollViewPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.view.RefreshListView;

/**
 * Created by pjs984312808 on 2016/6/4.
 */
public class NewsTabDetailPager extends MenuDetailBasePager {


    public static final String NEWSDATA_BROWSED = "newsdata_browsed";
    @ViewInject(R.id.vp_tabdetailpager)
    private HorizontalScrollViewPager vp_tabdetailpager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_poing_group)
    private LinearLayout ll_poing_group;

    @ViewInject(R.id.listview)
    private RefreshListView listview;

    //记录viewpager的当前位置
    private int currentPosition;

    //viewPager的适配器
    private MyPagerAdapter adapter;

    //lisrView的适配器
    private MyListAdapter listAdapter;


    private View viewpagerView;

    private boolean isPullRefresh = false;

    /**
     * 十二个子主题的数据
     */
    private LeftMenuBean.DataBean.ChildrenBean children;

    /**
     * 请求到的上部分的数据 viewpager中的数据
     */
    private List<TabDetailBean.DataBean.TopnewsBean> topnewsBeen;

    /**
     * 获取到listview的数据
     */
    private List<TabDetailBean.DataBean.NewsBean> newsBean;

    /**
     * 加载更多的连接
     */
    private String moreUrl;

    /**
     * 是否加载更多数据
     */
    private boolean isLoadMore = false;


    public NewsTabDetailPager(Context context, LeftMenuBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.children = childrenBean;

    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.tabdetailpager, null);
        x.view().inject(this, view);

        viewpagerView = View.inflate(context, R.layout.tabdetail_top, null);
        x.view().inject(this, viewpagerView);


//        listview.addHeaderView(viewpagerView);
        listview.setHeaderView(viewpagerView);


        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //因为有头部占据了一条信息 所以要减一
            int realPosition = position - 1;

            //得到当前一条数据的信息
            TabDetailBean.DataBean.NewsBean newsData = NewsTabDetailPager.this.newsBean.get(realPosition);

            //获取这条数据的id
            int newsDataId = newsData.getId();

            //获取保存浏览过的信息的id数据
            String browsedId = SPUtil.getBrowsedId(context, NEWSDATA_BROWSED);

            /**
             * 判断这个条信息是否已经浏览过
             * true 浏览过了
             * false 没有浏览过
             */
            if (!browsedId.contains(newsDataId + "")) {
                //保存数据
                String valuesId = browsedId + newsDataId + ",";
                SPUtil.saveBrowsedId(context, NEWSDATA_BROWSED, valuesId);
            }
            //同时更新适配器 来改变颜色
            adapter.notifyDataSetChanged();
            /**
             * 跳转到web页面
             */
            Intent intent = new Intent(context, NewsWebActivity.class);
            intent.putExtra("url", newsData.getUrl());
            context.startActivity(intent);

        }
    }

    @Override
    public void initData() {
        super.initData();

        handler = new myHandler();

        runnable = new myRunnable();

        listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            /**
             *下拉刷新监听
             */
            @Override
            public void onPullDownRefresh() {
                isPullRefresh = true;
                //请求数据
                getFromDataNet();
            }

            /**
             * 上拉加载监听
             */
            @Override
            public void onLoadMore() {

                if (TextUtils.isEmpty(moreUrl)) {
                    //没有更多数据
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                    listview.onRefreshFinished(context, false);
                } else {

                    //加载更多
                    getMoreDataFromNet();
                }

            }
        });

        /**
         * 联网请求新闻子类新闻top 的viewpager数据
         */
        String bufferString = SPUtil.getJsonString(context, URL.BASE_URL + children.getUrl());
        if (!TextUtils.isEmpty(bufferString)) {
            percessData(bufferString);
        }
        getFromDataNet();
    }

    /**
     * 加载更多
     */
    private void getMoreDataFromNet() {

//        RequestParams params = new RequestParams(URL.BASE_URL + children.getUrl());
        RequestParams params = new RequestParams(moreUrl);

        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "请求成功=" + result);
                //正在加载
                isLoadMore = true;
                //2.解析和显示数据
                percessData(result);

                listview.onRefreshFinished(context, true);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listview.onRefreshFinished(context, false);
                Log.i("TAG", "请求失败=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("TAG", "请求完成");
            }
        });

    }

    private void getFromDataNet() {
        RequestParams params = new RequestParams(URL.BASE_URL + children.getUrl());

        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (isPullRefresh) {
                    isPullRefresh = false;
                    listview.onRefreshFinished(context, true);
                }
                SPUtil.saveJsonString(context, URL.BASE_URL + children.getUrl(), result);
                percessData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (isPullRefresh) {
                    isPullRefresh = false;
                    listview.onRefreshFinished(context, false);
                }
                Log.i("TAG", "请求失败=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("TAG", "请求完成");
            }
        });
    }

    private void percessData(String json) {
        TabDetailBean tabDetailBean = parseJson(json);

        String more = tabDetailBean.getData().getMore();

        if (TextUtils.isEmpty(more)) {
            moreUrl = "";
        } else {
            moreUrl = URL.BASE_URL + more;
        }

        if (!isLoadMore) {

            //获取上面viewpager的数据
            topnewsBeen = tabDetailBean.getData().getTopnews();

            //获取下面listview的数据
            newsBean = tabDetailBean.getData().getNews();

            //viewpager适配器
            adapter = new MyPagerAdapter();
            vp_tabdetailpager.setAdapter(adapter);

            //Listview适配器
            listAdapter = new MyListAdapter();
            listview.setAdapter(listAdapter);
            listview.setOnItemClickListener(new MyOnItemClickListener());

            //先移除所有的红点
            ll_poing_group.removeAllViews();

            /**
             * 创建移动红点
             */
            for (int i = 0; i < topnewsBeen.size(); i++) {
                ImageView poin = new ImageView(context);
                poin.setBackgroundResource(R.drawable.pointabdetail_selector);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
                if (i == 0) {
                    poin.setEnabled(true);
                } else {
                    params.leftMargin = DensityUtil.dip2px(8);
                    poin.setEnabled(false);
                }
                poin.setLayoutParams(params);

                ll_poing_group.addView(poin);
            }
            vp_tabdetailpager.addOnPageChangeListener(new MyOnPagerChangeListener());
        } else {
            //加载更多
            newsBean.addAll(tabDetailBean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();

            isLoadMore = false;
        }

        if (adapter != null) {
            handler.postDelayed(runnable, 4000);
        }


    }

    private myHandler handler;

    private myRunnable runnable;


    /**
     * 自定义Handler
     */
    class myHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //循环
            vp_tabdetailpager.setCurrentItem((vp_tabdetailpager.getCurrentItem() + 1) % topnewsBeen.size());

            //死循环
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable,4000);
        }
    }

    /**
     * 自定义Runnable
     */
    class myRunnable implements Runnable {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private boolean isPager;
    /**
     * 对viewpager页面的监听
     */
    class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            //把上一次的变暗
            ll_poing_group.getChildAt(currentPosition).setEnabled(false);


            //把当前点高亮
            ll_poing_group.getChildAt(position).setEnabled(true);

            //赋值
            currentPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            //替换title
            tv_title.setText(topnewsBeen.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            switch (state){
                case ViewPager.SCROLL_STATE_DRAGGING://拖拽时
                    isPager=true;
                    if(handler!=null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;

                case ViewPager.SCROLL_STATE_IDLE://空闲时
                        if(isPager) {
                            isPager=false;
                            handler.removeCallbacksAndMessages(null);
                            handler.postDelayed(runnable,4000);
                        }
                    break;

                case ViewPager.SCROLL_STATE_SETTLING://固定
                    if(isPager) {
                        isPager=false;
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(runnable,4000);
                    }
                    break;
            }

        }
    }


    /**
     * 解析字符串
     * @param json
     * @return
     */
    private TabDetailBean parseJson(String json) {
        Gson gson = new Gson();
        TabDetailBean tabDetailBean = gson.fromJson(json, TabDetailBean.class);
        return tabDetailBean;
    }

    /**
     * viewpager的适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnewsBeen.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imagerView = new ImageView(context);
            imagerView.setBackgroundResource(R.drawable.home_scroll_default);
            imagerView.setScaleType(ImageView.ScaleType.FIT_XY);
            //联网请求图片-Glide
            String Url = topnewsBeen.get(position).getTopimage();

            Url = Url.replace("10.0.2.2", "192.168.3.104");

            Glide.with(context).load(Url)
                    .placeholder(R.drawable.home_scroll_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.home_scroll_default).into(imagerView);

            container.addView(imagerView);

            imagerView.setOnTouchListener(new MyOnTouchListener());

            return imagerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 自定义触摸监听事件
     */
    class MyOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);
                    break;

                case MotionEvent.ACTION_MOVE:
                    handler.removeCallbacksAndMessages(null);
                    break;

                case MotionEvent.ACTION_UP:
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(runnable,4000);
                    break;

               /* case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(runnable,4000);
                    break;*/

            }

            return true;
        }
    }

    /**
     * listView的适配器
     */
    class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return newsBean.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabDetailBean.DataBean.NewsBean newsBean = NewsTabDetailPager.this.newsBean.get(position);
            String url = newsBean.getListimage();
            url = url.replace("10.0.2.2", "192.168.3.104");

            Glide.with(context).load(url)
                    .placeholder(R.drawable.home_scroll_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_logo);

            viewHolder.tv_title.setText(newsBean.getTitle());

            viewHolder.tv_time.setText(newsBean.getPubdate());

            String browsedId = SPUtil.getBrowsedId(context, NEWSDATA_BROWSED);

            if (browsedId.contains(newsBean.getId() + "")) {
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }


            return convertView;
        }


    }

    static class ViewHolder {

        ImageView iv_logo;
        TextView tv_title, tv_time;
    }

}
