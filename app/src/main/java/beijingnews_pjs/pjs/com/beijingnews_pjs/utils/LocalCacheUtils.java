package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by pjs984312808 on 2016/6/9.
 * 本地缓存
 */
public class LocalCacheUtils {

    //内存缓存图片
    private MemoryCacheUtils memoryCache;

    public LocalCacheUtils(MemoryCacheUtils memoryCache) {
        this.memoryCache = memoryCache;
    }


    public void setBitmap(String url, Bitmap bitmap) {

        try {
            //文件名加密
            String fileName = MD5Encoder.encode(url);
            //路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beijingnews";

            File file = new File(path, fileName);

            File fileParent = file.getParentFile();

            //判断目录有没有
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }

            //判断文件有没有
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String url) {
        try {
            //得到加密的文件名
            String fileName = MD5Encoder.encode(url);
            //路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beijingnews";

            Log.i("TAG", "path="+path);
            File file = new File(path, fileName);

            if (file.exists()) {

                FileInputStream fis = new FileInputStream(file);

                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                memoryCache.setBitmap(url,bitmap);

                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
