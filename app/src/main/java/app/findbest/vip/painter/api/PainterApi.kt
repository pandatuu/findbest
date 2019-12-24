package app.findbest.vip.painter.api

import app.findbest.vip.project.model.PageModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface PainterApi {

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter")
    fun getPainterList(@Query("type") type: Int, @Query("size") size: Int,
                       @Query("weight") weight: Int?, @Query("star") star: Int?,
                       @Query("amountCompleted") amountCompleted: Int?, @Query("timeOfEntity") timeOfEntity: Int?): Observable<Response<PageModel>>

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter")
    fun getPainterList(@Query("type") type : Int?, @Query("size") size : Int, @Query("page") page : Int,
                       @Query("weight") weight : Int?,@Query("star") star : Int?,
                       @Query("amountCompleted") amountCompleted : Int?,@Query("timeOfEntity") timeOfEntity : Int?): Observable<Response<PageModel>>

    //获取公司团队画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter")
    fun getCompanyPainterList(@Query("type") type : Int, @Query("size") size : Int,
                              @Query("weight") weight : Int?,@Query("star") star : Int?,
                              @Query("amountCompleted") amountCompleted : Int?,@Query("timeOfEntity") timeOfEntity : Int?): Observable<Response<PageModel>>

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter")
    fun getCompanyPainterList(@Query("type") type : Int, @Query("size") size : Int, @Query("page") page : Int,
                              @Query("weight") weight : Int?,@Query("star") star : Int?,
                              @Query("amountCompleted") amountCompleted : Int?,@Query("timeOfEntity") timeOfEntity : Int?): Observable<Response<PageModel>>

}