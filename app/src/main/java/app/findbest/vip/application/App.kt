package app.findbest.vip.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceManager
import com.umeng.message.IUmengRegisterCallback


class App: Application() {

    private val appKey = "5dedfa5b4ca3579d01000010"
    private val appSecret = "245e0ab8fa4cada8d858591b214a2d21"

    override fun onCreate() {
        super.onCreate()

        UMConfigure.init(this, appKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, appSecret)

        //获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object: IUmengRegisterCallback{
            override fun onSuccess(p0: String?) {
                Log.i(TAG, "注册成功：deviceToken：-------->  $p0")
                val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val mEditor = mPerferences.edit()
                mEditor.putString("deviceToken", p0)
                mEditor.commit()
            }

            override fun onFailure(p0: String?, p1: String?) {
                Log.e(TAG, "注册失败：-------->  p0:$p0,p1:$p1")
            }
        })
    }
}