package app.findbest.vip.painter.model

import app.findbest.vip.project.model.PageModel
import com.google.gson.JsonArray
import java.io.Serializable

data class PainterInfo(
    var userId: String,
    var introduction: String,
    var userName: String,
    var logo: String,
    var star: Int,
    var country: String,
    var styles: ArrayList<String>,
    var works: PageModel
): Serializable