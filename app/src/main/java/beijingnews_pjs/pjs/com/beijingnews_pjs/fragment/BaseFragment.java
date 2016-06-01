package beijingnews_pjs.pjs.com.beijingnews_pjs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pjs984312808 on 2016/6/1.
 */
public abstract class BaseFragment extends Fragment {

    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    /**
     * 当View 创建的时候调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 子类实现 返回具体的子类
     * @return
     */
    public abstract View initView();


    /**
     * 当Activity被创建的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("TAG", "onActivityCreated");
        initData();
    }

    /**
     * 当孩子需要初始化数据的时候，重新该方法
     */
    public void initData() {

    }

}
