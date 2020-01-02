package app.findbest.vip.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.application.App
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.getInstance()
        PushAgent.getInstance(this@BaseActivity).onAppStart()

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()
    }
}