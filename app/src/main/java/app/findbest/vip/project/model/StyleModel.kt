package app.findbest.vip.project.model

import com.google.gson.JsonArray
import java.io.Serializable

data class StyleModel(
    var id: Int,
    var name: String,
    var categoryId: Int,
    var lang: String
): Serializable