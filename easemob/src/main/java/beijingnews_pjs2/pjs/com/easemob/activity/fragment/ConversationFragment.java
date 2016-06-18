package beijingnews_pjs2.pjs.com.easemob.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import beijingnews_pjs2.pjs.com.easemob.R;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class ConversationFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.conversation,null,false);
        return view;
    }

}
