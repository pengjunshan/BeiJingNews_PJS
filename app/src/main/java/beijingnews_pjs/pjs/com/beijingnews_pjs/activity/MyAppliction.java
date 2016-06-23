package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.BuildConfig;
import org.xutils.x;

import beijingnews_pjs.pjs.com.beijingnews_pjs.volley.VolleyManager;
import cn.jpush.android.api.JPushInterface;

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

    //初始化Volley
//        Volley.newRequestQueue(this);
    VolleyManager.init(this);


        //注册极光推送

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //初始化ImageLoader
        initImageLoader(this);

//        //初始化异常处理
//        AppException appException = AppException.getInstance();
//        appException.init(this);
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //设置当前线程的优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 缓存显示不同大小的同一张图片
                .denyCacheImageMultipleSizesInMemory()
                //将保存的时候的URI名称用MD5 加密
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
