package app.findbest.vip.individual.api

import app.findbest.vip.project.model.PageModel
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