package beijingnews_pjs2.pjs.com.easemob.activity;

import android.app.Application;

import beijingnews_pjs2.pjs.com.easemob.activity.model.IMModel;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class MyAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        //注册环信即时通讯
       /* EMOptions options=new EMOptions();

        EMClient.getInstance().init(this,options);
*/
        IMModel.getInstance().onInit(this);
    }
}
