package beijingnews_pjs.pjs.com.beijingnews_pjs.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class BasePager {

    //上下文
    public Context context;

    //要返回的view
    public View rootView;

    //要显示的字view
    @ViewInject(R.id.fl_content_basepager)
    public FrameLayout fl_content_basepager;

    //菜单按钮
    @ViewInject(R.id.ib_menu)
    public ImageButton ib_menu;

    //title标题
    @ViewInject(R.id.tv_title)
    public TextView tv_title;

    public BasePager(Context context) {
        this.context=context;
        rootView=initView();
    }

    public View initView() {
        View view=View.inflate(context, R.layout.basepager,null);
        x.view().inject(this,view);//xutil3注入view
       /* fl_content_basepager= (FrameLayout) view.findViewById(R.id.fl_content_basepager);
        ib_menu= (ImageButton) view.findViewById(R.id.ib_menu);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
*/
        return view;
    }

    public void initData(){

    }


}
