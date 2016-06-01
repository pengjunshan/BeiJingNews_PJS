package beijingnews_pjs.pjs.com.beijingnews_pjs.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class Content_Fragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;

    @ViewInject(R.id.rg_bottom)
    private RadioGroup rg_bottom;
    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.activity_content,null);
        x.view().inject(this,view);
        LogUtil.e("Content_Fragment初始化了");
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        rg_bottom.check(R.id.rb_home);
    }
}
