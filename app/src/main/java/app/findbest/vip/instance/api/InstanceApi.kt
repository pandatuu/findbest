package app.findbest.vip.instance.api

import app.findbest.vip.project.model.PageModel
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface InstanceApi {

    //根据筛选获取案例列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/works")
    fun instanceList(@Query("page") page :Int, @Query("size") size: Int,
                     @Query("category") category :Int?, @Query("styles") styles :Int?):  Observable<Response<PageModel>>

    //获取案例列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/works")
    fun instanceList(@Query("page") page :Int, @Query("size") size :Int):  Observable<Response<PageModel>>

    //根据搜索获取案例列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/works")
    fun instanceListSearch(@Query("page") page :Int, @Query("size") size :Int,
                           @Query("content") content :String?):  Observable<Response<PageModel>>


    //获取邀请画师的项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/published")
    fun getInviteProjectList(
        @Query("page") page :Int,
        @Query("size") size :Int,
        @Query("userId") userId :String

    ):  Observable<Response<String>>


    //邀请画师
    @Headers("Content-Type: application/json")
    @POST("/api/v1/invites")
    fun invitePainterAndGroup(
        @Body array: RequestBody
    ):  Observable<Response<String>>



}