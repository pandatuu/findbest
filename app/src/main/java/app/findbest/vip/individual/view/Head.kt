package app.findbest.vip.individual.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.individual.fragment.HActionbar
import app.findbest.vip.individual.fragment.HMain
import com.jaeger.library.StatusBarUtil
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class Head:AppCompatActivity() {
    lateinit var  hActionbar: HActionbar
    lateinit var  hMain: HMain

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var mainScreenId=1
        frameLayout{
            id = mainScreenId

            linearLayout{
                orientation = LinearLayout.VERTICAL
                //ActionBar
                var actionBarId = 2
                frameLayout {
                    id = actionBarId
                    hActionbar = HActionbar.newInstance()
                    supportFragmentManager.beginTransaction().add(actionBarId, hActionbar).commit()
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

                var newFragmentId = 3
                frameLayout {
                    id = newFragmentId
                    hMain = HMain.newInstance()
                    supportFragmentManager.beginTransaction().add(newFragmentId, hMain).commit()
                }.lparams(width = matchParent, height = matchParent){}

            }.lparams(){
                width = matchParent
                height = matchParent
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setActionBar(hActionbar.TrpToolbar)
        StatusBarUtil.setTranslucentForImageView(this@Head, 0, hActionbar.TrpToolbar)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        hActionbar.TrpToolbar!!.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }

    }
}