package app.findbest.vip.individual.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface individual {

    //获取个人信息
    @Headers("Content-Type: application/json")
    @GET("api/v1/user/information/detail")
    fun personInformation(): Observable<Response<JsonObject>>

    //意见反馈
    @Headers("Content-Type: application/json")
    @POST("api/v1/feedback")
    fun feedback(@Body array: RequestBody): Observable<Response<JsonObject>>
}