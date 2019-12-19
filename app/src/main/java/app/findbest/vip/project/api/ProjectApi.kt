package app.findbest.vip.project.api

import app.findbest.vip.project.model.PageModel
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProjectApi {

    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/page")
    fun getProjectList(@Query("page") page : Int, @Query("size") size : Int): Observable<Response<PageModel>>

}