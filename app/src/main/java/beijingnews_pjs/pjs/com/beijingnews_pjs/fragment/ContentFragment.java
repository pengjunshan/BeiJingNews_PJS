package beijingnews_pjs.pjs.com.beijingnews_pjs.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.activity.MainActivity;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BaseFragment;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.HomePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.NewsPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.PayPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.SetingPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.ShoppingPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.view.NoScollViewPager;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private NoScollViewPager viewpager;

    @ViewInject(R.id.rg_bottom)
    private RadioGroup rg_bottom;

    private List<BasePager> pagerList;

    private MyPagerAdapter adapter;

    private SlidingMenu slidingMenu;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.activity_content, null);
        x.view().inject(this, view);//xutil3注入view
        LogUtil.e("Content_Fragment初始化了");

        //初始化适配器
        adapter = new MyPagerAdapter();

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //默认选中第一个radioButton
        rg_bottom.check(R.id.rb_home);
        //默认viewpager显示第一个
//        viewpager.setCurrentItem(0);

        //初始化集合 并添加对象
        pagerList = new ArrayList<>();
        pagerList.add(new HomePager(context));
        pagerList.add(new NewsPager(context));
        pagerList.add(new ShoppingPager(context));
        pagerList.add(new PayPager(context));
        pagerList.add(new SetingPager(context));

        //设置适配器 显示数据
        viewpager.setAdapter(adapter);

        //默认初始化第一个数据
        pagerList.get(0).initData();

        //为radioGroup设置监听
        rg_bottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //对viewPager设置监听
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //得到MainActivity
        MainActivity mainActivity = (MainActivity) context;
        slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

    }
//
//    *
//     * 得到第二项view NewsPager
//     * @return
//
    public NewsPager getNewsPager() {
        BasePager basePager = pagerList.get(1);
        return (NewsPager) basePager;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagerList.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    viewpager.setCurrentItem(0, false);
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;

                case R.id.rb_news:
                    viewpager.setCurrentItem(1, false);
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;

                case R.id.rb_shopping:
                    viewpager.setCurrentItem(2, false);
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;

                case R.id.rb_pay:
                    viewpager.setCurrentItem(3, false);
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;

                case R.id.rb_seting:
                    viewpager.setCurrentItem(4, false);
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到一个子类的实例
            BasePager basePager = pagerList.get(position);

            //得到视图
            View view = basePager.rootView;
            //添加视图到viewPager中
            container.addView(view);

            //同时初始化数据 执行initData方法
            //初始化数据，不能再这里加载，此方法会预加载下一个页面，加载下一个页面的数据，会浪费用户流量
//            basePager.initData();

            return view;
        }
    }

}
