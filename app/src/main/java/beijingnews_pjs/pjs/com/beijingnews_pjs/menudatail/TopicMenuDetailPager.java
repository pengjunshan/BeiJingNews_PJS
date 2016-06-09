package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
public class TopicMenuDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.vp_newsmenudetail)
    private ViewPager vp_newsmenudetail;

    @ViewInject(R.id.tablayout)
    private TabLayout tablayout;

    @ViewInject(R.id.im_nextTabPager)
    private ImageView im_nextTabPager;

    private MyPagerAdapter adapter;


    /**
     * 菜单中的新闻页面中的子类集合
     */
    private List<TopicTabDetailPager> listTab;

    /**
     * 得到新闻的子新闻的数据
     */
    private List<LeftMenuBean.DataBean.ChildrenBean> childrenData;


    public TopicMenuDetailPager(Context context, LeftMenuBean.DataBean dataBean) {
        super(context);
        childrenData = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topicmenudetailpager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //1.初始化数据
        listTab = new ArrayList<>();
        for (int i = 0; i < childrenData.size(); i++) {

            listTab.add(new TopicTabDetailPager(context, childrenData.get(i)));

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
        //关联tablayout和viewpager
          tablayout.setupWithViewPager(vp_newsmenudetail);
        //一定要设置设置滚动模式
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        //给下一个按钮设置监听
        im_nextTabPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_newsmenudetail.setCurrentItem(vp_newsmenudetail.getCurrentItem() + 1);
            }
        });

        vp_newsmenudetail.setOnPageChangeListener(new MyOnPageChangeListener());

        for (int i = 0; i < tablayout.getTabCount(); i++) {
           TabLayout.Tab tab = tablayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
            }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

            if (position == 0) {
                //SlidingMenu可以滑动
                isEnableSlidemenu(true);
            } else {
                //不可以滑动
                isEnableSlidemenu(false);
            }

            TopicTabDetailPager tabDetailPager = listTab.get(position);
            tabDetailPager.initData();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 是否让左侧菜单可以滑动
     *
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


        public View getTabView(int position){
           View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
            TextView tv= (TextView) view.findViewById(R.id.iv_title);
            tv.setText(childrenData.get(position).getTitle());
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            img.setImageResource(R.drawable.dot_focus);
            return view;
           }
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
            TopicTabDetailPager tabDetailPager = listTab.get(position);
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
