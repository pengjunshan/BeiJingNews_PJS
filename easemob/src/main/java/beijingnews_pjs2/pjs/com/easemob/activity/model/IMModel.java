package beijingnews_pjs2.pjs.com.easemob.activity.model;

import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import beijingnews_pjs2.pjs.com.easemob.activity.bean.IMUser;
import beijingnews_pjs2.pjs.com.easemob.activity.db.UserAccountDB;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class IMModel {

    private static IMModel instance;

    private UserAccountDB userDB;

    private Context context;

    public static IMModel getInstance() {
        if (instance == null) {
            instance = new IMModel();
        }
        return instance;
    }

    public void onInit(Context context) {
        this.context = context;

        EMOptions option = new EMOptions();
        // 如果EaseUI返回为true，我们就可以正常的初始化我们业务模型Model，否则不做任何处理
        if (EaseUI.getInstance().init(context, option)) {
            userDB = new UserAccountDB(context);
        }

    }

    /**
     * 注册 保存用户
     *
     * @param immuser
     */
    public void addAccount(IMUser immuser) {
        userDB.addIMUser(immuser);
    }

    /**
     * 登录 获取用户
     *
     * @param mUserName
     */
    public IMUser getIMUser(String mUserName) {
        IMUser imuser = userDB.getIMUser(mUserName);
        return imuser;
    }

    public IMUser gtIMUserHxId(String HxId) {

        IMUser imuser =userDB.getIMUserHxId(HxId);
        return imuser;
    }
}
