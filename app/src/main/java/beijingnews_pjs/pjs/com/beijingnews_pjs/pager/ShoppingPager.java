package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class ShoppingPager extends BasePager {


    public ShoppingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("购物");
        TextView textView=new TextView(context);
        textView.setText("我是购物页面");
        textView.setTextSize(30);
        textView.setTextColor(Color.GREEN);
        textView.setGravity(Gravity.CENTER);
        fl_content_basepager.addView(textView);
    }
}
