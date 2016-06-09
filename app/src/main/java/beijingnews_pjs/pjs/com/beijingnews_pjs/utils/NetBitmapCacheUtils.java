package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pjs984312808 on 2016/6/8.
 */
public class NetBitmapCacheUtils {

    /**
     * 请求成功
     */
    public static final int SUCCESS = 0;

    /**
     * 请求失败
     */
    public static final int FALE = 1;
    private Handler handler;

    /**
     * 线程池
     */
    private ExecutorService service;

    //本地缓存图片
    private LocalCacheUtils localCache;

    //内存缓存图片
    private MemoryCacheUtils memoryCache;


    public NetBitmapCacheUtils(Handler handler, LocalCacheUtils localCache, MemoryCacheUtils memoryCache) {
        this.handler = handler;
        this.localCache = localCache;
        this.memoryCache = memoryCache;
        service = Executors.newFixedThreadPool(10);
    }

    public void getBitmapFromNet(String url, int position) {
//        new Thread(new MyRunable(url, position)).start();
        service.execute(new MyRunable(url, position));
    }


    class MyRunable implements Runnable {

        private int position;
        private String url;

        public MyRunable(String url, int position) {
            this.url = url;
            this.position = position;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                //请求地址
                URL url1 = new URL(url);
                Log.i("TAG", "url1="+url1);
                // 获取一个与服务器端的连接：HttpURLConnection
                connection = (HttpURLConnection) url1.openConnection();
                //设置请求方式
                connection.setRequestMethod("GET");
                //设置请求和读取的事件
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                //连接服务器
                connection.connect();

                int code = connection.getResponseCode();
                if (code == 200) {
                    inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //发送到主线程显示

                    //创建一个Message
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
                    msg.arg1 = position;
                    //发送消息
                    Log.i("TAG", "msghandler=" + handler);
                    handler.sendMessage(msg);

                    //向本地存一份
                    localCache.setBitmap(url,bitmap);

                    //向内存存一份

                   memoryCache.setBitmap(url,bitmap);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = FALE;
                msg.arg1 = position;
                handler.sendMessage(msg);

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
