package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class PayPager extends BasePager {


    public PayPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("支付");
        TextView textView=new TextView(context);
        textView.setText("我是支付页面");
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        fl_content_basepager.addView(textView);
    }
}
