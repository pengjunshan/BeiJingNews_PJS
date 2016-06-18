package beijingnews_pjs2.pjs.com.easemob.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import beijingnews_pjs2.pjs.com.easemob.R;
import beijingnews_pjs2.pjs.com.easemob.activity.bean.IMUser;
import beijingnews_pjs2.pjs.com.easemob.activity.model.IMModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity ac;

    private EditText et_login_username, et_login_password;
    private Button btn_register, btn_login;

    private String mUserName;
    private String mUserPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ac = this;
        initFind();


    }

    private void initFind() {
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mUserName = et_login_username.getText().toString();
        mUserPwd = et_login_password.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mUserPwd)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.btn_register:
                registerUser(mUserName, mUserPwd);
                break;

            case R.id.btn_login:
                loginUser(mUserName, mUserPwd);
                break;
        }
    }

    /**
     * 注册账号
     *北京市通州区北苑街道68号c座京贸国际公寓
     * 
     * @param mUserName
     * @param mUserPwd
     */
    private void registerUser(final String mUserName, final String mUserPwd) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                IMUser immuser = getIMUser(mUserName, mUserPwd);

                try {
                    EMClient.getInstance().createAccount(immuser.getHxId(), mUserPwd);

                    ac.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    IMModel.getInstance().addAccount(immuser);
                } catch (final HyphenateException e) {
                    e.printStackTrace();

                    ac.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册失败=" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.start();

    }

    private IMUser getIMUser(String mUserName, String mUserPwd) {
        return new IMUser(mUserName);
    }

    /**
     * 登录账号
     *
     * @param mUserName
     * @param mUserPwd
     */
    private void loginUser(final String mUserName, final String mUserPwd) {

        IMUser imUser = IMModel.getInstance().getIMUser(mUserName);

        if (imUser == null) {
            /*ac.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "请先注册账号", Toast.LENGTH_SHORT).show();
                }
            });*/
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    IMUser imUser = getIMUser(mUserName, mUserPwd);
                    loginHX(imUser,mUserPwd);
                }
            }.start();
        }else {
            loginHX(imUser,mUserPwd);
        }


    }

    /**
     * 获取环信服务器找到环信id
     * @param imUser
     * @param mUserPwd
     * 工作线程
     */
    private void loginHX(final IMUser imUser, String mUserPwd) {

        EMClient.getInstance().login(imUser.getHxId(), mUserPwd, new EMCallBack() {
            @Override
            public void onSuccess() {

                ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });

                IMModel.getInstance().addAccount(imUser);
                startActivity(new Intent(ac,MainActivity.class));
                ac.finish();
            }

            @Override
            public void onError(int i, String s) {
                ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


}
