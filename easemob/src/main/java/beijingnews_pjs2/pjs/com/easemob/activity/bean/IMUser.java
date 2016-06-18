package beijingnews_pjs2.pjs.com.easemob.activity.bean;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class IMUser {

    private String appUser;
    private String hxId;
    private String nick;
    private String avartar;

    public IMUser() {

    }

    public IMUser(String appUser) {
        this.appUser = appUser;
        hxId=appUser;
        nick=appUser;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }
}
