package beijingnews_pjs.pjs.com.beijingnews_pjs.utils;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class URL {

//    public static final String BASE_URL = "http://192.168.3.104:8080/zhbj";
    public static final String BASE_URL = "http://192.168.56.1:8080/zhbj";

    //本地的模拟器，访问本地电脑服务器
    /**
     * 新闻中心的连接地址
     */
    public static final String NEWSCENTER_URL = BASE_URL + "/categories.json";

    /**
     * 图组的数据
     */
    public static final String PHOTOS_URL = BASE_URL + "/photos/photos_1.json";

    /**
     * 购物的连接
     */
    public static final String SHOPPING_URL = "http://112.124.22.238:8081/course_api/wares/hot?pageSize=8&curPage=";


}
