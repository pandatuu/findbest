package app.findbest.vip.instance.model

import java.io.Serializable

data class InstanceStyle(
    val id: String,
    val name: String,
    val del: Boolean
): Serializable