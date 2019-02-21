package cn.company.figo.oss;


import android.text.TextUtils;

/**
 * 上传图片资源到阿里云oss所用到的bean
 */
public class OssUploadBean {
    public String path;
    public String ossPath;
    public String failReason;

    public boolean isUploadSuccess() {
        return !TextUtils.isEmpty(ossPath);
    }
}
