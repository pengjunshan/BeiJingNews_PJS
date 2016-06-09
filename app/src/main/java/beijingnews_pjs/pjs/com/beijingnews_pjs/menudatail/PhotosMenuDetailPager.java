package beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuDetailBasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.PhotosMenuBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.BitmapCacheUtils;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.NetBitmapCacheUtils;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.URL;
import beijingnews_pjs.pjs.com.beijingnews_pjs.volley.VolleyManager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {


    @ViewInject(R.id.pull_refresh_list)
    private PullToRefreshListView pull_refresh_list;

    @ViewInject(R.id.gridview_photos)
    private GridView gridview_photos;

    private  List<PhotosMenuBean.DataBean.NewsBean> newsData;

    private MyAdapter adapter;

    /**
     * 三级缓存请求图片
     */
    private BitmapCacheUtils bitmapCacheUtils;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case NetBitmapCacheUtils.SUCCESS://请求成功
                    int position = msg.arg1;
                    Bitmap bp = (Bitmap) msg.obj;
                    Log.i("TAG", "图片请求成功="+position);
                    if(pull_refresh_list!=null&&pull_refresh_list.isShown()) {
                       ImageView image = (ImageView) pull_refresh_list.findViewById(position);
                        if(bp!=null&&image!=null) {
                            image.setImageBitmap(bp);
                        }
                    }

                    if(gridview_photos!=null&&gridview_photos.isShown()) {
                        ImageView image = (ImageView) pull_refresh_list.findViewById(position);
                        if(bp!=null&&image!=null) {
                            image.setImageBitmap(bp);
                        }
                    }

                    break;

                case NetBitmapCacheUtils.FALE:
                    int position1 = msg.arg1;
                    Log.i("TAG", "图片请求失败="+position1);
                    break;

            }
        }
    };

    public PhotosMenuDetailPager(Context context) {
        super(context);
        bitmapCacheUtils=new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {

        View view=View.inflate(context, R.layout.photosmenudetailpager,null);
        x.view().inject(this,view);
        return view;

    }

    @Override
    public void initData() {
        super.initData();

        //得到缓存
        String bufferJson = SPUtil.getJsonString(context, URL.PHOTOS_URL);

        if(!TextUtils.isEmpty(bufferJson)) {

        }
        getFromData();
    }
    private void getFromData() {

//        RequestQueue requestQueue= Volley.newRequestQueue(context);

        StringRequest request=new StringRequest(Request.Method.GET, URL.PHOTOS_URL, new Response.Listener<String>() {
            /**
             * 成功
             * @param result
             */
            @Override
            public void onResponse(String result) {
//                Log.i("TAG", "请求成功="+result);
                SPUtil.saveJsonString(context,URL.PHOTOS_URL,result);

                processData(result);

            }
        }, new Response.ErrorListener() {
            /**
             * 失败
             * @param volleyError
             */
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.i("TAG", "请求失败="+volleyError.getMessage());
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                   var4.printStackTrace();
                }

                return super.parseNetworkResponse(response);
            }
        };

//        requestQueue.add(request);
        VolleyManager.getRequestQueue().add(request);
//        VolleyManager.addRequest(request,"111");

    }

    /**
     * 处理数据 显示数据
     * @param json
     */
    private void processData(String json) {
        PhotosMenuBean photomenuBean=parseJson(json);

        PhotosMenuBean.DataBean photomenuBeanData = photomenuBean.getData();

        newsData = photomenuBeanData.getNews();

        //绑定 显示
        adapter=new MyAdapter();
        pull_refresh_list.setAdapter(adapter);

    }

    /**
     * 解析json
     * @param json
     * @return
     */
    private PhotosMenuBean parseJson(String json) {
        return new Gson().fromJson(json,PhotosMenuBean.class);
    }

    private boolean isList=true;//默认Listview显示
    /**
     * 判断Listview 和gridView
     * @param iv_list_grid
     */
    public void setSwichAdapter(ImageView iv_list_grid) {
        if(isList) {
            isList=false;
            pull_refresh_list.setVisibility(View.GONE);

            gridview_photos.setVisibility(View.VISIBLE);

            gridview_photos.setAdapter(adapter);

            iv_list_grid.setBackgroundResource(R.drawable.icon_pic_list_type);

        }else {
            isList=true;

            pull_refresh_list.setVisibility(View.VISIBLE);

            gridview_photos.setVisibility(View.GONE);

            pull_refresh_list.setAdapter(adapter);

            iv_list_grid.setBackgroundResource(R.drawable.icon_pic_grid_type);
        }

        adapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView==null) {
                convertView=View.inflate(context,R.layout.item_photosmenu,null);
                viewHolder=new ViewHolder();
                viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            //获取一条数据
            PhotosMenuBean.DataBean.NewsBean newsBean = newsData.get(position);

            //填充数据
            viewHolder.tv_title.setText(newsBean.getTitle());
            String url=newsBean.getListimage();

            url=url.replace("10.0.2.2","192.168.3.104");

            /**
             * 方式一Volly请求
             */
//            loaderImager(viewHolder,url);

            /**
             * 方式二三级缓存
             */
            viewHolder.iv_icon.setTag(position);
           Bitmap bitmap = bitmapCacheUtils.getBitmapFromUrl(url,position);
            if(bitmap!=null) {
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }
    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }

}
