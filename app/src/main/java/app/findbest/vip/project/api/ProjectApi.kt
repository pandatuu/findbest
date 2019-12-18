package app.findbest.vip.project.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ProjectApi {

    //获取项目
    @Headers("Content-Type: application/json")
    @POST("/api/v1/projects/page")
    fun getProjectList(@Body array: RequestBody): Observable<Response<JsonObject>>
}