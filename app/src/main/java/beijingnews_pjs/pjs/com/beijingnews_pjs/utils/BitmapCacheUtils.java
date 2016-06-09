package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by pjs984312808 on 2016/6/8.
 * 图片缓存
 */
public class BitmapCacheUtils {

    //网络请求图片
    private NetBitmapCacheUtils netCacheBitmap;

    //本地缓存图片
    private LocalCacheUtils localCache;

    //内存缓存图片
    private MemoryCacheUtils memoryCache;


    public BitmapCacheUtils(Handler handler) {

        memoryCache = new MemoryCacheUtils();

        localCache = new LocalCacheUtils(memoryCache);

        netCacheBitmap = new NetBitmapCacheUtils(handler, localCache, memoryCache);
    }


    public Bitmap getBitmapFromUrl(String url, int position) {

        if (memoryCache != null) {
            Bitmap bitmap = memoryCache.getBitmap(url);
            if(bitmap!=null) {
                return bitmap;
            }
        }

        if (localCache != null) {
            Bitmap bitmap = localCache.getBitmap(url);
            if(bitmap!=null) {
                return bitmap;
            }
        }

        if (netCacheBitmap != null) {
            netCacheBitmap.getBitmapFromNet(url, position);
        }

        return null;
    }
}
