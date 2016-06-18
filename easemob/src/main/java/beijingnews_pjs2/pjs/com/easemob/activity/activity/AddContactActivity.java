package beijingnews_pjs2.pjs.com.easemob.activity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import beijingnews_pjs2.pjs.com.easemob.R;

public class AddContactActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button searchBtn,addContactBtn;
    private TextView searchedContactTextView;
    private EditText searchContactField;
    private Activity ac;
    /**
     * 获取环信id
     */
    private String hxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ac=this;

        initFindView();

        setOnClick();
    }

    private void setOnClick() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新常见个线程去从服务器查找对应的IMUser
                // 通过返回的IMUser去获得环信的ID号，然后再用环信的ID发送好友邀请

                String contact = searchContactField.getText().toString();

                if(TextUtils.isEmpty(contact)){
                    Toast.makeText(AddContactActivity.this,"搜索的联系人不能为空",Toast.LENGTH_LONG).show();

                    return;
                }

                hxId = searchContactField.getText().toString();

                linearLayout.setVisibility(View.VISIBLE);

                searchedContactTextView.setText(searchContactField.getText());
            }
        });

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact(hxId);
            }
        });
    }

    /**
     * 邀请 添加好友
     * @param hxId
     * 分线程
     */
    private void addContact(final String hxId) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    EMClient.getInstance().contactManager().addContact(hxId,"妹子价格好友吧");
                    ac.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "邀请消息已发出", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ac.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "邀请消息发送失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();

    }

    private void initFindView() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_add_contact);
        linearLayout.setVisibility(View.GONE);
        searchBtn = (Button) findViewById(R.id.btn_search_contact);
        searchedContactTextView = (TextView) findViewById(R.id.tv_searched_contact);
        searchContactField = (EditText) findViewById(R.id.et_contact);
        addContactBtn = (Button) findViewById(R.id.btn_add_contact);
    }


}
