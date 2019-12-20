package app.findbest.vip.instance.model

import java.io.Serializable

data class ProjectItem(
    var format: String,
    var size : String,
    var name: String,
    var id: String,
    var time:String

): Serializable