package beijingnews_pjs.pjs.com.beijingnews_pjs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingCart;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingPagerBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.CartProvider;

/**
 * Created by pjs984312808 on 2016/6/14.
 */
public class ShoppingPagerAdapter extends RecyclerView.Adapter<ShoppingPagerAdapter.MyViewHolder>{


    private final Context context;
    private  CartProvider cartProvider;

    private final List<ShoppingPagerBean.ListBean> dataList;

    public ShoppingPagerAdapter(Context context, List<ShoppingPagerBean.ListBean> dataList) {
        this.context=context;
        this.dataList=dataList;
        cartProvider=new CartProvider(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shopping,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ShoppingPagerBean.ListBean data = dataList.get(position);

        holder.tv_title.setText(data.getName());

        holder.tv_price.setText("¥"+data.getPrice());

        //使用Glide请求图片
        Glide.with(context).load(data.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                .placeholder(R.drawable.home_scroll_default)//加载过程中的图片
                .error(R.drawable.home_scroll_default)//加载失败的时候显示的图片
                .into(holder.iv_icon);//请求成功后把图片设置到的控件

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 清空数据
     */
    public void deleteData() {
        dataList.clear();
        notifyItemRangeChanged(0,dataList.size());
    }

    /**
     * 添加数据
     */
    public void setData(List<ShoppingPagerBean.ListBean> data) {

        addData(0,data);
    }


    public void addData(int position, List<ShoppingPagerBean.ListBean> data) {
        if(data!=null&&data.size()>0) {
            dataList.addAll(position,data);
            notifyItemRangeChanged(position,dataList.size());
        }

    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_title,tv_price;
        private Button btn_pay;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            btn_pay = (Button) itemView.findViewById(R.id.btn_pay);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            btn_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, ""+dataList.get(getLayoutPosition()).getPrice(), Toast.LENGTH_SHORT).show();
                    ShoppingPagerBean.ListBean listBean = dataList.get(getLayoutPosition());

                   ShoppingCart cart = cartProvider.conversion(listBean);
                    cartProvider.putCart(cart);
                }

            });
        }
    }

  /*  public interface OnBtnClickLisener{

        void onBtnClick();
    }

    private OnBtnClickLisener onBtnClickLisener;

    public void setOnBtnClickLisener(OnBtnClickLisener onBtnClickLisener) {
        this.onBtnClickLisener = onBtnClickLisener;
    }*/
}
