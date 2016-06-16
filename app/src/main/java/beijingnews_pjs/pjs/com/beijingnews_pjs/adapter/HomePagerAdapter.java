package beijingnews_pjs.pjs.com.beijingnews_pjs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;

/**
 * Created by pjs984312808 on 2016/6/13.
 */
public class HomePagerAdapter extends RecyclerView.Adapter<HomePagerAdapter.MyViewHolder> {


    private final Context context;
    private final List<String> data;

    private List<Integer> random;

    public HomePagerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
//        initData();
    }

    private void initData() {
        random=new ArrayList<>();
        for (int i=0; i<data.size();i++){
            random.add((int) (100+Math.random()*300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//
//        lp.height=random.get(position);
//
//        holder.itemView.setLayoutParams(lp);

        holder.tv_title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(int i, String str) {
        data.add(i,str);
        notifyItemInserted(i);
    }

    public void removeData(int i) {
        data.remove(i);
        notifyItemRemoved(i);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private ImageView iv_icon;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title= (TextView) itemView.findViewById(R.id.tv_title);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            /**
             * 对整个布局监听
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null) {
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });

            /**
             * 对头像监听
             */
            iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImageClickListener!=null) {
                        onImageClickListener.onImageClick(getLayoutPosition());
                    }
                }
            });


        }
    }

    /**
     * 添加数据的接口
     */
    public interface OnItemClickListener{
        void onItemClick(int layoutPosition);
    }

    private OnItemClickListener onItemClickListener;

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加删除数据的接口
     */
    public interface  OnImageClickListener{
        void onImageClick(int layoutPosition);
    }

    private OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
