package com.atguigu.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> datas;
    private MyAdapter myAdapter;

    private Button btn_add;
    private Button btn_remove;
    private Button btn_list;
    private Button btn_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_list = (Button) findViewById(R.id.btn_list);
        btn_grid = (Button) findViewById(R.id.btn_grid);

        setListener();
        initData();
        //设置适配器
        myAdapter = new MyAdapter(this,datas);
        recyclerView.setAdapter(myAdapter);
        //设置布局管理器
        /**
         * 第一个参数：上下文
         * 第二参数：方向：竖直和水平
         * 第三个参数：排序:升序和降序
         */
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        /**
         * 第一个参数：上下文
         * 第二个参数：设置多少列
         * 第三参数：设置方向：竖直和水平
         * 第四个参数：排序:升序和降序
         */
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,true);


        StaggeredGridLayoutManager staggeredGridLayoutManager =  new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //添加分割线
        recyclerView.addItemDecoration(new MyItemDecoration(this,MyItemDecoration.VERTICAL_LIST));

        //设置点击某一条的点击事件
        myAdapter.setItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, String content) {
                Toast.makeText(MainActivity.this, "content==" + content + ",position==" + postion, Toast.LENGTH_SHORT).show();

            }
        });

        myAdapter.setImageListener(new MyAdapter.OnClickImageListener() {
            @Override
            public void OnClickImage(View view, int postion, String content) {
                Toast.makeText(MainActivity.this, "我是图片哦content==" + content + ",position==" + postion, Toast.LENGTH_SHORT).show();

            }
        });

        //设置某条的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setListener() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.addData(0,"new Conttent");
                recyclerView.scrollToPosition(0);
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.removeData(0);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);

                recyclerView.setLayoutManager(linearLayoutManager);


            }
        });
        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 第一个参数：上下文
                 * 第二个参数：设置多少列
                 * 第三参数：设置方向：竖直和水平
                 * 第四个参数：排序:升序和降序
                 */
                GridLayoutManager gridLayoutManager =  new GridLayoutManager(MainActivity.this,3,GridLayoutManager.VERTICAL,true);


                recyclerView.setLayoutManager(gridLayoutManager);


            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        for(int i=0;i<20;i++){
            datas.add("Content"+i);
        }
    }
}
