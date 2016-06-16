package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.adapter.HomePagerAdapter;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.DividerGridItemDecoration;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class HomePager extends BasePager {

    private RecyclerView recyclerview;

    private HomePagerAdapter adapter;

    private List<String> data;

    private Button btn_add,btn_remove,btn_list,btn_grid,btn_staggered;

    private LinearLayoutManager linearLayoutManager;

    private GridLayoutManager gridLayoutManager;

    private  StaggeredGridLayoutManager staggeredGridLayoutManager;
    public HomePager(Context context) {
        super(context);
        init();
    }

    private void init() {
        data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            data.add("王老吉" + i);
        }

    }


    @Override
    public void initData() {
        super.initData();
        View view = View.inflate(context, R.layout.homepager, null);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_remove = (Button) view.findViewById(R.id.btn_remove);
        btn_list = (Button) view.findViewById(R.id.btn_list);
        btn_grid = (Button) view.findViewById(R.id.btn_grid);

        btn_staggered = (Button) view.findViewById(R.id.btn_staggered);

        fl_content_basepager.removeAllViews();
        fl_content_basepager.addView(view);

        adapter = new HomePagerAdapter(context, data);
        //设置adapter
        recyclerview.setAdapter(adapter);

        /**
         * listview
         */
       linearLayoutManager =  new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        /**
         * gridview
         *
         */
       gridLayoutManager  =new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false);

        /**
         * 瀑布流
         */
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        //设置布局管理器
        recyclerview.setLayoutManager(gridLayoutManager);

//        //设置分割线
//        recyclerview.addItemDecoration(new ItemDividerDecoration(context, ItemDividerDecoration.VERTICAL_LIST));

        recyclerview.addItemDecoration(new DividerGridItemDecoration(context));

        // 设置item动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        /**
         * 设置监听
         */
        adapter.setOnItemClickListener(new HomePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, "这是布局"+data.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnImageClickListener(new HomePagerAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                Toast.makeText(context, "这是头像"+data.get(position), Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * 添加数据
         */
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(0,"新的数据");
            }
        });

        /**
         * 删除数据
         */
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeData(0);
            }
        });

        /**
         * 转换listview和gridview
         */
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置布局管理器
                recyclerview.setLayoutManager(linearLayoutManager);
            }
        });

        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置布局管理器
                recyclerview.setLayoutManager(gridLayoutManager);
            }
        });

        btn_staggered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置布局管理器
                recyclerview.setLayoutManager(staggeredGridLayoutManager);
            }
        });

    }
}
