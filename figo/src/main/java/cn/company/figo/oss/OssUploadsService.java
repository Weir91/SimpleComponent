package cn.company.figo.oss;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import cn.company.figo.R;

/**
 * 上传文件
 *
 * @author shana
 */
public class OssUploadsService extends Service {

    private UploadListener mListener;
    private OSS mOss;
    private OSSAsyncTask mCurrentOssTask;

    private List<OssUploadBean> mOssUploadBeans;
    private String[] mFilePaths;

    private final IBinder mIBinder = new OssUploadServiceBind();
    private OssUploadType mOssUploadType = OssUploadType.IMAGE;
    private String userId = "unknown";

    public class OssUploadServiceBind extends Binder {
        public OssUploadsService getService() {
            return OssUploadsService.this;
        }
    }

    /**
     * 取消标志
     * true : 手动取消，不再重试 false ： 默认
     */
    private boolean flagCancel = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    /**
     * 启动服务
     */
    public static void start(Context context, ServiceConnection serviceConnection, int type) {
        Intent starter = new Intent(context, OssUploadsService.class);
        context.bindService(starter, serviceConnection, type);
    }

    /**
     * 启动图片上传
     *
     * @param context  上下文
     * @param userId   用户ID，未登录下填 null
     * @param uris     Uri列表
     * @param listener 回调
     */
    public void startUpload(Context context, String userId, List<Uri> uris, UploadListener listener) {
        mOssUploadBeans = new ArrayList<>(uris.size());
        String[] filePaths = new String[uris.size()];
        for (int i = 0; i < uris.size(); i++) {
            if (uris.get(i).toString().startsWith("content")) {
                filePaths[i] = getRealPathFromURI(context, uris.get(i));
            } else {
                filePaths[i] = uris.get(i).toString();
            }
        }
        startUpload(userId, filePaths, listener);
    }

