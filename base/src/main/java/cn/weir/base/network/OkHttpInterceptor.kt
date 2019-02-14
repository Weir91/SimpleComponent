package cn.weir.base.network

import com.blankj.utilcode.util.LogUtils
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Weir.
 * @date   2019/2/14.
 */
object OkHttpInterceptor {

    @JvmStatic
    fun getHttpLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor {
            LogUtils.i(it)
        }
        if (isDebug) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

}