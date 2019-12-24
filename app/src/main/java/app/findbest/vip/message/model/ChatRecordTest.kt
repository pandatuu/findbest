package cgland.job.sk_android.mvp.model.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ChatRecordModelTest(
    val uid:String,
    val userName:String,
    val position:String,
    var avatar:String,
    val massage:String,
    val number:String,
    val companyName:String,
    val lastPositionId:String?) : Parcelable