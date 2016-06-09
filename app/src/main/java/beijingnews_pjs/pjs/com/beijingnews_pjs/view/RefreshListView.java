package beijingnews_pjs.pjs.com.beijingnews_pjs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;

/**
 * Created by pjs984312808 on 2016/6/6.
 */
public class RefreshListView extends ListView {

    /**
     * 下拉刷新状态
     */
    public static final int PULL_DOWN_REFRESH = 1;

    /**
     * 手松刷新
     */
    public static final int RELEASE_REFRESH = 2;

    /**
     * 正在刷新
     */
    public static final int REFRESHING = 3;

    public static final String REFRESHTIME = "refreshtime";

    /**
     * 当前的状态
     */
    private int currentState = PULL_DOWN_REFRESH;

    /**
     * 顶部下拉刷新布局
     */
    private LinearLayout headerView;

    /**
     * 底部加载更多布局
     */
    private View fooView;

    /**
     * 头部下拉刷新view
     */
    @ViewInject(R.id.ll_pull_down_refresh)
    private LinearLayout ll_pull_down_refresh;

    //下拉箭头
    @ViewInject(R.id.iv_header_arrow)
    private ImageView iv_header_arrow;

    //加载圈
    @ViewInject(R.id.pb_header_state)
    private ProgressBar pb_header_state;

    //下拉的状态
    @ViewInject(R.id.tv_header_state)
    private TextView tv_header_state;

    //上次刷新的时间
    @ViewInject(R.id.tv_header_time)
    private TextView tv_header_time;

    //顶部轮播图
    private View topNewsPager;


    //得到listview的Y轴坐标
    private int ListViewY = -1;

    //得到头部view的高度
    private int topHeight;

    //得到底部view的高度
    private int fooViewHegiht;

    /**
     * 下拉头部view的箭头的动画
     *
     * @param context
     */
    private Animation upAnimotion;
    private Animation downAnimotion;

    /**
     * 判断底部view是否显示
     */
    private boolean isLoadMore;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化头部布局
        initTopView(context);

        //初始化底部布局
        initFootView(context);

