package com.atguigu.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2016/6/13 15:32
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private final Context context;
    private final ArrayList<String> datas;

    public MyAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;

    }

    /**
     * 相当于ListView中适配器的getview方法中的创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_main, null);
        return new MyViewHolder(view);
    }

    /**
     * 相当于ListView中适配器的getview方法中的绑定数据部分，在这个方法写
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String content = datas.get(position);
        holder.tv_name.setText(content);

    }

    /**
     * 得到数据的总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(itemClickListener != null){
                        itemClickListener.onItemClick(v,getLayoutPosition(),datas.get(getLayoutPosition()));
                    }
//                    Toast.makeText(context, "content==" + datas.get(getLayoutPosition()) + ",position==" + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            //设置图片的点击事件
            iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(imageListener != null){
                        imageListener.OnClickImage(v,getLayoutPosition(),datas.get(getLayoutPosition()));
                    }
                }
            });
        }


    }

    /**
     * 监听点击某一条
     */
    public interface OnItemClickListener {

        void onItemClick(View view, int postion, String content);
    }

    /**
     * 设置点击某条的点击事件
     * @param itemClickListener
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private  OnItemClickListener itemClickListener;


    public interface OnClickImageListener{
       public void OnClickImage(View view, int postion, String content);
    }

    private OnClickImageListener imageListener;

    /**
     * 设置点击图片的监听
     * @param imageListener
     */
    public void setImageListener(OnClickImageListener imageListener) {
        this.imageListener = imageListener;
    }


    /**
     * 增加数据
     * @param postion
     * @param data
     */
    public void addData(int postion,String data){
        if(datas != null){
            datas.add(postion,data);
            notifyItemInserted(postion);
        }
    }


    /**
     * 移除数据
     * @param postion
     */
    public void removeData(int postion){
        if(datas != null){
            datas.remove(postion);
            notifyItemRemoved(postion);
        }
    }
}
