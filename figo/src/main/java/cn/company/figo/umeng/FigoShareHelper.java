package cn.company.figo.umeng;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


public class FigoShareHelper {

    private static SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA};

    /**
     * 打开图文分享
     *
     * @param activity
     * @param title    标题
     * @param content  内容
     * @param imageUrl 图片链接
     * @param webUrl   分享打开链接
     * @param listener 分享回调
     */
    public static void openDefaultHelper(Activity activity, String title, String content, String imageUrl, String webUrl, UMShareListener listener) {
        openDefaultHelper(activity, title, content, imageUrl, webUrl, listener, shareMedia);
    }

    public static void openDefaultHelper(Activity activity, String title, String content, String imageUrl, String webUrl, UMShareListener listener, SHARE_MEDIA[] shareMedia) {
        //网络图片
        UMImage image = new UMImage(activity, imageUrl);
        UMWeb web = new UMWeb(webUrl);
        //标题
        web.setTitle(title);
        //缩略图
        web.setThumb(image);
        //描述
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(shareMedia)
                .setCallback(listener)
                .open();
    }

    /**
     * 打开纯图片分享
     *
     * @param activity
     * @param imageUrl 图片链接
     * @param listener 分享回调
     */
    public static void openImageShareHelper(Activity activity, String imageUrl, UMShareListener listener) {
        openImageShareHelper(activity, imageUrl, listener, shareMedia);
    }

    public static void openImageShareHelper(Activity activity, String imageUrl, UMShareListener listener, SHARE_MEDIA[] shareMedia) {
        //网络图片
        UMImage image = new UMImage(activity, imageUrl);
        new ShareAction(activity)
                .withMedia(image)
                .setDisplayList(shareMedia)
                .setCallback(listener)
                .open();
    }

}
