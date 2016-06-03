package beijingnews_pjs.pjs.com.beijingnews_pjs.base;

import android.content.Context;
import android.view.View;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public abstract class MenuPager {

    public Context context;

    public View rootView;

    public MenuPager(Context context) {
        this.context = context;

        rootView=initView();
    }

    public abstract View initView();

    public void initData(){

    }

}
