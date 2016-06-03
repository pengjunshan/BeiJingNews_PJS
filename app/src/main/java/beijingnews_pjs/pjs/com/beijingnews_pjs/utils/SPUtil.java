package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class SPUtil {

    /**
     * 保存第一次进入的标识
     * @param context
     * @param key
     * @param value
     */
    public static void saveIsOneOpen(Context context, String key, boolean value) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }


    /**
     * 获取是否是第一次进入的标识
     * @param context
     * @param key
     * @return
     */
    public static boolean getIsOpen(Context context, String key) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }


    /**
     * 保存左菜单的json数据
     * @param context
     */
    public static void saveLeftMenuJson(Context context, String key, String values) {
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }


}
