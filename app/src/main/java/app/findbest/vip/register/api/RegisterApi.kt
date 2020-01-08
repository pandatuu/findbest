package app.findbest.vip.register.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApi {

    //发送验证码
    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/register")
    fun registerUser(@Body array: RequestBody): Observable<Response<String>>

    //完善findbest信息
    @Headers("Content-Type: application/json")
    @POST("/api/v1/user/information")
    fun information(@Body array: RequestBody): Observable<Response<JsonObject>>

    //发送验证码
    @Headers("Content-Type: application/json")
    @POST("/api/v1/sms")
    fun sendvCode(@Body array: RequestBody): Observable<Response<String>>

    //发送验证码
    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/refresh-token")
    fun refreshToken(@Body array: RequestBody): Observable<Response<JsonObject>>
}