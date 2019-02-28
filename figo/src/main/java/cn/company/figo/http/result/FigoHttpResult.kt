package cn.company.figo.http.result

import com.google.gson.JsonObject

//返回值的统一处理基类
class FigoHttpResult {
    var code: Int = 0
    var msg: String = ""
    var data: JsonObject = JsonObject()


    val isSuccess: Boolean
        get() = if (code == 0) true else false

    val isTokenInvalid: Boolean
        get() = if (code == 401) true else false
}