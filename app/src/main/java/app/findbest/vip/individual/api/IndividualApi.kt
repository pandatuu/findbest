package app.findbest.vip.individual.api

import app.findbest.vip.project.model.PageModel
import app.findbest.vip.project.model.ProjectInfoModel
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface IndividualApi {

    //获取项目方的项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/list/consumer")
    fun getProjectSideList(@Query("page") page :Int, @Query("lang") lang :String, @Query("status") status :Int?): Observable<Response<PageModel>>

    //项目方获取项目详情
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/view/{id}")
    fun getProjectSideDetails(@Path("id") id :String): Observable<Response<ProjectInfoModel>>

    //项目方获取项目应征列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/applies/project/{projectId}")
    fun getProjectSideApplies(@Path("projectId") projectId :String, @Query("page") page :Int,@Query("size") size :Int): Observable<Response<PageModel>>

    //项目方获取邀请列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/invites/consumer/{id}")
    fun getProjectSideInviteById(@Path("id") id :String, @Query("page") page :Int, @Query("size") size :Int): Observable<Response<JsonObject>>

    //项目方拒绝应征
    @Headers("Content-Type: application/json")
    @PATCH("/api/v1/applies/reject/{commitId}")
    fun refuseApplies(@Path("commitId") commitId :String): Observable<Response<JsonObject>>



    //项目方取消邀请
    @Headers("Content-Type: application/json")
    @PATCH("/api/v1/invites/cancel/{inviteId}")
    fun cancelInvite(@Path("inviteId") inviteId :String): Observable<Response<JsonObject>>

    //获取画师的项目列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/projects/list")
    fun getPainterSideList(@Query("page") page :Int, @Query("lang") lang :String, @Query("status") status :Int?): Observable<Response<PageModel>>

    //获取画师项目的邀请列表
    @Headers("Content-Type: application/json")
    @GET("/api/v1/invites/invite/{id}")
    fun getPainterSideInviteById(@Path("id") id :String, @Query("lang") lang :String): Observable<Response<JsonObject>>

    //画师方拒绝邀请
    @Headers("Content-Type: application/json")
    @PATCH("/api/v1/invites/reject/{inviteId}")
    fun painterRefuseInvite(@Path("inviteId") inviteId :String, @Body body : RequestBody): Observable<Response<JsonObject>>
}