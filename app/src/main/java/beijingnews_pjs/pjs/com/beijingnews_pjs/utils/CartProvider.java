package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingCart;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingPagerBean;

/**
 * Created by pjs984312808 on 2016/6/14.
 * 创建数据存储类
 */
public class CartProvider {

    public static final String SHOPPING_CCART = "shopping_cart";
    private final Context context;

    /**
     * 性能比HashMap的一个控件，Android提供的
     */

    private SparseArray<ShoppingCart> datas;

    public CartProvider(Context context) {
        this.context = context;
        datas = new SparseArray<>(10);

        listToSparse();
    }

    /**
     * 从本地存储中 保存到SparseArray<ShoppingCart>中
     */
    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLoacl();
        for (int i = 0; i < carts.size(); i++) {
            //以id为key   以ShoppingCart对象为value
            datas.put(carts.get(i).getId(), carts.get(i));
        }

    }

    /**
     * 得到所有的数据
     */
    public List<ShoppingCart> getAllData() {

        return getDataFromLoacl();
    }


    /**
     * 从本地获取保存的数据
     */

    public List<ShoppingCart> getDataFromLoacl() {

        List<ShoppingCart> list = new ArrayList<>();
        String bufferData = SPUtil.getJsonString(context, SHOPPING_CCART);

        if (!TextUtils.isEmpty(bufferData)) {
            list = new Gson().fromJson(bufferData, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 增加数据
     */

    public void putCart(ShoppingCart cart) {

        ShoppingCart shoppingCart = datas.get(cart.getId());

        if (shoppingCart != null) {
            shoppingCart.setCount(shoppingCart.getCount() + 1);
        } else {
            shoppingCart = cart;
            shoppingCart.setCount(1);
        }

        //添加
        datas.put(shoppingCart.getId(), shoppingCart);

        //保持到本地
        commit();
    }

    /**
     * 更新数据
     */
    public void upData(ShoppingCart cart){
        //更新
        datas.put(cart.getId(),cart);

        //保持到本地
        commit();
    }

    /**
     * 更新数据
     */
    public void deletData(ShoppingCart cart){
        //删除数据
        datas.delete(cart.getId());

        //保持到本地
        commit();
    }


    /**
     * 保存到本地
     */
    private void commit() {
        List<ShoppingCart> list = sparseToList();

        //用gson把集合转换成字符串
        String json = new Gson().toJson(list);

        //保存到本地
        SPUtil.saveJsonString(context,SHOPPING_CCART,json);

    }

    /**
     * 把SparseArray<ShoppingCart> 转换为 List<ShoppingCart>
     * @return
     */
    public List<ShoppingCart> sparseToList(){
        List<ShoppingCart> list=new ArrayList<>(datas.size());

        for (int i=0;i<datas.size();i++){
            list.add(datas.valueAt(i));
        }
        return list;
    }

    public ShoppingCart conversion(ShoppingPagerBean.ListBean listBean) {

        ShoppingCart cart=new ShoppingCart();
        cart.setId(listBean.getId());
        cart.setName(listBean.getName());
        cart.setPrice(listBean.getPrice());
        cart.setImgUrl(listBean.getImgUrl());
        cart.setDescription(listBean.getDescription());
        cart.setSale(listBean.getSale());
        cart.setCount(1);
        cart.setChecked(true);
        return cart;
    }
}
