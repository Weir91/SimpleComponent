package cn.weir.simplecomponent

import android.app.Application
import cn.weir.base.SimpleComponentInterface

/**
 * @author Weir.
 * @date   2019/2/13.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SimpleComponentInterface.setDebugMode(true)
        SimpleComponentInterface.init(this)

    }

}