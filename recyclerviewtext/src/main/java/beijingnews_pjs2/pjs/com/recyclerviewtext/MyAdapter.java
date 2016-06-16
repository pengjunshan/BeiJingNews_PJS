package beijingnews_pjs2.pjs.com.recyclerviewtext;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjs984312808 on 2016/6/13.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private final Context context;
    private final List<String> data;

    private List<Integer> ruandom;

    public MyAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;

        initData();
    }

    private void initData() {
        ruandom=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            ruandom.add((int) (100+Math.random()*200));
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height=ruandom.get(position);
        holder.itemView.setLayoutParams(lp);
        holder.tv_title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
