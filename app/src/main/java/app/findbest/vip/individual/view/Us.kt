package app.findbest.vip.individual.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.individual.fragment.UsActionbar
import app.findbest.vip.individual.fragment.UsMain
import com.jaeger.library.StatusBarUtil
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class Us:AppCompatActivity() {
    lateinit var  usActionbar: UsActionbar
    lateinit var  usMain: UsMain

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var mainScreenId=1
        frameLayout{
            id = mainScreenId

            linearLayout{
                orientation = LinearLayout.VERTICAL
                //ActionBar
                val actionBarId = 2
                frameLayout {
                    id = actionBarId
                    usActionbar = UsActionbar.newInstance()
                    supportFragmentManager.beginTransaction().add(actionBarId, usActionbar).commit()
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

                val newFragmentId = 3
                frameLayout {
                    id = newFragmentId
                    usMain = UsMain.newInstance()
                    supportFragmentManager.beginTransaction().add(newFragmentId, usMain).commit()
                }.lparams(width = matchParent, height = matchParent){}

            }.lparams(){
                width = matchParent
                height = matchParent
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setActionBar(usActionbar.TrpToolbar)
        StatusBarUtil.setTranslucentForImageView(this@Us, 0, usActionbar.TrpToolbar)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        usActionbar.TrpToolbar!!.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }

    }
}