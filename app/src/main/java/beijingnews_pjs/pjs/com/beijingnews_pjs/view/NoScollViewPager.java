package beijingnews_pjs.pjs.com.beijingnews_pjs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class NoScollViewPager extends ViewPager {

    public NoScollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

}
