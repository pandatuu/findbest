package app.findbest.vip.message.model
import org.json.JSONObject
import java.io.Serializable


data class ChatRecordModel(
    val uid:String,
    val userName:String,
    val position:String,
    var avatar:String,
    val massage:String,
    val number:Int,
    val lastMsg: JSONObject,
    val companyName:String,
    val lastPositionId:String,
    val time:String

): Serializable

