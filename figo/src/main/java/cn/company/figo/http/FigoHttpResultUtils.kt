package cn.company.figo.http

import cn.company.figo.http.result.FigoHttpResult
import cn.company.figo.http.result.ListData
import com.google.gson.Gson
import io.reactivex.functions.Function

/**
 * @author Weir.
 * @date   2019/2/27.
 */
object FigoHttpResultUtils {

    inline fun <reified T> HandleResult(): Function<FigoHttpResult, T> {
        return Function {
            if (it.isSuccess) {
                Gson().fromJson(it.data, T::class.java)
            } else if (it.isTokenInvalid) {
                throw Exception("您的登录账号已过期，请重新登录")
            } else {
                throw Exception(it.msg)
            }
        }
    }

    inline fun <reified T> HandleResultList(): Function<FigoHttpResult, ListData<T>> {
        return Function {
            if (it.isSuccess) {
                val listData = ListData<T>()
                val data = it.data

                listData.totalElements = data.get("totalElements").asInt
                listData.totalPages = data.getAsJsonPrimitive("totalPages").asInt
                listData.last = data.getAsJsonPrimitive("last").asBoolean
                listData.first = data.getAsJsonPrimitive("first").asBoolean
                listData.numberOfElements = data.getAsJsonPrimitive("numberOfElements").asInt
                listData.size = data.getAsJsonPrimitive("size").asInt
                listData.number = data.getAsJsonPrimitive("number").asInt

                val jsonArray = data.getAsJsonArray("content")
                jsonArray.forEach {
                    listData.content.add(Gson().fromJson(it, T::class.java))
                }
                listData
            } else if (it.isTokenInvalid) {
                throw Exception("您的登录账号已过期，请重新登录")
            } else {
                throw Exception(it.msg)
            }
        }
    }
}