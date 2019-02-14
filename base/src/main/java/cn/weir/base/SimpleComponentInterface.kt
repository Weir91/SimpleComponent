package cn.weir.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils


/**
 * @author Weir.
 * @date   2019/2/13.
 */
class SimpleComponentInterface {
    companion object {

        private var isDebugMode = false

        /**
         * 必须在[init]前调用，才有效果
         */
        fun setDebugMode(isDebug: Boolean) {
            this.isDebugMode = isDebug
        }

        /**
         * 框架初始化入口
         */
        fun init(application: Application) {
            //ARouter
            if (isDebugMode) {
                ARouter.openLog()
                ARouter.openDebug()
            }
            ARouter.init(application)

            //utilcode
            Utils.init(application)
            LogUtils.getConfig().setLogSwitch(isDebugMode).setStackDeep(2)
        }

    }
}