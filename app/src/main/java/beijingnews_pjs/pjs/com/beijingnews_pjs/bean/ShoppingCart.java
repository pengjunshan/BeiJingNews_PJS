package beijingnews_pjs.pjs.com.beijingnews_pjs.bean;

import java.io.Serializable;

/**
 * Created by pjs984312808 on 2016/6/14.
 * 创建购物车类
 */
public class ShoppingCart extends ShoppingPagerBean.ListBean implements Serializable{

    private int count;

    private boolean isChecked=true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
