package beijingnews_pjs.pjs.com.beijingnews_pjs.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.util.DensityUtil;

import java.util.List;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.activity.MainActivity;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BaseFragment;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.LeftMenuBean;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pager.NewsPager;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public class LeftMenuFragment extends BaseFragment{

    private ListView listView;
    private MyAdapter adapter;

    //点击后的位置
    private int menuPosition = 0;

//    *
//     * 得到数据
//
    private List<LeftMenuBean.DataBean> leftMenuBeanData;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setBackgroundColor(Color.BLACK);
        //分割线0
        listView.setDividerHeight(0);
        //防止在低版本按下整个ListView变灰色
        listView.setCacheColorHint(Color.TRANSPARENT);
        //距离上距离
        listView.setPadding(0, DensityUtil.dip2px(40), 0, 0);
        //去掉在低版本中默认选中效果
        listView.setSelection(android.R.color.transparent);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuPosition = position;
                adapter.notifyDataSetChanged();
                Log.i("TAG", "listView被点击了");

//                *
//                 * 获取SlidingMenu对象

                MainActivity main = (MainActivity) context;
                SlidingMenu menu = main.getSlidingMenu();
                menu.toggle();

                setSwichMenu(position);

            }
        });

        return listView;
    }

    @Override
    public void initData() {
        super.initData();
        
    }

    public void setData(List<LeftMenuBean.DataBean> leftMenuBeanData) {
        this.leftMenuBeanData = leftMenuBeanData;

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        setSwichMenu(menuPosition);

    }

    public void setSwichMenu(int position){

        MainActivity main = (MainActivity) context;
        ContentFragment contentFragment = main.getContent_Fragment();
        NewsPager newsPager = contentFragment.getNewsPager();
        newsPager.setSwichMenu(position);

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return leftMenuBeanData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftfragment, null);

            LeftMenuBean.DataBean dataBean = leftMenuBeanData.get(position);
            textView.setText(dataBean.getTitle());

            //判断当前位置是否是选中的位置
            if (menuPosition == position) {
                textView.setEnabled(true);
            } else {
                textView.setEnabled(false);
            }
            return textView;
        }
    }
}
