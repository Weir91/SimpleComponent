package cn.company.figo.http

import cn.company.figo.http.result.FigoHttpResult
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Weir.
 * @date   2019/2/14.
 */
interface FigoApiService {

    @GET
    fun getData(@Url path: String, @QueryMap map: Map<String, String>): Observable<FigoHttpResult>

    @GET
    fun getListData(@Url path: String, @QueryMap map: Map<String, String>): Observable<FigoHttpResult>

    @POST
    @FormUrlEncoded
    fun postData(@Url path: String, @FieldMap map: Map<String, String>): Observable<FigoHttpResult>

    @POST
    @FormUrlEncoded
    fun postListData(@Url path: String, @FieldMap map: Map<String, String>): Observable<FigoHttpResult>


}