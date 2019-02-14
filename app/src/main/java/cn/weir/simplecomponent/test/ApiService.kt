package cn.weir.simplecomponent.test

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author Weir.
 * @date   2019/2/14.
 */
interface ApiService {

    @GET("/user")
    fun getUser(): Observable<String>

}