    /**
     * 启动图片上传
     *
     * @param userId    用户ID，未登录下填 null
     * @param filePaths 文件数据，数据格式
     * @param listener  回调
     */
    public void startUpload(String userId, String[] filePaths, UploadListener listener) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(getString(R.string.OSS_ACCESS_KEY), getString(R.string.OSS_SECRET_KEY));
        mOss = new OSSClient(getApplicationContext(), getString(R.string.OSS_HOST), credentialProvider);
        this.userId = userId != null ? userId : "unknown";
        this.mOssUploadType = OssUploadType.IMAGE;
        mListener = listener;
        mOssUploadBeans = new ArrayList<>();
        mFilePaths = filePaths;
        if (filePaths != null && filePaths.length > 0) {
            for (String filePath : filePaths) {
                OssUploadBean bean = new OssUploadBean();
                bean.path = filePath;
                mOssUploadBeans.add(bean);
            }
            for (int i = 0; i < mOssUploadBeans.size(); i++) {
                handleImages(i);
            }
        }
    }

    /**
     * 上传视频
     *
     * @param userId        用户id
     * @param videoFilePath 本地视频地址
     * @param listener      上传回调
     */
    public void startVideoUpload(String userId, String videoFilePath, UploadListener listener) {
        this.userId = userId != null ? userId : "unknown";
        this.mListener = listener;
        this.mOssUploadType = OssUploadType.VIDEO;
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(getString(R.string.OSS_ACCESS_KEY), getString(R.string.OSS_SECRET_KEY));
        mOss = new OSSClient(getApplicationContext(), getString(R.string.OSS_HOST), credentialProvider);
        OssUploadBean bean = new OssUploadBean();
        bean.path = videoFilePath;
        ossUpload(bean, 0);
    }

    /**
     * 获取Uri文件的绝对路径
     */
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contentUri.toString();
    }

    /**
     * 图片处理，压缩操作
     *
     * @param i 下标
     */
    private void handleImages(int i) {
        OssUploadBean bean = mOssUploadBeans.get(i);
        bean.path = mFilePaths[i];
        ossUpload(bean, i);


//        // 图片压缩处理
//        // 创建缓存路径
//        File f = new File(DataInterface.context.getExternalCacheDir(), String.format("tempPic%s.jpg", i));
//        try {
//            // 创建输入流
//            FileOutputStream fos = new FileOutputStream(f);
//            //                file to bitmap
//            Bitmap bitmap = BitmapFactory.decodeFile(mFilePaths[i]);
//            // 输出到缓存路径（并创建对应文件）
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
//            // 关闭流
//            fos.flush();
//            fos.close();
//            if (f.exists()) {
//                OssUploadBean bean = mOssUploadBeans.get(i);
//                bean.path = f.getPath();
//                ossUpload(bean, i);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            mListener.onFail(i, e.getMessage());
//        }
    }

    /**
     * 文件上传
     * 阿里云 方式
     *
     * @param bean  上传对象
     * @param index 列表下标
     */
    private void ossUpload(final OssUploadBean bean, final int index) {
        flagCancel = false;
        String suffix = FileUtils.getFileExtension(bean.path);
        PutObjectRequest put = new PutObjectRequest(getString(R.string.OSS_BUCKET), getOssPathKey(mOssUploadType, suffix, userId), bean.path);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                float progress = ((float) currentSize / (float) totalSize) * 100f;
                LogUtils.i("PutObject", "index:" + index + "currentSize: " + currentSize + " totalSize: " + totalSize + "\n progress:" + (int) progress);
                mListener.onProgress(index, progress);
            }
        });
        mCurrentOssTask = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                LogUtils.d("PutObject", "UploadSuccess");
                LogUtils.d("ETag", putObjectResult.getETag());
                LogUtils.d("RequestId", putObjectResult.getRequestId());
                bean.ossPath = putObjectRequest.getObjectKey();

                mListener.onSuccess(index, bean);
                checkUploadFinish();
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException clientException, ServiceException serviceException) {
                // 请求异常
                if (clientException != null) {
                    // 本地异常如网络异常等
                    clientException.printStackTrace();
                    bean.failReason = "本地异常如网络异常等";
                }
                if (serviceException != null) {
                    // 服务异常
                    bean.failReason = "服务器异常";
                    LogUtils.e("ErrorCode", serviceException.getErrorCode());
                    LogUtils.e("RequestId", serviceException.getRequestId());
                    LogUtils.e("HostId", serviceException.getHostId());
                    LogUtils.e("RawMessage", serviceException.getRawMessage());
                }

                mListener.onFail(index, bean.failReason);
                if (!flagCancel) {
                    //重新上传
                    ossUpload(bean, index);
                }
            }
        });
    }

    /**
     * 检查是否全部上传完成
     */
    private void checkUploadFinish() {
        for (OssUploadBean imageBean : mOssUploadBeans) {
            boolean uploadSuccess = imageBean.isUploadSuccess();
            if (!uploadSuccess) {
                return;
            }
        }
        mListener.onFinal(mOssUploadBeans);
    }

    /**
     * 根据上传类型，生成保存文件名
     *
     * @param ossUploadType 类型
     * @param suffix        后缀 eg. .png
     * @param userId        用户ID，用于识别文件的上传者
     * @return
     */
    private String getOssPathKey(OssUploadType ossUploadType, String suffix, String userId) {
        String mainCatalog = null;
        switch (ossUploadType) {
            case VIDEO:
                mainCatalog = "video";
                break;
            case IMAGE:
                mainCatalog = "image";
                break;
            default:
                mainCatalog = "unknow";
                break;
        }
        String dayPath = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
        int radiusNumber = new Random().nextInt(9999 - 1000) + 1000;
        String path = String.format("%s%s%s_%d_%d.%s", mainCatalog, dayPath, userId, System.currentTimeMillis() / 1000, radiusNumber, suffix);
        System.out.print("\npath:" + path);
        return path;
    }

    /**
     * 服务解绑 清除请求
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 取消当前的上传操作
     */
    public void onCancel() {
        if (mCurrentOssTask != null) {
            flagCancel = true;
            mCurrentOssTask.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface UploadListener {
        /**
         * 上传全部完成
         *
         * @param images 上传后的列表对象
         */
        void onFinal(List<OssUploadBean> images);

        /**
         * 每一个文件上传成功都会进行回调
         *
         * @param index     列表下标
         * @param imageBean 上传成功后生成对象
         */
        void onSuccess(int index, OssUploadBean imageBean);

        /**
         * 每一个文件上传失败都会进行回调
         *
         * @param index 列表下标
         * @param info  上传失败原因
         */
        void onFail(int index, String info);

        /**
         * 上传进度
         *
         * @param index    列表下标
         * @param progress 当前文件上传进度
         */
        void onProgress(int index, float progress);
    }
}
