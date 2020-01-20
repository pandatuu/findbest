package app.findbest.vip.instance.model

import java.io.Serializable

data class InstanceModel(
    var style: MutableList<InstanceStyle>,
    var workId: String,
    var workName: String,
    var width: Int,
    var height: Int,
    var url: String,
    var description: String,
    var id: String,
    var name: String,
    var onOpen: Int,
    var logo: String,
    var category: InstanceCategory
): Serializable