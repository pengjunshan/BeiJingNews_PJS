package beijingnews_pjs2.pjs.com.easemob.activity.fragment;

import android.content.Intent;
import android.view.View;

import com.hyphenate.easeui.ui.EaseContactListFragment;

import beijingnews_pjs2.pjs.com.easemob.R;
import beijingnews_pjs2.pjs.com.easemob.activity.activity.AddContactActivity;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class ContactListFragent extends EaseContactListFragment {

    @Override
    protected void setUpView() {
        super.setUpView();

        titleBar.setBackgroundResource(R.drawable.contact_add);

       titleBar.setRightLayoutClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getActivity().startActivity(new Intent(getActivity(), AddContactActivity.class));
           }
       });


    }
}
