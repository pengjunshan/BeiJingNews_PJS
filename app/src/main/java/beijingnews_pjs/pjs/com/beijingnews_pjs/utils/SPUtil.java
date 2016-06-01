package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class SPUtil {

    public static void saveIsOneOpen(Context context, String key, boolean value) {

        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }


    public static boolean getIsOpen(Context context, String key) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
}
