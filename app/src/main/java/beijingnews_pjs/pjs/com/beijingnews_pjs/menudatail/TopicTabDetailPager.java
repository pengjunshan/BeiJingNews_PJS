package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuDetailBasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.LeftMenuBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.TabDetailBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.URL;
import beijingnews_pjs.pjs.com.beijingnews_pjs.view.HorizontalScrollViewPager;

/**
 * Created by pjs984312808 on 2016/6/6.
 */
public class TopicTabDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.vp_tabdetailpager)
    private HorizontalScrollViewPager vp_tabdetailpager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_poing_group)
    private LinearLayout ll_poing_group;

    @ViewInject(R.id.pull_refresh_list)
    private PullToRefreshListView pullToRefreshListView;

    private ListView listview;

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


    public TopicTabDetailPager(Context context, LeftMenuBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.children = childrenBean;

    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.topictabdetailpager, null);
        x.view().inject(this, view);

        viewpagerView = View.inflate(context, R.layout.tabdetail_top, null);
        x.view().inject(this, viewpagerView);

        listview=pullToRefreshListView.getRefreshableView();

//        listview.addHeaderView(viewpagerView);
        listview.addHeaderView(viewpagerView);

        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener2());
        return view;
    }


    @Override
    public void initData() {
        super.initData();

        /**
         * 联网请求新闻子类新闻top 的viewpager数据
         */
        String bufferString = SPUtil.getJsonString(context, URL.BASE_URL + children.getUrl());
        if (!TextUtils.isEmpty(bufferString)) {
            percessData(bufferString);
        }
        getFromDataNet();
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2 {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            isPullRefresh = true;
            //请求数据
            getFromDataNet();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            if (TextUtils.isEmpty(moreUrl)) {
                //没有更多数据
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                pullToRefreshListView.onRefreshComplete();
            } else {

                //加载更多
                getMoreDataFromNet();
            }
        }
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

                pullToRefreshListView.onRefreshComplete();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pullToRefreshListView.onRefreshComplete();
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
                    pullToRefreshListView.onRefreshComplete();
                }
                SPUtil.saveJsonString(context, URL.BASE_URL + children.getUrl(), result);
                percessData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (isPullRefresh) {
                    isPullRefresh = false;
                    pullToRefreshListView.onRefreshComplete();
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

            adapter = new MyPagerAdapter();
            vp_tabdetailpager.setAdapter(adapter);

            listAdapter = new MyListAdapter();
            listview.setAdapter(listAdapter);


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

    }

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

        }
    }


    private TabDetailBean parseJson(String json) {
        Gson gson = new Gson();
        TabDetailBean tabDetailBean = gson.fromJson(json, TabDetailBean.class);
        return tabDetailBean;
    }

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
            return imagerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


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

            TabDetailBean.DataBean.NewsBean newsBean = TopicTabDetailPager.this.newsBean.get(position);
            String url = newsBean.getListimage();
            url = url.replace("10.0.2.2", "192.168.3.104");

            Glide.with(context).load(url)
                    .placeholder(R.drawable.home_scroll_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_logo);

            viewHolder.tv_title.setText(newsBean.getTitle());

            viewHolder.tv_time.setText(newsBean.getPubdate());

            return convertView;
        }


    }

    static class ViewHolder {

        ImageView iv_logo;
        TextView tv_title, tv_time;
    }
}
