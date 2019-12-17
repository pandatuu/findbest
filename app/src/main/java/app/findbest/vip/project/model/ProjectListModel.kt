package app.findbest.vip.project.model

import java.io.Serializable

data class ProjectListModel(
    var title: String,
    var isDefend: Boolean,
    var pixel: String,
    var format: String,
    var date: String,
    var country: String,
    var styleList: ArrayList<String>,
    var price: Float
): Serializable