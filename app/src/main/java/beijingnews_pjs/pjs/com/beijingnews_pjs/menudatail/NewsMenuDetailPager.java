package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuPager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class NewsMenuDetailPager extends MenuPager {


    private TextView textView;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView=new TextView(context);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("这是新闻页面");
    }
}
