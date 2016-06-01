package beijingnews_pjs.pjs.com.beijingnews_pjs.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class Left_Fragment extends BaseFragment{

    private TextView textView;
    @Override
    public View initView() {
        textView=new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("这是菜单");
    }
}
