package app.findbest.vip.individual.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface individual {

    //获取案例列表
    @Headers("Content-Type: application/json")
    @GET("api/v1/user/information/detail")
    fun personInformation(): Observable<Response<JsonObject>>
}