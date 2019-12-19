package app.findbest.vip.instance.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface InstanceApi {

    //获取案例列表
    @Headers("Content-Type: application/json")
    @GET("/mock/39/works")
    fun instanceList(
        @Query("category") category :Int?,
        @Query("styles") styles :Int?,
        @Query("page") page :Int,
        @Query("pagesize") pagesize :Int,
        @Query("content") content :String?

        ):  Observable<Response<String>>



}