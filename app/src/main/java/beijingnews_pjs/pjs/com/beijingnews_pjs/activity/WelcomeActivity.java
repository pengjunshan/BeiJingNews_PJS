package beijingnews_pjs.pjs.com.beijingnews_pjs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.SPUtil;

public class WelcomeActivity extends Activity {

    private RelativeLayout rl_welcome;
    private AnimationSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        rl_welcome = (RelativeLayout)findViewById(R.id.rl_welcome);

        //设置动画
        setAnimation();

        //设置监听
        set.setAnimationListener(new MyAnimationListener());

    }

    /**
     * 设置动画
     */
    private void setAnimation() {
        //方式一 使用xml文件
      /*  Animation animation= AnimationUtils.loadAnimation(this,R.anim.welcome_animotion);

        rl_welcome.startAnimation(animation);
*/
        //方式二
        //旋转
        RotateAnimation ra=new RotateAnimation(0,720, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        //缩放
        ScaleAnimation sa=new ScaleAnimation(0.5f,1,0.5f,1,0.5f,0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        //透明度
        AlphaAnimation aa=new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        set=new AnimationSet(true);
        set.addAnimation(ra);
        set.addAnimation(sa);
        set.addAnimation(aa);

        rl_welcome.startAnimation(set);
    }

    class MyAnimationListener implements Animation.AnimationListener {


        /**
         * 动画开始的时候回调
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 动画 结束的时候回调
        * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {

            boolean isOneOpen= SPUtil.getIsOpen(WelcomeActivity.this,GuideActivity.IS_ONEOPEN);
            Log.i("TAG", "isOneOpen="+isOneOpen);
            if(isOneOpen) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }else {
                Intent intet=new Intent(WelcomeActivity.this,GuideActivity.class);
                startActivity(intet);
            }

            finish();
        }

        /**
         * 动画重复的时候回调
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
