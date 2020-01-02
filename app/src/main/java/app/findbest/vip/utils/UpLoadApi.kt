package cgland.job.sk_android.utils

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpLoadApi {

    @POST("/api/v1/storage/chat-file")
    fun upLoadPic(@Body array : RequestBody): Observable<JsonObject>

    @POST("/api/v1/storage")
    fun upLoadVideo(@Body array : RequestBody): Observable<Response<JsonObject>>

    @POST("/api/v1/storage")
    fun upLoadFile(@Body array : RequestBody): Observable<Response<JsonObject>>
}