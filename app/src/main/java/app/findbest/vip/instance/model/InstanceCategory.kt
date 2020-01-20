package app.findbest.vip.instance.model

import java.io.Serializable

data class InstanceCategory(
    val id: String,
    val name: String,
    val del: Boolean
): Serializable