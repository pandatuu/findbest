package app.findbest.vip.painter.api

import app.findbest.vip.painter.model.PainterInfo
import app.findbest.vip.project.model.PageModel
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface PainterApi {

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/app/list")
    fun getPainterList(@Query("type") type: Int, @Query("size") size: Int, @Query("page") page : Int,
                       @Query("weight") weight: Int): Observable<Response<PageModel>>

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/app/list")
    fun getPainterList(@Query("type") type : Int?, @Query("size") size : Int, @Query("page") page : Int,
                       @Query("weight") weight : Int, @Query("category") category: Int, @Query("styles") styles: Int): Observable<Response<PageModel>>

    //获取公司团队画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/app/list")
    fun getCompanyPainterList(@Query("type") type : Int, @Query("size") size : Int,@Query("page") page : Int,
                              @Query("weight") weight : Int): Observable<Response<PageModel>>

    //获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/app/list")
    fun getCompanyPainterList(@Query("type") type : Int, @Query("size") size : Int, @Query("page") page : Int,
                              @Query("weight") weight : Int, @Query("category") category: Int, @Query("styles") styles: Int): Observable<Response<PageModel>>

    //通过搜索获取画师列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/app/list")
    fun getPainterListBySearch(@Query("type") type : Int?, @Query("size") size : Int, @Query("page") page : Int,
                       @Query("weight") weight : Int, @Query("content") content: String): Observable<Response<PageModel>>


    //获取画师详情
    @Headers("Content-Type: application/json")
    @GET("/api/v1/company-painter/detail/style")
    fun getPainterInfo(@Query("userId") userId : String, @Query("lang") lang : String, @Query("page") page : Int): Observable<Response<PainterInfo>>

}