package beijingnews_pjs2.pjs.com.recyclerviewtext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> data;
    private RecyclerView recyclerview;

    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        initData();

        adapter=new MyAdapter(this,data);

        recyclerview.setAdapter(adapter);

        LinearLayoutManager llm= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        StaggeredGridLayoutManager sgy=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        recyclerview.setLayoutManager(sgy);

//        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        recyclerview.addItemDecoration(decoration);

    }

    private void initData() {
        data=new ArrayList<>();
        for (int i=0;i<30;i++){
            data.add("黑凤梨"+i);
        }
    }
}
