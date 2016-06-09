package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.activity.MainActivity;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuDetailBasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.LeftMenuBean;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class NewsMenuDetailPager extends MenuDetailBasePager {


    @ViewInject(R.id.vp_newsmenudetail)
    private ViewPager vp_newsmenudetail;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;

    @ViewInject(R.id.im_nextTabPager)
    private ImageView im_nextTabPager;

    private MyPagerAdapter adapter;

    /**
     * 菜单中的新闻页面中的子类集合
     */
    private List<NewsTabDetailPager> listTab;

    /**
     * 得到新闻的子新闻的数据
     */
    private List<LeftMenuBean.DataBean.ChildrenBean> childrenData;


    public NewsMenuDetailPager(Context context, LeftMenuBean.DataBean dataBean) {
        super(context);
        childrenData = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenudetailpager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //1.初始化数据
        listTab = new ArrayList<>();
        for (int i = 0; i < childrenData.size(); i++) {

            listTab.add(new NewsTabDetailPager(context, childrenData.get(i)));

        }

        //2.viewpager关联适配器
        adapter = new MyPagerAdapter();

        vp_newsmenudetail.setAdapter(adapter);

        listTab.get(0).initData();

        /**
         * 1,TabPageIndicator关联ViewPage
           给TabPageIndicator关联ViewPage,一定要关联，否则报错
           当TabPageIndicator和ViewPage关联后，TabPageIndicator的数据由ViewPage的适配器来设置
         */
        tabPageIndicator.setViewPager(vp_newsmenudetail);

        //给下一个按钮设置监听
        im_nextTabPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_newsmenudetail.setCurrentItem(vp_newsmenudetail.getCurrentItem() + 1);
            }
        });

        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

            if(position==0){
                //SlidingMenu可以滑动
                isEnableSlidemenu(true);
            }else{
                //不可以滑动
                isEnableSlidemenu(false);
            }

            NewsTabDetailPager tabDetailPager = listTab.get(position);
            tabDetailPager.initData();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 是否让左侧菜单可以滑动
     * @param isEnableSlidemenu
     */
    private void isEnableSlidemenu(Boolean isEnableSlidemenu) {

        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (isEnableSlidemenu) {
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 是给TabPageIndicator的页签添加数据的
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return childrenData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return listTab.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public MyPagerAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsTabDetailPager tabDetailPager = listTab.get(position);
            View view = tabDetailPager.rootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
