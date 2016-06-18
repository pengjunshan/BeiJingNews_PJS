package beijingnews_pjs2.pjs.com.easemob.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import beijingnews_pjs2.pjs.com.easemob.R;
import beijingnews_pjs2.pjs.com.easemob.activity.activity.LoginActivity;
import beijingnews_pjs2.pjs.com.easemob.activity.bean.IMUser;
import beijingnews_pjs2.pjs.com.easemob.activity.model.IMModel;


/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class SettingFragment extends Fragment{


    private Button btn_exit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.setting,null,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setOnClick();
    }

    private void init() {
        btn_exit = (Button) getActivity().findViewById(R.id.btn_exit);

        //EMClient.getInstance().getCurrentUser() 返回的是环信id
        IMUser imuser = IMModel.getInstance().gtIMUserHxId(EMClient.getInstance().getCurrentUser());

        btn_exit.setText("退出登录("+imuser.getAppUser()+")");

    }

    private void setOnClick() {
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
        });
    }
}
