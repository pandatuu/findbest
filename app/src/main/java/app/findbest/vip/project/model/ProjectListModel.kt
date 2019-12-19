package app.findbest.vip.project.model

import com.google.gson.JsonArray
import java.io.Serializable

data class ProjectListModel(
    var id: String,
    var title: String,
    var isDefend: Boolean,
    var size: String,
    var format: String,
    var commitAt: Long,
    var country: String,
    var styleList: JsonArray,
    var minPrice: Float,
    var maxPrice: Float
): Serializable