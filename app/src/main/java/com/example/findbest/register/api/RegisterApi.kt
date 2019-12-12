package com.example.findbest.register.api

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApi {

    //发送验证码
    @Headers("Content-Type: application/json")
    @POST("/api/v1/sms")
    fun sendvCode(@Body array: RequestBody): Observable<Response<String>>

}