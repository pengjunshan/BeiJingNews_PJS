package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.activity.MainActivity;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.MenuDetailBasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.LeftMenuBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.fragment.LeftMenuFragment;
import beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail.InteracMenuDetailPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail.NewsMenuDetailPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail.PhotosMenuDetailPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.menudatail.TopicMenuDetailPager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.URL;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class NewsPager extends BasePager {

    //左菜单请求到的数据集合
    private List<LeftMenuBean.DataBean> leftMenuBeanData;

    private List<MenuDetailBasePager> menuList;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        ib_menu.setVisibility(View.VISIBLE);

//        *//**
//         * 监听菜单按钮
//         *//*
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main= (MainActivity) context;
               SlidingMenu menu = main.getSlidingMenu();
                menu.toggle();
            }
        });

        //先获取缓存的json数据
        String bufferString = SPUtil.getJsonString(context, URL.NEWSCENTER_URL);
        if(!TextUtils.isEmpty(bufferString)) {
            percessData(bufferString);
        }

        getFromDataNet();

    }

    public void getFromDataNet() {

        RequestParams params=new RequestParams(URL.NEWSCENTER_URL);


        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                //缓存json数据
                SPUtil.saveJsonString(context,URL.NEWSCENTER_URL,result);

                percessData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "NewsPager-result请求失败="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

//    *//**
//     * 处理Json
//     * @param json
//     *//*
    private void percessData(String json) {
        LeftMenuBean leftMenuBean = parseJson(json);

        //得到左菜单的数据 并把数据传递给 LeftMenuFragment
        leftMenuBeanData = leftMenuBean.getData();

        menuList=new ArrayList<>();
        menuList.add(new NewsMenuDetailPager(context,leftMenuBeanData.get(0)));
        menuList.add(new TopicMenuDetailPager(context,leftMenuBeanData.get(0)));
        menuList.add(new PhotosMenuDetailPager(context));
        menuList.add(new InteracMenuDetailPager(context));




        //得到MainActivity的对象
        MainActivity mainActivity= (MainActivity) context;
        LeftMenuFragment leftFragment=mainActivity.getLeft_Fragment();
        leftFragment.setData(leftMenuBeanData);
    }

//    *//**
//     * 解析数据
//     * @param json
//     *//*
    private LeftMenuBean parseJson(String json) {
        Gson gson=new Gson();
        LeftMenuBean leftMenuBean = gson.fromJson(json, LeftMenuBean.class);

        return leftMenuBean;
    }


    public void setSwichMenu(int position) {

        tv_title.setText(leftMenuBeanData.get(position).getTitle());

        //根据position创建子类实例
        MenuDetailBasePager menuPager = menuList.get(position);
        //移除所有的布局
        fl_content_basepager.removeAllViews();
        //初始化数据
        menuPager.initData();
        //把子布局存入到fremlyout中
        fl_content_basepager.addView(menuPager.rootView);

        /**
         * 如果是图组就显示图标
         */
        if(position==2) {
            iv_list_grid.setVisibility(View.VISIBLE);

            iv_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   PhotosMenuDetailPager photosmenu= (PhotosMenuDetailPager) menuList.get(2);
                    photosmenu.setSwichAdapter(iv_list_grid);
                }
            });
        }else {
            iv_list_grid.setVisibility(View.GONE);
        }


    }
}
