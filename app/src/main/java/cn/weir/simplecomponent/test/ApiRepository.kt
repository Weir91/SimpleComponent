package cn.weir.simplecomponent.test

import cn.weir.base.network.BaseRetrofit
import io.reactivex.Observable

/**
 * @author Weir.
 * @date   2019/2/14.
 */
class ApiRepository {

    private val retrofit = BaseRetrofit.getRetrofit("http://127.0.0.1:8080/")
    private val apiService = retrofit.create(ApiService::class.java)

    fun getUser(): Observable<String> {
        val user = apiService.getUser()
        return user
    }

}