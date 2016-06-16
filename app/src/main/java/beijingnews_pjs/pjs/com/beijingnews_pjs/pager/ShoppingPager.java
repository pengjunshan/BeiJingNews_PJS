package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.adapter.ShoppingPagerAdapter;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingPagerBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.URL;
import beijingnews_pjs.pjs.com.beijingnews_pjs.volley.VolleyManager;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class ShoppingPager extends BasePager {


    //正常状态
    private final String START_NORMAL="nomal";

    //下拉刷新
    private final String START_REFRESH="refresh";

    //上拉加载
    private final String START_MORE="loadmore";

    private String start=START_NORMAL;


    @ViewInject(R.id.sp_refresh)
    private MaterialRefreshLayout refresh;

    @ViewInject(R.id.sp_recyclerView)
    private RecyclerView recyclerView;

    private ShoppingPagerAdapter adapter;

    /**
     * 联网请求数据的连接
     */
    private String url;


    /**
     * 数据总条数
     */
    private int totalCount;

    /**
     * 数据总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currentPage=1;

    /**
     * 数据集合
     */
    private List<ShoppingPagerBean.ListBean> dataList;


    public ShoppingPager(Context context) {
        super(context);
    }


    @Override
    public void initData() {
        super.initData();
        tv_title.setText("购物");

        View view = View.inflate(context, R.layout.shopping, null);
        x.view().inject(this, view);
        //移除所有的布局
        fl_content_basepager.removeAllViews();
        //添加当前创建的布局
        fl_content_basepager.addView(view);


        //默认连接
        url= URL.SHOPPING_URL+currentPage;

        /**
         * 获取缓存
         */
        String bufferJson= SPUtil.getJsonString(context,url);
        if(!TextUtils.isEmpty(bufferJson)) {
            parcessJson(bufferJson);
        }
        getFromDataNet();

        setRefresh();
    }

    private void setRefresh() {
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {

            /**
             * 下拉刷新
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();

            }

            /**
             * 下拉刷新完成回调
             */
            @Override
            public void onfinish() {
                super.onfinish();
            }

            /**
             * 下拉加载
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(currentPage<totalPage) {
                    loadMore();
                    Toast.makeText(context, "立马加载", Toast.LENGTH_SHORT).show();
                }else {
                    refresh.finishRefreshLoadMore();
                    Toast.makeText(context, "没有数据了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMore() {
        currentPage=currentPage+1;
        start=START_MORE;
        getFromDataNet();
    }

    /**
     * 下拉刷新
     */
    private void refreshData() {
        //请求第一页的数据
        currentPage=1;
        //设置状态
        start=START_REFRESH;
        getFromDataNet();
    }

    /**
     * 联网请求数据
     */
    private void getFromDataNet() {

        url= URL.SHOPPING_URL+currentPage;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Log.i("TAG", "shoppingPager请求成功="+result);
                parcessJson(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("TAG", "shoppingPager请求失败="+volleyError.getMessage());
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

        VolleyManager.getRequestQueue().add(request);

    }

    /**
     * 处理数据显示
     * @param json
     */
    private void parcessJson(String json) {


        ShoppingPagerBean shoppBean= parseJson(json);
        //总共多少条数据
        totalCount=shoppBean.getTotalCount();

        //总共多少页数据
        totalPage=shoppBean.getTotalPage();

        //数据
        dataList=shoppBean.getList();


        //显示数据
        showData();



    }

    /**
     * 显示数据
     */
    private void showData() {
        switch (start){

            case START_NORMAL://正常状态
                //recyclerView关联适配器显示数据
                adapter = new ShoppingPagerAdapter(context,dataList);
                recyclerView.setAdapter(adapter);
                //设置布局样式
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                break;

            case START_REFRESH://下拉刷新状态

                //清空数据
                adapter.deleteData();

                //填充新的数据
                adapter.setData(dataList);

                //隐藏加载圈
                refresh.finishRefresh();
                Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
                break;

            case START_MORE://上拉加载状态

                //添加数据
                adapter.addData(adapter.getItemCount(),dataList);

                //隐藏加载圈
                refresh.finishRefreshLoadMore();
                Toast.makeText(context, "加载完成", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 解析json
     * @param json
     * @return
     */
    private ShoppingPagerBean parseJson(String json) {
        return new Gson().fromJson(json,ShoppingPagerBean.class);
    }
}
