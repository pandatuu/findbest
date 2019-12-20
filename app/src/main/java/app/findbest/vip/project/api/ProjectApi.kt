package app.findbest.vip.project.api

import app.findbest.vip.project.model.PageModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProjectApi {

    //获取项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/page")
    fun getProjectList(@Query("page") page : Int, @Query("size") size : Int): Observable<Response<PageModel>>

    //根据分类标签，获取项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/page")
    fun getProjectListByCategory(@Query("page") page : Int, @Query("size") size : Int, @Query("category") category : Int, @Query("style") style : Int): Observable<Response<PageModel>>

    //根据搜索，获取项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/page")
    fun getProjectListBySearch(@Query("page") page : Int, @Query("size") size : Int, @Query("searchKey") searchKey : String): Observable<Response<PageModel>>


    //获取分类标签
    @Headers("Content-Type: application/json")
    @GET("/api/v1/styles/category-list")
    fun getTypeList(@Query("lang") language : String): Observable<Response<JsonArray>>

    //获取分类标签
    @Headers("Content-Type: application/json")
    @GET("/api/v1/styles/styles-list")
    fun getStyleList(@Query("lang") language : String, @Query("categoryId") categoryId : Int): Observable<Response<JsonArray>>


}