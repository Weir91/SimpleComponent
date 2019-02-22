package cn.company.figo.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.company.figo.pay.event.PayFailEvent;
import cn.company.figo.pay.event.PaySuccessEvent;


public class PayHelper {


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    final IWXAPI msgApi;
    PayReq req = new PayReq();
    private Context mContext;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult2 payResult = new PayResult2((Map<String, String>) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.showShort("支付成功");
                        EventBus.getDefault().post(new PaySuccessEvent());
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showShort("支付结果确认中");
                            PayFailEvent event = new PayFailEvent();
                            event.info = "支付结果确认中";
                            EventBus.getDefault().post(event);
                        } else {
                            LogUtils.i("pay resultStatus:", resultStatus);

                            ToastUtils.showShort("支付失败");
                            PayFailEvent event = new PayFailEvent();
                            if (TextUtils.equals(resultStatus, "6001")) {
                                event.info = "用户取消";
                            } else if (TextUtils.equals(resultStatus, "4000")) {
                                event.info = "支付失败";
                            } else if (TextUtils.equals(resultStatus, "6002")) {
                                event.info = "网络连接出错";
                            } else if (TextUtils.equals(resultStatus, "6004")) {
                                event.info = "支付结果未知";
                            } else {
                                event.info = "支付失败";
                            }
                            EventBus.getDefault().post(event);
                        }

                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private Activity activity;

    public PayHelper(Activity activity, Context context, String weixinAppId) {
        this.mContext = context;
        this.activity = activity;
        msgApi = WXAPIFactory.createWXAPI(mContext, weixinAppId, false);
        msgApi.registerApp(weixinAppId);
    }

    public void openAlipay(final String pay) {
        ToastUtils.showShort("正在打开支付宝支付…");
        LogUtils.i("tag", pay);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(pay, true);
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

    public void openWechatPay(JSONObject data) {
        ToastUtils.showShort("正在打开微信支付…");
        try {
            req.appId = data.getString("appid");
            req.partnerId = data.getString("partnerid");
            req.prepayId = data.getString("prepayid");
            req.packageValue = data.getString("package");
            req.nonceStr = data.getString("noncestr");
            req.timeStamp = data.getString("timestamp");
            req.sign = data.getString("sign");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e("orion", req.toString());
        msgApi.sendReq(req);
    }

    public void openWechatPay(HashMap<String, String> data) {
        ToastUtils.showShort("正在打开微信支付…");
        try {
            req.appId = data.get("appid");
            req.partnerId = data.get("partnerid");
            req.prepayId = data.get("prepayid");
            req.packageValue = data.get("package");
            req.nonceStr = data.get("noncestr");
            req.timeStamp = data.get("timestamp");
            req.sign = data.get("sign");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("orion", req.toString());
        msgApi.sendReq(req);
    }

}