        //初始化动画
        initAnimotion();

    }

    /**
     * 初始化底部布局
     *
     * @param context
     */
    private void initFootView(Context context) {
        fooView = View.inflate(context, R.layout.refresh_footer, null);
        x.view().inject(this, fooView);

        //测量
        fooView.measure(0, 0);

        fooViewHegiht = fooView.getMeasuredHeight();

        //默认位置
        fooView.setPadding(0,fooViewHegiht,0,0);

        //添加到底部
        addFooterView(fooView);

        //监听Listview的滚动时间
        setOnScrollListener(new MyOnScrollListener());
    }

    class MyOnScrollListener implements OnScrollListener {
        /**
         * 当ListView滚动状态发生变化的时候回调这个方法
         * 静止->滑动
         * 滑动-->惯性滚动
         * 惯性滚动-静止
         *
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {

                //最后一个的时候
                if (getLastVisiblePosition() == getCount() - 1) {

                    //1.把加载更多UI显示出来
                    fooView.setPadding(10, 10, 10, 10);

                    //2.设置状态
                    isLoadMore = true;

                    //3.调用接口
                    if (onRefreshListener != null) {
                        onRefreshListener.onLoadMore();
                    }

                }

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    /**
     * 初始化下拉箭头的动画
     */
    private void initAnimotion() {

        upAnimotion = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimotion.setDuration(500);
        upAnimotion.setFillAfter(true);

        downAnimotion = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimotion.setDuration(500);
        downAnimotion.setFillAfter(true);

    }

    /**
     * 初始化头部view
     *
     * @param context
     */
    private void initTopView(Context context) {

        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_listview_header, null);
        x.view().inject(this, headerView);

        //测量高度
        ll_pull_down_refresh.measure(0, 0);

        //得到高度
        topHeight = ll_pull_down_refresh.getMeasuredHeight();

        //设置下拉加载布局的默认位置
        ll_pull_down_refresh.setPadding(0, -topHeight, 0, 0);
        //添加到头部布局
        this.addHeaderView(headerView);

        tv_header_time.setText("上次刷新时间：" + SPUtil.getRefreshTime(context, REFRESHTIME));

    }

    //开始时的Y轴坐标
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:
//                if (startY == -1) {
//                    startY = ev.getY();
//                }

                //正在加载
                if (currentState == REFRESHING) {
                    break;
                }

                //判断轮播图是否显示全
                boolean isFillPager = isFillPagerTop();
                if (!isFillPager) {
                    break;
                }


                //移动时的Y轴坐标
                float endY = ev.getY();

                Log.i("TAG", "endY*******=" + endY);

                //偏移量
                float distanceY = endY - startY;

                if (distanceY > 0) {

                    //得到头部view最终的位置
                    int finalY = (int) (-topHeight + distanceY);
                    Log.i("TAG", "finalY******=" + finalY);
                    if (finalY > 1.5 * topHeight) {
                        finalY = (int) (1.5f * topHeight);
                    }

                    if (finalY < 0 && currentState != PULL_DOWN_REFRESH) {
                        /**
                         * 头部布局还没全部显示
                         */
                        currentState = PULL_DOWN_REFRESH;
                        //更新UI
                        refreshHeaderState();
                    } else if (finalY >= 0 && currentState != RELEASE_REFRESH) {
                        /**
                         * 头部布局已经全部显示出来了
                         */
                        currentState = RELEASE_REFRESH;

                        refreshHeaderState();
                    }


                    ll_pull_down_refresh.setPadding(0, finalY, 0, 0);

                }

                break;

            case MotionEvent.ACTION_UP:
                //回到默认值
                startY = 0;

                if (currentState == PULL_DOWN_REFRESH) {
                    /**
                     * 下拉状态 隐藏头部布局
                     */
                    ll_pull_down_refresh.setPadding(0, -topHeight, 0, 0);

                } else if (currentState == RELEASE_REFRESH) {

                    /**
                     * 松开加载状态 松开后变成正在加载状态
                     */
                    currentState = REFRESHING;
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    refreshHeaderState();

                    //回调方法
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullDownRefresh();
                    }
                }

                break;

        }

        return super.onTouchEvent(ev);
    }

    /**
     * 根据头部布局的状态不同来显示不同的数据
     */
    private void refreshHeaderState() {

        switch (currentState) {

            case PULL_DOWN_REFRESH://下拉刷新
                tv_header_state.setText("下拉刷新...");
                iv_header_arrow.setVisibility(VISIBLE);
                iv_header_arrow.startAnimation(downAnimotion);
                pb_header_state.setVisibility(GONE);
                break;

            case RELEASE_REFRESH://松开刷新
                tv_header_state.setText("松开加载...");
                iv_header_arrow.setVisibility(VISIBLE);
                iv_header_arrow.startAnimation(upAnimotion);
                pb_header_state.setVisibility(GONE);
                break;

            case REFRESHING://加载数据
                tv_header_state.setText("正在加载");
                //清除动画
                iv_header_arrow.clearAnimation();
                pb_header_state.setVisibility(VISIBLE);
                iv_header_arrow.setVisibility(GONE);
                break;


        }
    }

    private boolean isFillPagerTop() {

        if (topNewsPager != null) {
            int[] locaion = new int[2];
            if (ListViewY == -1) {
                this.getLocationOnScreen(locaion);
                //数组第二个是Y的坐标
                ListViewY = locaion[1];
            }

            //得到轮播图的Y轴坐标
            topNewsPager.getLocationOnScreen(locaion);
            int topNewsPagerY = locaion[1];

            return ListViewY <= topNewsPagerY;
        }


        return true;
    }

    public void setHeaderView(View viewpagerView) {
        this.topNewsPager = viewpagerView;
        if (topNewsPager != null) {
            //将轮播图添加到头部view
            headerView.addView(topNewsPager);
        }
    }

    /**
     * 接口
     */

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 数据请求后调用
     *
     * @param context
     * @param b
     */
    public void onRefreshFinished(Context context, boolean b) {

        if (isLoadMore) {
            isLoadMore = false;
            fooView.setPadding(0, -fooViewHegiht, 0, 0);
        } else {

            currentState = PULL_DOWN_REFRESH;
            ll_pull_down_refresh.setPadding(0, -topHeight, 0, 0);
            tv_header_state.setText("下拉刷新");
            pb_header_state.setVisibility(GONE);
            iv_header_arrow.clearAnimation();

            if (b) {
                SPUtil.saveRefreshTime(context, REFRESHTIME, getCurrentTime());
                tv_header_time.setText("上次刷新时间：" + SPUtil.getRefreshTime(context, REFRESHTIME));
            }
        }

    }

    /**
     * 获取当前时间
     */
    private String getCurrentTime() {

        //格式化样式
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //返回当前时间
        return time.format(new Date());
    }

    public interface OnRefreshListener {

        /**
         * 当下拉刷新时回调
         */
        void onPullDownRefresh();

        /**
         * 当上拉加载时回调
         */
        void onLoadMore();
    }


}
