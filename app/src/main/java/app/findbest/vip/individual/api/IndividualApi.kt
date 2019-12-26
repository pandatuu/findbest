package app.findbest.vip.individual.api

import app.findbest.vip.project.model.PageModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IndividualApi {

    //获取案例列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/list/consumer")
    fun getProjectSideList(
        @Query("page") page :Int,@Query("lang") lang :String): Observable<Response<PageModel>>

}