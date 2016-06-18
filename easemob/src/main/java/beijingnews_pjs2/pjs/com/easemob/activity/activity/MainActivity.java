package beijingnews_pjs2.pjs.com.easemob.activity.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import beijingnews_pjs2.pjs.com.easemob.R;
import beijingnews_pjs2.pjs.com.easemob.activity.fragment.ContactListFragent;
import beijingnews_pjs2.pjs.com.easemob.activity.fragment.ConversationFragment;
import beijingnews_pjs2.pjs.com.easemob.activity.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main_tab;

    /**
     * 会话列表
     */
    private ConversationFragment conversationFragment;

    /**
     * 好友列表
     */
    private ContactListFragent contactListFragent;

    /**
     * 设置页面
     */
    private SettingFragment settingFragment;

    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setOnClick();
    }

    private void setOnClick() {
        rg_main_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment=null;
                    switch (checkedId){
                        case R.id.rb_conversation_list:
                            fragment=conversationFragment;
                            break;

                        case R.id.rb_contact_list:
                            fragment=contactListFragent;
                            break;

                        case R.id.rb_settings:
                            fragment=settingFragment;
                            break;
                    }

                    if(fragment!=null&&fragment!=currentFragment){
                        setFragment(fragment);
                    }

                }
        });
    }

    private void setFragment(Fragment fragment) {

        currentFragment=fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fl_fragment_container,fragment);

        transaction.commit();
    }

    private void init() {
        rg_main_tab = (RadioGroup) findViewById(R.id.rg_main_tab);

        conversationFragment = new ConversationFragment();
        contactListFragent = new ContactListFragent();
        settingFragment = new SettingFragment();

        setFragment(contactListFragent);

        rg_main_tab.check(R.id.rb_contact_list);

    }

}
