package app.findbest.vip.project.model

import com.google.gson.JsonArray
import java.io.Serializable

data class PageModel(
    var page: Int,
    var size: Int,
    var total: Int,
    var data: JsonArray
): Serializable