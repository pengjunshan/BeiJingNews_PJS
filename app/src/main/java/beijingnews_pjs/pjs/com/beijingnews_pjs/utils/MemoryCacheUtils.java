package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by pjs984312808 on 2016/6/9.
 * 内存缓存
 */
public class MemoryCacheUtils {


    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight()) / 1024;
            }
        };


    }

    /**
     * 保存到lruCache集合中
     *
     * @param url
     * @param bitmap
     */
    public void setBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }


    /**
     * 根据url在lruCache集合找到value
     *
     * @param url
     * @return
     */
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }
}
