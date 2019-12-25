package app.findbest.vip.message.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class ChatRecordModelTest(
    val uid:String,
    val userName:String,
    val position:String,
    var avatar:String,
    val massage:String,
    val number:String,
    val companyName:String,
    val lastPositionId:String?
): Serializable

