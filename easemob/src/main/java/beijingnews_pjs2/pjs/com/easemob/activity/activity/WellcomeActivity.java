package beijingnews_pjs2.pjs.com.easemob.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hyphenate.chat.EMClient;

import beijingnews_pjs2.pjs.com.easemob.R;

public class WellcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        },2000);

    }

    private void init() {
        if(EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
