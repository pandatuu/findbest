package app.findbest.vip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import imui.jiguang.cn.imuisample.messages.JitsiMeetActivitySon
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import java.net.URL

class Demo: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(getString(R.string.jitsiUrl)))
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        val roomId = "2413254325352"
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom(roomId)
            .setUserInfo(JitsiMeetUserInfo())
            .build()

//                    JitsiMeetActivitySon.launch(this@VideoRequestActivity, options, "")
        val intent = Intent(this@Demo, JitsiMeetActivitySon2::class.java)
        intent.action = "org.jitsi.meet.CONFERENCE"
        intent.putExtra("JitsiMeetConferenceOptions", options)
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }
}