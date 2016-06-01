package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class MyAppliction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化xutils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
