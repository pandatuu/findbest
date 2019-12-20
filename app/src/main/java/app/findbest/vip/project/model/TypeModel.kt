package app.findbest.vip.project.model

import java.io.Serializable

data class TypeModel(
    var id: Int,
    var name: String,
    var lang: String,
    var styleList: ArrayList<StyleModel>
): Serializable