package app.findbest.vip.login.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {


    //登录Apass账号
    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/login")
    fun loginApass(@Body array: RequestBody): Observable<Response<JsonObject>>

    //校验token
    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/valid-token")
    fun isToken(): Observable<Response<JsonObject>>
}