package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class HomePager extends BasePager {


    public HomePager(Context context) {
        super(context);
    }


    @Override
    public void initData() {
        super.initData();
        tv_title.setText("首页");
        TextView textView=new TextView(context);
        textView.setText("我是首页");
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content_basepager.addView(textView);
    }
}
