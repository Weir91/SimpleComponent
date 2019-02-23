package cn.weir.base.account

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils

/**
 * @author Weir.
 * @date   2019/2/22.
 */
object TokenRepository {

    fun isLogin(): Boolean {
        val token = SPUtils.getInstance("token").getString("token")
        return !StringUtils.isEmpty(token)
    }

    fun getToken(): String {
        val token = SPUtils.getInstance("token").getString("token")
        return token
    }

    fun setToken(token: String?) {
        if (StringUtils.isEmpty(token)) {
            SPUtils.getInstance("token").remove("token")
        } else {
            SPUtils.getInstance("token").put("token", token ?: "", true)
        }
    }
}