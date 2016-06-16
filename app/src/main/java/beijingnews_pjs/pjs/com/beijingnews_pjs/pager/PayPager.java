package beijingnews_pjs.pjs.com.beijingnews_pjs.pager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import beijingnews_pjs.pjs.com.beijingnews_pjs.R;
import beijingnews_pjs.pjs.com.beijingnews_pjs.adapter.PayPagerAdapter;
import beijingnews_pjs.pjs.com.beijingnews_pjs.base.BasePager;
import beijingnews_pjs.pjs.com.beijingnews_pjs.bean.ShoppingCart;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pay.PayResult;
import beijingnews_pjs.pjs.com.beijingnews_pjs.pay.SignUtils;
import beijingnews_pjs.pjs.com.beijingnews_pjs.utils.CartProvider;
import beijingnews_pjs.pjs.com.beijingnews_pjs.view.NumberAddSubView;

/**
 * Created by pjs984312808 on 2016/6/3.
 */
public class PayPager extends BasePager {

    /**
     * 付费状态
     */
    public static final String STATE_EDITE = "state_edite";
    /**
     * 删除状态
     */
    public static final String STATE_DELETE = "state_delete";

    private NumberAddSubView numberAddSubView;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox checkbox_all;

    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;

    @ViewInject(R.id.btn_order)
    private Button btn_order;

    @ViewInject(R.id.btn_delete)
    private Button btn_delete;

    @ViewInject(R.id.btn_edite)
    private Button btn_edite;

    private PayPagerAdapter adapter;

    private List<ShoppingCart> payDatas;

    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM5iFImoWzdFC7ud\n" +
            "kllBxZYJMySVxpeBYNOLFWXSwLCWY5DxsKO32mmjobwLOAPenIdSbZOnziyHiORy\n" +
            "HKxJT7rKtE126JkmcLBP8Zf4ZvDeFpDFnYD9auNEjtfCK9X5M477US4SQgIxNjdG\n" +
            "f7EcjoDbVh75AIrHVrcUdk753aqTAgMBAAECgYEAlEaZNNGVP19G77NkyuXbPFE9\n" +
            "wkItXPpiA3pAlFhXgkd6H0/VtbpInG8oqv5wby9HzF1nRpdgJFP4AREPEqTVNzdT\n" +
            "aUa3xby9aQDd10zB3wXVeQazN2bRP06BgMN3Vebl5srHcUBjmzZLQgra8PGggm74\n" +
            "Ch/Y5lWbO7n4RLrfrwkCQQDm2pbSzdcqz04rlU0GBJ26FAg9coYSVJHDZXEd2z51\n" +
            "+OaMWOXGCvGbISL4i/f9Gr3f2yWlHa3nqGP3VVTuPJ5/AkEA5N0egLyaaOsSdMph\n" +
            "NqIthLWReYvqWgZvsSThoFyRR9Nn8RpDao3fq5rSV63LOZMLsaCaTQOCxl5mOUEa\n" +
            "/4eR7QJBAOGlygTVlLrQlUz+i6IVQwLOb9t4JNqn9S5z6mRPmDcCCoAmqLmymsSS\n" +
            "WDdvwP+ScwPrlllVsFWef9AThYe0kwsCQA8ho3ulfJwFNsIsA0Nmc5X7nzOnEYaE\n" +
            "OGxA4P4GQMC79HpXXy+zU5937AJKBk63LyW+VZVT7xiIcz/D0zIj130CQD7OpIAL\n" +
            "mezZWO6zV/mBMTxOgaZphkmdvsA8XvZrCzUtPToVDimKmgQX2oNrNneHtCOdPp+l\n" +
            "MS4CgOXtanHTNL0=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOYhSJqFs3RQu7nZJZQcWWCTMk\n" +
            "lcaXgWDTixVl0sCwlmOQ8bCjt9ppo6G8CzgD3pyHUm2Tp84sh4jkchysSU+6yrRN\n" +
            "duiZJnCwT/GX+Gbw3haQxZ2A/WrjRI7XwivV+TOO+1EuEkICMTY3Rn+xHI6A21Ye\n" +
            "+QCKx1a3FHZO+d2qkwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    public PayPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("支付");

        View view = View.inflate(context, R.layout.paypager, null);

        x.view().inject(this, view);

        fl_content_basepager.removeAllViews();
        fl_content_basepager.addView(view);

        btn_edite.setVisibility(View.VISIBLE);
        btn_edite.setTag(STATE_EDITE);

        CartProvider cartProvider = new CartProvider(context);
        payDatas = cartProvider.getAllData();

        adapter = new PayPagerAdapter(context, payDatas,tv_total_price);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        setOnClick();

    }



    private void setOnClick() {

        /**
         * 支付
         */
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });

        /**
         * 删除
         */
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteData();
            }
        });

        /**
         * 编辑状态
         */
        btn_edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String btnTag = (String) btn_edite.getTag();
                if(btnTag==STATE_EDITE) {
                    //改变tag
                    btn_edite.setTag(STATE_DELETE);
                    btn_edite.setText("完成");
                    //隐藏付款按钮
                    btn_order.setVisibility(View.GONE);
                    //显示删除按钮
                    btn_delete.setVisibility(View.VISIBLE);

                    //全部设置非选中
                    adapter.setCheckBoxAll(false);
                    checkbox_all.setChecked(false);


                }else {
                    //改变tag
                    btn_edite.setTag(STATE_EDITE);
                    btn_edite.setText("编辑");
                    //隐藏付款按钮
                    btn_order.setVisibility(View.VISIBLE);
                    //显示删除按钮
                    btn_delete.setVisibility(View.GONE);

                    //全部设置选中
                    adapter.setCheckBoxAll(true);
                    checkbox_all.setChecked(true);
                }

            }
        });

        /**
         * 点击全选 非全选
         */
        checkbox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCheckBoxAll(checkbox_all.isChecked());
            }
        });

        /**
         * 点击某项 设置是否选中
         */
        adapter.setOnItemClickListener(new PayPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                boolean isCheck = payDatas.get(position).isChecked();
                adapter.setItemisCheck(position,!isCheck);
            }
        });
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("第一次支付宝", "该测试商品的详细描述1", "0.01");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
