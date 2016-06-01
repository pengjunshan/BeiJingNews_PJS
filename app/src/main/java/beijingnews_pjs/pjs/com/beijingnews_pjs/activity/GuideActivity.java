package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.DensityUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;

public class GuideActivity extends Activity {


    private ViewPager viewpager;
    private LinearLayout ll_point;
    private Button btn_comein;
    private ImageView iv_point;
    private int density;
    public static String IS_ONEOPEN="is_oneopen";

    /**
     * 两个点的距离
     */
    private int distancePoint;
    private int[] images = new int[]{
            R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3
    };
    private List<ImageView> viewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
        setData();
    }

    private void init() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        btn_comein = (Button) findViewById(R.id.btn_comein);
        iv_point = (ImageView) findViewById(R.id.iv_point);
        density = DensityUtil.dip2px(this, 10);

    }

    private void setData() {
        ImageView imageView;
        for (int i = 0; i < images.length; i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            viewList.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.poin_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(density, density);
            if (i != 0) {
                //从第二个开始间距为10dp
                params.leftMargin = density;
            }
            //给point设置参数
            point.setLayoutParams(params);
            //每创建一个point都添加到
            ll_point.addView(point);
        }

        //设置适配
        viewpager.setAdapter(new MyViewPageAdapter());

        //获取两点间的距离
        iv_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //对viewPager设置监听
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //为按钮设置监听
        btn_comein.setOnClickListener(new MyOnClickListener());

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            //第一次安装软件记录
            SPUtil.saveIsOneOpen(GuideActivity.this,IS_ONEOPEN,true);

            startActivity(new Intent(GuideActivity.this, MainActivity.class));

            finish();
        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //取消注册
            iv_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
            distancePoint = ll_point.getChildAt(1).getLeft() - ll_point.getChildAt(0).getLeft();
            Log.i("TAG", "distancePoint="+distancePoint);
        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当viewPager滑动的时候回调
         * @param position             当前页面的position项
         * @param positionOffset       滑动的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离 = 间距*屏幕滑动百分比
            float distanceX=distancePoint*positionOffset;
            //红点真实的坐标移动距离=间距*（位置+滑动的百分比）
//            distanceX=position+distancePoint+positionOffset*distancePoint;
            distanceX=distancePoint*(position+positionOffset);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(density,density);
            params.leftMargin= (int) distanceX;
            iv_point.setLayoutParams(params);
        }

        /**
         * 当前viewPager的position项
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {

            //判断引导页面是否为最后一页
            if(position==viewList.size()-1) {
                btn_comein.setVisibility(View.VISIBLE);
            }else {
                btn_comein.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = viewList.get(position);
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//             super.destroyItem(container, position, object);

            container.removeView((View) object);
        }
    }

}
