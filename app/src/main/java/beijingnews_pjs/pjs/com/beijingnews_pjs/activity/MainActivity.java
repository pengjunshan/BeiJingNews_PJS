package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.fragment.ContentFragment;
import beijingnews_pjs.pjs.com.beijingnews_pjs.fragment.LeftMenuFragment;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_FRAGMENT = "left_fragment";
    public static final String CONTENT_FRAGMENT = "content_fragment";
    private MainActivity ac;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              ac = this;

        //设置主界面
        setContentView(R.layout.fragment_content);

        //设置左侧菜单
        setBehindContentView(R.layout.fragment_left);

        //设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.fragment_left);

        //4.设置模式：左侧菜单+主页面；主页面+右侧菜单；左侧菜单+主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动区域：全屏滑动，边缘滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置菜单占据主界面(200)
        slidingMenu.setBehindOffset(DensityUtil.dip2px(ac, 200));

        //把Fragment添加到Activity中去
        initFragment();

    }


    private void initFragment() {

        //得到Fragment管理者
        FragmentManager fragmentManager = getSupportFragmentManager();

        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //替换Left Content Fragment
        transaction.replace(R.id.left_fragment, new LeftMenuFragment(), LEFT_FRAGMENT);
        transaction.replace(R.id.content_fragment, new ContentFragment(), CONTENT_FRAGMENT);

        //提交
        transaction.commit();

    }


    /**
     * 得到左菜单的对象
     *
     * @return
     */
    public LeftMenuFragment getLeft_Fragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftFragmnet = (LeftMenuFragment) fragmentManager.findFragmentByTag(LEFT_FRAGMENT);
        return leftFragmnet;
    }

    /**
     * 得到中间内容的对象
     *
     * @return
     */
    public ContentFragment getContent_Fragment(){
        FragmentManager manager = getSupportFragmentManager();
        ContentFragment contentFraagment = (ContentFragment) manager.findFragmentByTag(CONTENT_FRAGMENT);
        return contentFraagment;
    }
}
