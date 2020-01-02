package app.findbest.vip.login.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {


    //登录Apass账号
    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/login")
    fun loginApass(@Body array: RequestBody): Observable<Response<JsonObject>>

    //校验token
    @Headers("Content-Type: application/json")
    @GET("/api/v1/users/valid-token")
    fun isToken(): Observable<Response<JsonObject>>

    //重置密码
    @Headers("Content-Type: application/json")
    @PATCH("/api/v1/users/reset-password")
    fun resetPwd(@Body array: RequestBody): Observable<Response<JsonObject>>

    //登出
    @Headers("Content-Type: application/json")
    @PATCH("/api/v1/users/logout")
    fun logout(): Observable<Response<JsonObject>>
}