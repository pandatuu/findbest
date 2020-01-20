package app.findbest.vip.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.message.activity.VideoResultActivity
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

open class BaseActivity: AppCompatActivity() {

    override fun onResume() {
        super.onResume()
//        val bool = App.getInstance()?.getInviteVideoBool()
//        if(bool!!){
//            startActivity<VideoResultActivity>()
//            overridePendingTransition(R.anim.right_in, R.anim.left_out)
//        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.getInstance()
        PushAgent.getInstance(this@BaseActivity).onAppStart()

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()
    }
}