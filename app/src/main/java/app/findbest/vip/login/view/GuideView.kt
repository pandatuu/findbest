package app.findbest.vip.login.view

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.utils.RetrofitUtils
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GuideView: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            backgroundColor = Color.WHITE
        }
    }

    override fun onStart() {
        super.onStart()
        //当用户以前登陆过才校验token是否过期
        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@GuideView)
        val userToken = mPerferences.getString("token", "")
        if(userToken!=""){
            //登录过，校验token是否过期
            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                isToken()
            }
        }else{
            //没登录过，都去登录页面
            startActivity<LoginActivity>()
        }
    }

    private suspend fun isToken(){
        try {
            val retrofitUils =
                RetrofitUtils(this@GuideView, resources.getString(R.string.testRegisterUrl))
            val it = retrofitUils.create(LoginApi::class.java)
                .isToken()
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                println("token未过期")
            }
            if(it.code() == 401){
                println("token过期，需要登录")
                toast("token过期，需要重新登录")
                startActivity<LoginActivity>()
            }
        } catch (throwable: Throwable) {
            println(throwable)
        }
    }
}