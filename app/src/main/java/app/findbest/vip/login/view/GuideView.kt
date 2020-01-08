package app.findbest.vip.login.view

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonactivity.MainActivity
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.RetrofitUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*

class GuideView: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            backgroundColor = Color.TRANSPARENT
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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }
    }

    private suspend fun isToken(){
        try {
            val retrofitUils =
                RetrofitUtils(this@GuideView, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(LoginApi::class.java)
                .isToken()
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                println("token未过期")

//                val mPerferences: SharedPreferences =
//                    PreferenceManager.getDefaultSharedPreferences(this@GuideView)
//                val token = mPerferences.getString("token", "")

                startActivity<MainActivity>()
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
            if(it.code() == 400){
                println("token过期，需要登录")
                toast("token过期，需要重新登录")
                startActivity<LoginActivity>()
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        } catch (throwable: Throwable) {
            println(throwable)
        }
    }

}